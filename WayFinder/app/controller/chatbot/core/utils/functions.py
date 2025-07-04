import httpx
from datetime import datetime, date
from typing import Optional, Union
import requests
import json

def buscar_vuelos(origen, destino, fecha_salida, fecha_regreso, pasajeros, tipo_pasajero):
    print("📡 Función 'buscar_vuelos' llamada con los siguientes datos:")
    print(f"Origen: {origen}")
    print(f"Destino: {destino}")
    print(f"Fecha de salida: {fecha_salida}")
    print(f"Fecha de regreso: {fecha_regreso}")
    print(f"Número de pasajeros: {pasajeros}")
    print(f"Tipo de pasajero: {tipo_pasajero}")

    fecha_salida_iso = convertir_fecha(fecha_salida)
    if not fecha_salida_iso:
        return {"mensaje": "❌ Formato de fecha de salida inválido.", "datos": {}}

    fecha_regreso_iso = convertir_fecha(fecha_regreso) if fecha_regreso else None
    if fecha_regreso and not fecha_regreso_iso:
        return {"mensaje": "❌ Formato de fecha de regreso inválido.", "datos": {}}

    url = "http://localhost:8000/flight-search"

    payload = {
        "originLocationCode": origen.upper(),
        "destinationLocationCode": destino.upper(),
        "departureDate": fecha_salida_iso,
        "adults": pasajeros if pasajeros > 0 else 1,
        "max": 1
    }

    if fecha_regreso_iso:
        payload["returnDate"] = fecha_regreso_iso

    try:
        with httpx.Client() as client:
            response = client.post(url, json=payload)
            response.raise_for_status()
            data = response.json()

            if data.get("success") and data["offers"]:
                offer = data["offers"][0]
                precio = offer['price']['total']
                moneda = offer['price']['currency']
                duracion = offer['itineraries'][0]['duration']
                aerolinea = offer.get('validatingAirlineCodes', ["Desconocida"])[0]
                numero_vuelo = offer.get("number", "No disponible")

                mensaje = (
                    f"✈️ Vuelo encontrado:\n"
                    f"- Precio: {precio} {moneda}\n"
                    f"- Duración: {duracion}\n"
                    f"- Aerolínea: {aerolinea}\n"
                    f"- Número de vuelo: {numero_vuelo}"
                )

                datos = {
                    "precio": precio,
                    "moneda": moneda,
                    "duracion": duracion,
                    "aerolinea": aerolinea,
                    "numero_vuelo": numero_vuelo,
                }

                return {"mensaje": mensaje, "datos": datos}
            else:
                return {"mensaje": "❌ No se encontraron vuelos disponibles.", "datos": {}}

    except httpx.HTTPStatusError as e:
        return {"mensaje": f"❌ Error al buscar vuelos: {e.response.text}", "datos": {}}
    except Exception as e:
        return {"mensaje": f"❌ Error interno: {str(e)}", "datos": {}}

def buscar_hoteles(ciudad_codigo,
                   fecha_checkin=None,
                   fecha_checkout=None,
                   limite=5,
                   precio_defecto=100.0):
    print("📡 Función 'buscar_hoteles' llamada con los siguientes datos:")
    print(f"Ciudad código (IATA): {ciudad_codigo}")
    print(f"Fecha check‑in: {fecha_checkin}")
    print(f"Fecha check‑out: {fecha_checkout}")
    print(f"Límite de resultados: {limite}")
    print(f"Precio por defecto: {precio_defecto} EUR")

    url = "http://localhost:8000/hotel-search"
    payload = {
        "cityCode": ciudad_codigo.upper(),
        "checkInDate": convertir_fecha(fecha_checkin) if fecha_checkin else None,
        "checkOutDate": convertir_fecha(fecha_checkout) if fecha_checkout else None,
        "limit": 1,
    }
    payload = {k: v for k, v in payload.items() if v is not None}

    try:
        with httpx.Client() as client:
            response = client.post(url, json=payload)
            response.raise_for_status()
            data = response.json()

            if data.get("count", 0) > 0 and data["data"]:
                hotel = data["data"][0]
                nombre   = hotel.get("name", "Desconocido")
                precio   = float(hotel.get("price", precio_defecto))
                noches   = int(hotel.get("nights", 1))

                mensaje = (
                    f"🏨 Hotel encontrado:\n"
                    f"- Nombre: {nombre}\n"
                    f"- Precio total aprox: {precio:.2f} EUR\n"
                    f"- Noches: {noches}"
                )
                datos = {
                    "nombre_hotel": nombre,
                    "precio_hotel": precio,
                    "noches": noches,
                }
                return {"mensaje": mensaje, "datos": datos}

            return {"mensaje": "❌ No se encontraron hoteles disponibles.", "datos": {}}

    except httpx.HTTPStatusError as e:
        return {"mensaje": f"❌ Error al buscar hoteles: {e.response.text}", "datos": {}}
    except Exception as e:
        return {"mensaje": f"❌ Error interno: {str(e)}", "datos": {}}

def buscar_coches(ciudad_codigo: str,
                  tipo_vehiculo: str | None = None,
                  limite: int = 5):
    """
    Consulta la API local /vehicle-search y devuelve un dict:
    {
        "mensaje": str   -> texto listo para el chat
        "datos":   dict  -> detalles (precio, modelo, días, …)
    }
    """
    print("📡 Función 'buscar_coches' llamada con los siguientes datos:")
    print(f"Ciudad código (IATA): {ciudad_codigo}")
    print(f"Tipo de vehículo: {tipo_vehiculo}")
    print(f"Límite de resultados: {limite}")

    url = "http://localhost:8000/vehicle-search"
    payload = {
        "location": ciudad_codigo.upper(),
        "vehicleType": tipo_vehiculo if tipo_vehiculo else "car",
        "limit": limite,
    }

    try:
        with httpx.Client() as client:
            response = client.post(url, json=payload)
            response.raise_for_status()
            data = response.json()

            if data.get("count", 0) > 0 and data["data"]:
                vehicle = data["data"][0]
                nombre       = vehicle.get("name", "Desconocido")
                precio_dia   = float(vehicle.get("pricePerDay", 0.0))
                moneda       = vehicle.get("currency", "EUR")
                transmision  = vehicle.get("transmission", "N/A")
                combustible  = vehicle.get("fuelType", "N/A")
                año          = vehicle.get("year", "N/A")

                mensaje = (
                    f"🚘 Coche encontrado:\n"
                    f"- Modelo: {nombre}\n"
                    f"- Precio por día: {precio_dia:.2f} {moneda}\n"
                    f"- Año: {año}\n"
                    f"- Transmisión: {transmision}\n"
                    f"- Combustible: {combustible}"
                )
                datos = {
                    "nombre_coche": nombre,
                    "precio_coche": precio_dia,
                    "moneda": moneda,
                    "transmision": transmision,
                    "combustible": combustible,
                    "anio": año,
                }
                return {"mensaje": mensaje, "datos": datos}

            return {"mensaje": "❌ No se encontraron coches disponibles.", "datos": {}}

    except httpx.HTTPStatusError as e:
        return {"mensaje": f"❌ Error al buscar coches: {e.response.text}", "datos": {}}
    except Exception as e:
        return {"mensaje": f"❌ Error interno: {str(e)}", "datos": {}}
    
def convertir_fecha(fecha_str: Optional[Union[str, date]]) -> Optional[str]:
    if not fecha_str:
        return None

    if isinstance(fecha_str, (datetime, date)):
        return fecha_str.strftime("%Y-%m-%d")

    try:
        return datetime.strptime(fecha_str, "%d/%m/%Y").strftime("%Y-%m-%d")
    except ValueError:
        pass
    
    try:
        return datetime.strptime(fecha_str, "%d/%m").replace(year=datetime.now().year).strftime("%Y-%m-%d")
    except ValueError:
        pass
    
    try:
        datetime.strptime(fecha_str, "%Y-%m-%d")
        return fecha_str
    except ValueError:
        return None
    

from langchain.schema import HumanMessage
from langchain.chat_models import ChatOpenAI

model = ChatOpenAI(model="gpt-4o", temperature=0.0)

def es_afirmacion(mensaje: str) -> bool:
    """Determina si un mensaje es afirmativo usando el modelo de lenguaje."""
    prompt_afirmacion = f"""
¿El siguiente mensaje es una respuesta afirmativa a una pregunta?  
Responde solo "sí" o "no".

Mensaje: "{mensaje}"
"""
    response = model.invoke([HumanMessage(content=prompt_afirmacion)])
    return response.content.strip().lower() in ["sí", "si"]



def enviar_reserva_backend(datos_vuelo: dict,
                            datos_hotel: dict,
                            datos_coche: dict,
                            user) -> Optional[dict]:
    """Envía la reserva al endpoint /trips/ y devuelve la respuesta JSON."""
    url = "http://localhost:8000/trips/"

    def to_iso(d):
        if d is None:
            return None
        if isinstance(d, (date, datetime)):
            return d.isoformat()[:10]
        return str(d)

    aerolinea = datos_vuelo.get("aerolinea")
    num_vuelo = datos_vuelo.get("numero_vuelo")
    flight_name = None
    if aerolinea and num_vuelo and aerolinea != "Desconocida" and num_vuelo != "No disponible":
        flight_name = f"{aerolinea} {num_vuelo}"

    flight_price = float(datos_vuelo["precio"]) if datos_vuelo.get("precio") else None

    hotel_name  = datos_hotel.get("nombre_hotel")
    hotel_price = float(datos_hotel["precio_hotel"]) if datos_hotel.get("precio_hotel") else None
    hotel_nights = datos_hotel.get("noches")

    vehicle_model  = datos_coche.get("nombre_coche")
    vehicle_price  = float(datos_coche["precio_coche"]) if datos_coche.get("precio_coche") else None
    vehicle_days   = datos_coche.get("dias")

    total_price = sum(p for p in [flight_price, hotel_price, vehicle_price] if p)

    payload = {
        "user_id": user.nombre,
        "user_email": user.email,

        "origin": datos_vuelo.get("origen"),
        "destination": datos_vuelo.get("destino"),
        "departure_date": to_iso(datos_vuelo.get("fecha_salida")),
        "return_date":    to_iso(datos_vuelo.get("fecha_regreso")),
        "adults": 1,
        "children": 0,
        "hotel_limit": 5,
        "vehicle_limit": 5,
        "max_price": None,
        "user_name": None,

        "flight_id": None,
        "hotel_id": None,
        "vehicle_id": None,

        "flight_name":  flight_name,
        "flight_price": flight_price,

        "hotel_name":   hotel_name,
        "hotel_price":  hotel_price,
        "hotel_nights": hotel_nights,

        "vehicle_model": vehicle_model,
        "vehicle_price": vehicle_price,
        "vehicle_days":  vehicle_days,

        "total_price": total_price,
        "currency": datos_vuelo.get("moneda", "EUR"),
    }

    payload = {k: v for k, v in payload.items() if v is not None}

    print("Payload que se enviará a /trips/:")
    print(json.dumps(payload, indent=2, ensure_ascii=False))

    try:
        resp = requests.post(
            url,
            params={"user_email": user.email},   
            json=payload,                      
            timeout=10
        )
        resp.raise_for_status()
        return resp.json()

    except requests.RequestException as e:
        print("Error al enviar la reserva:", e)
        return None