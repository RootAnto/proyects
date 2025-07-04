# extractor_chatgpt.py
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
class DatosVuelo(BaseModel):
    origen: Optional[str] = Field(description="Ciudad o aeropuerto de origen")
    destino: Optional[str] = Field(description="Ciudad o aeropuerto de destino")
    fecha_salida: Optional[str] = Field(description="Fecha de salida en formato DD/MM/AAAA")
    fecha_regreso: Optional[str] = Field(description="Fecha de regreso en formato DD/MM/AAAA, puede ser None")
    tipo_pasajero: Optional[str] = Field(description="adulto o niño")

parser = PydanticOutputParser(pydantic_object=DatosVuelo)

template = """
Eres un extractor de datos de vuelo. Extrae solo los datos válidos y devuélvelos en JSON. Aplica las siguientes reglas estrictas:

1. `origen` y `destino`: extrae solo si se menciona claramente una ciudad o aeropuerto real.
    - Si el usuario menciona estar viviendo en una ciudad o encontrarse actualmente en un lugar (por ejemplo, "estoy en Tokyo" o "vivo en Madrid"), considera ese lugar como el origen del vuelo.
    - Devolver automaticamente el CODIGO IATA del aeropuerto de la ciudad que se nombra. 
2. `fecha_salida` y `fecha_regreso`: interpreta tanto fechas absolutas ("15 de septiembre", "el 1 de agosto") como relativas ("mañana", "el viernes que viene", "una semana después").
   - Convierte todas las fechas al formato exacto `YYYY-MM-DD`.
   - Convierte expresiones relativas de fechas como "el primer viernes de agosto", "el próximo lunes", "el segundo domingo de septiembre", etc., al formato exacto YYYY-MM-DD
   - Si no puedes interpretar una fecha concreta, pon `null`.
   - Si se menciona una fecha de regreso relativa (como "10 días después de la salida"), calcula la fecha exacta usando la fecha de salida como referencia y conviértela al formato `YYYY-MM-DD`.
3. `tipo_pasajero`: extrae solo si se menciona claramente “adulto” o “niño”. Cualquier otro caso, pon `null`.

Devuelve solo el JSON en este formato exacto:

```json
{{
  "origen": string | null,
  "destino": string | null,
  "fecha_salida": "YYYY-MM-DD" | null,
  "fecha_regreso": "YYYY-MM-DD" | null,
  "tipo_pasajero": "adulto" | "niño" | null
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

def extraer_datos(mensaje_usuario: str):
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
        print(f"[Error al extraer datos con GPT]: {e}")
        return {
            "origen": None,
            "destino": None,
            "fecha_salida": None,
            "fecha_regreso": None,
            "tipo_pasajero": None
        }

def convertir_fecha(fecha_str):
    try:
        return datetime.strptime(fecha_str, "%d/%m").replace(year=datetime.today().year).strftime("%Y-%m-%d")
    except ValueError:
        return None