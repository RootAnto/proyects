from pydantic import BaseModel, Field
from typing import List, Optional


class VehicleSearchQuery(BaseModel):
    location: str = Field(default='MAD', description="Código de ubicación para recoger el vehículo (ej. código IATA)")
    pickUpDate: str = Field(default='2025-08-01', description="Fecha de recogida del vehículo (YYYY-MM-DD)")
    dropOffDate: str = Field(default='2025-08-07', description="Fecha de devolución del vehículo (YYYY-MM-DD)")
    vehicleType: Optional[str] = Field(default="economy", description="Tipo de vehículo deseado (economy, SUV, etc.)")
    limit: int = Field(default=10, description="Número máximo de resultados a devolver")
    defaultPrice: float = Field(default=50.0, description="Precio por día estimado para filtrar resultados")


class VehicleInfo(BaseModel):
    vehicleId: str = Field(..., description="Identificador único del vehículo")
    name: str = Field(..., description="Nombre comercial del vehículo")
    cityCode: str = Field(..., description="Código de ciudad donde se encuentra el vehículo")
    available: bool = Field(default=True, description="Indica si el vehículo está disponible")
    pricePerDay: float = Field(..., description="Precio por día del vehículo")
    currency: str = Field(..., description="Moneda del precio (ej. EUR, USD)")
    vehicleType: Optional[str] = Field(None, description="Tipo de vehículo (economy, SUV, etc.)")
    brand: Optional[str] = Field(None, description="Marca del vehículo")
    model: Optional[str] = Field(None, description="Modelo del vehículo")
    year: Optional[int] = Field(None, description="Año del modelo")
    seats: Optional[int] = Field(None, description="Número de asientos")
    doors: Optional[int] = Field(None, description="Número de puertas")
    transmission: Optional[str] = Field(None, description="Tipo de transmisión (manual, automática)")
    fuelType: Optional[str] = Field(None, description="Tipo de combustible (gasolina, diésel, eléctrico)")


class VehicleSearchResponse(BaseModel):
    data: List[VehicleInfo] = Field(..., description="Lista de vehículos disponibles")
    count: int = Field(..., description="Cantidad total de vehículos encontrados")