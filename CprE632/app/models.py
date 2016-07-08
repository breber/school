from app import db
import datetime
import random
import string

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(32), unique=True)
    firstname = db.Column(db.String(32))
    lastname = db.Column(db.String(32))
    picture = db.Column(db.String(64))
    group = db.Column(db.Integer)
    military_id = db.Column(db.String(16), unique=True)
    ssn = db.Column(db.Integer, unique=True)
    creation_date = db.Column(db.DateTime)

    def __init__(self, username, firstname, lastname, group, ssn):
        self.username = username.lower()
        self.firstname = firstname.title()
        self.lastname = lastname.title()
        self.group = group
        self.picture = 'static/default.jpg'
        self.creation_date = datetime.datetime.now()
        self.ssn= ssn
        self.military_id = ''.join(random.choice(string.ascii_uppercase + string.ascii_lowercase + string.digits) for _ in range(16))

    def get_fullname(self):
        return str(self.firstname) + ' ' + str(self.lastname)
    def email(self):
        return '%s@mil.du' % self.username
    def get_group(self):
        return Groups.query.filter(Groups.id == self.group).first().groupname
    def get_groupid(self,id):
        return Groups.query.filter(Groups.id == id).first().groupname

    """
    For flask_login
    """
    is_active = True
    is_anonymous = False

    def get_id(self):
        """
        For flask_login
        """
        return self.username

    @classmethod
    def get_user_by_username(cls, username):
        """
        Gets a user object by the username
        :param username: The username to lookup.
        :returns: The user object if it exists, else None
        """
        return User.query.filter(User.username == username).first()

    def __repr__(self):
        return '{}'.format(self.username)

class Groups(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    groupname = db.Column(db.String(64), unique=True)

    def __init__(self, groupname):
        self.groupname = groupname

    @classmethod
    def get_nominatable_groups(cls):
        """
        Gets all groups that users can be added to
        """
        all_groups = Groups.query.all()

        filtered_groups = []
        for g in all_groups:
            if not g.groupname in ['president', 'secretary of war']:
                filtered_groups.append(g)

        return filtered_groups

    def get_group(self):
        return Groups.query.filter(Groups.id == self.group).first().groupname

    def __repr__(self):
        return '<group:{}>'.format(self.groupname)

class Orders(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    orders = db.Column(db.String(1000))
    seen = db.Column(db.Boolean)
    issued_by = db.Column(db.String(32))
    issued_for = db.Column(db.String(32))
    completed = db.Column(db.Boolean)
    completed_date = db.Column(db.DateTime)
    issued_date = db.Column(db.DateTime)

    def __init__(self, orders, issued_by, issued_for):
        self.orders = orders
        self.seen = False
        self.issued_by = issued_by
        self.issued_for = issued_for
        self.completed = False
        self.issued_date = datetime.datetime.now()
        self.completed_date = None
    def mark_completed(self):
        self.completed=True
        self.completed_date = datetime.datetime.now()
    def __repr__(self):
        return '<{} issued the following orders to {}: {}>'.format( self.issued_by,self.issued_for,self.orders)

class Report(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    summary = db.Column(db.String(1000))
    username = db.Column(db.String(32))
    date = db.Column(db.DateTime)

    def __init__(self, username, summary):
        self.username = username
        self.summary = summary
        self.date = datetime.datetime.now()
