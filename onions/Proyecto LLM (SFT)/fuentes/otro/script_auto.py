#  @file script.py
#  @brief Realiza búsqueda, evaluación, extracción y transformación de información académica relacionada con vulnerabilidades de 
#        seguridad informática y transformar los fichero json guardados a SET.
#  @author Hanae Afallah
#  @date 2025-05-30


import os
import sys
import subprocess
import time
import json
import requests

# Función para instalar librerías automáticamente si no están instaladas
def instalar_libreria(libreria):
    try:
        __import__(libreria)
        print(f"La librería '{libreria}' ya está instalada.")
    except ImportError:
        print(f"La librería '{libreria}' no está instalada. Instalando...")
        subprocess.check_call([sys.executable, "-m", "pip", "install", libreria])
        print(f"Librería '{libreria}' instalada correctamente.")

# Instalar requests
instalar_libreria("requests")

# Evalúa si un artículo cumple con los criterios de fiabilidad
def evaluar_fiabilidad(articulo):
    criterios = [
        articulo.get("Citas", 0) > 50,
        int(articulo.get("Año", 0)) >= 2018,
        "ieee" in articulo.get("Fuente", "").lower(),
        "acm" in articulo.get("Fuente", "").lower()
    ]
    return sum(criterios) >= 2

# Busca artículos en Semantic Scholar
def buscar_en_semantic_scholar(frases_clave, cantidad=5, reintentos=3, espera=10):
    url = "https://api.semanticscholar.org/graph/v1/paper/search"
    query = "vulnerabilities"

    params = {
        "query": query,
        "limit": 20,
        "fields": "title,abstract,authors,year,venue,citationCount,url"
    }

    for intento in range(1, reintentos + 1):
        print(f"Intento Semantic Scholar {intento} de {reintentos}...")
        try:
            response = requests.get(url, params=params)
            if response.status_code == 429:
                print("Demasiadas solicitudes. Esperando 10 segundos...")
                time.sleep(10)
                continue

            response.raise_for_status()
            data = response.json()
            articulos_raw = data.get("data", [])
            print(f"Se recibieron {len(articulos_raw)} artículos de Semantic Scholar.")

            articulos_fiables = []
            for item in articulos_raw:
                titulo = item.get("title", "")
                texto_extraido = item.get("abstract", "")
                
                # Filtrar artículos sin resumen o con resumen muy corto
                if not texto_extraido or len(texto_extraido.strip()) < 300:
                    print(f"[Descartado] Resumen muy corto o inexistente: '{titulo}'")
                    continue

                fuente = item.get("venue", "Desconocido")
                año = item.get("year", "")
                citas = item.get("citationCount", 0)
                enlace = item.get("url", "")
                autores = ", ".join([a.get("name", "Desconocido") for a in item.get("authors", [])])

                articulo = {
                    "Título": titulo,
                    "Autores": autores,
                    "Texto_extraido": texto_extraido,
                    "Año": año,
                    "Fuente": fuente,
                    "Citas": citas,
                    "Enlace": enlace
                }

                if evaluar_fiabilidad(articulo):
                    articulo["Fiable"] = True
                    print(" *** FIABLE ***:", titulo)
                    articulos_fiables.append(articulo)

                if len(articulos_fiables) >= cantidad:
                    break

            if articulos_fiables:
                return articulos_fiables

        except Exception as e:
            print(f"Error al consultar Semantic Scholar: {e}")

        if intento < reintentos:
            print(f"Reintentando en {espera} segundos...\n")
            time.sleep(espera)

    print("No se pudieron obtener artículos fiables de Semantic Scholar.")
    return []

# Guarda artículos en JSON
def guardar_en_json(data, ruta):
    with open(ruta, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=4)

# Transforma artículos a formato SFT
def transformar_a_sft(input_file, output_file):
    with open(input_file, "r", encoding="utf-8") as f:
        datos = json.load(f)

    sft_data = []

    for entrada in datos:
        cve_id = entrada.get("cve_id", "CVE-UNKNOWN")
        title = entrada.get("Título", "")
        desc = entrada.get("Texto_extraido", "")
        cvss = entrada.get("cvss_score", "")
        vector = entrada.get("cvss_vector", "")
        producto = ", ".join(entrada.get("products", [])) if "products" in entrada else ""
        protocolo = ", ".join(entrada.get("protocols", [])) if "protocols" in entrada else ""
        mitigacion = entrada.get("mitigation", "")
        tipo = entrada.get("vulnerability_type", "")
        exploit = "Sí" if entrada.get("exploit_available") else "No"

        input_text = (
            f"Título: {title}\n"
            f"CVÉ: {cve_id}\n"
            f"Descripción: {desc}\n"
            f"CVSS: {cvss}\n"
            f"Vector: {vector}\n"
            f"Productos: {producto}\n"
            f"Protocolos: {protocolo}\n"
            f"Tipo: {tipo}\n"
            f"Exploit disponible: {exploit}"
        )

        output_text = (
            f"Esta vulnerabilidad afecta a {producto}. El tipo de ataque es {tipo} y puede ser explotado a través de {protocolo}, con un score CVSS de {cvss}.\n"
            f"{desc if desc else ''}\n"
            f"{f'La mitigación recomendada es: {mitigacion}' if mitigacion else ''}"
        )

        sft_data.append({
            "instruction": f"Describe la vulnerabilidad {cve_id}",
            "input": input_text.strip(),
            "output": output_text.strip()
        })

    with open(output_file, "w", encoding="utf-8") as f:
        json.dump(sft_data, f, ensure_ascii=False, indent=2)

    print(f"Dataset SFT guardado en {output_file} con {len(sft_data)} ejemplos.")

def main():
    frases_clave = [
        "vulnerabilidades", "ciberseguridad", "seguridad cibernética", "ataques informáticos",
        "malware", "ransomware", "phishing", "amenazas persistentes avanzadas",
        "criptografía", "seguridad en redes", "seguridad en IoT", "fallos de seguridad",
        "brechas de seguridad", "debilidades del sistema", "exposiciones", "riesgos",
        "huecos de seguridad", "fugas de información"
    ]

    carpeta_resultados = "carpetaFichero"
    os.makedirs(carpeta_resultados, exist_ok=True)

    print("----> Buscando artículos fiables en Semantic Scholar...")
    articulos_fiables = buscar_en_semantic_scholar(frases_clave, cantidad=5)

    if not articulos_fiables:
        print(" No se encontraron artículos fiables.")
        return

    ruta_json = os.path.join(carpeta_resultados, "articulos_raw.json")
    guardar_en_json(articulos_fiables, ruta_json)
    print(f" ===> Artículos guardados en esta ruta: {ruta_json}")

    # Transformar a SFT
    output_sft = os.path.join(carpeta_resultados, "dataset_sft.json")
    transformar_a_sft(ruta_json, output_sft)

if __name__ == "__main__":
    main()