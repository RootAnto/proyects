from firebase_admin import firestore


class CardQueries:
    """
    The CardQueries class provides static methods to interact with the Firestore database
    for card-related operations such as inserting a new card.
    """

    @staticmethod
    def insert_card(name, game, collection, number, price):
        """
        Inserts a new card into the Firestore database if a card with the same name does not already exist.

        Parameters
        ----------
        name : str
            The name of the card.
        game : str
            The game to which the card belongs.
        collection : str
            The collection to which the card belongs.
        number : int
            The number of the card in the collection.
        price : float
            The price of the card.

        Returns
        -------
        str
            A message indicating whether the card was successfully inserted or if a card with the same name already exists.
        """
        db = firestore.client()

        # Check if a card with the same name already exists
        existing_card = db.collection('cards').document(name).get()
        if existing_card.exists:
            # Handle the case where a card with the same name already exists
            return "A card with the same name already exists"
        else:
            # If the card does not exist, proceed with the insertion
            db.collection('cards').document(name).set({
                'name': name,
                'game': game,
                'collection': collection,
                'number': number,
                'price': price
            })
            return "Card insert successful"

    @staticmethod
    def search_for_value(value):
        """
        Searches for documents in the 'cards' collection that contain the specified value
        in any of the fields: 'name', 'game', 'collection', 'number', or 'price'.

        Parameters
        ----------
        value : str or int
            The value to search for in the 'cards' collection.

        Returns
        -------
        list
            A list of dictionaries where each dictionary represents a document
            that matches the search criteria.
        """
        db = firestore.client()
        found_cards = []

        # Search for documents containing the value in the 'name' field
        results_name = db.collection('cards').where('name', '==', value).get()
        for document in results_name:
            card = document.to_dict()
            if card not in found_cards:
                found_cards.append(card)

        # Search for documents containing the value in the 'game' field
        results_game = db.collection('cards').where('game', '==', value).get()
        for document in results_game:
            card = document.to_dict()
            if card not in found_cards:
                found_cards.append(card)

        # Search for documents containing the value in the 'collection' field
        results_collection = db.collection('cards').where('collection', '==', value).get()
        for document in results_collection:
            card = document.to_dict()
            if card not in found_cards:
                found_cards.append(card)

        # Search for documents containing the value in the 'number' field
        results_number = db.collection('cards').where('number', '==', value).get()
        for document in results_number:
            card = document.to_dict()
            if card not in found_cards:
                found_cards.append(card)

        # Search for documents containing the value in the 'price' field
        results_price = db.collection('cards').where('price', '==', value).get()
        for document in results_price:
            card = document.to_dict()
            if card not in found_cards:
                found_cards.append(card)

        return found_cards

    @staticmethod
    def show_all_cards():
        """
        This static method retrieves all card records from the Firestore database.

        - A connection to the Firestore client is established.
        - The method accesses the 'cards' collection in the Firestore database.
        - It retrieves all documents within this collection using the `get()` method.
        - Each document is converted to a dictionary format and appended to the `cards_founds` list.
        - The method returns a list of dictionaries, where each dictionary represents a card's data.

        Returns:
            List[dict]: A list containing all card records from the 'cards' collection in dictionary format.
        """
        db = firestore.client()
        results = db.collection('cards').get()
        cards_founds = []
        for document in results:
            cards_founds.append(document.to_dict())
        return cards_founds

    @staticmethod
    def delete_card(name):
        """
            Deletes a specific card document from the Firestore database.

            This static method connects to the Firestore database and deletes a document
            from the 'cards' collection identified by the provided card name.

            Args:
                name (str): The name of the card to be deleted. This name should match
                            the document ID in the Firestore 'cards' collection.

            Example:
                >>> MyClass.delete_card('CardName')

            Notes:
                - Ensure that the Firestore client is properly initialized and authenticated
                  before calling this method.
                - Deleting a card will remove the document from the Firestore collection
                  permanently, so use this method with caution.
                - This method is static, meaning it can be called on the class itself rather
                  than on an instance of the class.

            Raises:
                google.cloud.exceptions.GoogleCloudError: If there is an issue connecting
                to the Firestore database or deleting the document.
            """
        db = firestore.client()
        db.collection('cards').document(name).delete()

    @staticmethod
    def modify_card(current_name, new_name, new_game, new_collection, new_number, new_price):
        """
        Modify the details of an existing card.

        This method updates the details of a card in the Firestore database. If the name of the card
        changes, it creates a new document with the new name and deletes the old document.

        Parameters:
            current_name (str): The current name of the card to be updated.
            new_name (str): The new name for the card.
            new_game (str): The new game associated with the card.
            new_collection (str): The new collection to which the card belongs.
            new_number (int): The new number of the card.
            new_price (float): The new price of the card.

        Returns:
            str: A message indicating the result of the operation.
        """
        db = firestore.client()

        # Check if the card exists
        old_card_ref = db.collection('cards').document(current_name)
        old_card = old_card_ref.get()

        if old_card.exists:
            # If the new name is different, create a new document with the new name and delete the old one
            if current_name != new_name:
                # Create a new document with the new name
                new_card_ref = db.collection('cards').document(new_name)
                new_card_ref.set({
                    'name': new_name,
                    'game': new_game,
                    'collection': new_collection,
                    'number': new_number,
                    'price': new_price
                })

                # Delete the old document
                old_card_ref.delete()
            else:
                # If the name hasn't changed, just update the existing document
                old_card_ref.update({
                    'name': new_name,
                    'game': new_game,
                    'collection': new_collection,
                    'number': new_number,
                    'price': new_price
                })

            return "Card updated successfully"
        else:
            return "Card not found"

