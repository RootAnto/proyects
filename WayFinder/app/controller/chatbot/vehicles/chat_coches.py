import os
from langchain.chat_models import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from app.controller.chatbot.vehicles.extractor_coches import extraer_datos_coche
from app.controller.chatbot.core.utils.functions import buscar_coches, es_afirmacion
from app.controller.chatbot.core.bussiness_info import info
from app.config import OPENAI_API_KEY

os.environ["OPENAI_API_KEY"] = OPENAI_API_KEY

with open("app/controller/chatbot/vehicles/template_coches.txt", "r", encoding="utf-8") as f:
    template = f.read()

model  = ChatOpenAI(model="gpt-4o", temperature=0.0)
prompt = ChatPromptTemplate.from_template(template)
chain  = prompt | model


def actualizar_datos_coche(datos_actuales: dict, nuevos: dict) -> dict:
    for k, v in nuevos.items():
        if v:
            datos_actuales[k] = v
    return datos_actuales


def procesar_mensaje_coche(
    mensaje: str,
    datos_actuales: dict,
    contexto: str,
    datos_vuelo: dict | None = None,
    datos_hotel: dict | None = None,
):
    """
    Devuelve (respuesta, nuevos_datos, nuevo_contexto, coches_msg, reserva_bool)
    """
    nuevos_datos = extraer_datos_coche(mensaje)
    actualizar_datos_coche(datos_actuales, nuevos_datos)

    if not datos_actuales.get("ciudad"):
        datos_actuales["ciudad"] = (
            (datos_hotel or {}).get("ciudad")
            or (datos_vuelo  or {}).get("destino")
            or "no definida"
        )

    prompt_input = {
        "bussiness_info": info,
        "context":  contexto,
        "question": mensaje,
        "ciudad":   datos_actuales.get("ciudad", "no definida"),
        "tipo_vehiculo": datos_actuales.get("tipo_vehiculo", "cualquiera"),
    }

    llm_res = chain.invoke(prompt_input)
    respuesta = llm_res.content if hasattr(llm_res, "content") else str(llm_res)
    nuevo_contexto = contexto + f"Tú: {mensaje}\nBot: {respuesta}\n"

    coches_msg = None
    reservar   = False

    if datos_actuales.get("ciudad") and datos_actuales.get("tipo_vehiculo"):
        resultado = buscar_coches(
            ciudad_codigo = datos_actuales["ciudad"],
            tipo_vehiculo = datos_actuales.get("tipo_vehiculo"),
            limite = 5,
        )
        coches_msg = resultado["mensaje"]
        datos_actuales.update(resultado["datos"])
        nuevo_contexto += f"Bot: {coches_msg}\n"

    if es_afirmacion(mensaje):
        reservar = True

    return respuesta, nuevos_datos, nuevo_contexto, coches_msg, reservar
