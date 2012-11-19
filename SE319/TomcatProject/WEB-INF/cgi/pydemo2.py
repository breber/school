#!/usr/bin/python
# (on a *nix machine, above line would be '#!/usr/bin/python'
#  or whatever the path to your Python interpreter is)

#######################################################################
# Adapted from Example 16-10 (pp. 995-996) in _Programming Python_
# by Mark Lutz, 3rd edition. O'Reilly, 2006.
#######################################################################

import cgi, sys
sys.stderr = sys.stdout # redirect error messages to browser
form = cgi.FieldStorage() # parse form data
print "Content-type: text/html\n"
html = """
<title>tutor4.py</title>
<h1>Greetings</h1>
<hr />
<h4>{0}</h4>
<h4>{1}</h4>
<h4>{2}</h4>
<hr />
"""

line1 = ""
if not form.has_key('user'):
    line1 = "Who are you?"
else:
    line1 = "Hello," + form['user'].value

line2 = "You're talking to a {srv} server.".format(srv=sys.platform)
line3 = ""
if form.has_key('age'):
    try:
        line3 = "Your age squared is {0}!".format(int(form['age'].value) ** 2)
    except:
        line3 = "Sorry, I can't compute {0} ** 2.".format(form['age'].value)

print html.format(line1, line2, line3)
