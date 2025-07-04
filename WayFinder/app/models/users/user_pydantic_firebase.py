from pydantic import BaseModel

# Modelos Pydantic
class UserCreate(BaseModel):
    email: str
    password: str
    nombre: str
    birthdate: str
    acceptTerms: bool

class UserLogin(BaseModel):
    email: str
    password: str