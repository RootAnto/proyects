# extractor_coches.py
from langchain.chat_models import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from langchain.output_parsers import PydanticOutputParser
from pydantic import BaseModel, Field
from typing import Optional
import os
from datetime import datetime
from langchain_openai import ChatOpenAI

from app.config import OPENAI_API_KEY  

import os
os.environ["OPENAI_API_KEY"] = OPENAI_API_KEY
class DatosCoche(BaseModel):
    ciudad: Optional[str] = Field(description="Código IATA de la ciudad donde se desea alquilar el coche")
    tipo_vehiculo: Optional[str] = Field(description="Tipo de coche: compacto, SUV, furgoneta, etc.")

parser = PydanticOutputParser(pydantic_object=DatosCoche)

template = """
Eres un extractor de datos para alquiler de coches. Extrae solo los datos válidos y devuélvelos en JSON. Aplica las siguientes reglas estrictas:

1. `ciudad`: extrae el **código IATA** si el usuario menciona claramente una ciudad. Por ejemplo, Madrid → MAD, Barcelona → BCN, París → PAR, Roma → ROM. Si no puedes identificar una ciudad, devuelve null.

2. `tipo_vehiculo`: extrae solo si el usuario menciona claramente un tipo de vehículo: “sedán”, “compacto”, “SUV”, “furgoneta”, “eléctrico”, etc. Si no está claro, devuelve null.

Devuelve solo el JSON en este formato exacto:


```json
{{
  "ciudad": string | null,
  "tipo_vehiculo": string | null
}}

{format_instructions}

Mensaje del usuario:
{input}
"""

chat_prompt = ChatPromptTemplate.from_template(template)

model = ChatOpenAI(
    model="gpt-4o",
    temperature=0.7
)

def extraer_datos_coche(mensaje_usuario: str):
    messages = chat_prompt.format_messages(
        input=mensaje_usuario,
        format_instructions=parser.get_format_instructions()
    )
    try:
        response = model(messages)
        salida_texto = response.content
        datos = parser.parse(salida_texto)
        return datos.dict()
    except Exception as e:
        print(f"[Error al extraer datos de coche]: {e}")
        return {
            "ciudad": None,
            "tipo_vehiculo": None
        }
