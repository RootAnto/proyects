# AUTOMATIZACIÓN DEL PROCESO DE TRANSFORMACIÓN DATOS(SFT) PARA ENTRENAMIENTO DEL LLM

## OBJETIVO

Crear una estructura de scripts automatizados para que cualquier persona pueda ejecutar todo el proceso de extracción y transformación de datos de una vez, sin tener que hacerlo paso a paso y manualmente.


En la `FASE 3: TRANSFORMACION AL FORMATO SFT` definimos cómo transformar vulnerabilidades al formato SFT necesario para entrenar el modelo. Cada fuente (CVE, MITRE ICS, ExploitDB, Google Scholar, ACM, etc.) ha preparado un script para extraer y transformar los datos a JSON y SFT.

Ahora, en esta misma fase, lo que hemos hecho para continuar es:
- Establecer la estructura de esta parte del proyecto, es decir dónde se guardan los scripts y archivos.

- Añadir scripts automáticos para cada fuente.

- Crear un script general (run_all.py) que los ejecute todos de forma ordenada y sin errores.

## ESTRUCTURA DEL PROYECTO
``` 
    proyecto/
    │
    ├── run_all.py                      # Script que ejecuta todos los scripts automaticos de las fuentes
    ├── data/                           # Carpeta donde se guarda el resultado final (.json por fuente)
    │
    └── fuentes/
        ├── cve/
        │   └── script_auto.py          # Script automático de extracción + transformación 
        ├── exploitdb/
        │   └── script_auto.py
        └── ...                         # Otras fuentes    
``` 


## ¿CÓMO FUNCIONA script_auto.py DE CADA FUENTE?

Lo que hace cada script_auto.py es:

- Si necesita algo para funcionar (como una librería), el propio script se encarga de instalarlo.

- Extrae los datos de su fuente.

- Guarda los datos en formato .json.

- Transforma los datos al formato SFT.

- Guarda el resultado final en la carpeta data.

Esto permite que el script run_all.py no tenga que preocuparse por instalar cosas o preparar nada. Solo ejecuta y recoge los resultados.


## ¿CÓMO FUNCIONA run_all.py?

El script run_all.py:

- Ejecuta todos los script_auto.py de las fuentes de la lista.

- Verifica si se generó correctamente un archivo JSON final para cada una.

``` 
def run_all_processes():
    fuentes = ["cve", "exploitDB", "mitre"]

    base_dir = Path(__file__).resolve().parent
    data_dir = base_dir / "data"

    data_dir.mkdir(exist_ok=True)

    for fuente in fuentes:
        print(f"Procesando: {fuente}")
        script_path = base_dir / "fuentes" / fuente / "script_auto.py"
        output_file = data_dir / f"{fuente}.json"

        if script_path.exists():
            try:
                subprocess.run(["python3", str(script_path)], check=True)

                if output_file.exists():
                    print(f"{fuente}.json generado correctamente.\n")
                else:
                    print(f"El script se ejecutó pero no se generó {fuente}.json\n")

            except subprocess.CalledProcessError:
                print(f"Error al ejecutar el script de {fuente}\n")
        else:
            print(f"No se encontró el script: {script_path}\n")
``` 

## CONCLUSIÓN

En conclusión, esto nos permite automatizar por completo la generación de datasets con formato SFT para todas las fuentes.
Además gracias a esta automatización, el proceso es más facil de realizar ya que no se tiene que ir haciendo paso por paso.

El siguiente paso será preparar el fine-tuning final del modelo.