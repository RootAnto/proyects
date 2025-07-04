import subprocess
from pathlib import Path

def run_all_processes():
    fuentes = ["cve", "exploitDB", "otros"]

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


if __name__ == "__main__":
    run_all_processes()