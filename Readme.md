# TelegramFileHosting

## Review
This is an asynchronous file-sharing service that utilizes a message broker, RabbitMQ, 
and a microservice architecture. Registered users can upload text, documents, 
or photos and receive a download link in return. If a user is not registered, 
they will be prompted to register before they can access the service. 
It's a secure and easy way to share and access files with authorized users.

## The main functions of the application:

Register users
Upload document or photo and receive a link to download in return

## Deployment
This project is in progress

### Technology stack
* Java 11
* Spring Boot
* Maven
* Lombok
* Postgresql
* RabbitMQ
* Docker

## Endpoints
* /start 
* /registration
* /cancel
* /help

## static ip
чтобы получить static ip используй localtunnel (require node.js)  
Mac OS:
$ brew install localtunnel  

$ lt --port 8080

Use port on which dispatcher server is set. It will return temporal static
ip that will be a bot.uri in dispatcher application.properties.  
It works for 2 hours. Than you should do it again  
While using $ lt --port 8080 terminal has to be opened

Otherwise you can use 
brew install --cask ngrok







