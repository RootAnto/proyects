from pydantic_settings import BaseSettings
from pathlib import Path

class Settings(BaseSettings):

    REACT_APP_STRIPE_PUBLISHABLE_KEY: str

    class Config:
        env_file = Path(__file__).parent.parent / "frontend/.env"

settings = Settings()
