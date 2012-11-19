#!/usr/bin/python
# (on a *nix machine, above line would be '#!/usr/bin/python'
#  or whatever the path to your Python interpreter is)

#######################################################################
# Adapted from Example 16-8 (p. 989) in _Programming Python_
# by Mark Lutz, 3rd edition. O'Reilly, 2006.
#######################################################################

import cgi
form = cgi.FieldStorage()       # parse form data
print "Content-type: text/html" # plus blank line

html = """
<title>Form Result</title>
<h1>Greetings</h1><hr />
<p>{0}</p><hr />"""

if not form.has_key('user'):
    print html.format("Who are you?")
else:
    print html.format("Hi," + form['user'].value)
