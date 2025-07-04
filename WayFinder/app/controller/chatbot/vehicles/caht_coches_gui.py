import tkinter as tk
from tkinter import scrolledtext, messagebox
from app.controller.chatbot.vehicles.chat_coches import procesar_mensaje_coche
from app.controller.chatbot.core.utils.functions import es_afirmacion

class ChatCochesWindow(tk.Toplevel):
    def __init__(self, master=None):
        super().__init__(master)
        self.title("Chat de Coches - Planificador IA")
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
        self.entry.pack(side="left", fill="x", expand=True, padx=(0, 10), ipady=8)
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

        self.datos_coche = {
            "ciudad": None,
            "tipo_vehiculo": None,
        }
        self.context = ""
        self.esperando_confirmacion_inicial = True
        self.esperando_confirmacion_reserva = False

        self.resultado = {}

        self.escribir_chat("🚗 Bot: ¿Te gustaría reservar un coche?")

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

        if self.esperando_confirmacion_inicial:
            if es_afirmacion(user_msg):
                self.esperando_confirmacion_inicial = False
                self.escribir_chat("🚗 Bot: ¡Perfecto! ¿En qué ciudad necesitas el coche?")
            else:
                self.escribir_chat("🚗 Bot: Sin problema. ¡Buen viaje!")
                self.resultado = {}
                self.after(1500, self.destroy)
            return

        if user_msg.lower() in ["stop", "salir"]:
            self.escribir_chat("Bot: Terminando la fase de coches. ¡Hasta luego!")
            self.resultado = {}
            self.after(1500, self.destroy)
            return

        if self.esperando_confirmacion_reserva:
            if es_afirmacion(user_msg):
                self.escribir_chat("Coche reservado. Gracias por usar el planificador.")
                self.resultado = self.datos_coche.copy()
            else:
                self.escribir_chat("Reserva cancelada.")
                self.resultado = {}
            self.esperando_confirmacion_reserva = False
            self.after(1500, self.destroy)
            return

        respuesta, nuevos_datos, nuevo_context, coches, reserva = procesar_mensaje_coche(
            user_msg, self.datos_coche, self.context
        )
        self.datos_coche.update({k: v for k, v in nuevos_datos.items() if v})
        self.context = nuevo_context

        self.escribir_chat(f"🤖 Bot: {respuesta}")

        if coches:
            self.escribir_chat(f"🚘 Bot: {coches}")
            self.escribir_chat("Bot: ¿Quieres reservar este coche?")
            self.esperando_confirmacion_reserva = True

        if reserva:
            self.escribir_chat("Coche reservado. Gracias por usar el planificador.")
            self.resultado = self.datos_coche.copy()
            self.after(1500, self.destroy)

    def cerrar_ventana(self):
        if not hasattr(self, "resultado"):
            self.resultado = {}
        self.destroy()
