from flask import render_template, flash, redirect, request, session, url_for, make_response
from app import app, utils
from .forms import LoginForm, SignupForm, OrdersForm, CompleteOrderForm, PromoteUserForm, EditUserForm, ReportForm, UNMessageForm
from .models import db, User, Orders, Groups, Report
from functools import wraps
from sqlalchemy.exc import *
from sqlalchemy import desc
import flask_login
import forms

import logging

def get_user(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if not flask_login.current_user.is_anonymous:
            user = flask_login.current_user
        else:
            user = None

        logging.warn('get_user: %s --> %s' % (flask_login.current_user, user))
        return f(user=user, *args, **kwargs)
    return decorated_function

@app.route('/')
@app.route('/index')
@get_user
def index(*args, **kwargs):
   return render_template('index.html', title='Home', user=kwargs.get('user'))

@app.route('/login', methods =['GET', 'POST'])
@get_user
def login(*args, **kwargs):
    # If the user is already logged in, redirect to profile
    if kwargs.get('user'):
        return make_response(redirect('/profile'))

    form = LoginForm()
    if request.method == 'POST':
        if form.validate_on_submit():
            user = User.get_user_by_username(request.form.get('username'))
            if user:
                logging.warn('logging in user: %s' % user)
                flask_login.login_user(user, remember=False)
                return make_response(redirect('/profile'))

        flash('Login request failed')
        return render_template('login.html', form=form)
    elif request.method == 'GET':
        return render_template('login.html', title='Sign in', form=form)

@app.route("/logout", methods=['GET'])
@get_user
def logout(*args, **kwargs):
    flask_login.logout_user()
    return make_response(redirect('/index'))

@app.route('/signup', methods =['GET', 'POST'])
@get_user
def signup(*args, **kwargs):
    # If the user is already logged in, redirect to profile
    if kwargs.get('user'):
        return make_response(redirect('/profile'))

    form = SignupForm()
    if request.method == 'POST':
        if not form.validate_on_submit():
            return render_template('signup.html', title='Signup', form=form)
        else:
            User.new_user(request.form.get('username'),
                          request.form.get('firstname'),
                          request.form.get('lastname'),
                          request.form.get('password'),
                          request.form.get('ssn'))
            return redirect(url_for('login'))
    elif request.method == 'GET':
        return render_template('signup.html', form=form)

@app.route('/profile')
@get_user
def profile(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    return render_template('profile.html', user=user)

@app.route('/users')
@get_user
def users(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    users = User.query.all()
    return render_template('show_users.html', users=users, user=user)

@app.route('/users/<username>')
@get_user
def viewuser(username, *args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Only show spy information for president and secretary of war
    view_user = User.get_user_by_username(username)
    if view_user.get_group() == 'spy' and not user.get_group() in ['president', 'secretary of war']:
        return make_response(redirect('/users'))

    return render_template('user.html', user=user, viewuser=view_user)

@app.route('/orders')
@get_user
def orders(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    orders_issued = Orders.query.filter(Orders.issued_by == user.username).order_by(desc(Orders.issued_date)).all()
    new_orders_received = Orders.query.filter(Orders.issued_for == user.username, Orders.seen ==False).all()
    orders_received = Orders.query.filter(Orders.issued_for == user.username).order_by(desc(Orders.issued_date)).all()
    number_of_new_orders = Orders.query.filter(Orders.issued_for == user.username, Orders.seen == False).count()
    return render_template('orders.html', user=user, orders_issued=orders_issued, orders_received=orders_received, number_of_new_orders=number_of_new_orders, new_orders_received=new_orders_received)

@app.route('/order/<id>', methods=['GET', 'POST'])
@get_user
def order(id, *args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    order = Orders.query.filter(Orders.id == id, Orders.issued_for == user.username).first()
    if order:
        order.seen = True
        form = CompleteOrderForm()
        if request.method == 'POST':
            if form.validate_on_submit():
                order.mark_completed()
        db.session.commit()

        return render_template('order.html', order=order, form=form, user=kwargs.get('user'))

    return make_response(redirect('/orders'))

@app.route('/vieworder/<id>', methods=['GET', 'POST'])
@get_user
def vieworder(id, *args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    order = Orders.query.filter(Orders.id == id, Orders.issued_by == user.username).first()
    if order:
        form = CompleteOrderForm()
        if request.method == 'POST':
            if form.validate_on_submit():
                order.mark_completed()
                db.session.commit()

        return render_template('order.html', order=order, form=form, user=kwargs.get('user'))

    return make_response(redirect('/orders'))

@app.route('/giveorders', methods =['GET', 'POST'])
@get_user
def giveorders(*args,**kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    form = OrdersForm()
    form.issued_for.choices = [(g.username, g.username) for g in User.query.all()]
    username = user.username
    if request.method == 'POST':
        if form.validate_on_submit():
            neworders = Orders(form.orders.data, user.username, form.issued_for.data)
            db.session.add(neworders)
            db.session.commit()
            return redirect(url_for('orders'))

    return render_template('giveorders.html', form=form, user=kwargs.get('user'))

@app.route('/recruiter', methods =['GET', 'POST'])
@get_user
def recruiter(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Verify user's group
    if user.get_group() == 'recruiter':
        form = PromoteUserForm()
        form.promote_to.choices = [(g.groupname, g.groupname) for g in Groups.get_nominatable_groups()]
        recruits = User.query.filter(User.group == Groups.query.filter(Groups.groupname == 'recruit').first().id).all()
        if request.method == 'POST':
            if form.validate_on_submit():
                User.change_group(request.form.get('promote_user'), form.promote_to.data)
                return redirect(url_for('recruiter'))

        return render_template('/recruiter.html', form=form, user=user, recruits=recruits)

    return make_response(redirect('/'))

@app.route('/hr')
@get_user
def hr(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Verify user's group
    if user.get_group() == 'HR':
        # TODO: all users? how does spy work?
        return render_template('/hr.html', user=user, users=User.query.all())

    return make_response(redirect('/'))

@app.route('/hr/<id>', methods=['GET', 'POST'])
@get_user
def edituser(id, *args ,**kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Verify user's group
    if user.get_group() == 'HR':
        edituser = User.get_user_by_id(id)
        form = EditUserForm(request.form)
        form.group.choices = [(g.id, g.groupname) for g in Groups.query.all()]

        if request.method == 'POST':
            if form.validate_on_submit():
                edituser.firstname = request.form.get('firstname')
                edituser.lastname = request.form.get('lastname')
                edituser.military_id = request.form.get('military_id')
                edituser.ssn = request.form.get('ssn')
                edituser.group =  request.form.get('group')
                db.session.commit()
                return redirect(url_for('hr'))

        # initialize form with current data
        form.firstname.default = edituser.firstname.title()
        form.lastname.default = edituser.lastname.title()
        form.military_id.default = edituser.military_id
        form.ssn.default = edituser.ssn
        form.group.default = int(edituser.group)
        form.process()

        return render_template('/edit_user.html', user=user, edituser=edituser, form=form)

    return make_response(redirect('/'))

@app.route('/settings', methods=['GET', 'POST'])
@get_user
def settings(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Only allow updating of first/last names and SSN
    form = forms.SettingsForm(request.form)
    if request.method == 'POST':
        if form.validate_on_submit():
            user.firstname = request.form.get('firstname')
            user.lastname = request.form.get('lastname')
            user.ssn = request.form.get('ssn')
            db.session.commit()
            return redirect(url_for('profile'))

    # initialize form with current data
    form.firstname.default = user.firstname.title()
    form.lastname.default = user.lastname.title()
    form.ssn.default = user.ssn
    form.process()

    return render_template('/settings.html', user=user, form=form)

@app.route('/createreport', methods=['GET', 'POST'])
@get_user
def createreport(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Verify user's group
    if user.get_group() == 'spy':
        form = ReportForm()
        if request.method == 'POST':
            if form.validate_on_submit():
                newreport = Report(user.username, form.summary.data)
                db.session.add(newreport)
                db.session.commit()
                return redirect('/report')
        return render_template('create_report.html', form=form, user=user)

    return make_response(redirect('/'))

@app.route('/report')
@get_user
def report(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Verify user's group
    if user.get_group() in ['spy', 'secretary of war', 'president']:
        return render_template('/view_reports.html', reports=Report.query.order_by(desc(Report.date)).all(), user=user)

    return make_response(redirect('/'))

@app.route('/report/<id>')
@get_user
def viewreport(id,*args,**kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Verify user's group
    if user.get_group() in ['spy', 'secretary of war', 'president']:
        report = Report.query.filter(Report.id == id).first()
        return render_template('/view_report.html', report=report, user=user)

    return make_response(redirect('/'))

@app.route('/un/')
@get_user
def un_messages(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Verify user's group
    if user.get_group() in ['secretary of war', 'president']:
        try:
            messages = utils.get_messages()
        except:
            messages = None
        return render_template('un_msg_list.html', user=user, messages=messages)

    return make_response(redirect('/'))

@app.route('/un/new/', methods=['GET', 'POST'])
@get_user
def un_new_msg(*args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Verify user's group
    if user.get_group() in ['secretary of war', 'president']:
        form = UNMessageForm()

        if request.method == 'POST':
            if form.validate_on_submit():
                mtype = form.type.data
                body = form.body.data
                destination = form.destination.data
                try:
                    utils.post_message(body, destination, type=mtype)
                    return redirect(url_for('un_messages'))
                except:
                    logging.warn('error sending message to UN')

        return render_template('un_msg_new.html', user=user, form=form)

    return make_response(redirect('/'))

@app.route('/un/<id>/')
@get_user
def un_details(id, *args, **kwargs):
    user = kwargs.get('user')

    # If the user is not logged in, redirect to home
    if not user:
        return make_response(redirect('/'))

    # Verify user's group
    if user.get_group() in ['secretary of war', 'president']:
        try:
            message = utils.get_message(id)
        except:
            message = None
        return render_template('un_msg_detail.html', user=user, message=message)

    return make_response(redirect('/'))
