---
layout: layout
title: Education
summary: List of Classwork
category: general
---

## Iowa State Coursework

{% for post in site.categories.education %}
* [{{ post.title }}]({{ post.url }}) - {{ post.summary }}
{% endfor %}
