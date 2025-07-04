import requests
import os
import zipfile
from pathlib import Path
import json
import threading
import subprocess
import sys
#import pkg_resources

REQUIRED_PACKAGES = [
    'requests>=2.25.1'
]

#Instala las dependencias necesarias si no están presentes.
def instalar_dependencias():
   def instalar_dependencia(paquete):
    """Intenta importar un paquete y lo instala si no está disponible."""
    try:
        __import__(paquete)
    except ImportError:
        print(f"Instalando {paquete}...")
        try:
            subprocess.check_call([sys.executable, "-m", "pip", "install", paquete])
            print(f"{paquete} instalado correctamente.")
        except subprocess.CalledProcessError as e:
            print(f"Error al instalar {paquete}: {e}")
            sys.exit(1)

    for paquete in REQUIRED_PACKAGES:
        instalar_dependencia(paquete)

#Descarga un archivo desde una URL y lo guarda el proyecto
#url (str): Enlace directo al archivo.
#   nombre_guardado (str, opcional): Nombre del archivo local. Si es None, se usará el de la URL.
#   mostrar_progreso (bool): Muestra una barra de progreso si es True.
def descargar_archivo(url, nombre_guardado=None):
    
    try:
        # Configurar el nombre del archivo si no se especifica
        if nombre_guardado is None:
            nombre_guardado = url.split('/')[-1]  # Extrae el nombre de la URL

        # Conexión a la URL con stream para descarga en bloques
        with requests.get(url, stream=True) as respuesta:
            respuesta.raise_for_status()  # Lanza error si la descarga falla

            # Tamaño total del archivo (para la barra de progreso)
            tamano_total = int(respuesta.headers.get('content-length', 0))

            # Modo de escritura binaria
            with open(nombre_guardado, 'wb') as archivo:
                # Descarga sin barra de progreso
                for chunk in respuesta.iter_content(chunk_size=8192):
                    archivo.write(chunk)

        print(f"\n¡Descarga completada! Archivo guardado como: {nombre_guardado}")
        return nombre_guardado

    except Exception as e:
        print(f"\nError al descargar el archivo: {e}")
        if os.path.exists(nombre_guardado):
            os.remove(nombre_guardado)  # Elimina archivo parcial si existe
        return None

#Descomprime el archivo zip en el directorio
#   archivo_comprimido (str): Ruta al archivo ZIP.
#   directorio_destino (str): Directorio donde se extraerán los archivos.
def descomprimir_archivo(archivo_comprimido, directorio_destino):
    
    try:
        # Crear directorio si no existe
        os.makedirs(directorio_destino, exist_ok=True)
        
        with zipfile.ZipFile(archivo_comprimido, "r") as archivo_zip:
            # Extrae todos los archivos y carpetas al destino especificado
            archivo_zip.extractall(directorio_destino)
        print(f"Archivo {archivo_comprimido} descomprimido exitosamente en {directorio_destino}")
    except FileNotFoundError:
        print(f"El archivo {archivo_comprimido} no existe.")
    except zipfile.BadZipFile:
        print(f"El archivo {archivo_comprimido} no es un ZIP válido.")
    except Exception as e:
        print(f"Ocurrió un error durante la extracción: {e}")

#Procesa un archivo JSON individual en un hilo.
#   file_path (Path): Ruta del archivo JSON.
#   datos_consolidados (list): Lista compartida donde se almacenan los datos procesados.
#   lock (threading.Lock): Bloqueo para acceso seguro a la lista compartida.
def procesar_archivo(file_path, datos_consolidados, lock):
    
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            datos = json.load(f)
            datos_transformados = transformar_json(datos)

            if not datos_transformados:
                print(f"{file_path} no incluido porque fue rechazado por el CVE")
            else:
                # Bloqueo para evitar problemas de concurrencia
                with lock:
                    datos_consolidados.append(datos_transformados)

    except json.JSONDecodeError:
        print(f"Advertencia: {file_path} no es un JSON válido. Se omitirá.")
    except Exception as e:
        print(f"Error procesando {file_path}: {e}")

#Consolida múltiples archivos JSON en paralelo usando hilos.
#   directorio_base (str): Directorio raíz donde se encuentran los archivos JSON.
#   archivo_salida (str): Nombre del archivo de salida consolidado.
def consolidar_json(directorio_base, archivo_salida):
    
    try:
        root_dir = Path(directorio_base)
        archivos_json = list(root_dir.rglob("*.json"))
        total_archivos = len(archivos_json)

        print(f"Procesando {total_archivos} archivos JSON...")

        datos_consolidados = []
        lock = threading.Lock()  # Evita condiciones de carrera

        # Crear y lanzar hilos
        hilos = []
        for file_path in archivos_json:
            hilo = threading.Thread(target=procesar_archivo, args=(file_path, datos_consolidados, lock))
            hilos.append(hilo)
            hilo.start()

        # Esperar a que todos los hilos terminen
        for hilo in hilos:
            hilo.join()

        # Guardar todos los datos en un archivo JSON
        with open(archivo_salida, 'w', encoding='utf-8') as f:
            json.dump(datos_consolidados, f, indent=2, ensure_ascii=False)

        print(f"\nConsolidación completada. Se procesaron {total_archivos} archivos JSON.")
        print(f"Archivo guardado como: {archivo_salida}")

    except Exception as e:
        print(f"Error en la consolidación: {e}")


#Transforma una lista de un CVE en un formato estructurado.
#   archivo_json (list): lista con los datos de una vulnerabilidad CVE.
def transformar_json(archivo_json:list):
    archivo = []
    cvss_veriones = {}
    text_versiones = str()
    input_cvss = str()


    estado = archivo_json["cveMetadata"]["state"]
    #Solo se guardan los archivos JSON que han sido publicados en el registro CVE
    if estado == "PUBLISHED":
        title, description, cvss_score, vector, tipo, producto, solucion, exploit = "NONE", "NONE", "NONE",  "NONE", "NONE", "NONE","NONE", "NONE"

        cve_id = archivo_json["cveMetadata"]["cveId"]
        cna = archivo_json.get("containers", {}).get("cna", {"NONE"})

        if isinstance(cna, dict):
            title = archivo_json["containers"]["cna"].get("title","NONE")
            description = archivo_json.get("containers", {}).get("cna", {"NONE"}).get("descriptions", [{}])[0].get("value", "none description")
            metrics = archivo_json.get("containers", {}).get("cna", {}).get("metrics", [{}])
            cvss_versions = ["cvssV4_0", "cvssV3_1", "cvssV3_0", "cvssV2_0"]
            #Las versiones CVSS posibles del archivo
            versiones = []
            
            for metric in metrics:
                for version in cvss_versions:
                    if version in metric and isinstance(metric[version], dict):
                        cvss_score = metric[version].get("baseScore", "NONE")
                        vector = metric[version].get("vectorString", "NONE")
                        tipo = metric[version].get("baseSeverity", "NONE")
                       
                        versiones.append({
                            "version": version,
                            "cvss_score": cvss_score,
                            "vector": vector,
                            "tipo": tipo
                        })  
                        input_cvss += f" Version {version}: cvss_score {cvss_score}, tipo de vulnerabilidad {tipo} \n"
                        text_versiones += f"Version {version} con una puntuación base de {cvss_score}\n"
            if not versiones : versiones = "NONE"

            producto = archivo_json.get("containers", {}).get("cna", {}).get("affected", [{"NONE"}])[0].get("product", "NONE")
            solucion = archivo_json.get("containers", {}).get("cna", {}).get("solutions", [{}])[0].get("value", "NONE")
        
        adp = archivo_json.get("containers", {}).get("adp", {"NONE"})
        if  isinstance(adp, dict):
            exploit = archivo_json.get("containers", {}).get("adp", {})[0].get("exploits", "NONE")

        input_text = f"""Título: {title}
                        CVE: {cve_id}
                        CVSS: {input_cvss}
                        Vector: {vector}
                        Productos: {producto}
                        Descripción: {description}
                        Exploit disponible: {exploit}"""
        
        output_text = f"""La vulnerabilidad detectada afecta a {producto} y presenta los siguientes niveles de severidad:
                    {text_versiones.strip()}.
                   {f"Descripción (en): {description}" if description else ""}  
                   {f"Accion recomendada: {solucion}" if solucion else ""}"""
        
        archivo.append({
            "instruction": f"Describe la vulnerabilidad {cve_id}",
            "input": input_text.strip(),
            "output": output_text.strip()        
        })
    
    return archivo


if __name__ == "__main__":
    instalar_dependencias()
    
    # Configuración
    url = "https://github.com/CVEProject/cvelistV5/archive/refs/heads/main.zip"
    nombre_zip = "Descarga_list.zip"
    directorio_descompresion = "cvelistV5-main"
    directorio_final = "./Data"  #Directorio donde se guardara el archivo final
    archivo_json_final = os.path.join(directorio_final, "cve_list.json")
    
    try:
        # Paso 1: Descargar el archivo
        print("Iniciando descarga...")
        archivo_descargado = descargar_archivo(url, nombre_zip)
        
        if not archivo_descargado:
            raise Exception("La descarga falló. No se puede continuar.")
        
        # Paso 2: Descomprimir el archivo
        print("\nDescomprimiendo archivo...")
        descomprimir_archivo(archivo_descargado, directorio_descompresion)

        # Paso 3: Une todos los archivos JSONs
        print("\nConsolidando archivos JSON...")
        consolidar_json(os.path.join(directorio_descompresion,"cvelistV5-main"), archivo_json_final)
        
    except Exception as e:
        print(f"\nError en el proceso principal: {e}")