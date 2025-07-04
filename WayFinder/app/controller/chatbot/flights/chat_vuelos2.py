from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from app.controller.chatbot.flights.extractor_vuelos import extraer_datos
from app.controller.chatbot.core.utils.functions import buscar_vuelos, es_afirmacion
from app.controller.chatbot.core.bussiness_info import info
from datetime import date
from app.config import OPENAI_API_KEY
import os
os.environ["OPENAI_API_KEY"] = OPENAI_API_KEY

template = """
Eres un asistente de viajes que responde en español.

Información de la empresa:
{bussiness_info}

Estado actual del vuelo:
origen: {origen}
destino: {destino}
fecha_salida: {fecha_salida}
fecha_regreso: {fecha_regreso}
tipo_pasajero: {tipo_pasajero}

Historial de conversación:
{context}

Usuario: {question}

Responde brevemente y sin repetir saludos.
Pregunta solo por los datos que faltan para completar la reserva.
Si el usuario pide recomendaciones, responde con sugerencias.
Ve fijándote los datos que faltan y pregúntalos en orden, de uno en uno.
Si por ejemplo la ciudad todavia no se sabe no preguntar por las fechas, a menos que el usuario te pregunte sobre fechas.
Preguntar cosa por cosa, no adelantarse y no parecer insistente.

Asistente:
"""

model = ChatOpenAI(model="gpt-4o", temperature=0.0)
prompt = ChatPromptTemplate.from_template(template)
chain = prompt | model


def procesar_mensaje_vuelo(question, datos_vuelo, context):
    nuevos_datos = extraer_datos(question)

    for campo, valor in nuevos_datos.items():
        if valor:
            datos_vuelo[campo] = valor

    fecha_salida_str = (
        datos_vuelo["fecha_salida"].strftime("%Y-%m-%d")
        if isinstance(datos_vuelo["fecha_salida"], date)
        else datos_vuelo["fecha_salida"]
    )
    fecha_regreso_str = (
        datos_vuelo["fecha_regreso"].strftime("%Y-%m-%d")
        if isinstance(datos_vuelo["fecha_regreso"], date)
        else datos_vuelo["fecha_regreso"]
    )

    prompt_input = {
        "bussiness_info": info,
        "context": context,
        "question": question,
        "origen": datos_vuelo.get("origen") or "no definido",
        "destino": datos_vuelo.get("destino") or "no definido",
        "fecha_salida": fecha_salida_str or "no definida",
        "fecha_regreso": fecha_regreso_str or "no definida",
        "tipo_pasajero": datos_vuelo.get("tipo_pasajero") or "no definido",
    }

    result = chain.invoke(prompt_input)
    respuesta = result.content if hasattr(result, "content") else str(result)

    nuevo_context = context + f"Tú: {question}\nBot: {respuesta}\n"

    vuelos = None
    reserva = False

    if datos_vuelo.get("origen") and datos_vuelo.get("destino") and datos_vuelo.get("fecha_salida") and datos_vuelo.get("tipo_pasajero"):
        resultado = buscar_vuelos(
            origen=datos_vuelo["origen"],
            destino=datos_vuelo["destino"],
            fecha_salida=fecha_salida_str,
            fecha_regreso=fecha_regreso_str,
            pasajeros=1,
            tipo_pasajero=datos_vuelo["tipo_pasajero"],
        )
        vuelos = resultado["mensaje"]
        datos_vuelo.update(resultado["datos"])
        if es_afirmacion(question):
            reserva = True

    return respuesta, nuevos_datos, nuevo_context, vuelos, reserva
