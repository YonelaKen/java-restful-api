package com.example.server;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.example.Controllers.UserController;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.staticfiles.Location;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

public class App {
    
    private static Javalin server;
    private static int port = 7070;
    private static String DIR = "public";
    private static String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) {
        JavalinThymeleaf.configure(templateEngine());
        setupServer();
        startServer();
    }
    
    public static void setupServer(){
        server = Javalin.create(config -> {
            config.addStaticFiles(DIR, Location.CLASSPATH);
        }).routes(configEndpointsGroup());
    }

    private static EndpointGroup configEndpointsGroup() {
        return ()-> {
            ApiBuilder.get("/", context -> context.redirect("/users/login"));
            ApiBuilder.path("/users", ()-> {
                ApiBuilder.post("/login", UserController.login);
                ApiBuilder.get("/login", UserController.loginForm);
                ApiBuilder.post("/register", UserController.register);
                ApiBuilder.get("/register", UserController.registerForm);
                ApiBuilder.get("/logout", UserController.logout);
                ApiBuilder.get("/all", UserController.all);
                ApiBuilder.get("/add", UserController.add);
                ApiBuilder.get("/edit", UserController.edit);

            });
        };
    }

    public static void startServer(){
        server.start(port);
    }

    public static void stopServer(){
        server.stop();
    }

    private static TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix(TEMPLATES_DIR);
        templateEngine.setTemplateResolver(resolver);
        templateEngine.addDialect(new LayoutDialect());
        return templateEngine;
    }
}