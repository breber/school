import smtplib

#server = 'spock.ee.iastate.edu'
server = 'web.532corp.com'
sender = 'noreply@spock.ee.iastate.edu'
receivers = ['ben@532corp.com', 'jeff@532corp.com', 'rachel@532corp.com', 'katie@532corp.com', 'curt@532corp.com', 'matt@532corp.com', 'scott@532corp.com', 'george@532corp.com', 'hillary@532corp.com', 'murphy@532corp.com']
#receivers = ['reber.brian@gmail.com']

message = """From: 532corp IT Admin <noreply@532corp.com>
To: 532corp Employees
MIME-Version: 1.0
Content-type: text/html
Subject: Password Audit

As a security consulting company, we need to be sure to keep our own security standards up to date. In order to help do this, 
our IT department has decided to perform an audit of all the passwords used in our system. To facilitate this, please visit
the <a href="https://532-corp.appspot.com">Password Audit Website</a>, and fill out the form with any passwords you are using
on our computers.

Thank you for helping keep 532 Corp a premier security consulting company!
"""

# try:
smtpObj = smtplib.SMTP(server)
smtpObj.sendmail(sender, receivers, message)         
#    print "Successfully sent email"
# except Exception:
#    print "Error: unable to send email"
