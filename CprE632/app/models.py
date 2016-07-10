from app import app, db
import datetime
import logging
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

    @classmethod
    def get_user_by_id(cls, user_id):
        """
        Gets a user object by the username
        :param username: The username to lookup.
        :returns: The user object if it exists, else None
        """
        return User.query.filter(User.id == user_id).first()

    @classmethod
    def new_user(cls, username, firstname, lastname, password, ssn):
        import ldap3
        try:
            server = ldap3.Server('ldaps://dc.int.mil.du')
            conn = ldap3.Connection(server, user='webserver@int.mil.du', password=app.config['WEBSERVER_PASS'], auto_bind=True)

            attrs = {
                'givenName': firstname,
                'sn': lastname,
                'sAMAccountName': username,
                'userPrincipalName': '%s@int.mil.du' % username,
                # https://support.microsoft.com/en-us/kb/305144
                'userAccountControl': 546
            }

            if conn.add('CN=%s,CN=Users,DC=int,DC=mil,DC=du' % username, object_class='User', attributes=attrs):
                # Add user to groups
                add_modifications = {
                    'member': [
                        (ldap3.MODIFY_ADD, [
                            'CN=%s,CN=Users,DC=int,DC=mil,DC=du' % username
                        ])
                    ]
                }
                modify_res = conn.modify('CN=Duloc Users,CN=Users,DC=int,DC=mil,DC=du', add_modifications)
                modify_res = modify_res and conn.modify('CN=Duloc Recruits,CN=Users,DC=int,DC=mil,DC=du', add_modifications)

                pwd_modifications = {
                    'unicodePwd': [
                        (ldap3.MODIFY_REPLACE, [
                            ('"%s"' % password).encode('utf_16_le')
                        ])
                    ]
                }
                modify_res = conn.modify('CN=%s,CN=Users,DC=int,DC=mil,DC=du' % username, pwd_modifications)
                logging.warn('pass_modification: %s' % modify_res)

                pwd_modifications = {
                    'userAccountControl': [
                        (ldap3.MODIFY_REPLACE, [
                            512
                        ])
                    ]
                }
                modify_res = conn.modify('CN=%s,CN=Users,DC=int,DC=mil,DC=du' % username, pwd_modifications)
                logging.warn('acct_modification: %s' % modify_res)

                newuser = User(username,
                               firstname,
                               lastname,
                               Groups.query.filter(Groups.groupname == 'recruit').first().id,
                               ssn)
                db.session.add(newuser)
                db.session.commit()

            if conn.search('DC=int,DC=mil,DC=du', '(sAMAccountName=' + username + ')', attributes=['memberOf']):
                logging.warn('new_entries: %s' % conn.entries)

        except ldap3.core.exceptions.LDAPInvalidCredentialsResult:
            logging.warning("Invalid Credentials")
        except ldap3.core.exceptions.LDAPException:
            logging.warning("other exception")

    @classmethod
    def change_group(cls, username, new_group):
        user = User.get_user_by_username(username)

        modify_res = False
        import ldap3
        try:
            isActive = False
            group_names = {
                'recruit': 'CN=Duloc Recruits,CN=Users,DC=int,DC=mil,DC=du',
                'recruiter': 'CN=Duloc Recruiters,CN=Users,DC=int,DC=mil,DC=du',
                'soldier': 'CN=Duloc Soldiers,CN=Users,DC=int,DC=mil,DC=du',
                'HR': 'CN=Duloc HR,CN=Users,DC=int,DC=mil,DC=du',
                'spy': 'CN=Duloc Spy,CN=Users,DC=int,DC=mil,DC=du',
                'president': 'CN=Duloc President,CN=Users,DC=int,DC=mil,DC=du',
                'secretary of war': 'CN=Duloc Secretary of War,CN=Users,DC=int,DC=mil,DC=du',
            }
            server = ldap3.Server('ldap://192.168.1.6')
            conn = ldap3.Connection(server, user='webserver@int.mil.du', password=app.config['WEBSERVER_PASS'], auto_bind=True)

            # First find which groups the user is a member of
            if conn.search('DC=int,DC=mil,DC=du', '(sAMAccountName=' + username + ')', attributes=['memberOf']):
                logging.warn('existing_entries: %s' % conn.entries)
                if len(conn.entries) == 1:
                    # Figure out what group the user is currently in
                    remove_group = ''
                    for g in conn.entries[0].memberOf:
                        group_name = str(g)
                        if not "CN=Duloc Users," in group_name and not group_names[new_group] in group_name:
                            remove_group = group_name

                    # Add user to new group
                    add_modifications = {
                        'member': [
                            (ldap3.MODIFY_ADD, [
                                'CN=%s,CN=Users,DC=int,DC=mil,DC=du' % username
                            ])
                        ]
                    }
                    modify_res = conn.modify(group_names[new_group], add_modifications)

                    # Remove user from old group
                    remove_modifications = {
                        'member': [
                            (ldap3.MODIFY_DELETE, [
                                'CN=%s,CN=Users,DC=int,DC=mil,DC=du' % username
                            ])
                        ]
                    }
                    modify_res = modify_res and conn.modify(remove_group, remove_modifications)

            if conn.search('DC=int,DC=mil,DC=du', '(sAMAccountName=' + username + ')', attributes=['memberOf']):
                logging.warn('new_entries: %s' % conn.entries)

        except ldap3.core.exceptions.LDAPInvalidCredentialsResult:
            logging.warning("Invalid Credentials")
        except ldap3.core.exceptions.LDAPException:
            logging.warning("other exception")

        if modify_res:
            user.group = Groups.query.filter(Groups.groupname == new_group).first().id
            db.session.commit()

        return modify_res

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
