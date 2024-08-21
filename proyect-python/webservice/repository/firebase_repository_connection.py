import firebase_admin
from firebase_admin import credentials, firestore
import socket


class FirebaseRepositoryConnection:
    """
    The FirebaseRepositoryConnection class initializes a connection to Firebase
    using a service account certificate.

    Methods
    -------
    __init__():
        Loads the certification and initializes the Firebase app.
    test_connection_with_timeout():
        Makes a test query to ensure the connection is working with a timeout.
    """

    def __init__(self):
        """
        Initializes the FirebaseRepositoryConnection class by loading the service
        account certification and initializing the Firebase app.
        """
        try:
            # Load certification
            self.cred = credentials.Certificate("static/clase-8cdd6-firebase-adminsdk-8xoiy-90f1d0006f.json")
            # Initialize the Firebase app with the loaded credentials
            if not firebase_admin._apps:
                firebase_admin.initialize_app(self.cred)
                print("Firebase connection established successfully.")
            else:
                print("Firebase app already initialized.")

            # Test the connection
            self.test_connection_with_timeout()

        except Exception as e:
            print(f"Failed to initialize Firebase: {e}")
            raise

    @staticmethod
    def test_connection_with_timeout():
        """
        Makes a test query to ensure the connection is working with a timeout.
        """
        try:
            socket.setdefaulttimeout(5)
            db = firestore.client()
            db.collection('test').document("test").delete()
        except Exception as e:
            print(f"Error during test query: {e}")
            raise
