#!/usr/bin/env python
from app import db, models
#roles 1-recruit 2-recruiter 3-soldier 4-HR 5-spy 6-president 7-secretary
db.session.add(models.Groups('recruit'))
db.session.add(models.Groups('recruiter'))
db.session.add(models.Groups('soldier'))
db.session.add(models.Groups('HR'))
db.session.add(models.Groups('spy'))
db.session.add(models.Groups('president'))
db.session.add(models.Groups('secretary of war'))
db.session.add(models.User('rectuit','Jeff','Neel','recruit@mil.gov','pw',1,380648776))
db.session.add(models.User('recruiter','Jeff','Neel','recruiter@iastate.edu','pw',2,78352964))
db.session.add(models.User('soldier','Jeff','Neel','soldier@iastate.edu','pw',3,476395765))
db.session.add(models.User('hr','Jeff','Neel','hr@iastate.edu','pw',4,846382968))
db.session.add(models.User('spy','Jeff','Neel','commander@iastate.edu','pw',5,876598635))
db.session.add(models.User('president','Jeff','Neel','president@iastate.edu','pw',6,275420847))
db.session.add(models.User('sow','Jeff','Neel','sow@iastate.edu','pw',7,444276666))
db.session.commit()
