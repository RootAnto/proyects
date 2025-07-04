import tkinter as tk
from tkinter import scrolledtext, messagebox
from app.controller.chatbot.flights.chat_vuelos2 import procesar_mensaje_vuelo
from app.controller.chatbot.core.utils.functions import es_afirmacion


class ChatVuelosWindow(tk.Toplevel):
    def __init__(self, master=None):
        super().__init__(master)
        self.title("Chat de Vuelos - Planificador IA")
        self.geometry("550x600")
        self.resizable(False, False)
        self.configure(bg="#1e1e1e")

        self.chat_log = scrolledtext.ScrolledText(
            self, state="disabled", wrap="word", font=("Segoe UI", 12),
            bg="#2d2d2d", fg="#d0ffd6", insertbackground="#f1f1f1",
            relief="flat", borderwidth=0
        )
        self.chat_log.pack(padx=10, pady=10, fill="both", expand=True)

        input_frame = tk.Frame(self, bg="#1e1e1e")
        input_frame.pack(padx=10, pady=(0, 10), fill="x")

        self.entry = tk.Entry(
            input_frame, font=("Segoe UI", 16), bg="#3c3c3c", fg="#f1f1f1",
            insertbackground="#f1f1f1", relief="flat", borderwidth=6
        )
        self.entry.pack(side="left", fill="x", expand=True,
                        padx=(0, 10), ipady=8)
        self.entry.bind("<Return>", self.enviar_mensaje)
        self.entry.focus()

        self.btn_enviar = tk.Button(
            input_frame, text="Enviar", font=("Segoe UI", 14, "bold"),
            bg="#00aaff", fg="white", activebackground="#0088cc",
            activeforeground="white", relief="flat", command=self.enviar_mensaje,
            width=12, cursor="hand2", pady=6
        )
        self.btn_enviar.pack(side="right")

        self.protocol("WM_DELETE_WINDOW", self.cerrar_ventana)

        self.datos_vuelo = {
            "origen": None,
            "destino": None,
            "fecha_salida": None,
            "fecha_regreso": None,
            "tipo_pasajero": None,
            "precio": None,
            "moneda": None,
            "duracion": None,
            "aerolinea": None,
            "numero_vuelo": None,
        }
        self.context = ""
        self.esperando_confirmacion_reserva = False

        self.resultado = {}
        self.vuelos = None

        self.escribir_chat("🛫 Bot: ¡Hola! Soy tu asistente de vuelos. ¿En qué puedo ayudarte?")

    def escribir_chat(self, texto: str):
        self.chat_log.config(state="normal")
        self.chat_log.insert("end", texto + "\n\n")
        self.chat_log.see("end")
        self.chat_log.config(state="disabled")

    def enviar_mensaje(self, event=None):
        user_msg = self.entry.get().strip()
        if not user_msg:
            return
        self.entry.delete(0, "end")
        self.escribir_chat(f"🧑 Tú: {user_msg}")

        if self.esperando_confirmacion_reserva:
            if es_afirmacion(user_msg):
                self.escribir_chat("Vuelo reservado. Gracias por usar el planificador.")
                self.resultado = self.datos_vuelo.copy()
                self.vuelos = self.datos_vuelo.copy()
            else:
                self.escribir_chat("Reserva cancelada.")
                self.resultado = {}
                self.vuelos = None
            self.esperando_confirmacion_reserva = False
            self.after(1500, self.destroy)
            return

        if user_msg.lower() in {"stop", "salir"}:
            self.escribir_chat("Bot: Terminando la fase de vuelos. ¡Hasta luego!")
            self.resultado = {}
            self.vuelos = None
            self.after(1500, self.destroy)
            return

        respuesta, nuevos_datos, nuevo_ctx, vuelos_msg, reserva = procesar_mensaje_vuelo(
            user_msg, self.datos_vuelo, self.context
        )
        self.datos_vuelo.update({k: v for k, v in nuevos_datos.items() if v})
        self.context = nuevo_ctx
        self.escribir_chat(f"🤖 Bot: {respuesta}")

        if vuelos_msg and "Error" not in vuelos_msg:
            self.escribir_chat(f"🛩️ Bot: {vuelos_msg}")
            self.escribir_chat("Bot: ¿Quieres reservar el vuelo?")
            self.esperando_confirmacion_reserva = True
            self.vuelos = self.datos_vuelo.copy()
            return
        elif vuelos_msg:
            self.escribir_chat(f"🛩️ Bot: {vuelos_msg}")

        if reserva:
            self.escribir_chat("Vuelo reservado. Gracias por usar el planificador.")
            self.resultado = self.datos_vuelo.copy()
            self.vuelos = self.datos_vuelo.copy()
            self.after(1500, self.destroy)

    def cerrar_ventana(self):
        self.destroy()
