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
To start RabbitMq: $ docker run -d --hostname rabbitmq --name rabbitmq -p 5672:5672 -p 15672:15672 -v rabbitmq_data:/var/lib/rabbitmq --restart=unless-stopped rabbitmq:3.11.0-management
To start PostgreSql: $ docker run -d --hostname test --name test -p 5432:5432 -e POSTGRES_USER=test -e POSTGRES_PASSWORD=test -e POSTGRES_DB=test -v test_data:/var/lib/postgresql/data --restart=unless-stopped postgres:14.5
Use data from these commands in your application.properties and add your bot token
Start all services:
1) dispatcher
2) node
3) rest-service

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
To get static ip use localtunnel (require node.js)  
Mac OS:
$ brew install localtunnel  

$ lt --port <port of disparcher service>

It will return temporal static
ip that will be a bot.uri in dispatcher application.properties.  
It works for 2 hours. Than you should do it again  
While using $ lt --port 8080 terminal has to be opened

Otherwise you can use 
brew install --cask ngrok
Then register on website and get secret token
use following command with your token:  
$ ngrok config add-authtoken <token>
$ ngrok http <port of disparcher service>







