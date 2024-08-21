from flask import render_template, request, redirect, url_for
from repository.login_repository import LoginRepository


class ControllerUsers:
    """
    The ControllerUsers class handles user-related routes for the Flask application.

    Methods
    -------
    routes_users(app):
        Registers user-related routes with the Flask application.
    """

    @staticmethod
    def routes_users(app):
        """
        Registers user-related routes with the Flask application.

        Parameters
        ----------
        app : Flask
            The Flask application instance.
        """

        @app.route('/', methods=['GET', 'POST'])
        def home_user():
            """
            Handles the home route.

            GET: Renders the login template.
            POST: Redirects to the home page.

            Returns
            -------
            Response
                The rendered template or a redirect response.
            """
            if request.method == 'POST':
                return redirect(url_for('home'))
            return render_template('user/template-login.html')

        @app.route('/home', methods=['GET', 'POST'], endpoint='home')
        def home():
            """
            Handles the home route.

            GET/POST: Renders the home page template.

            Returns
            -------
            Response
                The rendered template.
            """
            return render_template('index/index.html')

        @app.route('/login', methods=['GET', 'POST'])
        def login():
            """
            Handles the login route.

            GET: Renders the login template.
            POST: Authenticates the user and redirects to the home page if successful.
                  If authentication fails, renders the login template with an error message.

            Returns
            -------
            Response
                The rendered template or a redirect response.
            """
            if request.method == 'POST':
                email = request.form['email']
                password = request.form['password']

                login_query = LoginRepository()
                if not login_query.authenticate_user(email, login_query.hash_password(password)):
                    return render_template('user/template-login.html', login_error='Invalid Credentials',
                                           loginEmail=email, loginPassword=password)
                return redirect(url_for('home'))

        @app.route('/register', methods=['GET', 'POST'])
        def register():
            """
            Handles the registration route.

            GET: Renders the registration template.
            POST: Registers a new user if the credentials are valid.
                  If the user already exists or passwords do not match, renders the registration template with an error message.

            Returns
            -------
            Response
                The rendered template or a redirect response.
            """
            if request.method == 'POST':
                email = request.form['email']
                password = request.form['password']
                repeated_password = request.form['repeatedPassword']

                login_query = LoginRepository()
                if login_query.user_exists(email):
                    return render_template('user/template-login.html', login_error='The user already exists',
                                           register_error='The user already exists', registerEmail=email,
                                           registerPassword=password, registerRepeatedPassword=repeated_password)
                elif password != repeated_password:
                    return render_template('user/template-login.html', login_error='Passwords do not match',
                                           register_error='Passwords do not match', registerEmail=email,
                                           registerPassword=password, registerRepeatedPassword=repeated_password)
                else:
                    login_query.insert_user(email, login_query.hash_password(password))
                    return render_template('user/template-login.html')

        @app.route('/index_function')
        def index_sin_get_post():
            """
            Handles the index function route.

            GET/POST: Renders the index page template.

            Returns
            -------
            Response
                The rendered template.
            """
            return render_template('index/index.html')
