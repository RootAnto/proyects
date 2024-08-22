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

    @staticmethod
    def show_all_figures():
        """
        Retrieves all figures from the Firestore database.

        This method accesses the Firestore client and queries the 'figures' collection
        to retrieve all documents. It then iterates over the results, converting each
        document to a dictionary and adding it to a list. Finally, it returns the list
        of all figures found in the collection.

        Returns:
            list: A list of dictionaries representing all figures found in the Firestore collection.
        """
        # Access the Firestore client instance
        db = firestore.client()

        # Query to retrieve all documents in the 'figures' collection
        results = db.collection('figures').get()

        # Initialize a list to store the query results
        figures_found = []

        # Iterate over the results and add them to the list
        for document in results:
            figures_found.append(document.to_dict())

        # Return the list of found figures
        return figures_found

    @staticmethod
    def modify_figure(current_series, new_series, new_name, new_price, new_year):
        """
        Modify the details of an existing figure.

        This method updates the details of a figure in the Firestore database. If the series of the figure
        changes, it creates a new document with the new series and deletes the old document.

        Parameters:
            current_series (str): The current series identifier of the figure to be updated.
            new_series (str): The new series identifier for the figure.
            new_name (str): The new name for the figure.
            new_price (float): The new price for the figure.
            new_year (int): The new year for the figure.

        Returns:
            str: A message indicating the result of the operation.
        """
        db = firestore.client()

        # Check if the figure exists
        old_figure_ref = db.collection('figures').document(current_series)
        old_figure = old_figure_ref.get()

        if old_figure.exists:
            if current_series != new_series:
                # Create a new document with the new series
                new_figure_ref = db.collection('figures').document(new_series)
                new_figure_ref.set({
                    'name': new_name,
                    'series': new_series,
                    'price': new_price,
                    'year': new_year
                })

                # Delete the old document
                old_figure_ref.delete()
            else:
                # Update the existing document
                old_figure_ref.update({
                    'name': new_name,
                    'series': new_series,
                    'price': new_price,
                    'year': new_year
                })

            return "Figure updated successfully"
        else:
            return "Figure not found"







