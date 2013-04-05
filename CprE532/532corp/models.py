from google.appengine.ext import ndb

class Password(ndb.Model):
    password = ndb.StringProperty()
