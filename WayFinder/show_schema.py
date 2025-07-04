from sqlalchemy import create_engine, inspect
from app.config import settings

engine = create_engine(settings.SQLALCHEMY_DATABASE_URL)
inspector = inspect(engine)

tables = inspector.get_table_names()
print("Tablas en la base de datos:")
for table in tables:
    print(f"Tabla: {table}")
    columns = inspector.get_columns(table)
    for column in columns:
        print(f"  - {column['name']} ({column['type']})")
