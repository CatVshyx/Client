
# FinFlow application 

<p align="center"><img src="https://user-images.githubusercontent.com/47032134/236701078-5683f9dc-7d81-4397-847f-1a2634f0efe5.png" width="150" height="150"></p>


A desktop finance app that helps people manipulate their products, discounts, revenues, etc. It is typically a tool designed to assist businesses in managing their financial operations. Such an app could help businesses optimize their pricing strategies, identify cost-saving opportunities, and track their revenues and expenses. A mobile application(iOS) was created by my [teammate](https://github.com/Nems0n)

## Installation

-Clone the repository to your local folder [git clone https://github.com/CatVshyx/Client.git]


-Open command prompt in this folder and type 'mvn package'. If you don`t have Maven, install it on [official website](https://maven.apache.org/download.cgi)

-Then you will have a built jar project with naming 'some-name-jar-with-dependencies' in target folder. And since then you can already use it.

-If you want to have .exe file, then launch4j program can be used for it

## Teck Stack

Language: Java

Build Tool: Maven

Architecture: MVC + Service Layer

Libraries: JavaFX, Jackson Core, Jackson Datatype, ItextPDF

Backend: written on Spring Framework, [repository](https://github.com/CatVshyx/SpringServer) 

## Features

- Authentication and Authorization of users with Spring Security on the server side

- Data storage and data update are implemented with Service Layer(see com/example/client/service folder) and Session class which updates data every 5 minutes

- Each account has specified roles which allows/forbids them to view, edit, delete items in the company

- Saving all financial info into pdf file using ITextPDF library 

- Allowing users to upload photos into their profiles which was implemented with IO Streams

- Implementation of the search field in the top with animation. It looks up the specified user/discount/product every time you print with using state realization

- Different currency support. You can use/add currencies into Finance Pane. It uses Fixer API to get the latest currency values.

- Email consultation. You can write the issues/questions you have while working with the app. Your email will be sent to the server and saved. You will be given an answer onto your email.[More detailed description of this feature is written in [telegramBot repo](https://github.com/CatVshyx/TelegramBot) and [Server repo](https://github.com/CatVshyx/SpringServe)]

## Screenshots
<img src="https://user-images.githubusercontent.com/47032134/236699392-9a5bcdf1-49d2-40af-a354-e877b18b9f70.jpg" width="1000" height="600">

This is the main menu of the app - storage frame. It shows what product you have, amount, price etc. It`s quite light and well-informative. Besides, here you can look up the products you are interested in, add new products if you are allowed and so on. 

<p align="center"><img src="https://user-images.githubusercontent.com/47032134/236700162-72b08142-62ea-48ce-8516-2d0d0ab76782.gif" width="800" height="400"></p>

here is the short visualization how sorting products does.

In each menu bar there are different types of things which can be sorted by you

<img src="https://user-images.githubusercontent.com/47032134/236699785-6b897948-e40d-4a7e-b4c8-86b332645aad.jpg" width="1000" height="600">
This administration pane. The second main pane where admins can view and edit profiles of the users. 

Besides, new users of your company can be invited here with two ways: specify roles of the future member or just invite by link

<img src="https://user-images.githubusercontent.com/47032134/236699775-bf107926-d8c1-42a7-9814-8d37598da666.jpg" width="1000" height="600">
Revenues of a company are shown with charts and  statistics fields on the top.

Each chart can be hidden/shown - just click on it the right bar.

To download all the revenues - click on arrow and it will generate a pdf file on your desktop
<img src="https://user-images.githubusercontent.com/47032134/236699741-7fe7f500-73d3-46c7-ac2f-e7fbdfa793f8.gif" width="1000" height="600">


Here is a presentation how help frame works - just ask a question and send it, that`s it! And in a couple of days you be answered.
