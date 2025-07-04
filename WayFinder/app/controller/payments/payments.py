import stripe
from sqlalchemy.orm import Session
from app.database.db import get_db
from app.models.trips.trip_db import Trip
from app.models.trips.trip_pydantic import TripStatus
from fastapi import HTTPException
from loguru import logger
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.config import settings

stripe.api_key = settings.STRIPE_SECRET_KEY

router = APIRouter()

@router.post("/payments/payment-intent")
def payment_intent(trip_id: str, db: Session = Depends(get_db)):
    """
    @brief Creates a Stripe payment intent for a specific trip.

    This endpoint initializes a Stripe PaymentIntent for the given trip ID.
    It retrieves trip details from the database and delegates intent creation
    to a helper function.

    @param trip_id: str
        The ID of the trip for which the payment is being initiated.

    @param db: Session
        SQLAlchemy database session (injected by FastAPI dependency).

    @return dict
        A dictionary containing the `client_secret` used on the frontend
        to complete the Stripe payment.

    @throws HTTPException 404
        If the trip with the given ID does not exist.

    @throws HTTPException 400
        If the trip has already been accepted or rejected and payment is not allowed.

    @throws HTTPException 500
        For any unexpected errors during payment intent creation.
    """
    try:
        client_secret = create_payment_intent_from_trip(trip_id, db)
        return {"client_secret": client_secret}
    except HTTPException as e:
        raise e
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


def create_payment_intent_from_trip(trip_id: str, db: Session) -> str:
    """
    @brief Helper function to create a Stripe PaymentIntent from a trip object.

    This function checks the status of the trip and creates a Stripe PaymentIntent
    if the trip is eligible for payment (i.e., not accepted or rejected). The amount
    is calculated based on the trip's total price.

    @param trip_id: str
        The unique identifier of the trip.

    @param db: Session
        SQLAlchemy database session used to fetch the trip.

    @return str
        The `client_secret` of the created Stripe PaymentIntent.

    @throws HTTPException 404
        If no trip is found with the given ID.

    @throws HTTPException 400
        If the trip is already accepted or rejected, making payment invalid.

    @throws stripe.error.StripeError
        If the Stripe API fails to create a PaymentIntent.
    """
    trip = db.query(Trip).filter(Trip.id == trip_id).first()
    if not trip:
        raise HTTPException(status_code=404, detail="Reserva no encontrada")

    if trip.status in [TripStatus.aceptada, TripStatus.rechazada]:
        logger.info(f"No se puede confirmar pago para reserva {trip_id} con estado {trip.status}")
        raise HTTPException(status_code=400, detail=f"La reserva ya fue {trip.status.value}")

    intent = stripe.PaymentIntent.create(
        amount=int(trip.total_price * 100),
        currency=trip.currency,
        metadata={"trip_id": trip.id},
        payment_method_types=["card"]
    )

    return intent.client_secret