#!/usr/bin/env python
import sys
sys.path.append('/usr/local/lib/python2.7/dist-packages')

from app import app
app.run(host='0.0.0.0', debug=True)
