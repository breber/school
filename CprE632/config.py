import os
basedir = os.path.abspath(os.path.dirname(__file__))

# TODO: change password
SQLALCHEMY_DATABASE_URI = 'mysql://root:cdc@localhost/country'

WTF_CSRF_ENABLED = False

# TODO: change secret
SECRET_KEY = 'It-is-a-secret'

COUNTRY_NAME = 'Duloc'

# UN Settings
# TODO: use real key
UN_NATION_KEY = 'test-nation-key'
UN_URL = 'http://un.hq.net' # NO TRAILING SLASH
UN_ORIGIN_FQDN = 'mil.du'
