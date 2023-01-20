# restful_api_demo

Tech Stack:
>Maven (I used version 3.6.3)
>Javalin (I used version 5.3.1)
>Java (I used Java 17.0.4)
>JUnit (I used version 5.6.2)

To build the Web server we will use Javalin which is a light weight web framework built on top of Jetty that will do all the heavy work for us. Therefore we won't have to worry about the low level implementation that comes with building a server that conforms with Java Servlet specifications. Which will allow us to build our REST API

# Create a Maven project. 
As it is much easier to handle project dependencies in Maven.

# Add the Javalin dependency to your pom file.
    <!-- Javalin -->
        <dependency>
            <groupId>io.javalin</groupId>
            <artifactId>javalin-bundle</artifactId>
            <version>5.3.1</version>
        </dependency>

# Add JUnit dependencies to your pom file (Optional):
Testing our applications is always a important step. I mean how else can we ensure that our applications work as intended.
    <!-- Testing dependencies-->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.6.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.6.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-params</artifactId>
            <version>5.6.2</version>
            <scope>test</scope>
        </dependency>

# Create a User class:
    We need to create a User class to model our user data.

    Add user class code snippet

    The default constructor is what will allow us to parse our JSON request bodies automatically into our model classes, eg context.bodyAsClass(User.class). The paramatized constructor is used when we have to define our in-memory user objects later on in the program.

# Create a UserDAO class:
    The UserDAO class will be our Data Access Object (DAO) since we will use an in-memory object to store our users.

    Add UserDAO code snippt
# Create a UserController class:
    The UserController class will handle all the REST endpoints.

    Add code snippet.

# Create a Role class:
    This class will define the roles for our system. We can achieve by implementing the RouteRole interface from the io.javalin.security package. For this project we will define 4 roles ADMIM, ANYONE, USER_READ and USER_WRITE.

    Add code snippet.

# Create the Auth class:
    This Auth class is responsible for ensuring that the correct handler is called based on the context and what roles have been set for the REST endpoint. This class will need to implement the AccessManager interface from the io.javalin.security package. The AccessManager interface is a way of implementing per-endpoint security management. You will need to override the access method/function from the interface. The method takes 3 arguments a Handler, Context and set of Roles. 

    Add code snippet.

    The getBasicAuthCredentials method/function takes context from the request as a parameter and then gets Authorization header from the context. It decoded it and then returns a pair of strings containing the username and password.

    The userRoles method/function then returns a list of roles associated with the user once the user has been authenticated. (See code snippet below)

    Add code snippet.

    We created a Map that where the keys are username+password in clear text and the values are the associated user roles. Please note that storing user password in plain text is not advised instead hash/salt the user passwords should this be a live system.

# Set up the server and add the API endpoints:
    Add a code snippet here

