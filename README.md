# Banking App Project 

##Table of Contents
- [Features](#features)
- [Steps To Run The Project](#steps-to-run-the-project)
- [JWT Token Creation](#jwt-token-creation)
- [Technologies Used](#technologies-used)
- [User API'S](#user-api)
- [Account API's](#account-api)
- [Transaction API's](#transaction-api)


## Features

- User , Account , Transaction
- Jwt Token
- Balance Updation of Account in case of transaction
- Role Based Access.
- Containerized with Docker
- 

## Steps To Run The Project 
-  git clone https://github.com/Mattus122/Bankingapp.git
-  cd Bankingapp
-  BUILD THE WAR FILE FOR APPLICATION :  ./mvwn clean package -DskipTests
-  BUILD THE DOCKER IMAGGE FOR TOMCAT AND POSTGRES DB :  docker-compose build
-  START AND RUN ALL THE SERVICES IN docker-compose.yml(running the postgres and tomcat image): docker-compose up

## Technologies Used
- JDK 17
- DOCKER
- Dbeaver [POSTGRES DATABASE]
- IntelliJ IDEA
- Spring Boot version 3.3.2

## UserApi Endpoints : 

1.Generate Jwt Token (User Enters its username and password in the request Body , the generated jwt token has subject as user Role , and claims as email and password).

2. POST  request for Creating a User  , Only the   ADMIN role are able to create a user :

3.GET Request For Getting all Users , Ony the ADMIN are able to get all the user details in the form of List<ResponseUserDTO> , incase of role as USER it will ony return the ResponseUserDTO (which contains the details of User)

4.GET Request for getting User By Id , Only the Admin role is able to get any usr By its id , if its USER role then it will only be able to get his own information incase it enter any other userId then a 403 Forbidden Exception is thrown.

5.UDATE USER  , Only ADMIN role can update the user.
6.DELETE USER  , Only Admin role is able to Delete User.

## Account Api Endpoint  : 

1.POST  : Create account  ,  for account creation , only admin is able to create account for a user and admin  and is not able to create a self account , userId is passed as a PathVariable.
2.GET   : Get Account Information , for getting the account information of a particular user  ,  the particular user should pass his own email and assword in jwt and it should match with id of the user with that email , then he is able to get the information as List<AccountDTO> of all his accounts.
3. PUT  : update account information , only admin is able to perform this articular operation.
4. DELETE : delete the account , only admin is able to perform this articular operation.

## Transaction Controller API Endpoints : 

1. POST : Create transaction : Only User is able to create transaction .
2. GET  : Retrieve Transactions of an account By UserId  . get transaction of all accounts associated with a particular user.
3. GET  : Retrieve Transactions of an account By AccountId  . get all transactions of a particular account. 
