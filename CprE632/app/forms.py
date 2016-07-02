from flask.ext.wtf import Form
from app import app
from wtforms import StringField, SelectField,  BooleanField, SubmitField, validators, ValidationError, PasswordField, IntegerField, \
    SelectMultipleField
from wtforms.ext.sqlalchemy.fields import QuerySelectField
from wtforms.validators import DataRequired
from wtforms.widgets import TextArea
from .models import db, User, Session, Groups, Orders
from types import *


class LoginForm(Form):

    username = StringField("username",  [validators.DataRequired("Please enter your username." )])
    password = PasswordField('Password', [validators.DataRequired("Please enter a password.")])
    submit = SubmitField("Sign In")

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

    def validate(self):
        if not Form.validate(self):
            return False

        user = User.query.filter(User.username == self.username.data.lower()).first()
        if user and user.check_password(self.password.data):
            return True
        elif not user:
            self.username.errors.append("Invalid username")
        else:
            self.password.errors.append("Invalid password")
        return False



class SignupForm(Form):
    username = StringField("username",  [validators.DataRequired("Please enter your username."), validators.length(4,32, "Your username must be between %(min)d and %(max)d characters")])
    ssn = IntegerField("Social Security Number",  [validators.DataRequired("Please enter your social security number. This must be a nine digit integer with no hyphens."), validators.NumberRange(100000000,999999999, "Your Soscial Security Number must be a nine digit integer ")])
    firstname = StringField("First name",  [validators.DataRequired("Please enter your first name.")])
    lastname = StringField("Last name",  [validators.DataRequired("Please enter your last name.")])
    email = StringField("Email",  [validators.DataRequired("Please enter your email address."), validators.Email("Please enter your email address.")])
    group = SelectField(u'Group',coerce=int)
    password = PasswordField('Password', [validators.DataRequired("Please enter a password."),validators.length(2, 12, "Your password must be between %(min)d and %(max)d characters.")])
    submit = SubmitField("Create account")

    def __init__(self, *args, **kwargs):
       Form.__init__(self, *args, **kwargs)

    def validate(self):
        if not Form.validate(self):
            return False
        elif User.query.filter(User.username == str(self.username.data.lower())).first() is not None:
            self.username.errors.append("Username is already taken")
            return False
        elif User.query.filter(User.email == str(self.email.data.lower())).first() is not None:
            self.email.errors.append("E-mail is already taken")
            return False
        else:
            return True


class OrdersForm(Form):
    orders = StringField("Orders",  [validators.DataRequired("Enter Your orders here")], widget=TextArea())
    issued_for = SelectField(u'User',coerce=str)

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

    def validate(self):
        if not Form.validate(self):
            return False
        return True


class CompleteOrderForm(Form):

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

    def validate(self):
        if not Form.validate(self):
            return False
        return True

class PromoteUserForm(Form):
    promote_to = SelectField(u'Role',coerce=str)
    promeote_user = StringField()
    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

    def validate(self):
        if not Form.validate(self):
            return False
        return True

class EditUserForm(Form):
    ssn = IntegerField("Social Security Number",  [validators.DataRequired("Please enter your social security number. This must be a nine digit integer with no hyphens."), validators.NumberRange(100000000,999999999, "Your Soscial Security Number must be a nine digit integer ")])
    firstname = StringField("First name",  [validators.DataRequired("Please enter your first name.")])
    lastname = StringField("Last name",  [validators.DataRequired("Please enter your last name.")])
    military_id = StringField("Military ID",  [validators.DataRequired("Please enter your last name.")])
    email = StringField("Email",  [validators.DataRequired("Please enter your email address."), validators.Email("Please enter your email address.")])
    group = SelectField(u'Group',coerce=int)
    password = StringField('Password', [validators.DataRequired("Please enter a password."),validators.length(2, 12, "Your password must be between %(min)d and %(max)d characters.")])

    submit = SubmitField("edit profile")

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

    def validate(self):
        if not Form.validate(self):
            return False
        return True

class ReportForm(Form):
    summary = StringField("Orders",  [validators.DataRequired("Enter Your orders here")])

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

    def validate(self):
        if not Form.validate(self):
            return False
        return True



class UNMessageForm(Form):
    MESSAGE_TYPES = [
        ('message', 'Message'),
        ('treaty', 'Peace Treaty'),
        ('declaration', 'Declare War'),
    ]

    COUNTRIES = [
        ('mil.aq', 'North Pole (TEST)'),
        ('mil.ab', 'Agrabah'),
        ('mil.av', 'Alvania'),
        ('mil.an', 'Anchuria'),
        ('mil.ba', 'Bialya'),
        ('mil.cy', 'Cymmeria'),
        ('mil.du', 'Duloc'),
        ('mil.ec', 'Eastern Coalition'),
        ('mil.fl', 'Florin'),
        ('mil.gn', 'Genovia'),
        ('mil.gd', 'Gondor'),
        ('mil.gu', 'Guilder'),
        ('mil.kb', 'Kambezi'),
        ('mil.ll', 'Loompaland'),
        ('mil.mc', 'Malicuria'),
        ('mil.md', 'Moldavia'),
        ('mil.ro', 'Romanovia'),
        ('mil.tm', 'Telmar'),
        ('mil.te', 'Termina'),
        ('mil.vp', 'Vespugia'),
    ]

    type = SelectField('type', choices=MESSAGE_TYPES)
    body = StringField('body')
    destination = SelectMultipleField('destination', choices=COUNTRIES)
