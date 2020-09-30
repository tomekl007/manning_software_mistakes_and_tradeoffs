package com.tomekl007.CH02.services.sharing.payment;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentRestResource {

	private final PaymentService paymentService = new PaymentService();
	private final AuthService authService = new AuthService();

	@GET
	@Path("/{token}")
	public Response getAllPayments(@PathParam("token") String token) {
		if(authService.validToken(token)) {
			return Response.ok(paymentService.getAllPayments()).build();
		}else{
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

}
