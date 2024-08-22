from flask import *

from repository.figure_queries import FigureQueries


class ControllerFigures:
    @staticmethod
    def routes_figures(app):
        """
        Configures routes for handling figure-related views and operations in the Flask application.

        This method sets up the routes that are used to render templates and handle form submissions related
        to figures. It includes routes for viewing the index of figures, creating a new figure, and processing
        figure creation requests.

        Parameters:
        - app (Flask): The Flask application instance to which the routes will be registered.
        """

        # Route for displaying the figure index page
        @app.route('/index-figures')
        def template_index_figure():
            """
            Renders the index page for figures.

            This route displays a template that lists or provides an overview of all figures in the system.
            The corresponding HTML template `figure/template-figures-index.html` is rendered to show the
            figure index page to the user.

            Returns:
            - Rendered HTML template for the figure index page.
            """
            return render_template('figure/template-figures-index.html')

        # Route for displaying the figure creation page
        @app.route('/create-figure')
        def template_create_figure():
            """
            Renders the figure creation page.

            This route displays a form where users can input details to create a new figure. The form allows
            users to enter the name, series, price, and year for the figure. The corresponding HTML template
            `figure/template-figures-create.html` is rendered to provide the figure creation interface.

            Returns:
            - Rendered HTML template for the figure creation page.
            """
            return render_template('figure/template-figures-create.html')

        # Handles form submission for creating a new figure
        @app.route('/figures/create', methods=['POST'])
        def figure_create():
            """
            Processes the form submission for creating a new figure.

            This route handles POST requests from the figure creation form. It retrieves the form data, including
            the name, series, price, and year of the figure. It then checks if the confirmation checkbox was
            selected. If so, it attempts to insert the figure into the database using the `FigureQueries.create_figure`
            method. Depending on the result, it either redirects to the figure index page or re-renders the
            creation page with an error message if a figure with the same series already exists.

            Returns:
            - Redirect to the figure index page if the figure is successfully inserted.
            - Rendered HTML template for the figure creation page with an error message if the figure already exists.
            """
            if request.method == 'POST':
                # Retrieve form data
                name = request.form['name']
                series = request.form['series']
                price = float(request.form['price'])
                year = int(request.form['year'])

                # Check if the confirmation checkbox was checked
                if 'check' in request.form:
                    # Attempt to insert the new figure
                    result = FigureQueries.create_figure(name, series, price, year)
                    if result == "Figure successfully inserted":
                        # Redirect to the figure index page on successful insertion
                        return redirect('/index-figures')
                    else:
                        # Render the creation page with an error message if figure already exists
                        return render_template('figure/template-figures-create.html', mensaje=result)
            # Para que te lleve al html TemplateFigurasBuscar

        @app.route('/delete-figure')
        def template_figure_delete():
            """
            Renders the template for deleting figures.

            This route displays a template that provides an interface for searching and removing figures.
            The HTML template `figure/template-figures-delete.html` is rendered, which includes a search form
            and a table to display search results.

            Returns:
            - Rendered HTML template for the figure deletion page.
            """
            return render_template('figure/template-figures-delete.html')

        @app.route('/figure/search/delete', methods=['POST'])
        def delete_figure_search():
            """
            Handles the search and display of figures for deletion.

            This route processes POST requests from the search form used to find figures based on the provided
            search value. It calls the `FigureQueries.search_figure` method to retrieve figures matching the search
            criteria and then renders the `figure/template-figures-delete.html` template with the search results.

            Returns:
            - Rendered HTML template displaying figures found by the search.
            """
            if request.method == 'POST':
                # Get the search value from the form
                value = request.form['value']

                # Execute the search by value
                figures_found = FigureQueries.search_figure(value)

                # Render the result in the corresponding template
                return render_template('figure/template-figures-delete.html', figures=figures_found)

        @app.route('/figure/delete/<figure_value>', methods=['POST'])
        def delete_figures(figure_value):
            """
            Handles the deletion of a specific figure.

            This route processes POST requests to delete a figure identified by the `figure_value` included in the URL.
            It calls the `FigureQueries.delete_figure` method to remove the specified figure from the database and then
            redirects to the index page of figures or another confirmation page.

            Parameters:
            - figure_value (str): The unique identifier of the figure to be deleted.

            Returns:
            - Redirect to the figure index page or another confirmation page after deletion.
            """
            if request.method == 'POST':
                # Attempt to delete the figure by its value
                FigureQueries.delete_figure(figure_value)

                # Redirect to the main page or another confirmation page
                return render_template('figure/template-figures-index.html')

        @app.route('/figures/show-all')
        def show_all_figures():
            """
            Renders a template that displays all figures.

            This route fetches all figures from the Firestore database by calling the
            `FigureQueries.show_all_figures` method. The fetched figures are then passed
            to the HTML template 'template-figures-view-all.html' for rendering, allowing
            the user to view all figures in the system.

            Returns:
                Rendered HTML template displaying all figures.
            """
            # Retrieve all figures using the show_all_figures method
            figures = FigureQueries.show_all_figures()
            # Render the HTML template and pass the figures to the template
            return render_template('figure/template-figures-view-all.html',
                                   result=figures)

        @app.route('/figures/search')
        def search_template_figures():
            return render_template("figure/template_figures_read.html")

        @app.route('/figures/search-query', methods=['POST'])
        def search_figures():
            """
            Handles POST requests to search for figures based on user input.

            This route is designed to handle form submissions where users search
            for figures in the database. The form should send a POST request with
            a search value.

            The function retrieves the search value from the form data, performs
            a search in the database using the `FigureQueries.search_figure`
            method, and then renders a template to display the search results.

            Returns:
                Response: A rendered HTML template displaying the search results.

            Raises:
                KeyError: If 'value' is not found in the form data.
            """
            if request.method == 'POST':
                # Retrieve the search value from the form data
                value = request.form['value']

                # Execute the search query using the FigureQueries class
                figures = FigureQueries.search_figure(value)

                # Render the template with the search results
                return render_template('figure/template_figures_read.html', result=figures)

        @app.route('/figures/update')
        def figures_template_update():
            """
            Render the template for updating figures.

            This route is used to display the form for updating figure details.
            The template `template-figures-update.html` is rendered to the user.
            """
            return render_template('figure/template-figures-update.html')

        @app.route('/figures/search-update', methods=['POST'])
        def figures_update_search():
            """
            Handle the search query for figures to update.

            This route processes the search request submitted via POST method.
            It retrieves the search value from the form, performs the search
            using the `FigureQueries.search_figure` method, and renders the
            update template with the search results.
            """
            if request.method == 'POST':
                # Retrieve the search value from the form submission
                value = request.form['value']

                # Execute the search query to find figures matching the search value
                figures_found = FigureQueries.search_figure(value)

                # Render the update template with the search results
                return render_template('figure/template-figures-update.html', result=figures_found)

        @app.route('/figures/modify-query', methods=['POST'])
        def modify_figure():
            """
            Handles the modification of figure details.

            This route processes the form submission for updating a figure. It updates the figure details
            based on the information provided by the user.

            Steps:
            - Retrieves the form data for the figure to be updated.
            - Calls the `modify_figure` method of the `FigureQueries` class to update the figure details.
            - Redirects to the main page if the update is successful.
            - Displays an error message if the update fails.
            - Handles any exceptions that may occur during the update process.

            Returns:
                Redirects to the main page if the update is successful. Otherwise, renders the update template with an error message.
            """
            if request.method == 'POST':
                try:
                    # Retrieve the form data
                    current_series = request.form['current_series']
                    new_series = request.form['new_series']
                    new_name = request.form['new_name']
                    new_price = float(request.form['new_price'])
                    new_year = int(request.form['new_year'])

                    # Debugging: Print form data to console
                    print(
                        f"Current Series: {current_series}, New Series: {new_series}, New Name: {new_name}, New Price: {new_price}, New Year: {new_year}")

                    # Update the figure details
                    result = FigureQueries.modify_figure(current_series, new_series, new_name, new_price, new_year)
                    print(f"Update Result: {result}")  # Log the result

                    if result == "Figure updated successfully":
                        # Redirect to the main page upon successful update
                        return redirect('/figures/update')
                    else:
                        # Render the update template with an error message if the update fails
                        return render_template('figure/template-figures-update.html', error=result)
                except Exception as e:
                    # Render the update template with an error message if an exception occurs
                    print(f"Exception: {str(e)}")  # Log the exception
                    return render_template('figure/template-figures-update.html', error=str(e))
            else:
                return render_template('figure/template-figures-update.html')

