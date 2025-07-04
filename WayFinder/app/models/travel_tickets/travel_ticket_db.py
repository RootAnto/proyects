from sqlalchemy import Column, String
from app.database.db import Base

class Ticket(Base):
    __tablename__ = "tickets"

    id         = Column(String(255), primary_key=True, index=True)
    user_id    = Column(String(255), nullable=False)
    flight_id  = Column(String(255), nullable=True)
    hotel_id   = Column(String(255), nullable=True)
    vehicle_id = Column(String(255), nullable=True)

