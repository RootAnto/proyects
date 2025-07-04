# app/services/payment_service.py (o en otro archivo dedicado)
from fastapi import Request, HTTPException
import stripe
from fastapi import APIRouter
from app.config import settings

router = APIRouter()

endpoint_secret = settings.STRIPE_WEBHOOK_SECRET

@router.post("/webhook")
async def stripe_webhook(request: Request):
    payload = await request.body()
    sig_header = request.headers.get('stripe-signature')

    try:
        event = stripe.Webhook.construct_event(payload, sig_header, endpoint_secret)
    except ValueError:
        # Payload inválido
        raise HTTPException(status_code=400, detail="Invalid payload")
    except stripe.error.SignatureVerificationError:
        # Firma inválida
        raise HTTPException(status_code=400, detail="Invalid signature")

    # Procesa eventos específicos
    if event['type'] == 'payment_intent.succeeded':
        payment_intent = event['data']['object']
        trip_id = payment_intent['metadata'].get('trip_id')
        # Aquí puedes ejecutar tu lógica para enviar el correo,
        # actualizar base de datos, etc.
        # Por ejemplo:
        # send_confirmation_email(trip_id)
        print(f"Pago exitoso para trip_id: {trip_id}")

    return {"status": "success"}
