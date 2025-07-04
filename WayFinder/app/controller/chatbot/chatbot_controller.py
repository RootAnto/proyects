from fastapi import APIRouter, HTTPException
import threading
from app.controller.chatbot.core.gui_controller import iniciar_chat_multifase

from fastapi import APIRouter, HTTPException, Body
from pydantic import BaseModel
from typing import Optional

router = APIRouter()

class User(BaseModel):
    email: str
    nombre: Optional[str] = None

@router.post("/abrir-chat-multifase")
async def abrir_chat_multifase(user: User = Body(...)):
    try:
        hilo = threading.Thread(target=iniciar_chat_multifase, args=(user,), daemon=True)
        hilo.start()
        return {"status": "Chat multifase iniciado"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error al abrir chat multifase: {e}")
