from fastapi import APIRouter, HTTPException
from amadeus import Client, ResponseError
from datetime import datetime
import random
from app.models.amadeus_pydantic.trip_suggested import TripSearchQuery, TripSearchResponse, TripCostSummary
from app.models.amadeus_pydantic.hotel import HotelInfo
from app.models.amadeus_pydantic.vehicle import VehicleInfo
from app.models.amadeus_pydantic.flight import FlightOffer, FlightSegment, FlightItinerary, FlightPrice
from app.config import settings

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

@router.post("/trip-search", response_model=TripSearchResponse)
async def search_trip(query: TripSearchQuery) -> TripSearchResponse:
    '''
    Performs a combined search for flights, hotels, and vehicles.

    @param query: TripSearchQuery object containing trip details.
    @return: TripSearchResponse containing the flight, hotel, vehicle data, and cost summary.
    @raises HTTPException: If invalid parameters or API errors occur.
    '''
    try:
        # ---------------------- DATE VALIDATION ----------------------
        check_in = datetime.strptime(query.checkInDate, "%Y-%m-%d")
        check_out = datetime.strptime(query.checkOutDate, "%Y-%m-%d")
        nights = (check_out - check_in).days
        if nights <= 0:
            raise HTTPException(status_code=400, detail="Check-out date must be after check-in date.")

        # ------------------------ FLIGHT SEARCH ------------------------
        flight_response = amadeus.shopping.flight_offers_search.get(
            originLocationCode=query.originLocationCode,
            destinationLocationCode=query.destinationLocationCode,
            departureDate=query.departureDate,
            returnDate=query.returnDate,
            adults=query.adults,
            max=query.max
        )

        flight_offers = []
        total_flight_price = 0.0

        for offer in flight_response.data:
            itineraries = []
            for itinerary in offer['itineraries']:
                segments = [
                    FlightSegment(
                        departureAirport=seg['departure']['iataCode'],
                        departureTime=seg['departure']['at'],
                        arrivalAirport=seg['arrival']['iataCode'],
                        arrivalTime=seg['arrival']['at'],
                        carrierCode=seg['carrierCode'],
                        flightNumber=seg['number'],
                        aircraftCode=seg.get('aircraft', {}).get('code'),
                        duration=seg['duration']
                    )
                    for seg in itinerary['segments']
                ]
                itineraries.append(FlightItinerary(duration=itinerary['duration'], segments=segments))

            price = FlightPrice(
                total=offer['price']['total'],
                currency=offer['price']['currency']
            )
            total_flight_price += float(price.total)

            flight_offers.append(FlightOffer(
                id=offer['id'],
                source=offer['source'],
                price=price,
                itineraries=itineraries
            ))

        currency = flight_offers[0].price.currency if flight_offers else "EUR"

        # ------------------------ HOTEL SEARCH ------------------------
        hotel_response = amadeus.reference_data.locations.hotels.by_city.get(cityCode=query.cityCode)

        hotels = []
        total_hotel_price = 0.0

        for h in hotel_response.data[:query.hotelLimit]:
            price = query.defaultHotelPrice * nights
            total_hotel_price += price
            hotels.append(HotelInfo(
                hotelId=h.get('hotelId', ''),
                name=h.get('name', ''),
                cityCode=h.get('cityCode', query.cityCode),
                latitude=h.get('geoCode', {}).get('latitude', 0.0),
                longitude=h.get('geoCode', {}).get('longitude', 0.0),
                available=True,
                price=round(price, 2),
                currency=currency,
                nights=nights
            ))

        # ------------------------ VEHICLE SIMULATION ------------------------
        city_vehicles = {
            "MAD": [("SEAT", "Ibiza"), ("Renault", "Clio"), ("BMW", "X1")],
            "BCN": [("Ford", "Focus"), ("Volkswagen", "Golf"), ("Tesla", "Model 3")],
            "PAR": [("Peugeot", "208"), ("Citroën", "C3"), ("Mercedes", "A-Class")]
        }
        vehicle_brands = city_vehicles.get(query.vehicleLocation.upper(), [("Toyota", "Corolla")])

        vehicles = []
        total_vehicle_price = 0.0

        for i in range(query.vehicleLimit):
            brand, model = random.choice(vehicle_brands)
            price_per_day = round(random.uniform(30, 100), 2)
            total_price = price_per_day * nights
            total_vehicle_price += total_price

            vehicles.append(VehicleInfo(
                vehicleId=f"{query.vehicleLocation[:3].upper()}-{i+1:03d}",
                name=f"{brand} {model}",
                cityCode=query.vehicleLocation,
                available=True,
                pricePerDay=price_per_day,
                currency=currency,
                vehicleType="economy",
                brand=brand,
                model=model,
                year=random.randint(2019, 2024),
                seats=5,
                doors=4,
                transmission=random.choice(["Manual", "Automatic"]),
                fuelType=random.choice(["Gasoline", "Electric"])
            ))

        # ------------------------ TOTAL COST SUMMARY ------------------------
        grand_total = total_flight_price + total_hotel_price + total_vehicle_price

        summary = TripCostSummary(
            flightTotal=round(total_flight_price, 2),
            hotelTotal=round(total_hotel_price, 2),
            vehicleTotal=round(total_vehicle_price, 2),
            currency=currency,
            grandTotal=round(grand_total, 2)
        )

        return TripSearchResponse(
            flights=flight_offers,
            hotels=hotels,
            vehicles=vehicles,
            summary=summary
        )

    except ResponseError as error:
        raise HTTPException(status_code=400, detail=f"Amadeus API error: {str(error)}")
    except Exception as error:
        raise HTTPException(status_code=500, detail=f"Internal server error: {str(error)}")
