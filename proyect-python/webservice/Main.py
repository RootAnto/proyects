"""
This module serves as the entry point for the Flask application.
"""
from controllers.controller import Controller

if __name__ == "__main__":
    """
    Main entry point of the application. It creates an instance of the Controller class
    and runs the Flask application.
    """
    controller = Controller()
    controller.run()
