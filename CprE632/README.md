This is the web application written for the Iowa State University graduate capstone course. The premise of the application is that a student will be in charge of hosting the website for a government. Each student will be assigned a country. They will customize the site for their country and secure the application. 

The site will have different types of users including
- Military commanders
- Military personnel
- President

Setup
=====
The database has been created for you. You need to customize `student_setup.py` to contain your student-specific information and run it to initialize the database. DO NOT run `db_init.py`.

You will also need to update the following values in `config.py`:

`UN_NATION_KEY`: This should be your country's UN API key.

`UN_ORIGIN_FQDN`: This will be 'mil.<tld>' for example, Antarctica would put 'mil.aq' here.

`COUNTRY_NAME`: This should be the name of your country.
