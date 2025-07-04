from fastapi.responses import RedirectResponse
from fastapi import APIRouter, HTTPException, Query
from amadeus import Client, ResponseError
import httpx
from app.config import settings

from app.models.amadeus_pydantic.hotel import (
    HotelSearchQuery,
    HotelSearchResponse,
    HotelInfo
)
from datetime import datetime

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

@router.post("/hotel-search", response_model=HotelSearchResponse)
async def search_hotels(query: HotelSearchQuery) -> HotelSearchResponse:
    """
    Search for hotels by city code using the Amadeus API.

    You must provide at least the `cityCode` parameter.

    - **cityCode**: IATA city code (e.g. 'MAD' for Madrid)
    - **checkInDate**: Optional check-in date (not used directly)
    - **checkOutDate**: Optional check-out date (not used directly)
    - **radius**: Optional search radius (not used in city search)
    - **limit**: Maximum number of hotels to return (default is 10)
    - **defaultPrice**: Default price to assign if not provided (default is 100)
    - **nights**: Calculated from checkInDate and checkOutDate
    """
    try:
        # Ensure cityCode is provided
        if not query.cityCode:
            raise HTTPException(
                status_code=400,
                detail="The 'cityCode' parameter is required"
            )

        # Parse checkInDate and checkOutDate
        check_in = datetime.strptime(query.checkInDate, "%Y-%m-%d") \
            if query.checkInDate else None
        check_out = datetime.strptime(query.checkOutDate, "%Y-%m-%d") \
            if query.checkOutDate else None

        # Calculate nights if both dates are provided
        nights = (check_out - check_in).days if check_in and check_out else None

        # Fetch hotel data from Amadeus API
        response = amadeus.reference_data.locations.hotels.by_city.get(
            cityCode=query.cityCode
        )
        historial = "a"
        mensaje_usuario = "a"
        respuesta = "respuesta"

        historial += f"\nUsuario: {mensaje_usuario}\nAsistente: {respuesta}"

        hotels = []
        max_results = query.limit or 10
        price = query.defaultPrice or 100.0

        # Prepare the hotel data for response
        for hotel in response.data[:max_results]:
            hotels.append(HotelInfo(
                hotelId=hotel.get('hotelId', ''),
                name=hotel.get('name', ''),
                cityCode=hotel.get('cityCode', ''),
                latitude=hotel.get('geoCode', {}).get('latitude', 0.0),
                longitude=hotel.get('geoCode', {}).get('longitude', 0.0),
                available=True,
                price=price,
                currency="EUR",
                nights=nights  # Include calculated nights
            ))

        # Return the hotel search response
        return HotelSearchResponse(
            data=hotels,
            count=len(hotels)
        )

    except ResponseError as error:
        raise HTTPException(
            status_code=400,
            detail=f"Amadeus API error: {str(error)}"
        )
    except Exception as error:
        raise HTTPException(
            status_code=500,
            detail=f"Internal error while searching hotels: {str(error)}"
        )


@router.get("/hotel-image/")
async def get_hotel_image(query: str = Query(..., description="Hotel name or city")):
    """
    @brief Retrieves a representative hotel or city image using the Unsplash API.

    This endpoint fetches the most relevant landscape-oriented image based on the
    given query, which can be a hotel name or a city name. It uses the Unsplash API
    and returns the URL of the first matching image.

    @param query: str
        A search term representing the hotel name or the city for which to retrieve an image.

    @return dict
        A dictionary containing the image URL with the key `image_url`.

    @throws HTTPException 500
        If the Unsplash API key is missing.

    @throws HTTPException 503
        If a network error occurs or the Unsplash service is unavailable.

    @throws HTTPException 404
        If no matching images are found in the Unsplash API response.
    """


    if not UNSPLASH_ACCESS_KEY:
        raise HTTPException(status_code=500, detail="Missing Unsplash API key")

    url = "https://api.unsplash.com/search/photos"
    params = {
        "query": query,
        "client_id": UNSPLASH_ACCESS_KEY,
        "orientation": "landscape",
        "per_page": 1,
        "order_by": "relevant"
    }

    async with httpx.AsyncClient() as client:
        try:
            res = await client.get(url, params=params)
            res.raise_for_status()
            data = res.json()
        except httpx.RequestError as exc:
            raise HTTPException(status_code=503, detail=f"Network error connecting to Unsplash: {exc}")
        except httpx.HTTPStatusError as exc:
            raise HTTPException(status_code=exc.response.status_code, detail="Unsplash API returned an error")

    if not data.get("results"):
        raise HTTPException(status_code=404, detail="No images found")

    image_url = data["results"][0]["urls"]["regular"]
    return {"image_url": image_url}






