from pydantic import BaseModel

class UserCreate(BaseModel):
    email: str
    password: str
    nombre: str
    birthdate: str
    acceptTerms: bool

class UserLogin(BaseModel):
    email: str
    password: str
