from fastapi import APIRouter, HTTPException, Query
from amadeus import Client
import random
from fastapi import APIRouter, HTTPException
from app.config import settings
import httpx
from app.models.amadeus_pydantic.vehicle import (
    VehicleSearchQuery,
    VehicleSearchResponse,
    VehicleInfo
)

UNSPLASH_ACCESS_KEY = settings.UNSPLASH_ACCESS_KEY

amadeus = Client(
    client_id=settings.AMADEUS_CLIENT_ID,
    client_secret=settings.AMADEUS_CLIENT_SECRET
)

router = APIRouter(
    tags=["Amadeus Controller"],
     responses={
        200: {"description": "Request completed successfully"},
        400: {"description": "Bad request due to invalid parameters"},
        500: {"description": "Internal server error"},
    },
)

@router.post("/vehicle-search", response_model=VehicleSearchResponse)
async def search_vehicles(query: VehicleSearchQuery) -> VehicleSearchResponse:
    """
    @brief Simulates a vehicle search based on location and vehicle type.

    This endpoint generates mock vehicle data based on a provided city code.
    It returns a list of available vehicles with randomly generated attributes.

    @param query A VehicleSearchQuery object containing:
        - location: IATA city code
        - vehicleType: Type of vehicle requested
        - limit: Number of results to return

    @return VehicleSearchResponse with a list of simulated vehicle data.

    @exception HTTPException 500 If an internal error occurs while processing the request.
    """
    try:
        city_vehicles = {
            "MAD": [("SEAT", "Ibiza"), ("Renault", "Clio"), ("BMW", "X1")],
            "BCN": [("Ford", "Focus"), ("Volkswagen", "Golf"), ("Tesla", "Model 3")],
            "PAR": [("Peugeot", "208"), ("Citroën", "C3"), ("Mercedes", "A-Class")],
            "ROM": [("Fiat", "500"), ("Alfa Romeo", "Giulietta"), ("Audi", "A3")]
        }

        location = query.location.upper()
        vehicle_list = city_vehicles.get(location, [("Toyota", "Corolla"), ("Honda", "Civic"), ("Nissan", "Micra")])

        simulated_vehicles = []
        for i in range(query.limit or 10):
            brand, model = random.choice(vehicle_list)
            vehicle_id = f"{location[:3]}-{i+1:03d}"
            price = round(random.uniform(30, 120), 2)
            year = random.randint(2018, 2024)

            simulated_vehicles.append(VehicleInfo(
                vehicleId=vehicle_id,
                name=f"{brand} {model}",
                cityCode=location,
                available=True,
                pricePerDay=price,
                currency="EUR",
                vehicleType=query.vehicleType,
                brand=brand,
                model=model,
                year=year,
                seats=random.choice([4, 5, 7]),
                doors=random.choice([3, 4, 5]),
                transmission=random.choice(["Manual", "Automatic"]),
                fuelType=random.choice(["Gasoline", "Diesel", "Electric"])
            ))

        return VehicleSearchResponse(
            data=simulated_vehicles,
            count=len(simulated_vehicles)
        )

    except Exception as error:
        raise HTTPException(status_code=500, detail=f"Internal error while searching vehicles: {str(error)}")


@router.get("/vehicle-image/")
async def get_vehicle_image(
    brand: str = Query(..., description="Vehicle brand"),
    model: str = Query(..., description="Vehicle model")
):
    """
    @brief Retrieves a vehicle image from the Unsplash API.

    This endpoint searches Unsplash for an image matching the given vehicle brand and model.
    If no match is found, it retries with only the brand as a fallback.

    @param brand The brand of the vehicle (e.g., "BMW").
    @param model The model of the vehicle (e.g., "X5").

    @return A JSON object containing the image URL:
        - image_url: direct link to the vehicle image

    @exception HTTPException 500 If the Unsplash API key is missing.
    @exception HTTPException 503 If an error occurs while connecting to Unsplash.
    @exception HTTPException 404 If no image is found for the vehicle.
    """

    if not UNSPLASH_ACCESS_KEY:
        raise HTTPException(status_code=500, detail="Unsplash API key is missing")

    url = "https://api.unsplash.com/search/photos"
    params = {
        "query": f"{brand} {model}",
        "client_id": UNSPLASH_ACCESS_KEY,
        "orientation": "landscape",
        "per_page": 1
    }

    async with httpx.AsyncClient() as client:
        try:
            res = await client.get(url, params=params)
            res.raise_for_status()
            data = res.json()
        except Exception as e:
            raise HTTPException(status_code=503, detail=f"Error connecting to Unsplash: {e}")

        if not data.get("results"):
            # Fallback: try only the brand
            params["query"] = brand
            try:
                res = await client.get(url, params=params)
                res.raise_for_status()
                data = res.json()
            except Exception as e:
                raise HTTPException(status_code=503, detail=f"Error connecting to Unsplash during fallback: {e}")

        if not data.get("results"):
            raise HTTPException(status_code=404, detail="No images found for this vehicle")

        image_url = data["results"][0]["urls"]["regular"]
        return {"image_url": image_url}





