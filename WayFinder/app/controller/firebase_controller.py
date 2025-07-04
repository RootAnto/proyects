from fastapi import APIRouter, HTTPException, status
from datetime import datetime
from passlib.context import CryptContext
from firebase_admin import auth
from firebase_admin.exceptions import FirebaseError
from app.models.users.user_pydantic_firebase import UserCreate, UserLogin
from app.services.email_service import send_verification_email
from fastapi import Body

from app.database.firebase import (
    email_exists,
    create_user,
    get_user_by_email,
    update_user,
    db
)

router = APIRouter(prefix="/auth", tags=["Authentication"])
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# Registro de usuario con verificación de correo
@router.post("/register")
async def register_user(user: UserCreate):
    try:
        # 1. Crear usuario en Firebase Authentication
        firebase_user = auth.create_user(
            email=user.email,
            password=user.password,
            display_name=user.nombre
        )

        # 2. Generar enlace de verificación de Firebase
        verification_link = auth.generate_email_verification_link(user.email)

        hashed_password = pwd_context.hash(user.password)

        # 3. Guardar datos en Firestore
        user_data = {
            "firebase_uid": firebase_user.uid,
            "email": user.email,
            "nombre": user.nombre,
            "password": hashed_password,
            "birthdate": user.birthdate,
            "acceptTerms": user.acceptTerms,
            "email_verified": False,  # Inicialmente no verificado
            "created_at": datetime.now(),
            "active": True,
            "last_login": None,
            "roles": ["user"]
        }

        user_id = create_user(user_data)

        # 4. Enviar el enlace de verificación
        send_verification_email(to_email=user.email, to_name=user.nombre, verification_link=verification_link)

        return {"id": user_id, "email": user.email, "nombre": user.nombre}

    except auth.EmailAlreadyExistsError:
        raise HTTPException(status_code=400, detail="El correo ya está registrado")
    except FirebaseError as e:
        auth.delete_user(firebase_user.uid)
        raise HTTPException(status_code=400, detail=f"Error: {str(e)}")

# Inicio de sesión con verificación de correo
@router.post("/login")
async def login_user(credentials: UserLogin):
    try:
        # 1. Verificar usuario en Firebase Auth (esto autentica las credenciales)
        firebase_user = auth.get_user_by_email(credentials.email)

        # 2. Verificar si el correo está confirmado
        if not firebase_user.email_verified:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail="Debes verificar tu correo electrónico primero"
            )

        # 3. Sincronizar Firestore con el estado de Firebase Auth
        user_ref = db.collection("usuarios").where("firebase_uid", "==", firebase_user.uid).get()[0]
        update_user(user_ref.id, {
            "email_verified": firebase_user.email_verified,
            "last_login": datetime.now()
        })

        # 4. Obtener datos del usuario para la respuesta
        user_data = get_user_by_email(credentials.email)

        if not pwd_context.verify(credentials.password, user_data["password"]):
            raise HTTPException(status_code=401, detail="Contraseña incorrecta")

        # 5. Actualizar último login
        update_user(user_ref.id, {"last_login": datetime.now()})

        return {"id": user_ref.id, "email": user_data["email"], "nombre": user_data["nombre"]}

    except auth.UserNotFoundError:
        raise HTTPException(status_code=404, detail="Usuario no encontrado")
    except FirebaseError as e:
        raise HTTPException(status_code=500, detail=f"Error de Firebase: {str(e)}")

@router.post("/google-login")
async def google_login(email: str = Body(...), nombre: str = Body(...), uid: str = Body(...)):
    try:
        # Verificar que el UID exista en Firebase Auth
        firebase_user = auth.get_user(uid)

        # Buscar usuario en Firestore
        existing_user = get_user_by_email(email)

        if not existing_user:
            # Crear nuevo usuario si no existe
            user_data = {
                "firebase_uid": uid,
                "email": email,
                "nombre": nombre,
                "birthdate": None,
                "acceptTerms": True,
                "email_verified": firebase_user.email_verified,
                "created_at": datetime.now(),
                "active": True,
                "last_login": datetime.now(),
                "roles": ["user"]
            }
            user_id = create_user(user_data)
        else:
            user_id = existing_user["id"]
            update_user(user_id, {"last_login": datetime.now()})

        return {"id": user_id, "email": email, "nombre": nombre}

    except auth.UserNotFoundError:
        raise HTTPException(status_code=404, detail="Usuario de Google no encontrado")
    except FirebaseError as e:
        raise HTTPException(status_code=500, detail=f"Error de Firebase: {str(e)}")