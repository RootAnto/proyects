from pydantic import BaseModel, Field, field_validator
from typing import List, Optional
from datetime import datetime, date
import re

class FlightSegment(BaseModel):
    departureAirport: str = Field(..., description="Código IATA del aeropuerto de salida")
    departureTime: datetime = Field(..., description="Hora de salida en formato ISO 8601")
    arrivalAirport: str = Field(..., description="Código IATA del aeropuerto de llegada")
    arrivalTime: datetime = Field(..., description="Hora de llegada en formato ISO 8601")
    carrierCode: str = Field(..., description="Código de la aerolínea")
    flightNumber: str = Field(..., description="Número de vuelo")
    aircraftCode: Optional[str] = Field(None, description="Código del tipo de aeronave")
    duration: str = Field(..., description="Duración del segmento, por ejemplo 'PT2H30M'")

    @field_validator('duration')
    @classmethod
    def duration_must_be_iso8601(cls, v):
        pattern = r'^P(T(\d+H)?(\d+M)?(\d+S)?)?$'
        if not re.match(pattern, v):
            raise ValueError('duration debe seguir el formato ISO 8601 (ej: PT2H30M)')
        return v

class FlightItinerary(BaseModel):
    duration: str = Field(..., description="Duración total del itinerario, ejemplo 'PT5H45M'")
    segments: List[FlightSegment] = Field(..., description="Lista de segmentos de vuelo")

    @field_validator('duration')
    @classmethod
    def duration_must_be_iso8601(cls, v):
        pattern = r'^P(T(\d+H)?(\d+M)?(\d+S)?)?$'
        if not re.match(pattern, v):
            raise ValueError('duration debe seguir el formato ISO 8601 (ej: PT5H45M)')
        return v

class FlightPrice(BaseModel):
    total: str = Field(..., description="Precio total del vuelo")
    currency: str = Field(..., description="Moneda del precio, por ejemplo 'EUR'")

class FlightOffer(BaseModel):
    id: str = Field(..., description="Identificador único de la oferta de vuelo")
    source: str = Field(..., description="Fuente del proveedor de vuelo")
    price: FlightPrice
    itineraries: List[FlightItinerary]

class FlightSearchResponse(BaseModel):
    success: bool = Field(..., description="Indica si la búsqueda fue exitosa")
    offers: List[FlightOffer] = Field(..., description="Lista de ofertas de vuelo encontradas")
    count: int = Field(..., description="Número total de ofertas encontradas")
    currency: str = Field(..., description="Moneda utilizada para los precios")

class FlightSearchQuery(BaseModel):
    originLocationCode: str = Field(default='MAD', description="Código IATA del aeropuerto de origen")
    destinationLocationCode: str = Field(default='NYC', description="Código IATA del aeropuerto de destino")
    departureDate: date = Field(default=date(2025, 6, 15), description="Fecha de salida (formato YYYY-MM-DD)")
    returnDate: Optional[date] = Field(default=date(2025, 6, 25), description="Fecha de regreso opcional")
    adults: int = Field(default=1, description="Número de adultos")
    max: Optional[int] = Field(default=5, description="Cantidad máxima de resultados a devolver")
