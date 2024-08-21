from firebase_admin import firestore


class FigureQueries:
    @staticmethod
    def create_figure(name, series, price, year):
        """
        Creates a new figure document in Firestore.

        This method attempts to insert a new document into the 'figures' collection in Firestore.
        It first checks if a document with the same 'series' already exists to avoid duplicate entries.

        Parameters:
        - name (str): The name of the figure.
        - series (str): The series identifier of the figure. This is used as the document ID in Firestore.
        - price (float): The price of the figure.
        - year (int): The year associated with the figure.

        Returns: - str: A message indicating the result of the operation. - If a figure with the same series already
        exists, it returns "A figure with the same series already exists". - If the figure is successfully inserted,
        it returns "Figure successfully inserted".

        """
        # Access the Firestore instance
        db = firestore.client()

        # Check if a figure with the same series already exists
        existing_figure = db.collection('figures').document(series).get()
        if existing_figure.exists:
            # Handle the case where a figure with the same series already exists
            # Return a message indicating that the figure with the same series already exists
            return "A figure with the same series already exists"
        else:
            # If it doesn't exist, proceed with the insertion
            # Create a new document with the provided details
            db.collection('figures').document(series).set({
                'name': name,
                'series': series,
                'price': price,
                'year': year
            })
            # Return a success message
            return "Figure successfully inserted"

    @staticmethod
    def search_figure(value):
        """
        Searches for figures in the Firestore database based on the given value.

        This method queries the Firestore collection 'figures' to find documents where the value
        is present in any of the following fields: 'name', 'series', 'price', or 'year'.
        It collects the unique figures that match the criteria.

        Args:
            value (str): The value to search for in the 'name', 'series', 'price', or 'year' fields.

        Returns:
            list: A list of dictionaries representing the figures found in the Firestore collection.
        """
        # Access the Firestore client instance
        db = firestore.client()

        # Initialize a list to store the query results
        figures_found = []

        # Perform the query to search for documents where the 'name' field matches the value
        name_results = db.collection('figures').where('name', '==', value).get()
        for document in name_results:
            figure = document.to_dict()
            if figure not in figures_found:
                figures_found.append(figure)

        # Perform the query to search for documents where the 'series' field matches the value
        series_results = db.collection('figures').where('series', '==', value).get()
        for document in series_results:
            figure = document.to_dict()
            if figure not in figures_found:
                figures_found.append(figure)

        # Perform the query to search for documents where the 'price' field matches the value
        price_results = db.collection('figures').where('price', '==', value).get()
        for document in price_results:
            figure = document.to_dict()
            if figure not in figures_found:
                figures_found.append(figure)

        # Perform the query to search for documents where the 'year' field matches the value
        year_results = db.collection('figures').where('year', '==', value).get()
        for document in year_results:
            figure = document.to_dict()
            if figure not in figures_found:
                figures_found.append(figure)

        return figures_found

    @classmethod
    def delete_figure(cls, value):
        """
        Deletes a figure from the Firestore database based on the provided identifier.

        This method accesses the Firestore client instance and deletes the document in the 'figures' collection
        that corresponds to the provided `value`. This `value` typically represents a unique identifier, such as
        a figure's series or name, used to locate and remove the specific figure from the database.

        Args:
            value (str): The unique identifier of the figure to be deleted.

        Returns:
            None: This method does not return a value; it performs a database operation to delete the document.
        """
        # Access the Firestore client instance
        db = firestore.client()

        # Delete the document from the 'figures' collection based on the provided identifier
        db.collection('figures').document(value).delete()
