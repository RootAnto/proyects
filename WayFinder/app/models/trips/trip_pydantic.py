from pydantic import BaseModel
from typing import Optional
from datetime import date
from enum import Enum


class TripStatus(str, Enum):
    pendiente = "pendiente"
    aceptada = "aceptada"
    rechazada = "rechazada"


class TripCreate(BaseModel):
    user_id: str
    origin: str
    destination: str
    departure_date: date
    return_date: Optional[date] = None
    adults: int = 1
    children: int = 0
    hotel_limit: int = 5
    vehicle_limit: int = 5
    max_price: Optional[float] = None
    user_email: Optional[str] = None
    user_name: Optional[str] = None

    # IDs
    flight_id: Optional[str] = None
    hotel_id: Optional[str] = None
    vehicle_id: Optional[str] = None

    # Flight details
    flight_name: Optional[str] = None
    flight_price: Optional[float] = None

    # Hotel details
    hotel_name: Optional[str] = None
    hotel_price: Optional[float] = None
    hotel_nights: Optional[int] = None

    # Vehicle details
    vehicle_model: Optional[str] = None
    vehicle_price: Optional[float] = None
    vehicle_days: Optional[int] = None

    total_price: float = 0.0  # Mejor valor por defecto explícito
    currency: str = "EUR"     # Igual, por defecto fijo


class TripOut(TripCreate):
    id: str
    confirmed: bool = False
    status: TripStatus = TripStatus.pendiente

    model_config = {
        "from_attributes": True
    }
