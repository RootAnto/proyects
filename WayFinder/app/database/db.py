from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from app.config import settings

'''
@brief SQLAlchemy Engine instance.

@details
Creates a connection pool and manages connections to the database.
'''
engine = create_engine(settings.SQLALCHEMY_DATABASE_URL)

'''
@brief SQLAlchemy session factory.

@details
Creates session objects for interacting with the database.
- autocommit=False: disables automatic commit after each statement.
- autoflush=False: disables automatic flush before queries.
- bind=engine: binds sessions to the created engine.
'''
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

'''
@brief Base class for declarative models.

@details
Used as a base class to define database models/tables with SQLAlchemy ORM.
'''
Base = declarative_base()

'''
@brief Dependency function to get a database session.

@details
This function is intended to be used with FastAPI's Depends to provide
a database session to path operations. The session is closed automatically
after the request is finished.

@yield A SQLAlchemy session object.
'''
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
