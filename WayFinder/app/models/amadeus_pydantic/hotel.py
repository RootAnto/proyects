from pydantic import BaseModel, Field
from typing import List, Optional


class HotelSearchQuery(BaseModel):
    cityCode: str = Field(default='MAD', description="Código de la ciudad (IATA o estándar interno)")
    radius: int = Field(default=10, description="Radio de búsqueda en kilómetros desde el centro de la ciudad")
    checkInDate: str = Field(default='2025-08-01', description="Fecha de entrada al hotel (formato YYYY-MM-DD)")
    checkOutDate: str = Field(default='2025-08-07', description="Fecha de salida del hotel (formato YYYY-MM-DD)")
    limit: int = Field(default=10, description="Límite de resultados de hoteles a devolver")
    defaultPrice: float = Field(default=100.0, description="Precio base estimado por noche para filtrar resultados")


class HotelInfo(BaseModel):
    hotelId: str = Field(..., description="Identificador único del hotel")
    name: str = Field(..., description="Nombre del hotel")
    cityCode: str = Field(..., description="Código de la ciudad donde se encuentra el hotel")
    available: bool = Field(default=True, description="Indica si hay habitaciones disponibles")
    price: float = Field(..., description="Precio total estimado de la estancia")
    currency: str = Field(..., description="Moneda del precio, por ejemplo 'EUR'")
    nights: int = Field(..., description="Número total de noches de la estancia")


class HotelSearchResponse(BaseModel):
    data: List[HotelInfo] = Field(..., description="Lista de hoteles encontrados")
    count: int = Field(..., description="Cantidad total de hoteles encontrados")
