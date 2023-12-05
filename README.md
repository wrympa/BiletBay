[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/vuIpIQ7W)
# BiletBay

This is a 4 person project. The website allows users to resell
tickets for events.

# Requirements:
- tomcat7
- mysql server
- jdk 19+
- for more info see pom.xml

then run tomcat7 for the website server on

# Setup and run
- on your mysql server, run the table creation scripts
- add the dependencies in pom.xml in your project


# Functionalities:

Users

- users can register with a username, phone number and profile pictures and log in accordingly
- any user can upload their own ticket for an event, if the event is not listed they may request it to get added.
- they can rate and get rated by other users
- they can report and get reported by other users
- they can have achievements pop up on their page based on their actions
- they can have interested events for quick access
- they can edit or delete their own tickets

# Administartors

- they can add events
- remove events
- remove users
- remove tickets
- read and act on reports by users
- make a user an admin
- pull up statistics on an event or user
- approve user-submitted events

# Tickets

- every ticket can be either bought directly or auctioned off
- if you buy a ticket, a buy request is sent to the poster so they can approve of the purchase
- if you bid on an auction, an auto checker will see it and automatically give it to you after the time passes
- if you do buy a ticket, its image will show up in your bought tickets tab on mypage
