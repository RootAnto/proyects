from pydantic import BaseModel, Field
from typing import List, Optional
from app.models.amadeus_pydantic.flight import FlightOffer
from app.models.amadeus_pydantic.hotel import HotelInfo
from app.models.amadeus_pydantic.vehicle import VehicleInfo


class TripSearchQuery(BaseModel):
    originLocationCode: str = Field(default="MAD", description="Código de origen (IATA)")
    destinationLocationCode: str = Field(default="BCN", description="Código de destino (IATA)")
    departureDate: str = Field(default="2025-08-01", description="Fecha de salida (YYYY-MM-DD)")
    returnDate: Optional[str] = Field(default="2025-08-07", description="Fecha de regreso (YYYY-MM-DD)")

    cityCode: str = Field(default="BCN", description="Código de ciudad para búsqueda de hoteles")
    vehicleLocation: str = Field(default="BCN", description="Código de ciudad para alquiler de vehículo")

    checkInDate: str = Field(default="2025-08-01", description="Fecha de entrada al hotel")
    checkOutDate: str = Field(default="2025-08-07", description="Fecha de salida del hotel")

    adults: int = Field(default=1, ge=1, description="Número de adultos")
    max: int = Field(default=5, ge=1, description="Número máximo de resultados de vuelos")
    hotelLimit: int = Field(default=5, ge=1, description="Número máximo de hoteles")
    vehicleLimit: int = Field(default=5, ge=1, description="Número máximo de vehículos")

    defaultHotelPrice: float = Field(default=100.0, ge=0, description="Precio por noche por defecto")


class TripCostSummary(BaseModel):
    flightTotal: float = Field(..., ge=0, description="Costo total de vuelos")
    hotelTotal: float = Field(..., ge=0, description="Costo total del alojamiento")
    vehicleTotal: float = Field(..., ge=0, description="Costo total del alquiler del vehículo")
    currency: str = Field(..., description="Moneda utilizada (ej. EUR)")
    grandTotal: float = Field(..., ge=0, description="Costo total del viaje (vuelos + hotel + vehículo)")


class TripSearchResponse(BaseModel):
    flights: List[FlightOffer] = Field(..., description="Lista de ofertas de vuelos")
    hotels: List[HotelInfo] = Field(..., description="Lista de hoteles disponibles")
    vehicles: List[VehicleInfo] = Field(..., description="Lista de vehículos disponibles")
    summary: TripCostSummary = Field(..., description="Resumen de costos del viaje")
