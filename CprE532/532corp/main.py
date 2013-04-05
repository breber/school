from google.appengine.ext import ndb
from google.appengine.ext.webapp import template
import webapp2
import models
import os

class MainHandler(webapp2.RequestHandler):
    def get(self):
        path = os.path.join(os.path.dirname(__file__), 'templates/base.html')
        self.response.out.write(template.render(path, {}))

class RecordHandler(webapp2.RequestHandler):
    def post(self):
        password = self.request.get("password")
        
        if password:
            toStore = models.Password()
            toStore.password = password
            toStore.put()
        
        self.redirect("/")

app = webapp2.WSGIApplication([
    ('/', MainHandler),
    ('/record', RecordHandler)
], debug=True)
