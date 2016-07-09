"""
Utilities for dealing with the UN
"""
import json

import time
import requests

from app import app
from app.forms import UNMessageForm

def get_messages(key=None, count=None, since=None):
    """
    Get the message list for this nation from the UN
    :param str key: Optional auth key
    :param int count: Number of messages to get
    :param int since: Unix Timestamp, will only get messages after that time
    :return list: The response from the UN
    """
    url = "{url}/messages.json".format(url=app.config['UN_URL'])

    if not key:
        key = app.config['UN_NATION_KEY']

    params = {}

    if count:
        params['count'] = count

    if since:
        params['since'] = since

    data = requests.get(url, headers={'Authorization': key}, params=params)
    return data.json()


def get_message(id, key=None):
    """
    Get an individual message from the UN
    :param int id: The id of the message
    :param str key: Optional auth key
    :return dict: The message body
    """
    url = "{url}/messages/{id}.json".format(
        url=app.config['UN_URL'],
        id=id,
    )

    if not key:
        key = app.config['UN_NATION_KEY']

    data = requests.get(url, headers={'Authorization': key})
    return data.json()


def post_message(body, dst, key=None, type="message"):
    """
    Send a message to the UN
    :param str body: The message body
    :param list[str] dst: FQDNs of the destination countries
    :param str key: Optional auth key
    :param str type: message | treaty | declaration

    :raises requests.exceptions.HTTPError: If the post fails
    """
    data = {
        "type": type,
        "body": body,
        "time": int(time.time()),
        "origin": app.config["UN_ORIGIN_FQDN"],
        "destination": dst,
    }

    if not key:
        key = app.config["UN_NATION_KEY"]

    url = "{url}/messages.json".format(url=app.config['UN_URL'])
    data = requests.post(url, headers={'Authorization': key}, json=data)
    data.raise_for_status()


@app.template_filter('ctime')
def timectime(s):
    return time.ctime(s)

@app.template_filter('message_header')
def message_header(m):
    origin = resolve_country(m['origin'])
    if m['type'] == 'message':
        return "Message from {}".format(origin)
    elif m['type'] == 'treaty':
        return "Treaty from {}".format(origin)
    elif m['type'] == 'declaration':
        return "{} has declared war".format(origin)
    else:
        return m['type']


@app.template_filter('country')
def resolve_country(country):
    for k, v in UNMessageForm.COUNTRIES:
        if k == country:
            return v
    return None
