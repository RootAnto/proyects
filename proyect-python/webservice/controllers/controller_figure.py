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
