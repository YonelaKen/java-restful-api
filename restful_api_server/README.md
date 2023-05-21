# restful_api_demo

Tech Stack:

> Maven (I used version 3.6.3)
> Javalin (I used version 5.3.1)
> Java (I used Java 17.0.4)
> JUnit (I used version 5.6.2)

To build the Web server we will use Javalin which is a light weight web framework built on top of Jetty that will do all the heavy work for us. Therefore we won't have to worry about the low level implementation that comes with building a server that conforms with Java Servlet specifications. Which will allow us to build our REST API

# Create a Maven project.

    As it is much easier to handle project dependencies in Maven.

# Add the Javalin dependency to your pom file.

    ```xml
    <!-- Javalin -->
        <dependency>
            <groupId>io.javalin</groupId>
            <artifactId>javalin-bundle</artifactId>
            <version>5.3.1</version>
        </dependency>
    ```

# Add JUnit dependencies to your pom file (Optional):

    Testing our applications is always a important step. I mean how else can we ensure that our applications work as intended.

    ```xml
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
    ```

# Add Unirest dependency to able to test in the API endpoint in our tests (Optional - only if you planning on testing your application with JUnit):

    ```xml
        <dependency>
            <groupId>com.konghq</groupId>
            <artifactId>unirest-java</artifactId>
            <version>3.14.1</version>
        </dependency>
    ```

# Create a User class:

    We need to create a User class to model our user data.

    ```Java
    public class User {
        private String name = "";
        private String email = "";

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public User(){
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
    ```

    The default constructor is what will allow us to parse our JSON request bodies automatically into our model classes, eg context.bodyAsClass(User.class). The paramatized constructor is used when we have to define our in-memory user objects later on in the program.

# Create a UserDAO class:

    The UserDAO class will be our Data Access Object (DAO) since we will use an in-memory object to store our users. This class contains methods for performing CRUD (create, read, update, delete) operations on a collection of users stored in a HashMap. The collection is initialized with a set of hard-coded users when the class is loaded. The class contains methods for getting all users, getting a specific user by ID, creating a new user, updating an existing user, and deleting a user. Each of these methods interacts with the users HashMap to perform the desired operation. Additionally, it has a randomId method that generates a random ID for a new user.

    ```Java
    public class UserDAO {
        private static HashMap<String, User> users = new HashMap<>();

        static {
            users.put("1", new User("Alice", "alice@alice.kt"));
            users.put("2", new User("Bob", "bob@bob.kt"));
            users.put("3", new User("Carol", "carol@carol.kt"));
            users.put(randomId(), new User("Dave", "dave@dave.kt"));
            users.put(randomId(), new User("Yonela", "yonela@admin.kt"));
        }

        public static String randomId() {
            return UUID.randomUUID().toString();
        }

        public static Set<String> getUsers() {
            return users.keySet();
        }

        public static Object getUserById(String userID) {
            return users.get(userID) != null ? users.get(userID) : "{\"message\": \"User does not exist.\"}";
        }

        public static String createUser(User user) {
            users.put(randomId(), user);
            return "User created successfully";
        }

        public static String updateUser(String userId, User user) {
            if (users.get(userId) == null)
                return "Unable to update user details.Could not find user with ID : "+ userId;
            users.put(userId, user);
            return "User details updated successfully";
        }

        public static String deleteUser(String userId) {
            if (!getUserById(userId).getClass().equals(User.class)) return "Could not find user with ID : "+ userId;
            users.remove(userId);
            return "User deleted successfully";
        }
    }
    ```

# Create a UserController class:

    The UserController class will handle all the REST endpoints. This class is responsible for handling HTTP requests, extracting relevant information from the request and passing it to the UserDAO class for processing, and sending the results back to the client.

    ```Java
    public class UserController {

        public static void getAllUserIds(Context ctx) {
            ctx.json(UserDAO.getUsers());
        }

        public static void createUser(Context ctx) {
            ctx.result(UserDAO.createUser(ctx.bodyAsClass(User.class)));
        }

        public static void getUserById(Context ctx) {
            ctx.json(UserDAO.getUserById(ctx.pathParam("userId")));
        }

        public static void updateUser(Context ctx) {
            ctx.result(UserDAO.updateUser(ctx.pathParam("userId"), ctx.bodyAsClass(User.class)));
        }

        public static void deleteUser(Context ctx) {
            ctx.result(UserDAO.deleteUser(ctx.pathParam("userId")));
        }

    }
    ```

    The getAllUserIds method takes in a Context object and retrieves all the user ids from the UserDAO.class and sends it to the client as a JSON object.

    The createUser method also takes in a Context object, extracts the user object from the request body, and creates a new user by passing this object to the UserDAO.createUser method. It then sends the result of this method to the client

    The getUserById method takes in a Context object, extracts the userId path parameter, retrieves the user object with the corresponding ID from the UserDAO class and sends it to the client as a JSON object.

    The updateUser method takes in a Context object, extracts the userId path parameter and user object from the request body, and updates the user in the UserDAO class with the passed user object. It then sends the result of this method to the client.

    The deleteUser method takes in a Context object, extracts the userId path parameter, and deletes the user object with the corresponding ID from the UserDAO class and sends the result of this method to the client.

# Create a Role enum:

    Here we will define the roles for our system and this will allows us to check if the user has the right role to access a specific route or endpoint. We can achieve by implementing the RouteRole interface from the io.javalin.security package. For this project we will define 4 roles ADMIM, ANYONE, USER_READ and USER_WRITE.

    ```Java
        public enum Role implements RouteRole {
            ADMIN, ANYONE, USER_READ, USER_WRITE;
        }
    ```

# Create the Auth class:

    This Auth class is responsible for managing access to the different routes in the application by checking the roles that the user has against the roles that are permitted for the route. It uses the basic authentication mechanism to authenticate the user. This class will need to implement the AccessManager interface from the io.javalin.security package. The AccessManager interface is a way of implementing per-endpoint security management. You will need to override the access method/function from the interface. The method takes 3 arguments a Handler, Context and set of Roles.

    The class also have a private field called "UserRolesMap" which is a map that contains a list of roles for each user. The key of this map is a pair of strings representing the username and password and the value is a list of roles that the user has. Please note that storing user password in plain text is not advised instead hash/salt the user passwords should this be a live system.

    ```Java
        public class Auth implements AccessManager{

            @Override
            public void manage(Handler handler, Context ctx, Set<? extends RouteRole> permittedRoles) throws Exception {
                if (permittedRoles.contains(Role.ANYONE) || userRoles(ctx).stream().toList().contains(Role.ADMIN)) {
                    handler.handle(ctx);
                } else if (userRoles(ctx).stream().anyMatch(permittedRoles::contains)) {
                    handler.handle(ctx);
                } else {
                    ctx.header(Header.WWW_AUTHENTICATE, "Basic");
                    throw new UnauthorizedResponse();
                }
            }

            private Map<Pair<String, String>, List<Role>> UserRolesMap = Map.of(
                new Pair<>("alice", "weak-1234"), Arrays.asList(Role.USER_READ),
                new Pair<>("bob", "weak-123456"), Arrays.asList(Role.USER_READ, Role.USER_WRITE),
                new Pair<>("yonela", "iamAdmin-12345"), Arrays.asList(Role.ADMIN)
            );   
        }
    ```

    We will also need to add userRoles method/function that returns a list of roles associated with the user once the user has been authenticated.

    ```Java
        private List<Role> userRoles(Context ctx) {
            Pair<String, String> credentials = getBasicAuthCredentials(ctx);
            if (credentials != null) {
                List<Role> roles = UserRolesMap.get(credentials);
                if (roles != null) {
                    return roles;
                }
            }
            return List.of();
        }
    ```

    And then procceed to add the getBasicAuthCredentials(ctx) method that will be used by the userRoles method. The getBasicAuthCredentials method/function takes context from the request as a parameter and then gets Authorization header from the context. It decodes it and then returns a pair of strings containing the username and password.

    ```Java
        private Pair<String, String> getBasicAuthCredentials(Context ctx) {
            String authHeader = ctx.header("Authorization");
                if (authHeader != null && authHeader.startsWith("Basic")) {
                    String encodedCredentials = authHeader.substring("Basic".length()).trim();
                    byte[] decodedCredentials = Base64.getDecoder().decode(encodedCredentials);
                    String credentials = new String(decodedCredentials, StandardCharsets.UTF_8);
                    String[] parts = credentials.split(":");
                    return new Pair<>(parts[0], parts[1]);
                }
            return null;
        }
    ```

# Set up the server and add the API endpoints:
    Create a class that will configure your server and add all the API endpoints.

    ```Java
    public class Server {
        public static void main(String[] args) {
            Javalin.create(config -> {
                config.accessManager(new Auth());
            }).routes(() -> {
                ApiBuilder.get("/", ctx -> ctx.redirect("/users"), Role.ANYONE);
                ApiBuilder.path("users", () -> {
                    ApiBuilder.get(UserController::getAllUserIds, Role.ANYONE);
                    ApiBuilder.post(UserController::createUser, Role.ADMIN);
                    ApiBuilder.path("{userId}", () -> {
                        ApiBuilder.get(UserController::getUserById, Role.USER_READ);
                        ApiBuilder.patch(UserController::updateUser, Role.USER_WRITE);
                        ApiBuilder.delete(UserController::deleteUser, Role.ADMIN);
                    });
                });
            }).start(7070);
        }
    }
    ```
