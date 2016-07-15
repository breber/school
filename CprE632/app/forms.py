from flask.ext.wtf import Form
from wtforms import StringField, SelectField,  BooleanField, SubmitField, validators, ValidationError, PasswordField, IntegerField, \
    SelectMultipleField
from wtforms.ext.sqlalchemy.fields import QuerySelectField
from wtforms.validators import DataRequired
from wtforms.widgets import TextArea
from .models import db, User, Groups, Orders
from types import *
import logging

class LoginForm(Form):
    username = StringField("username",  [validators.DataRequired("Please enter your username." )])
    password = PasswordField('Password', [validators.DataRequired("Please enter a password.")])
    submit = SubmitField("Sign In")

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

    def validate(self):
        if not Form.validate(self):
            return False

        user = None

        import ldap3
        try:
            isActive = False
            group = ''

            server = ldap3.Server('ldap://192.168.1.6')
            conn = ldap3.Connection(server, user=self.username.data + '@int.mil.du', password=self.password.data, auto_bind=True)
            logging.warn('server: %s' % server)
            if conn.search('DC=int,DC=mil,DC=du', '(sAMAccountName=' + self.username.data + ')', attributes=['memberOf']):
                logging.warn('entries: %s' % conn.entries)
                if len(conn.entries) > 0:
                    for g in conn.entries[0].memberOf:
                        group_names = str(g)

                        logging.warn("Groups: %s" % group_names)
                        if "CN=Duloc Users," in group_names:
                            isActive = True
                        if "CN=Duloc Recruits," in group_names:
                            group = 'recruit'
                        if "CN=Duloc Recruiters," in group_names:
                            group = 'recruiter'
                        if "CN=Duloc Soldiers," in group_names:
                            group = 'soldier'
                        if "CN=Duloc HR," in group_names:
                            group = 'HR'
                        if "CN=Duloc Spy," in group_names:
                            group = 'spy'
                        if "CN=Duloc President," in group_names:
                            group = 'president'
                        if "CN=Duloc Secretary of War," in group_names:
                            group = 'secretary of war'

            logging.warn('active: %s - %s' % (isActive, group))
            if isActive and group:
                user = User.get_user_by_username(self.username.data)

                logging.warn('found_user: %s' % (user))

                if user:
                    group_obj = Groups.query.filter(Groups.groupname == group).first()
                    logging.warn('found_group: %s' % (group_obj))
                    if group_obj:
                        user.group = group_obj.id
                        logging.warn('group_id: %s' % (user.group))

        except ldap3.core.exceptions.LDAPInvalidCredentialsResult:
            logging.warning("Invalid Credentials")
            user = None
        except ldap3.core.exceptions.LDAPBindError:
            logging.warning("Invalid Credentials")
            user = None

        if user:
            db.session.commit()
            return True
        elif not user:
            self.username.errors.append("Invalid username or password")

        return False

class SignupForm(Form):
    username = StringField("username", [validators.DataRequired("Please enter your username."), validators.length(4, 32, "Your username must be between %(min)d and %(max)d characters")])
    ssn = IntegerField("Social Security Number", [validators.DataRequired("Please enter your social security number. This must be a nine digit integer with no hyphens."), validators.NumberRange(100000000, 999999999, "Your Social Security Number must be a nine digit integer")])
    firstname = StringField("First name", [validators.DataRequired("Please enter your first name.")])
    lastname = StringField("Last name", [validators.DataRequired("Please enter your last name.")])
    password = PasswordField('Password', [validators.DataRequired("Please enter a password."), validators.length(6, 24, "Your password must be between %(min)d and %(max)d characters.")])
    submit = SubmitField("Create account")

    def __init__(self, *args, **kwargs):
       Form.__init__(self, *args, **kwargs)

    def validate(self):
        if not Form.validate(self):
            return False
        elif User.get_user_by_username(self.username.data) is not None:
            self.username.errors.append("Username is already taken")
            return False
        elif User.query.filter(User.ssn == self.ssn.data).first() is not None:
            self.ssn.errors.append("Invalid SSN")
            return False
        return True

class OrdersForm(Form):
    orders = StringField("Orders", [validators.DataRequired("Enter Your orders here")], widget=TextArea())
    issued_for = SelectField("User", coerce=str)

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

class CompleteOrderForm(Form):
    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

class PromoteUserForm(Form):
    promote_to = SelectField("Role", coerce=str)
    promote_user = StringField()

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

class EditUserForm(Form):
    ssn = IntegerField("Social Security Number", [validators.DataRequired("Please enter your social security number. This must be a nine digit integer with no hyphens."), validators.NumberRange(100000000,999999999, "Your Social Security Number must be a nine digit integer")])
    firstname = StringField("First name", [validators.DataRequired("Please enter your first name.")])
    lastname = StringField("Last name", [validators.DataRequired("Please enter your last name.")])
    military_id = StringField("Military ID", [validators.DataRequired("Please enter your last name.")])
    group = SelectField("Group", coerce=int)
    submit = SubmitField("edit profile")

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

class SettingsForm(Form):
    ssn = IntegerField("Social Security Number", [validators.DataRequired("Please enter your social security number. This must be a nine digit integer with no hyphens."), validators.NumberRange(100000000, 999999999, "Your Social Security Number must be a nine digit integer")])
    firstname = StringField("First name", [validators.DataRequired("Please enter your first name.")])
    lastname = StringField("Last name", [validators.DataRequired("Please enter your last name.")])
    submit = SubmitField("edit profile")

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

class ReportForm(Form):
    summary = StringField("Orders", [validators.DataRequired("Enter Your orders here")])

    def __init__(self, *args, **kwargs):
        Form.__init__(self, *args, **kwargs)

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
