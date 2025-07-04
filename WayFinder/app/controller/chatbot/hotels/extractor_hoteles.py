from langchain.chat_models import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from langchain.output_parsers import PydanticOutputParser
from pydantic import BaseModel, Field
from typing import Optional
import os
from datetime import datetime, timedelta
import re
from langchain_openai import ChatOpenAI
from app.config import OPENAI_API_KEY  
import os
os.environ["OPENAI_API_KEY"] = OPENAI_API_KEY
class DatosHotel(BaseModel):
    ciudad: Optional[str] = Field(description="Ciudad donde se desea reservar el hotel")
    fecha_checkin: Optional[str] = Field(description="Fecha de entrada al hotel en formato YYYY-MM-DD")
    fecha_checkout: Optional[str] = Field(description="Fecha de salida del hotel en formato YYYY-MM-DD")
    personas: Optional[int] = Field(description="Número de personas que se hospedarán, si está especificado")
    tipo_habitacion: Optional[str] = Field(description="Tipo de habitación si se menciona, como estándar, doble, suite, etc.")

parser = PydanticOutputParser(pydantic_object=DatosHotel)
template = """
Eres un extractor de datos para reservas de hotel. Extrae solo la información mencionada explícitamente por el usuario.

Devuelve los datos en este formato JSON exacto (y sin explicaciones adicionales):
json

  "ciudad": string | null,
  "fecha_checkin": "YYYY-MM-DD" | null,
  "fecha_checkout": "YYYY-MM-DD" | null,
Reglas:
- Devolver automaticamente el CODIGO IATA del aeropuerto de la ciudad que se nombra. 
Convierte fechas relativas ("mañana", "el lunes próximo", "5 días después del 10 de julio") al formato YYYY-MM-DD.

Si no puedes interpretar una fecha o dato, deja null.

Si se menciona "una semana", asume 7 noches desde la fecha de entrada.

Si se menciona "una habitación doble", pon "tipo_habitacion": "doble", etc.

{format_instructions}
Mensaje del usuario:
{input}
"""

chat_prompt = ChatPromptTemplate.from_template(template)

model = ChatOpenAI(
model="gpt-4o",
temperature=0
)

def extraer_datos_hotel(mensaje_usuario: str):
    messages = chat_prompt.format_messages(
        input=mensaje_usuario,
        format_instructions=parser.get_format_instructions()
    )
    try:
        response = model.invoke(messages) 
        salida_texto = response.content
        datos = parser.parse(salida_texto)
        return datos.dict()
    except Exception as e:
        print(f"[Error al extraer datos con GPT]: {e}")
        return {
            "ciudad": None,
            "fecha_checkin": None,
            "fecha_checkout": None,
            "personas": None,
            "tipo_habitacion": None
        }
