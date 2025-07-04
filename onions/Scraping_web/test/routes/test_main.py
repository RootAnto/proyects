# @ Author: Antonio Llorente. Aitea Tech Becarios
# <antoniollorentecuenca@gmail.com>

# @ Create Time: 2025-05-5 12:17:59

# @ Modified time: 2025-05-9 20:17:59

# @ Project: Cebolla

# @ Description: This script initializes the FastAPI application, configures
# routes for# managing RSS feed ingestion and news article scraping using
# Scrapy, and establishes a connection pool to a PostgreSQL database using
# asyncpg. It includes lifecycle event handlers to manage the connection pool
# during startup and shutdown. When executed directly, it launches the
# application with Uvicorn in development mode with hot-reloading enabled

from app.controllers.routes.tiny_postgres_controller import router as postgre_feeds
from app.controllers.routes.scrapy_news_controller import router as newsSpider
from loguru import logger
from fastapi import FastAPI
import asyncpg
import uvicorn


app = FastAPI()

# Include the feeds router (which handles Scrapy-related routes)
app.include_router(postgre_feeds)
app.include_router(newsSpider)

# Create a connection pool for the PostgreSQL database
async def create_pool()-> None:
    """
    Initializes a connection pool to the PostgreSQL database using asyncpg.

    This function sets up a connection pool and attaches it to the global
    application state (`app.state.pool`). This allows other parts of the
    application to reuse database connections efficiently.

    Raises:
        Exception: If there is an error during the creation of the connection
        pool.
    """
    try:
        logger.info("Database connecting...")
        app.state.pool = await asyncpg.create_pool(
            user='postgres',
            password='password123',
            database='postgres',
            host='127.0.0.1', # ONLY DEVELOPER, PORT EXPOSED DB TINY POSTGRES
            port=5432,
            min_size=5,
            max_size=20
        )

        logger.info("Database connection pool created successfully.")
    except Exception as e:
        logger.error(f"Error creating database connection pool: {str(e)}")
        raise e


async def close_pool()-> None:
    """
    Closes the database connection pool if it exists.

    This function checks whether the FastAPI application's state has an
    active PostgreSQL connection pool (`app.state.pool`). If it does,
    it closes the pool to gracefully release all database connections.
    """
    if hasattr(app.state, "pool"):
        await app.state.pool.close()
        logger.info("Database connection pool closed.")

# Register event handlers OUTSIDE of __main__ block so they are used by uvicorn
app.add_event_handler("startup", create_pool)
app.add_event_handler("shutdown", close_pool)

# Main entry point
if __name__ == '__main__':
    """
    Launches the FastAPI application using Uvicorn.

    This block is executed only when the script is run directly.
    It initializes and runs the FastAPI app with hot-reload enabled
    for development purposes.
    """
    logger.info("Initializing FastAPI application...")
    uvicorn.run("main:app", host="127.0.0.1", port=8000, reload=True)

