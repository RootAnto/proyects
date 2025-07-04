# Wayfinder

**Wayfinder** es una iniciativa tecnológica enfocada en facilitar la exploración, descubrimiento y conexión de comunidades, servicios y experiencias a través de una plataforma digital intuitiva. Nuestro objetivo es empoderar a las personas con herramientas que les permitan orientarse en su entorno de forma eficiente, personalizada y significativa.

---

## Propósito

Wayfinder nace con el propósito de:
- **Conectar personas** con espacios, eventos y servicios relevantes en su entorno.
- **Ofrecer experiencias personalizadas** basadas en intereses y ubicación.
- **Facilitar la toma de decisiones** mediante herramientas interactivas y datos relevantes.
- **Promover la inclusión digital**, diseñando interfaces accesibles y centradas en el usuario.

---

## Arquitectura del Proyecto

Esta aplicación está dividida en dos partes:
- **Frontend**: Construido con React.js
- **Backend**: Desarrollado con FastAPI en Python

Ambas deben ejecutarse por separado en consolas distintas.

Para ejecutar la aplicacion por el lado del frontend seguir los siguientes pasos.
1- Moverse al directorio cd frontend
2- Ejecutar el comando "npm install" para las dependencias necesarias.
3- Ejecutar el comando "npm start" para iniciar la aplicación.


Para ejecutar la aplicacion por el lado del backend seguir los siguientes pasos.
1- Ejecutar el comnado "python -m venv venv" para cvenv\Scripts\activaterear un entorno virtual.
2- Ejecutar el comando "venv\Scripts\activate" para activar el entorno virtual.  
3- Ejecutar el comando "pip install -r requirements.txt" para las dependencias necesarias.
4- Ejecutar el comando "python -m uvicorn app.main:app --reload" para iniciar la aplicación.
