from typing import List
from fastapi import APIRouter, HTTPException, Depends, status
from fastapi.responses import HTMLResponse, JSONResponse
from sqlalchemy.orm import Session
from app.services.email_service import *
from app.models.trips.trip_pydantic import TripCreate, TripOut, TripStatus
from app.models.trips.trip_db import Trip
from app.database.db import get_db
import uuid
from fastapi import Query

router = APIRouter(prefix="/trips", tags=["Trips"])

@router.post("/", response_model=TripOut)
def create_trip(
    trip: TripCreate,
    user_email: str = Query(...),
    db: Session = Depends(get_db),
) -> TripOut:
    '''
    @brief Creates a new trip and stores it in the database.

    This function receives trip data and a user's email address, stores the trip in the database,
    and sends two emails: one confirmation email and another containing the paid ticket with a QR code.

    @param trip An instance of TripCreate containing the trip's details.
    @param user_email The user's email address, provided as a query parameter.
    @param db A SQLAlchemy database session provided via FastAPI dependency injection.

    @return TripOut object containing the created trip's data.
    '''

    logger.info("Starting trip creation process for user: {}", user_email)

    try:
        trip_id = str(uuid.uuid4())
        trip_data = trip.model_dump()

        trip_data.pop("user_email", None)
        user_name = trip_data.pop("user_name", None)
        trip_data.pop("flight_id", None)
        trip_data.pop("hotel_id", None)
        trip_data.pop("vehicle_id", None)

        for key in [
            "flight_name", "flight_price", "hotel_name", "hotel_price",
            "hotel_nights", "vehicle_model", "vehicle_price", "vehicle_days",
            "total_price", "currency"
        ]:
            trip_data.pop(key, None)

        logger.debug("Trip metadata prepared. Trip ID: {}", trip_id)

        db_trip = Trip(
            id=trip_id,
            user_email=user_email,
            user_name=user_name,
            flight_id=trip.flight_id,
            hotel_id=trip.hotel_id,
            vehicle_id=trip.vehicle_id,
            status=TripStatus.pendiente,
            flight_name=trip.flight_name,
            flight_price=trip.flight_price,
            hotel_name=trip.hotel_name,
            hotel_price=trip.hotel_price,
            hotel_nights=trip.hotel_nights,
            vehicle_model=trip.vehicle_model,
            vehicle_price=trip.vehicle_price,
            vehicle_days=trip.vehicle_days,
            total_price=trip.total_price,
            currency=trip.currency,
            **trip_data
        )

        db.add(db_trip)
        db.commit()
        db.refresh(db_trip)

        logger.success("Trip successfully created and saved to the database. Trip ID: {}", trip_id)

        trip_out = TripOut.from_orm(db_trip)

        send_confirmation_ticket(
            to_email=user_email,
            to_name=user_name or "cliente",
            trip=trip_out
        )

        logger.info("Confirmation ticket sent to user: {}", user_email)

        return trip_out

    except Exception as e:
        logger.exception("An error occurred during trip creation: {}", e)
        raise


@router.get("/get-email-from-trip")
def get_user_data_from_trip(trip_id: str = Query(...), db: Session = Depends(get_db)):
    '''
    @brief Retrieves the user's email and name from a trip ID.

    @param trip_id The unique identifier of the reservation (trip), provided as a query parameter.
    @param db Database session dependency.

    @return A dictionary containing the user's email and name. If the name is not available, "Cliente" is returned as default.

    This endpoint is useful for retrieving user contact information associated with a specific trip.
    '''
    trip = db.query(Trip).filter(Trip.id == trip_id).first()
    if not trip:
        raise HTTPException(status_code=404, detail=f"Trip with id '{trip_id}' not found")

    return {
        "user_email": trip.user_email,
        "user_name": trip.user_name or "Cliente"
    }



@router.get("/confirm-trip")
def confirm_trip(trip_id: str, user_email: str, db: Session = Depends(get_db)) -> JSONResponse:
    """
    @brief Confirms a reservation for a trip using the provided trip ID and user email.
    @route GET /confirm-trip
    @param trip_id The ID of the trip to be confirmed.
    @param user_email The email of the user attempting to confirm the reservation.
    @param db SQLAlchemy database session dependency.
    @return JSONResponse indicating success, failure, or status of the reservation.

    The function performs several checks:
    - Validates that the reservation exists.
    - Validates that the reservation has an email assigned.
    - Confirms that the provided email matches the one associated with the reservation.
    - Ensures the reservation hasn't already been rejected or confirmed.
    - Updates the reservation status to "accepted" and sends a confirmation email with tickets.

    Possible response statuses:
    - 200: Reservation confirmed or already confirmed/rejected.
    - 400: Email mismatch, invalid state for confirmation.
    - 404: Reservation not found.
    """
    logger.info(f"Intentando confirmar la reserva: trip_id={trip_id}, user_email={user_email}")

    trip_id = trip_id.strip()
    trip = db.query(Trip).filter(Trip.id == trip_id).first()

    if not trip:
        logger.warning(f"Reserva no encontrada para trip_id={trip_id}")
        return JSONResponse(status_code=404, content={
            "status": "error",
            "message": "Reserva no encontrada.",
            "showReturnHome": True
        })

    if not trip.user_email:
        logger.error(f"La reserva no tiene email registrado: trip_id={trip_id}")
        return JSONResponse(status_code=400, content={
            "status": "error",
            "message": "La reserva no tiene email registrado.",
            "showReturnHome": True
        })

    if trip.user_email.lower() != user_email.lower():
        logger.error(f"Email proporcionado '{user_email}' no coincide con '{trip.user_email}'")
        return JSONResponse(status_code=400, content={
            "status": "error",
            "message": "El email no coincide con la reserva.",
            "showReturnHome": True
        })

    status = trip.status.value if hasattr(trip.status, "value") else trip.status

    if status == TripStatus.rechazada:
        logger.info(f"Reserva rechazada: trip_id={trip_id}")
        return JSONResponse(status_code=200, content={
            "status": "rejected",
            "message": "La reserva ha sido rechazada previamente. No es posible confirmarla.",
            "showReturnHome": True
        })

    if status == TripStatus.aceptada:
        logger.info(f"Reserva ya confirmada: trip_id={trip_id}")
        return JSONResponse(status_code=200, content={
            "status": "success",
            "message": "Reserva ya confirmada.",
            "showReturnHome": True
        })

    if status != TripStatus.pendiente:
        logger.info(f"Estado inválido para confirmar: {status}")
        return JSONResponse(status_code=400, content={
            "status": "error",
            "message": f"No se puede confirmar una reserva en estado '{status}'.",
            "showReturnHome": True
        })

    trip.confirmed = True
    trip.status = TripStatus.aceptada
    db.commit()
    logger.success(f"Reserva confirmada: trip_id={trip_id}")

    trip_out = TripOut.from_orm(trip)
    try:
        send_paid_ticket_with_qr(to_email=user_email, trip=trip_out)
        logger.info(f"Correo enviado a {user_email}")
    except Exception as e:
        logger.exception(f"Error enviando correo: {e}")

    return JSONResponse(status_code=200, content={
        "status": "success",
        "message": "Reserva confirmada. Los tickets han sido enviados al correo proporcionado.",
        "showReturnHome": True
    })


@router.get("/reservations/{trip_id}/reject")
def reject_reservation(trip_id: str, db: Session = Depends(get_db)):
    '''
    @brief Rejects a reservation if it hasn't already been accepted or confirmed.

    @param trip_id The unique identifier of the reservation (trip).
    @param db Database session dependency.

    @return A dictionary with a rejection message.

    This function performs the following steps:
      - Retrieves the reservation from the database.
      - Checks if the reservation exists.
      - Verifies that the reservation is not already rejected, accepted, or confirmed.
      - Changes the reservation status to "rejected".
      - Sends a rejection email to the user.
    '''
    logger.info(f"Starting rejection process for reservation {trip_id}")

    trip = db.query(Trip).filter(Trip.id == trip_id).first()
    status = trip.status.value if hasattr(trip.status, "value") else trip.status

    if not trip:
        logger.warning(f"Reservation not found: {trip_id}")
        raise HTTPException(status_code=404, detail="Reservation not found")

    if status == TripStatus.rechazada:
        logger.info(f"Reservation {trip_id} has already been rejected")
        return HTMLResponse(content=f"""
            <html>
                <body style="font-family: Arial, sans-serif; text-align:center; margin-top: 50px;">
                    <h2>Reserva ya rechazada</h2>
                    <p>Hola {trip.user_name or 'cliente'}, tu reserva con ID <strong>{trip.id}</strong> ya había sido rechazada previamente.</p>
                </body>
            </html>
        """)

    if status == TripStatus.aceptada or trip.confirmed:
        logger.info(f"Cannot reject reservation {trip_id} - already accepted or confirmed/paid")
        return HTMLResponse(content=f"""
            <html>
                <body style="font-family: Arial, sans-serif; text-align:center; margin-top: 50px;">
                    <h2>No es posible rechazar la reserva</h2>
                    <p>Hola {trip.user_name or 'cliente'}, tu reserva con ID <strong>{trip.id}</strong> ya ha sido aceptada y no puede ser rechazada.</p>
                </body>
            </html>
        """)


    logger.info(f"Changing status to 'rejected' for reservation {trip_id}")
    trip.status = TripStatus.rechazada
    db.commit()
    db.refresh(trip)

    # Convert ORM object to Pydantic model if needed
    trip_out = TripOut.from_orm(trip)

    try:
        logger.info(f"Sending rejection email to {trip.user_email}")
        send_rejection_email(to_email=trip.user_email, to_name=trip.user_name or "Cliente", trip_id=trip.id)
    except Exception as e:
        logger.error(f"Error sending rejection email for reservation {trip_id}: {e}")

    return HTMLResponse(content=f"""
        <html>
            <body style="font-family: Arial, sans-serif; text-align:center; margin-top: 50px;">
                <h2>Reserva rechazada con éxito</h2>
                <p>Gracias, {trip.user_name or 'cliente'}. Tu reserva con ID {trip.id} ha sido cancelada.</p>
            </body>
        </html>
    """)

@router.get("/user/{user_email}", response_model=List[TripOut])
def get_trips_by_user(user_email: str, db: Session = Depends(get_db)) -> List[TripOut]:
    """
    @brief Returns all confirmed trips for a given user by email.

    @param user_email The email of the user to filter trips.
    @param db SQLAlchemy session.

    @return List of confirmed TripOut objects.
    """
    trips = db.query(Trip).filter(
        Trip.user_email == user_email,
        Trip.status == TripStatus.aceptada
    ).all()

    if not trips:
        return []

    return trips


@router.get("/{trip_id}", response_model=TripOut)
def get_trip(trip_id: str, db: Session = Depends(get_db)) -> Trip:
    '''
    @brief Retrieve a trip by its ID.

    @param trip_id The UUID of the trip to retrieve.
    @param db Database session dependency.

    @throws HTTPException 404 if the trip is not found.

    @return The Trip object matching the given ID.
    '''
    trip = db.query(Trip).filter(Trip.id == trip_id).first()
    if not trip:
        raise HTTPException(status_code=404, detail="Viaje no encontrado")
    return trip


@router.get("/", response_model=List[TripOut])
def get_all_trips(db: Session = Depends(get_db)) -> List[Trip]:
    '''
@brief Retrieve all trips belonging to a specific user.

@param user_id The UUID of the user whose trips to retrieve.
@param db Database session dependency.

@return List of Trip objects for the user.
'''
    trips = db.query(Trip).all()
    return trips


@router.put("/{trip_id}", response_model=TripOut)
def update_trip(
    trip_id: str,
    updated_trip: TripCreate,
    db: Session = Depends(get_db)
) -> Trip:
    '''
    @brief Update an existing trip by its ID.

    @param trip_id The UUID of the trip to update.
    @param updated_trip Data to update the trip with.
    @param db Database session dependency.

    @throws HTTPException 404 if the trip is not found.

    @note The trip is updated in the database and changes are committed.

    @return The updated Trip object.
    '''
    trip = db.query(Trip).filter(Trip.id == trip_id).first()
    if not trip:
        raise HTTPException(status_code=404, detail="Viaje no encontrado")

    update_data = updated_trip.model_dump(exclude_unset=True)

    for key, value in update_data.items():
        setattr(trip, key, value)

    db.commit()
    db.refresh(trip)
    return trip


@router.delete("/{trip_id}", status_code=204)
def delete_trip(trip_id: str, db: Session = Depends(get_db)) -> None:
    '''
    @brief Delete a trip by its ID.

    @param trip_id The UUID of the trip to delete.
    @param db Database session dependency.

    @throws HTTPException: 404 if the trip is not found.

    @note The trip is permanently deleted from the database.

    @return None
    '''
    trip = db.query(Trip).filter(Trip.id == trip_id).first()
    if not trip:
        raise HTTPException(status_code=404, detail="Viaje no encontrado")

    db.delete(trip)
    db.commit()
    return


@router.delete("/trips/clear", status_code=status.HTTP_204_NO_CONTENT)
def clear_trips(db: Session = Depends(get_db)):
    """
    @brief Deletes all trip records from the database.

    @param db SQLAlchemy session dependency.

    @return JSON message indicating how many trips were deleted.
    @throws HTTPException 500 if the deletion process fails.
    """
    logger.info("Attempting to clear all trips from the database.")
    try:
        deleted = db.query(Trip).delete()
        db.commit()
        logger.success(f"Successfully deleted {deleted} trip(s) from the database.")
        return {"message": f"{deleted} trip(s) have been deleted."}
    except Exception as e:
        db.rollback()
        logger.exception("Failed to clear trips from the database.")
        raise HTTPException(status_code=500, detail="An error occurred while clearing trips.")

