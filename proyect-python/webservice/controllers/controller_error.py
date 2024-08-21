from flask import Flask, render_template


class ControllerErrors:

    @staticmethod
    def routes_error(app):
        @app.route('/error')
        def error_connection():
            return render_template('error/template-error.html')
