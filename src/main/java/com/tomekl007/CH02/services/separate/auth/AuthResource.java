package com.tomekl007.CH02.services.separate.auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

  private final AuthService authService = new AuthService();

  @GET
  @Path("/validate/{token}")
  public Response validateToken(@PathParam("token") String token) {
    if (authService.validToken(token)) {
      return Response.ok().build();
    } else {
      return Response.status(Status.UNAUTHORIZED).build();
    }
  }
}
