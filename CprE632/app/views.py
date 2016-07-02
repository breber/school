from flask import render_template, flash, redirect, request, session, url_for, make_response
from app import app, utils
from .forms import LoginForm, SignupForm, OrdersForm, CompleteOrderForm, PromoteUserForm, EditUserForm, ReportForm, UNMessageForm
from .models import db, User, Session, Orders, Groups, Report
from functools import wraps
from sqlalchemy.exc import *
from sqlalchemy import  desc
from random import *

def get_user(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        session = None
        user = None
        try:
            session = Session.query.filter(Session.session_id == request.cookies.get('session_id')).first()
            if session is not None:
                user = session.get_user()
        except NoSuchTableError:
            pass
        return f(user=user, session=session, *args, **kwargs)
    return decorated_function

@app.route('/')
@app.route('/index')
def index():
   user = request.cookies.get('session_id')
   return render_template('index.html', title='Home', user=user, group=request.cookies.get('group'))

@app.route('/login', methods =['GET', 'POST'])
def login():

    #logs the user out if they are already logged in
    if request.cookies.get('session_id') is not None:
        response= make_response(redirect('/login'))
        response.set_cookie('session_id', '', expires=0)
        response.set_cookie('group', '', expires=0)
        return response

    form=LoginForm()
    if request.method == 'POST':
        if form.validate_on_submit() == False:
            flash('login request Failed for username= "%s", password=%s' %(form.username.data, str(form.password.data)))
            return render_template('login.html',form=form, group=request.cookies.get('group'))
        elif form.validate_on_submit() == True:
            user = User.query.filter(User.username == request.form.get('username')).first()
            password = request.form.get('password')
            if user.exists:
                if user.password == password:
                    session_id = request.cookies.get('session_id', password + str(randint(1,999)))
                    new_session = Session(user.username, session_id, True)
                    db.session.add(new_session)
                    db.session.commit()
                    response = make_response(redirect('/profile'))
                    response.set_cookie('session_id', value=session_id)
                    group=Groups.query.filter(Groups.id == user.group).first().groupname
                    response.set_cookie('group', value=group)
                    return response
                error = "Password is incorrect."
                flash('Password "%s" is incorrect' % (form.password.data))
            else:
                flash('User "%s" does not exist'% (form.username.data))
        return render_template('/login', title= 'Login', group=request.cookies.get('group'))
    elif request.method == 'GET':
        return render_template('login.html', title='Sign in', form=form, group=request.cookies.get('group'))

@app.route("/logout", methods=['GET'])
@get_user
def logout(*args, **kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    session.active = False
    db.session.commit()
    response = make_response(redirect('/index'))
    response.set_cookie('session_id', '', expires=0)
    return response

@app.route('/signup', methods =['GET', 'POST'])
def signup():
    form = SignupForm()
    form.group.choices = [(g.id, g.groupname) for g in Groups.query.all() ]
    if request.method == 'POST':
        if not form.validate_on_submit():
            flash('signup FAILED for requested username="{}", email="{}"'.format(form.username.data, str(form.email.data)))
            return render_template('signup.html', title='Signup', form=form, group=request.cookies.get('group'))
        else:
            newuser = User(request.form.get('username'), request.form.get('firstname'),request.form.get('lastname'),request.form.get('email'),request.form.get('password'),request.form.get('group'),request.form.get('ssn'))
            db.session.add(newuser)
            db.session.commit()
            flash('Signup successful for requested username="{}", email="{}"'.format(form.username.data, str(form.email.data)))
            return redirect(url_for('login'))
    elif request.method == 'GET':
        return render_template('signup.html', form=form, group=request.cookies.get('group'))

@app.route('/profile')
@get_user
def profile(*args,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    return render_template('profile.html', user=user, session=session, group=request.cookies.get('group'))

@app.route('/testdb')
def testdb():
    if db.session.query("1").from_statement("SELECT 1").all():
        return 'It works.'
    else:
        return 'Something is broken.'

@app.route('/users')
@get_user
def users(*args,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    users = User.query.all()
    return render_template('show_users.html', users=users,user=user, group=request.cookies.get('group'))

@app.route('/sessions')
@get_user
def sessions(*args,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    sessions = Session.query.all()
    return render_template('show_sessions.html', sessions=sessions,user=user, group=request.cookies.get('group'))

@app.route('/about')
@get_user
def about(*args,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    return render_template('about.html', group=request.cookies.get('group'))

@app.route('/orders')
@get_user
def orders(*args,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    orders_issued = Orders.query.filter(Orders.issued_by == user.username).order_by(desc(Orders.issued_date)).all()
    new_orders_received = Orders.query.filter(Orders.issued_for == user.username, Orders.seen ==False).all()
    orders_received = Orders.query.filter(Orders.issued_for == user.username).order_by(desc(Orders.issued_date)).all()
    number_of_new_orders = Orders.query.filter(Orders.issued_for == user.username, Orders.seen == False).count()
    return render_template('orders.html',user=user, session=session, orders_issued=orders_issued,orders_received=orders_received, number_of_new_orders=number_of_new_orders,new_orders_received=new_orders_received, group=request.cookies.get('group'))


@app.route('/order/<id>', methods =['GET', 'POST'])
@get_user
def order(id ,*args ,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    order = Orders.query.filter(Orders.id == id).first()
    order.seen = True
    db.session.commit()
    form = CompleteOrderForm()
    if request.method == 'POST':
        if form.validate_on_submit() == False:
            return redirect('order/' + str(order.id))
        else:
            order.mark_completed()
            db.session.commit()
            return redirect('order/' + str(order.id))
    elif request.method == 'GET':
        return render_template('order.html',order=order,form=form,user=user, group=request.cookies.get('group'))

@app.route('/vieworder/<id>', methods =['GET', 'POST'])
@get_user
def vieworder(id ,*args ,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    order = Orders.query.filter(Orders.id == id).first()
    form = CompleteOrderForm()
    if request.method == 'POST':
        if form.validate_on_submit() == False:
            return redirect('order/' + str(order.id))
        else:
            order.mark_completed()
            db.session.commit()
            return redirect('order/' + str(order.id))
        # mark post as completed
    elif request.method == 'GET':
        return render_template('order.html',order=order,form=form,user=user, group=request.cookies.get('group'))


@app.route('/giveorders', methods =['GET', 'POST'])
@get_user
def giveorders(*args,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    form=OrdersForm()
    form.issued_for.choices =[(g.username, g.username) for g in User.query.all() ]
    username=user.username
    if request.method == 'POST':
        if form.validate_on_submit() == False:
            return render_template('giveorders', form=form, group=request.cookies.get('group'))
        elif form.validate_on_submit() == True:
            neworders = Orders(form.orders.data, user.username, form.issued_for.data)
            db.session.add(neworders)
            db.session.commit()
            return redirect(url_for('orders'))
    elif request.method == 'GET':
        return render_template('giveorders.html', form=form,user=user, group=request.cookies.get('group'))
    return render_template('giveorders.html', group=request.cookies.get('group'))

@app.route('/users/<username>')
@get_user
def viewuser(username ,*args ,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    viewuser = User.query.filter(User.username == username).first()
    return render_template('user.html',session=session,user=user,viewuser=viewuser, group=request.cookies.get('group'))

@app.route('/recruiter', methods =['GET', 'POST'])
@get_user
def recruiter(*args ,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    group = request.cookies.get('group')
    if group == 'recruiter':
        form = PromoteUserForm()
        form.promote_to.choices = [(g.groupname, g.groupname) for g in Groups.query.all() ]
        recruits = User.query.filter(User.group == Groups.query.filter(Groups.groupname == 'recruit').first().id).all()
        if request.method == 'POST':
            if form.validate_on_submit() == False:
                return render_template('/recruiter', form=form, recruits=recruits, group=request.cookies.get('group'))
            elif form.validate_on_submit() == True:
                modusername = request.form.get('promote_user')
                moduser = User.query.filter(User.username == modusername).first()
                moduser.group = Groups.query.filter(Groups.groupname == form.promote_to.data).first().id
                db.session.commit()
                return redirect(url_for('recruiter'))
        elif request.method == 'GET':
            return render_template('/recruiter.html',form=form, user=user, session=session,recruits=recruits, group=request.cookies.get('group'))
    return render_template('/unauthorized.html',user=user, session=session, group=request.cookies.get('group'))

@app.route('/hr')
@get_user
def hr(*args ,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    users = User.query.all()
    group = request.cookies.get('group')
    if group == 'HR':
        return render_template('/hr.html',user=user,session=session,users=users, group=request.cookies.get('group'))
    return render_template('/unauthorized.html',user=user, session=session, group=request.cookies.get('group'))

@app.route('/hr/<id>', methods =['GET', 'POST'])
@get_user
def edituser(id, *args ,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    edituser = User.query.filter(User.id == id).first()   
    moduser = User.query.filter(User.id == id).first()
    form = EditUserForm(request.form)
    form.group.choices = [(g.id, g.groupname) for g in Groups.query.all() ]
    # initialize form with current data
    form.firstname.default = moduser.firstname.title()
    form.lastname.default = moduser.lastname.title()
    form.military_id.default = moduser.military_id
    form.ssn.default = moduser.ssn
    form.email.default = moduser.email
    form.password.default = moduser.password
    form.group.default = int(moduser.group)  
    form.process()
    if request.method == 'POST':
        if form.validate_on_submit() == False:
            flash('request validation failed' )
            return render_template('/edit_user.html', form=form, edituser=edituser,user=user,session=session , group=request.cookies.get('group'))
        elif form.validate_on_submit() == True:
            moduser.firstname = request.form.get('firstname')
            moduser.lastname = request.form.get('lastname')
            moduser.password = request.form.get('password')
            moduser.email = request.form.get('email')
            moduser.military_id = request.form.get('military_id')
            moduser.ssn = request.form.get('ssn')
            moduser.group =  request.form.get('group')
            db.session.commit()
            return redirect(url_for('hr'))
    elif request.method == 'GET':
        return render_template('/edit_user.html',user=user,session=session,edituser=edituser,form=form, group=request.cookies.get('group'))

@app.route('/settings', methods =['GET', 'POST'])
@get_user
def settings( *args ,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    form = EditUserForm(request.form)
    form.group.choices = [(g.id, g.groupname) for g in Groups.query.all() ]
    # initialize form with current data
    form.firstname.default = user.firstname.title()
    form.lastname.default = user.lastname.title()
    form.military_id.default = user.military_id
    form.ssn.default = user.ssn
    form.email.default = user.email
    form.password.default = user.password
    form.group.default = int(user.group)
    form.process()
    if request.method == 'POST':
        if form.validate_on_submit() == False:
            flash('request validation failed' )
            return render_template('/settings.html', form=form, user=user,session=session , group=request.cookies.get('group'))
        elif form.validate_on_submit() == True:
            user.firstname = request.form.get('firstname')
            user.lastname = request.form.get('lastname')
            user.password = request.form.get('password')
            user.email = request.form.get('email')
            user.military_id = request.form.get('military_id')
            user.ssn = request.form.get('ssn')
            user.group =  request.form.get('group')
            db.session.commit()
            return redirect(url_for('profile'))
    elif request.method == 'GET':
        return render_template('/settings.html',user=user,session=session,form=form, group=request.cookies.get('group'))

@app.route('/unauthorized')
def unauthorized():
    return render_template('/unauthorized.html', group=request.cookies.get('group'))


@app.route('/createreport', methods =['GET', 'POST'])
@get_user
def createreport(*args,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    form=ReportForm()
    group = request.cookies.get('group')
    if group == 'spy':
        if request.method == 'POST':
            if form.validate_on_submit() == False:
                return render_template('/create_report.html', form=form, group=request.cookies.get('group'))
            elif form.validate_on_submit() == True:
                newreport = Report( user.username, form.summary.data)
                db.session.add(newreport)
                db.session.commit()
                return redirect('/report')
                return render_template('view_reports.html', reports=Report.query.filter(Report.username==user.username),user=user,session=session, group=request.cookies.get('group'))
        elif request.method == 'GET':
            return render_template('create_report.html', form=form,user=user, group=request.cookies.get('group'))
    return render_template('/unauthorized.html',user=user, session=session, group=request.cookies.get('group'))

@app.route('/report')
@get_user
def report(*args,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    group = request.cookies.get('group')
    if group == 'spy' or group == "secretary of war" or group == "president":
        return render_template('/view_reports.html', reports=Report.query.order_by(desc(Report.date)).all(), user=user,session=session, group=request.cookies.get('group'))
    return render_template('/unauthorized.html',user=user, session=session, group=request.cookies.get('group'))

@app.route('/report/<id>')
@get_user
def viewreport(id,*args,**kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    report = Report.query.filter(Report.id == id).first()
    return render_template('/view_report.html', report=report, user=user,session=session, group=request.cookies.get('group'))



@app.route('/un/')
@get_user
def un_messages(*args, **kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    try:
        messages = utils.get_messages()
    except:
        messages = None
    group = request.cookies.get('group')
    if group == "secretary of war" or group == "president":
        return render_template('un_msg_list.html', user=user, session=session, messages=messages, group=request.cookies.get('group'))
    return render_template('/unauthorized.html',user=user, session=session, group=request.cookies.get('group'))

@app.route('/un/new/', methods=['GET', 'POST'])
@get_user
def un_new_msg(*args, **kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')

    form = UNMessageForm()
    #form.process()

    if request.method == 'POST':
        if form.validate_on_submit():
            mtype = form.type.data
            body = form.body.data
            destination = form.destination.data
            utils.post_message(body, destination, type=mtype)
            return redirect(url_for('un_messages'))

    return render_template('un_msg_new.html', user=user, session=session, form=form, group=request.cookies.get('group'))


@app.route('/un/<id>/')
@get_user
def un_details(id, *args, **kwargs):
    user = kwargs.get('user')
    session = kwargs.get('session')
    try:
        message = utils.get_message(id)
    except:
        message = None
    return render_template('un_msg_detail.html', user=user, session=session, message=message, group=request.cookies.get('group'))

