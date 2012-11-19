#!/usr/bin/python
# (on a *nix machine, above line would be '#!/usr/bin/python'
#  or whatever the path to your Python interpreter is)
import cgi, sys
sys.stderr = sys.stdout   # redirect error messages to browser
form = cgi.FieldStorage() # parse form data

print "Content-type: text/html\n"
html_top = """
<title>Multiplication Table</title>
<h1>Custom multiplication tables</h1>
<h3>{start} * {start} to {end} * {end}</h3>
<hr />
<table border="1">
"""

if not (form.has_key('start') and form.has_key('end')):
    print html_top.format(start='?', end='?')
    print '<tr><td><strong>Error! Start or end not given</strong></td></tr>'
else:
    try:
        # Values from an HTML form are initially stored as strings;
        # need to convert them to ints for math operations
        start_num = int(form['start'].value)
        end_num   = int(form['end'].value)
    except:
        print html_top.format(start='?', end='?')
        print '<tr><td>'
        print '<strong>Error! Start and end values must be numbers.</strong>'
        print '</td></tr>'
    else: # This block is executed only if no exceptions are raised
        print html_top.format(start=start_num, end=end_num)

        # Reverse start and end numbers if start < end
        if start_num > end_num:
            temp = start_num
            start_num = end_num
            end_num = temp
            print '<tr><th colspan="{0}"><strong>'.format(end_num-start_num+2)
            print 'Your start number was greater than your end number. ',
            print 'Fixed that for you!'
            print '<strong></td></tr>'
            
        # Print header row
        print '<tr><th></th>' # blank first cell
        for x in range(start_num, end_num + 1):
            print '<th>{0}</th>'.format(x)
        print '</tr>'

        # Print rows of table
        for x in range(start_num, end_num + 1):
            print '<tr><th>{0}</th>'.format(x)
            for y in range(start_num, end_num + 1):
                print '<td>{0}</td>'.format(x * y)
            print '</tr>'            

print '</table>'
