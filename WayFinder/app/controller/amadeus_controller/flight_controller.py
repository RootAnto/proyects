from fastapi import APIRouter, HTTPException
from amadeus import Client, ResponseError
from fastapi import APIRouter, HTTPException, Query
from app.config import settings
from app.models.amadeus_pydantic.flight import (
        FlightSearchQuery,
        FlightSearchResponse,
        FlightOffer,
        FlightPrice,
        FlightItinerary,
        FlightSegment
    )

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

@router.post("/flight-search", response_model=FlightSearchResponse)
async def search_flights(query: FlightSearchQuery) -> FlightSearchResponse:
    try:
        params = {
            "originLocationCode": query.originLocationCode,
            "destinationLocationCode": query.destinationLocationCode,
            "departureDate": query.departureDate,
            "returnDate": query.returnDate,
            "adults": query.adults,
            "max": 50
        }

        response = amadeus.shopping.flight_offers_search.get(**params)

        results = []
        seen_departure_times = set()

        for offer in response.data:
            # Si ya tenemos la cantidad deseada, parar
            if len(results) >= query.max:
                break

            itineraries = []
            for itinerary in offer['itineraries']:
                segments = []
                for segment in itinerary['segments']:
                    segments.append(FlightSegment(
                        departureAirport=segment['departure']['iataCode'],
                        departureTime=segment['departure']['at'],
                        arrivalAirport=segment['arrival']['iataCode'],
                        arrivalTime=segment['arrival']['at'],
                        carrierCode=segment['carrierCode'],
                        flightNumber=segment['number'],
                        aircraftCode=segment.get('aircraft', {}).get('code'),
                        duration=segment['duration']
                    ))
                itineraries.append(FlightItinerary(
                    duration=itinerary['duration'],
                    segments=segments
                ))

            # Filtrar duplicados por hora de salida del primer segmento
            first_departure_time = itineraries[0].segments[0].departureTime
            if first_departure_time in seen_departure_times:
                continue  # Omitir duplicado
            seen_departure_times.add(first_departure_time)

            price = FlightPrice(
                total=offer['price']['total'],
                currency=offer['price']['currency']
            )

            results.append(FlightOffer(
                id=offer['id'],
                source=offer['source'],
                price=price,
                itineraries=itineraries
            ))

        results = sorted(
            results,
            key=lambda offer: offer.itineraries[0].segments[0].departureTime
        )

        return FlightSearchResponse(
            success=True,
            offers=results,
            count=len(results),
            currency=results[0].price.currency if results else "EUR"
        )

    except ResponseError as error:
        raise HTTPException(
            status_code=400,
            detail=f"Error en la API de Amadeus: {str(error)}"
        )
    except Exception as error:
        raise HTTPException(
            status_code=500,
            detail=f"Error interno al buscar vuelos: {str(error)}"
        )

@router.get("/location-search")
def location_search(
    keyword: str,
    subType: str = Query(default="CITY", regex="^(CITY|AIRPORT)$")
):
    try:
        response = amadeus.reference_data.locations.get(
            keyword=keyword,
            subType=subType
        )
        return {"locations": response.data}
    except ResponseError as error:
        raise HTTPException(status_code=500, detail=str(error))