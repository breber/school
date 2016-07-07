from flask import Flask
from flask.ext.sqlalchemy import SQLAlchemy
from flask_login import LoginManager

app = Flask(__name__)
app.config.from_object('config')
db = SQLAlchemy(app)

def user_unauthorized_callback():
    """
    Helper required by Flask-Login for returning a redirect to the login page.
    """
    return redirect('/login')

def load_user(username):
    """
    Helper required by Flask-Login for returning the current user.
    :param username: The username to lookup.
    """
    from models import User
    return User.get_user_by_username(username)

# Setting up the login manager for Flask-Login
login_manager = LoginManager()
login_manager.init_app(app)
login_manager.unauthorized_handler(user_unauthorized_callback)
login_manager.user_loader(load_user)

from app import views
