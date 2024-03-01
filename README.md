# Movie Database

This project is an app for managing a database of movies, TV shows and actors. Currently, users can interact with it from the CLI. The users saved in the database are one of three types (regulars, contributors and admins), each role having different permissions. A logged user is show a menu with all the valid actions that they can take.

The main motivation for making this project was getting accustomed to OOP
practices by writing a program that is larger in scope than the ones I've written before (about 2000 lines of code). The most complex functionalities were implemented using commomn design patterns. These are: a singleton for the IMDB class, a builder for versatile instantiation of users from their personal information, a factory class for creating a new user based on type, observer for managing the notifications sent to the users, and strategy for choosing the amount of experience that a user gains based on the triggered event.

Currently the biggest missing functionality is that the changes made during the process's lifetime are lost when exiting the app. I could solve this by serializing the new data and write it to the json files.
