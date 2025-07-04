from pydantic_settings import BaseSettings
from pathlib import Path

class Settings(BaseSettings):
    # Stripe
    STRIPE_SECRET_KEY: str
    STRIPE_WEBHOOK_SECRET: str

    # Amadeus
    AMADEUS_CLIENT_ID: str
    AMADEUS_CLIENT_SECRET: str

    # UNSPLASH
    UNSPLASH_ACCESS_KEY: str

    # SQLAchemic
    SQLALCHEMY_DATABASE_URL: str

    # Open AI
    OPENAI_API_KEY: str

    class Config:
        env_file = Path(__file__).parent.parent / "app/.env"

settings = Settings()
OPENAI_API_KEY = settings.OPENAI_API_KEY
