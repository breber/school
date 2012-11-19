#!/usr/bin/python
# (on a *nix machine, above line would be '#!/usr/bin/python'
#  or whatever the path to your Python interpreter is)

#######################################################################
# Adapted from Example 16-12 (pp. 1001-1002) in _Programming Python_
# by Mark Lutz, 3rd edition. O'Reilly, 2006.
#######################################################################

import cgi, sys
sys.stderr = sys.stdout   # redirect error messages to browser
form = cgi.FieldStorage() # parse form data

print "Content-type: text/html\n"
html = """
<title>Reading Many Inputs with Python/CGI</title>
<h1>Greetings</h1>
<hr />
<h4>Your name is {name}</h4>
<h4>You wear {shoesize} shoes</h4>
<h4>Your current job: {job}</h4>
<h4>You type programs using {editor}</h4>
<h4>You also said:</h4>
<p>{comment}</p>
<hr />"""

# This 'for' loop iterates over the expected input fields in 'form',
# the dictionary that contains the CGI form input.
data = {} # an empty dictionary
for field in ('name', 'shoesize', 'job', 'editor', 'comment'):
    if not form.has_key(field):
        data[field] = '(unknown)'
    else:
        if type(form[field]) != list:
            data[field] = form[field].value
        else:
            values = [x.value for x in form[field]]
                # above line is a 'list comprehension':
                # gets all values selected for the given input field
            data[field] = ' and '.join(values)

print html.format(name=data['name'], shoesize=data['shoesize'], job=data['job'],
                  editor=data['editor'], comment=data['comment'])
