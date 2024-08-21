from firebase_admin import firestore
import hashlib


class LoginRepository:
    """
    The LoginRepository class handles user authentication and user-related database operations for the application.

    Methods
    -------
    authenticate_user(email, password):
        Authenticates a user by checking their email and password against the Firestore database.
    user_exists(email):
        Checks if a user with the given email already exists in the Firestore database.
    insert_user(email, password):
        Inserts a new user with the given email and password into the Firestore database.
    hash_password(password):
        Hashes a password using SHA-256 with a predefined salt.
    """

    @staticmethod
    def authenticate_user(email, password):
        """
        Authenticates a user by checking their email and password against the Firestore database.

        Parameters
        ----------
        email : str
            The email of the user.
        password : str
            The hashed password of the user.

        Returns
        -------
        bool
            True if the user is authenticated, False otherwise.
        """
        db = firestore.client()
        user_ref = db.collection('users').where('email', '==', email).where('password', '==', password).get()
        if user_ref:
            return True
        return False

    @staticmethod
    def user_exists(email):
        """
        Checks if a user with the given email already exists in the Firestore database.

        Parameters
        ----------
        email : str
            The email of the user.

        Returns
        -------
        bool
            True if the user exists, False otherwise.
        """
        db = firestore.client()
        user_ref = db.collection('users').where('email', '==', email).get()
        if user_ref:
            return True
        return False

    @staticmethod
    def insert_user(email, password):
        """
        Inserts a new user with the given email and password into the Firestore database.

        Parameters
        ----------
        email : str
            The email of the user.
        password : str
            The hashed password of the user.
        """
        db = firestore.client()
        db.collection('users').document(email).set({
            'email': email,
            'password': password
        })

    @staticmethod
    def hash_password(password):
        """
        Hashes a password using SHA-256 with a predefined salt.

        Parameters
        ----------
        password : str
            The password to be hashed.

        Returns
        -------
        str
            The hashed password.
        """
        salt = "abcdefghijklmnopqrstuvwxyz"
        hashed_password = hashlib.sha256((password + salt).encode('utf-8')).hexdigest()
        return hashed_password
