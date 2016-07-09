import os
basedir = os.path.abspath(os.path.dirname(__file__))

SQLALCHEMY_DATABASE_URI = 'mysql://root:632iowastatepassword@localhost/country'
# For session management
SECRET_KEY = '\x83\xf0\x18\x93\x97\x8dx\xf5\xe1\xe4\xf0L\x13A\xac\xa6G\xa4@V\xa3Yy '
WTF_CSRF_ENABLED = False

COUNTRY_NAME = 'Duloc'

WEBSERVER_PASS = 'thepasswordtochangepasswords'

# UN Settings
UN_NATION_KEY = 'prd5eXc+KWZ/J+uaHRqu3A=='
UN_URL = 'http://un.hq.net' # NO TRAILING SLASH
UN_ORIGIN_FQDN = 'mil.du'
