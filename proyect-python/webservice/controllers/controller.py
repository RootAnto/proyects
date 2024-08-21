"""
This module defines the main controller for the Flask application.
"""
import firebase_admin
from flask import Flask, render_template
from controllers.controller_card import ControllerCards
from controllers.controller_figure import ControllerFigures
from controllers.controller_user import ControllerUsers
from controllers.controller_error import ControllerErrors
import os
import time

from repository.firebase_repository_connection import FirebaseRepositoryConnection


class Controller:
    """
    The Controller class initializes the Flask application, registers routes, and handles Firebase connection.

    Methods
    -------
    __init__():
        Initializes the Flask application, registers routes, and connects to Firebase.
    register_routes():
        Registers routes for the sub-controllers.
    run():
        Runs the Flask application.
    firebase_connect():
        Initializes the connection to Firebase.
    """

    def __init__(self):
        """
        Initializes the Flask application and calls the method to register routes.
        """

        # Initialize the Flask application with the specified template and static folders
        template_dir = os.path.abspath('views/templates')
        static_dir = os.path.abspath('static')
        self.app = Flask(__name__, template_folder=template_dir, static_folder=static_dir)

        # Log the directories for debugging purposes
        print(f"Template folder set to: {template_dir}")
        print(f"Static folder set to: {static_dir}")

        # Establish connection to Firebase
        self.firebase_connect(self.app)

    @staticmethod
    def firebase_connect(app):
        """
        Initializes the connection to Firebase.
        """
        max_retries = 5
        attempts = 0

        while attempts < max_retries:
            try:
                # Check if Firebase is already initialized
                if not firebase_admin._apps:
                    FirebaseRepositoryConnection()
                else:
                    FirebaseRepositoryConnection().test_connection_with_timeout()

                # Register routes for the application
                Controller.register_routes(app)
                return  # Exit the loop if the connection is successful
            except Exception as e:
                print(f"Error during Firebase connection: {e}")

                # Register error routes only once
                if not any(rule.endpoint == 'error_connection' for rule in app.url_map.iter_rules()):
                    ControllerErrors.routes_error(app)

                # Register route to show the error template
                if not any(rule.endpoint == 'show_error' for rule in app.url_map.iter_rules()):
                    @app.route('/')
                    def show_error():
                        return render_template('error/template-error.html')

                attempts += 1
                print(f"Retrying connection in 2 seconds... Attempt {attempts}/{max_retries}")
                time.sleep(2)

        # If all attempts fail, log and raise an error or handle it appropriately
        print("Failed to connect to Firebase after multiple attempts. Please check your configuration.")

    @staticmethod
    def register_routes(app):
        """
        Registers routes for the ControllerCards, ControllerFigures and ControllerUsers sub-controllers.
        """
        # Register routes for ControllerUsers
        ControllerUsers.routes_users(app)
        # Register routes for ControllerCards
        ControllerCards.routes_cards(app)
        # Register routes for ControllerFigures
        ControllerFigures.routes_figures(app)

    def run(self):
        """
        Runs the Flask application in debug mode.
        """
        # Run the Flask application with debugging enabled
        self.app.run(debug=True)
