package com.tomekl007.CH02.services.sharing.person;

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

  private final PersonService personService = new PersonService();
  private final AuthService authService = new AuthService();

  @GET
  @Path("/{token}/{id}")
  public Response getPersonById(@PathParam("token") String token, @PathParam("id") String id) {
    if (authService.validToken(token)) {
      return Response.ok(personService.getById(id)).build();
    } else {
      return Response.status(Status.UNAUTHORIZED).build();
    }
  }
}
