package com.tomekl007.CH02.services.separate.person;

import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonRestResource {

  private final PersonService paymentService = new PersonService();
  private final AuthService authService = new AuthService();

  @GET
  @Path("/{token}/{id}")
  public Response getPersonById(@PathParam("token") String token, @PathParam("id") String id)
      throws IOException {
    if (authService.validToken(token)) {
      return Response.ok(paymentService.getById(id)).build();
    } else {
      return Response.status(Status.UNAUTHORIZED).build();
    }
  }
}
