from app.database.db import Base
from sqlalchemy import Column, String, Date, Integer, Float, Boolean, Enum as SqlEnum
import enum

class TripStatusEnum(enum.Enum):
    pendiente = "pendiente"
    aceptada = "aceptada"
    rechazada = "rechazada"

class Trip(Base):
    __tablename__ = "trips"

    id = Column(String(255), primary_key=True, index=True)
    user_id = Column(String(255), nullable=False)

    origin = Column(String(255), nullable=False)
    destination = Column(String(255), nullable=False)
    departure_date = Column(Date, nullable=False)
    return_date = Column(Date, nullable=True)

    adults = Column(Integer, default=1)
    children = Column(Integer, default=0)

    hotel_limit = Column(Integer, default=5)
    vehicle_limit = Column(Integer, default=5)

    max_price = Column(Float, nullable=True)

    flight_id = Column(String(255), nullable=True)
    hotel_id = Column(String(255), nullable=True)
    vehicle_id = Column(String(255), nullable=True)
    confirmed = Column(Boolean, default=False)

    status = Column(SqlEnum(TripStatusEnum), default=TripStatusEnum.pendiente, nullable=False)

    user_email = Column(String(255), nullable=True)
    user_name = Column(String(255), nullable=True)

    # Campos adicionales para detalles y precios
    flight_name = Column(String(255), nullable=True)
    flight_price = Column(Float, nullable=True)

    hotel_name = Column(String(255), nullable=True)
    hotel_price = Column(Float, nullable=True)
    hotel_nights = Column(Integer, nullable=True)

    vehicle_model = Column(String(255), nullable=True)
    vehicle_price = Column(Float, nullable=True)
    vehicle_days = Column(Integer, nullable=True)

    total_price = Column(Float, nullable=True)
    currency = Column(String(10), nullable=True)
