from pydantic import BaseModel
from typing import Optional
from datetime import datetime, date


class TripCreate(BaseModel):
    user_id: str
    origin: str
    destination: str
    departure_date: str
    return_date: Optional[str] = None
    adults: int
    children: int
    hotel_limit: int
    vehicle_limit: int
    max_price: Optional[float] = None

class TripOut(TripCreate):
    id: str  # UUID como string
    flight_id: Optional[str] = None
    hotel_id: Optional[str] = None
    vehicle_id: Optional[str] = None

    hotel_name: Optional[str] = None
    vehicle_name: Optional[str] = None
    total_days: Optional[int] = None
    total_price: Optional[float] = None
    user_email: Optional[str] = None
    user_name: Optional[str] = None

    model_config = {
        "from_attributes": True
    }


# Modelo para crear un billete
class TicketCreate(BaseModel):
    ticket_id: Optional[str] = None
    userId: str
    tripId: str
    issueDate: datetime
    seat_number: Optional[str] = None
    price: Optional[float] = None
    currency: Optional[str] = "USD"
    status: Optional[str] = "issued"
    issue_place: Optional[str] = None
    valid_until: Optional[datetime] = None
    passenger_name: Optional[str] = None
    additional_info: Optional[str] = None


# Modelo de salida del billete
class TicketOut(TicketCreate):
    id: str  # ID único del billete

    model_config = {
        "from_attributes": True
    }
