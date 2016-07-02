import os
basedir = os.path.abspath(os.path.dirname(__file__))

#SQLALCHEMY_DATABASE_URI = 'sqlite:///' + os.path.join(basedir, 'app.db')
SQLALCHEMY_DATABASE_URI = 'mysql://root:cdc@localhost/country'
SQLALCHEMY_MIGRATE_REPO = os.path.join(basedir, 'db_repository')

WTF_CSRF_ENABLED = False
SECRET_KEY = 'It-is-a-secret'

COUNTRY_NAME = 'South Pole'

# UN Settings
UN_NATION_KEY = 'test-nation-key'
UN_URL = 'http://un.hq.net' # NO TRAILING SLASH
UN_ORIGIN_FQDN = 'mil.aq'
