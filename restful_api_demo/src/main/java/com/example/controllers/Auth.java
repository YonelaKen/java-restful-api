package com.example.controllers;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.Header;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;

import java.util.Set;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import kotlin.Pair;

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

    /**
     * It takes the "Authorization" header from the request, decodes it, and returns a pair of strings
     * containing the username and password
     * 
     * @param ctx The context of the request.
     * @return A pair of strings.
     */
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

    /**
     * If the user is authenticated, return the list of roles associated with the user
     * 
     * @param ctx The context of the request.
     * @return A list of roles
     */
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
}
