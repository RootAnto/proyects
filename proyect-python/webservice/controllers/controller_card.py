from flask import Flask, render_template, request, redirect
from repository.card_queries import CardQueries


class ControllerCards:
    @staticmethod
    def routes_cards(app):
        """
        Registers routes for card-related templates.

        Parameters
        ----------
        app : Flask
            The Flask application instance.
        """

        @app.route('/index-card')
        def template_index_card():
            """
            Renders the main card template page.

            Returns
            -------
            str
                The rendered HTML template for the card index page.
            """
            return render_template('card/template-cards-index.html')

        @app.route('/create-card')
        def template_create_card():
            """
            Renders the create card template page.

            Returns
            -------
            str
                The rendered HTML template for the create card page.
            """
            return render_template('card/template-cards-create.html')

        @app.route('/create-card/create-card-query', methods=['POST'])
        def create_card_query():
            """
            Handles the card creation form submission. If the card already exists,
            it redirects back to the same page with an error message indicating that
            the card already exists.

            Methods
            -------
            POST:
                Processes the form data to create a new card.

            Returns
            -------
            redirect:
                If the card creation is successful, redirects to the card index page.
            render_template:
                If the card already exists or there is any error, renders the card creation
                page with an appropriate error message.
            """
            if request.method == 'POST':
                # Get the data from the form
                name = request.form['name']
                game = request.form['game']
                collection = request.form['collection']
                number = int(request.form['number'])
                price = float(request.form['price'])

                # Check if the confirmation checkbox has been checked
                if 'check' in request.form:
                    # Try to insert the card
                    result = CardQueries.insert_card(name, game, collection, number, price)
                    if result == "Card create successful":
                        # If the insertion is successful, redirect to the success page
                        return redirect('/index-card')
                    else:
                        # If a card with the same name already exists, show an error message to the user
                        return render_template('card/template-cards-create.html', message=result)

        @app.route('/delete-card')
        def delete_card():
            """
            Route for deleting a card. This function renders a template to display all cards or an error template if no
            cards are found.

            Returns
            -------
            str
                The rendered HTML template for deleting a card.
            """
            return render_template('card/template-cards-delete.html')

        @app.route('/delete-card/search', methods=['POST'])
        def delete_card_search():
            """
            Route for searching cards to delete. This function handles the search request, retrieves matching cards, and
            renders the delete card template with the results.

            Methods
            -------
            POST
                Accepts the search value from a form and retrieves matching cards.

            Returns
            -------
            str
                The rendered HTML template for deleting a card with search results.
            """
            if request.method == 'POST':
                # Get the search value from the form
                value = request.form['value']

                # Execute the search for the given value
                found_cards = CardQueries.search_for_value(value)

                # Render the result in the corresponding template
                return render_template('card/template-cards-delete.html', cards=found_cards)

        @app.route('/cards/delete/<name_card>', methods=['POST'])
        def delete_card_query(name_card):
            """
            Deletes a card from the database based on the provided card name.

            This view function handles POST requests to delete a card from the Firestore
            database. It attempts to delete the card using the `delete_card` method
            from the `CardQueries` class, identified by the `name_card` passed in the
            URL. Upon successful deletion, the user is redirected to a confirmation page.

            Args:
                name_card (str): The name of the card to be deleted. This is passed
                                 as a URL parameter.

            Returns:
                flask.Response: A rendered template for the card deletion confirmation
                                page (`template-cards-delete.html`).

            Example:
                This route can be accessed via a POST request to:
                /cards/delete/<name_of_the_card>

                Where `<name_of_the_card>` is the name of the card you want to delete.
            """
            if request.method == 'POST':
                # Attempt to delete the card by its name
                CardQueries.delete_card(name_card)

                # Redirect to the main page or some confirmation page
                return render_template('card/template-cards-delete.html')

        @app.route('/cards/api')
        def api_template():
            """
            This route renders the template that displays all cards from the API.

            When the user accesses this route, it returns the HTML template 'template-cards-read-api.html'.
            This template is designed to interact with the API to display all the available cards.
            """
            return render_template('card/template-cards-read-api.html')

        @app.route('/cards/search')
        def search_template():
            """
            This route renders the template for searching cards manually.

            When the user accesses this route, it returns the HTML template 'template-cards-read.html'.
            This template contains a search form that allows the user to search for specific cards
            based on the criteria they provide.
            """
            return render_template('card/template-cards-read.html')

        # Executes the search query based on the value provided in the search form
        @app.route('/cards/search-query', methods=['POST'])
        def card_search_query():
            """
            This route handles the search query for cards based on the user's input.

            When a POST request is made to this route, it retrieves the search value from the form.
            The method then uses the 'search_for_value' function in 'CardQueries' to search the database for matching cards.
            Finally, it renders the results using the 'template-cards-read.html' template, displaying the found cards to the user.
            """
            if request.method == 'POST':
                # Get the search value from the form
                value = request.form['value']

                # Execute the search for the given value
                found_cards = CardQueries.search_for_value(value)

                # Render the result in the corresponding template
                return render_template('card/template-cards-read.html', cards=found_cards)

        @app.route('/cards/view-all', methods=['GET'])
        def view_all_template():
            """
            This route handles the HTTP GET request for viewing all cards.
            It interacts with the CardQueries class to retrieve a list of all cards in the database.

            - The `show_all_cards` method of the `CardQueries` class is invoked to fetch all card records.
            - The retrieved list of cards is then passed to the 'template-cards-view-all.html' template.
            - The HTML template will display the list of cards in a table format.

            Returns:
                A rendered HTML template ('template-cards-view-all.html') populated with the data of all cards.
            """
            cards = CardQueries.show_all_cards()
            return render_template('card/template-cards-view-all.html', cards=cards)

        @app.route('/cards/update')
        def update_template():
            """
            Renders the HTML template for updating cards.

            This route serves the page where users can search and update card details.

            Returns:
                A rendered HTML template ('template-cards-update.html') for updating cards.
            """
            return render_template('card/template-cards-update.html')

        @app.route('/update/search', methods=['POST'])
        def update_search():
            """
            Handles the search functionality for cards.

            This route processes the search request submitted via a POST form. It retrieves card records
            based on the search value provided by the user.

            Steps:
            - Extracts the search value from the form submission.
            - Uses the `search_for_value` method of the `CardQueries` class to find matching cards.
            - Renders the HTML template with the list of found cards.

            Returns:
                A rendered HTML template ('template-cards-update.html') with search results displayed.
            """
            if request.method == 'POST':
                # Retrieve the search value from the submitted form
                value = request.form['value']

                # Perform the search operation and get matching cards
                found_cards = CardQueries.search_for_value(value)

                # Render the results in the update template
                return render_template('card/template-cards-update.html', cards=found_cards)

        @app.route('/cards/modify-query', methods=['POST'])
        def modify_card():
            """
            Handles the modification of card details.

            This route processes the form submission for updating a card. It updates the card details
            based on the information provided by the user.

            Steps:
            - Retrieves the form data for the card to be updated.
            - Calls the `modify_card` method of the `CardQueries` class to update the card details.
            - Redirects to the main page if the update is successful.
            - Displays an error message if the update fails.
            - Handles any exceptions that may occur during the update process.

            Returns:
                Redirects to the main page if the update is successful. Otherwise, renders the update template with an error message.
            """
            if request.method == 'POST':
                try:
                    # Retrieve the form data
                    current_name = request.form['current_name']
                    new_name = request.form['new_name']
                    new_game = request.form['new_game']
                    new_collection = request.form['new_collection']
                    new_number = int(request.form['new_number'])
                    new_price = float(request.form['new_price'])

                    # Debugging: Print form data to console
                    print(
                        f"Current Name: {current_name}, New Name: {new_name}, New Game: {new_game}, New Collection: {new_collection}, New Number: {new_number}, New Price: {new_price}")

                    # Update the card details
                    result = CardQueries.modify_card(current_name, new_name, new_game,
                                                     new_collection, new_number, new_price)
                    print(f"Update Result: {result}")  # Log the result

                    if result == "Card updated successfully":
                        # Redirect to the main page upon successful update
                        return redirect('/cards/update')
                    else:
                        # Render the update template with an error message if the update fails
                        return render_template('card/template-cards-update.html', error=result)
                except Exception as e:
                    # Render the update template with an error message if an exception occurs
                    print(f"Exception: {str(e)}")  # Log the exception
                    return render_template('card/template-cards-update.html', error=str(e))
            else:
                return render_template('card/template-cards-update.html')


