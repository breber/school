#!/usr/bin/env python
from app import db, models
"""
This file will create all necessary data for the web application with a good set of data

create roles in database MUST BE DONE FIREST 

 1-recruit 2-recruiter 3-soldier 4-HR 5-spy 6-president 7-secretary
"""
TLD ="[[INSERT TLD HERE]]" 
db.session.add(models.Groups('recruit'))
db.session.add(models.Groups('recruiter'))
db.session.add(models.Groups('soldier'))
db.session.add(models.Groups('HR'))
db.session.add(models.Groups('spy'))
db.session.add(models.Groups('president'))
db.session.add(models.Groups('secretary of war'))


#create users replace all informatinon if the format [[REPLACE]] with your student specific information
#initilize users format User(username, firstname, lastname, email, password, group number, ssn)

#Recruit
username = 'ewallace'
password = '[[USERS PASSWORD]]'
ssn = [[USERS SSN]]
firstname = 'Eli'
lastname = 'Wallace'
db.session.add( models.User( username , firstname , lastname , username + '@mil.' + TLD , password , 1 , ssn))

#Recruit
username = 'carmstrong'
password = '[[USERS PASSWORD]]'
ssn = [[USERS SSN]]
firstname = 'Chloe'
lastname = 'Armstrong'
db.session.add( models.User( username , firstname , lastname , username + '@mil.' + TLD , password , 1 , ssn))

#Recruiter
username='dtelford'
password = '[[USERS PASSWORD]]'
ssn = [[USERS SSN]]
firstname = 'David'
lastname = 'Telford'
db.session.add( models.User( username , firstname , lastname , username + '@mil.' + TLD , password , 2 , ssn))


#Soldier
username='rgreer'
password = '[[USERS PASSWORD]]'
ssn = [[USERS SSN]]
firstname = 'Ronald'
lastname = 'Greer'
db.session.add( models.User( username , firstname , lastname , username + '@mil.' + TLD , password , 3 , ssn))

#Soldier
username='mscott'
password = '[[USERS PASSWORD]]'
ssn = [[USERS SSN]]
firstname = 'Matthew'
lastname = 'Scott'
db.session.add( models.User( username , firstname , lastname , username + '@mil.' + TLD , password , 3 , ssn))

#HR
username='cwray'
password = '[[USERS PASSWORD]]'
ssn = [[USERS SSN]]
firstname = 'Camile'
lastname = 'Wray'
db.session.add( models.User( username , firstname , lastname , username + '@mil.' + TLD , password , 4 , ssn))

#President
username='hhayes'
password = '[[USERS PASSWORD]]'
ssn = [[USERS SSN]]
firstname = 'Henry'
lastname = 'Hayes'
db.session.add( models.User( username , firstname , lastname , username + '@mil.' + TLD , password , 6 , ssn))

#Secretary Of War
username='eyoung'
password = '[[USERS PASSWORD]]'
ssn = [[USERS SSN]]
firstname = 'Everett'
lastname = 'Young'
db.session.add( models.User( username , firstname , lastname , username + '@mil.' + TLD , password , 7 , ssn))

#spy
spyusername = 'blacktop'
password = '[[USERS PASSWORD]]'
ssn = [[USERS SSN]]
firstname = '[[USERS FIRST NAME]]'
lastname = '[[USERS LAST NAME]]'
db.session.add( models.User( spyusername , firstname , lastname , spyusername + '@mil.' + TLD , password , 5 , ssn))

#initilize reports 
#format Report(username, mission summary)
report_flag = '[[INPUT REPORT FLAG HERE]]'
mission_details = '[[write your own mission report for this users first mission]]'
mission_details = mission_details + 'During my mission I found the following code I\'m not sure what it means but maybe our staff will be able to decode it. ' + report_flag
db.session.add( models.Report( spyusername , mission_details))
mission_details = '[[write your own mission report for this users second mission]]'
db.session.add( models.Report( spyusername , mission_details))

#initilize orders
#format Order(Orders, issued_by, issued_for)
"""
NOTE: Feel free to add as many of your own orders as you want this is your site customize it and have
 some fun with it. you can add them here or through the web application it\'s self
"""
#ORDER WITH THE FLAG!!!!!
orders_flag = '[[PLACE ORDER FLAG HERE]]'
orders='Colonel Young, \n\n your orders are to defemd this country by any means necessary! \n\n Keep Our Soldiers Safe \n President Hayes. \n\n Identifcation code:' + orders_flag 
db.session.add( models.Orders( orders, 'hhayes', 'eyoung'))

#Recruiter to New Recruits
orders='Welcome Recruit, \n\n We are currently reviewing your information and will appoint you to a permanent position shortly. HR will be in contact with you to verify some of your information. \n\n Best of Luck \n Recruiter Telford.' 
db.session.add( models.Orders( orders, 'dtelford', 'ewallace'))

orders='Welcome Recruit, \n\n We are currently reviewing your information and will appoint you to a permanent position shortly. HR will be in contact with you to verify some of your information. \n\n Best of Luck \n Recruiter Telford.' 
db.session.add( models.Orders( orders, 'dtelford', 'carmstrong'))

#HR to New Recruits
orders='Welcome Recruit, \n\n We are working to verify your information for Recruiter Telford. Please send me a order containing the following information. \n\nFull Name \n Email address \n\n Welcome, \n Camile Wray' 
db.session.add( models.Orders( orders, 'cwray', 'ewallace'))

orders='Welcome Recruit, \n\n We are working to verify your information for Recruiter Telford. Please send me a order containing the following information. \n \n Full Name \n Email address \n\n Welcome, \n Camile Wray' 
db.session.add( models.Orders( orders, 'cwray', 'carmstrong'))

#New Recruits to HR
orders='Ms. Wray\n\n Here is the information that you requested \n Email:ewallace@mil.'+TLD+'\n Name:Eli Wallace  \n Thank You, \n\n Eli Wallace'
db.session.add( models.Orders( orders, 'ewallace', 'cwray'))

orders='Ms. Wray\n\n Here is the information that you requested \n Email:carmstrong@mil.'+TLD+'\n Name:Chloe Armstrong  \n Thank You, \n\n Chloe Armstrong' 
db.session.add( models.Orders( orders, 'carmstrong', 'cwray'))

#SOW to Soldiers
orders='Soldier, \n\n We have gathered intelligence that some of our enemies are plotting an attack on our homeland. You and your battalion are being called on to server our county. \n\n Secretary Of War \n Everett Young'
db.session.add( models.Orders( orders, 'eyoung', 'rgreer'))

orders='Soldier, \n\n We have gathered intelligence that some of our enemies are plotting an attack on our homeland. You and your battalion are being called on to server our county. \n\n Secretary Of War \n Everett Young'
db.session.add( models.Orders( orders, 'eyoung', 'mscott'))

# President to mscott
orders='Matthew Scott \n\n We Have recognized your outstanding commitment to serving your country and would like to recognize you for your efforts. Your commanding officer will contact you with more details \n\n President Hayes' 
db.session.add( models.Orders( orders, 'hhayes', 'mscott'))

#cmaile to telford
orders='Mr. Telford, \n\n The president has informed me that we are in dire need of new soldiers. If you have any recruits that are ready for deployment please promote them to soldiers.'

db.session.add( models.Orders( orders, 'cwray', 'dtelford'))

db.session.commit()
