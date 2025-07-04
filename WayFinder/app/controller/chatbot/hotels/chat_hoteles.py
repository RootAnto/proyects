import os
from langchain.chat_models import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from app.controller.chatbot.hotels.extractor_hoteles import extraer_datos_hotel
from app.controller.chatbot.core.utils.functions import buscar_hoteles, es_afirmacion
from app.controller.chatbot.core.bussiness_info import info
from app.config import OPENAI_API_KEY
import os
os.environ["OPENAI_API_KEY"] = OPENAI_API_KEY
with open("app/controller/chatbot/hotels/template_hoteles.txt", "r", encoding="utf-8") as f:
    template = f.read()

model = ChatOpenAI(model="gpt-4o", temperature=0.0)
prompt = ChatPromptTemplate.from_template(template)
chain = prompt | model


def procesar_mensaje_hotel(mensaje: str, datos_actuales: dict, contexto: str):
    nuevos_datos = extraer_datos_hotel(mensaje)

    for k, v in nuevos_datos.items():
        if v:
            datos_actuales[k] = v

    prompt_input = {
        "bussiness_info": info,
        "context": contexto,
        "question": mensaje,
        "ciudad":         datos_actuales.get("ciudad", "no definido"),
        "fecha_entrada":  datos_actuales.get("fecha_checkin", "no definida"),
        "fecha_salida":   datos_actuales.get("fecha_checkout", "no definida"),
        "personas":       str(datos_actuales.get("personas", "1")),
        "tipo_habitacion": datos_actuales.get("tipo_habitacion", "estándar"),
    }

    respuesta_llm = chain.invoke(prompt_input)
    respuesta = respuesta_llm.content if hasattr(respuesta_llm, "content") else str(respuesta_llm)
    nuevo_contexto = contexto + f"Tú: {mensaje}\nBot: {respuesta}\n"

    hoteles_msg = None
    reservar = False

    if all([datos_actuales.get("ciudad"),
            datos_actuales.get("fecha_checkin"),
            datos_actuales.get("fecha_checkout")]):

        resultado = buscar_hoteles(
            ciudad_codigo = datos_actuales["ciudad"],
            fecha_checkin = datos_actuales["fecha_checkin"],
            fecha_checkout= datos_actuales["fecha_checkout"],
        )
        hoteles_msg = resultado["mensaje"]
        datos_actuales.update(resultado["datos"])
        nuevo_contexto += f"Bot: {hoteles_msg}\n"

    if es_afirmacion(mensaje):
        reservar = True

    return respuesta, nuevos_datos, nuevo_contexto, hoteles_msg, reservar
