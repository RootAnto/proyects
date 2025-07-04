# 🧠 LLM de Ciberseguridad en Español

Este proyecto open source utiliza un modelo de lenguaje basado en **DeepSeek LLM 7B** entrenado con técnicas de **LoRA + cuantización 4-bit** para responder en español sobre temas de **ciberseguridad y vulnerabilidades**.

Funciona en máquinas con solo **8 GB de GPU** y se puede usar tanto por consola como con una **interfaz web amigable** (Gradio).

---

## 📁 Estructura del Proyecto

```
llm-ciberseguridad-es/
├── data/                      # Datos de entrenamiento en JSON
│   ├── fuente1.json
│   └── fuente2.json
├── modelo_finetune_lora/     # Modelo entrenado con LoRA
├── train.py                  # Script de entrenamiento
├── inferencia.py             # Chat por consola
├── chat_web.py               # Chat web con Gradio
├── requirements.txt          # Dependencias del entorno
└── README.md                 # Este documento
```

---

## 📚 Formato de los datos (data/*.json)

Cada archivo JSON debe tener el siguiente formato:

```json
{
  "instruccion": "Pregunta o tarea en español",
  "input": "Contexto opcional (puede ser vacío)",
  "output": "Respuesta esperada en lenguaje natural"
}
```

Ejemplo:

```json
{
  "instruccion": "Explica qué es una vulnerabilidad de tipo XSS",
  "input": "",
  "output": "Una vulnerabilidad XSS permite que un atacante inyecte scripts maliciosos..."
}
```

Coloca todos los archivos en la carpeta `/data`.

---

## 🚀 Entrenamiento del modelo

> Solo debes hacerlo si cambias los datos o quieres actualizar el modelo.

```bash
source venv/bin/activate     # Activa tu entorno virtual
python train.py              # Entrena el modelo con los JSON en /data
```

El modelo adaptado se guardará en la carpeta `modelo_finetune_lora/`.

---

## 💬 Uso en consola (terminal)

```bash
source venv/bin/activate
python inferencia.py
```

Esto abrirá un chat interactivo para hacer preguntas sobre ciberseguridad directamente desde la terminal.

---

## 🌐 Uso con interfaz web (Gradio)

```bash
source venv/bin/activate
python chat_web.py
```

Esto abrirá una interfaz gráfica accesible desde tu navegador en:  
👉 http://127.0.0.1:7860

---

## 📦 Requisitos

Instala las dependencias necesarias ejecutando:

```bash
pip install -r requirements.txt
```

### Contenido de `requirements.txt`:

```
transformers
peft
trl
datasets
bitsandbytes
accelerate
gradio
```

---

## ⚙️ Notas técnicas

- ✅ Modelo base: [`deepseek-ai/deepseek-llm-7b`](https://huggingface.co/deepseek-ai/deepseek-llm-7b)
- ✅ Cuantización 4-bit para reducir uso de memoria (via `bitsandbytes`)
- ✅ Finetuning con LoRA (parámetros ligeros adaptativos)
- ✅ Entrenamiento con `SFTTrainer` (supervised fine-tuning)

---

## 🔓 Licencia

MIT — Puedes usar, modificar y distribuir libremente este proyecto.

---



