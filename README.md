Setup instructions:  

Make sure you have mysql server running on port 3307 or edit application.properties  

crete schema finance_tracker    

eddit application.properties with your username and password

in table users, add users and encrypt passwords you want to use using this link 
https://bcrypt-generator.com/  

First use sign in endpoint (AuthController), copy bearer token, and then you can use any api with the bearer token.
