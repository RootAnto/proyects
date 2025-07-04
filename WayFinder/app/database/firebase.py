from pathlib import Path
import firebase_admin
from firebase_admin import credentials, firestore
from typing import Optional, Dict, Any
from loguru import logger

"""
@brief Firebase configuration and Firestore client initialization.

This module handles the setup and initialization of the Firebase app using a service account key.
It also provides functions to perform CRUD operations on the 'usuarios' collection in Firestore.

The logging configuration uses loguru to store logs in 'logs/firebase.log' with rotation and retention policies.
"""

# Logging configuration
logger.add("logs/firebase.log", rotation="1 MB", retention="7 days", level="DEBUG")

# Firebase configuration
SERVICE_ACCOUNT_PATH = Path(__file__).parent / "serviceAccountKey.json"

if not firebase_admin._apps:
    try:
        cred = credentials.Certificate(str(SERVICE_ACCOUNT_PATH))
        firebase_admin.initialize_app(cred, {
            'projectId': 'wayfinder-6a444',
            'storageBucket': 'wayfinder-6a444.appspot.com'
        })
        logger.success("Firebase initialized successfully")
    except Exception as e:
        logger.exception(f"Error initializing Firebase: {str(e)}")
        raise

db = firestore.client()

"""
@brief Checks if an email already exists in the 'usuarios' collection.

@param email The email address to check.

@return True if the email exists, False otherwise.

@throws Exception if there is an error during the query.
"""
def email_exists(email: str) -> bool:
    try:
        logger.debug(f"Checking if email exists: {email}")
        email = email.lower()
        users_ref = db.collection('usuarios')
        query = users_ref.where('email', '==', email).limit(1)
        results = query.get()
        exists = len(results) > 0
        logger.debug(f"Email existence result for '{email}': {exists}")
        return exists
    except Exception as e:
        logger.exception(f"Error checking email existence '{email}': {e}")
        raise

"""
@brief Creates a new user in the 'usuarios' collection.

@param user_data A dictionary containing user data to store.

@return The newly created user's document ID.

@throws Exception if there is an error during creation.
"""
def create_user(user_data: Dict[str, Any]) -> str:
    try:
        if 'email' in user_data:
            user_data['email'] = user_data['email'].lower()
        logger.debug(f"Creating user with data: {user_data}")
        _, doc_ref = db.collection('usuarios').add(user_data)
        logger.success(f"User created with ID: {doc_ref.id}")
        return doc_ref.id
    except Exception as e:
        logger.exception(f"Error creating user: {e}")
        raise

"""
@brief Retrieves a user document by email.

@param email The email of the user to retrieve.

@return A dictionary with user data if found, None otherwise.

@throws Exception if there is an error during retrieval.
"""
def get_user_by_email(email: str) -> Optional[Dict[str, Any]]:
    try:
        email = email.lower()
        logger.debug(f"Searching for user with email: {email}")
        query = db.collection('usuarios').where('email', '==', email).limit(1)
        docs = query.get()
        if docs:
            user_doc = docs[0]
            user_data = user_doc.to_dict()
            user_data["id"] = user_doc.id
            logger.debug(f"User found: {user_data}")
            return user_data
        else:
            logger.debug(f"No user found with email: {email}")
            return None
    except Exception as e:
        logger.exception(f"Error retrieving user by email '{email}': {e}")
        raise

"""
@brief Updates an existing user document.

@param user_id The document ID of the user to update.
@param updates A dictionary with fields to update.

@throws Exception if there is an error during update.
"""
def update_user(user_id: str, updates: Dict[str, Any]) -> None:
    try:
        # Convert email to lowercase before updating
        if 'email' in updates:
            updates['email'] = updates['email'].lower()
        logger.debug(f"Updating user {user_id} with data: {updates}")
        db.collection('usuarios').document(user_id).update(updates)
        logger.success(f"User {user_id} updated successfully")
    except Exception as e:
        logger.exception(f"Error updating user '{user_id}': {e}")
        raise

"""
@brief Retrieves a user document by user ID.

@param user_id The document ID of the user to retrieve.

@return A dictionary with user data if found, None otherwise.

@throws Exception if there is an error during retrieval.
"""
def get_user_by_id(user_id: str) -> Optional[Dict[str, Any]]:
    try:
        logger.debug(f"Searching for user by ID: {user_id}")
        doc = db.collection('usuarios').document(user_id).get()
        if doc.exists:
            logger.debug(f"User found: {doc.to_dict()}")
            return doc.to_dict()
        else:
            logger.debug(f"No user found with ID: {user_id}")
            return None
    except Exception as e:
        logger.exception(f"Error retrieving user by ID '{user_id}': {e}")
        raise
