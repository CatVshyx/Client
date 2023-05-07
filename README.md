[app icon]
# FinFlow application 

A desktop finance app that helps people manipulate their products, discounts, revenues, etc. It is typically a tool designed to assist businesses in managing their financial operations. Such an app could help businesses optimize their pricing strategies, identify cost-saving opportunities, and track their revenues and expenses. A mobile application(iOS) was created by my 
[teammate]:(https://github.com/Nems0n)

## Installation

-Clone the repository to your local folder [git clone https://github.com/CatVshyx/Client.git]


-Open command prompt in this folder and type 'mvn package' [If you don`t have Maven, install it on official website https://maven.apache.org/download.cgi] 

-Then you will have a built jar project with naming 'some-name-jar-with-dependencies' in target folder. And since then you can already use it.

-If you want to have .exe file, then launch4j program can be used for it

## Teck Stack

Language: Java

Build Tool: Maven

Architecture: MVC + Service Layer

Libraries: JavaFX, Jackson Core, Jackson Datatype, ItextPDF

Backend: written on Spring Framework [https://github.com/CatVshyx/SpringServer] 

## Features

- Authentication and Authorization of users with Spring Security on the server side

- Data storage and data update are implemented with Service Layer(see com/example/client/service folder) and Session class which updates data every 5 minutes

- Each account has specified roles which allows/forbids them to view, edit, delete items in the company

- Saving all financial info into pdf file using ITextPDF library 

- Allowing users to upload photos into their profiles which was implemented with IO Streams

- Implementation of the search field in the top with animation. It looks up the specified user/discount/product every time you print with using state realization

- Different currency support. You can use/add currencies into Finance Pane. It uses Fixer API to get the latest currency values.

- Email consultation. You can write the issues/questions you have while working with the app. Your email will be sent to the server and saved. You will be given an answer onto your email.[More detailed description of this feature is written in telegramBot repo and Server repo]

## Screenshots

[main menu - 1]

This is the main menu of the app - storage frame. It shows what product you have, amount, price etc. It`s quite light and well-informative. Besides, here you can look up the products you are interested in, add new products if you are allowed and so on. 

[storage sort gif - 1]

here is the short visualization how sorting products does. In each menu bar there are different types of things which can be sorted by you
[admin- 2 ]
This administration pane. The second main pane where admins can view and edit profiles of the users. Besides, new users of your company can be invited here with two ways: specify roles of the future member or just invite by link
[finance - 1]

Revenues of a company are shown with charts and  statistics fields on the top. Each chart can be hidden/shown - just click on it the right bar. To download all the revenues - click on arrow and it will generate a pdf file on your desktop

[help - gif] 

Here is a presentation how help frame works - just ask a question and send it, that`s it! And in a couple of days you be answered.
