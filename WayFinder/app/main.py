from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse
import uvicorn
from fastapi.middleware.cors import CORSMiddleware
from app.controller.amadeus_controller.flight_controller import router as fligth_router
from app.controller.amadeus_controller.hotel_controller import router as hotel_router
from app.controller.amadeus_controller.vehicle_controller import router as vehicle_router
from app.controller.amadeus_controller.trip_suggested_controller import router as automatic_travel_router
from app.controller.chatbot.chatbot_controller import router as chatbot_router
from app.controller.trips_tickets import router as trips_tickets_route
from app.controller.firebase_controller import router as auth_router
from app.controller.payments.payments import router as paymetn_route
from loguru import logger

app = FastAPI()

@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request: Request, exc: RequestValidationError) -> JSONResponse:
    logger.info(f"Validation error for request: {request.method} {request.url}")
    logger.info(f"Errors: {exc.errors()}")
    # Aquí puedes regresar una respuesta custom si quieres
    return JSONResponse(
        status_code=422,
        content={"detail": exc.errors(), "body": exc.body}
    )

@app.get("/")
def read_root():
    return {"message": "Welcome a WayFinder"}

# CORS to allow connection from the frontend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routes
app.include_router(auth_router)
app.include_router(fligth_router)
app.include_router(hotel_router)
app.include_router(vehicle_router)
app.include_router(automatic_travel_router)
app.include_router(trips_tickets_route)
app.include_router(paymetn_route)
app.include_router(chatbot_router)

if __name__ == "__main__":
    uvicorn.run("app.main:app", host="127.0.0.1", port=8000, reload=True)
