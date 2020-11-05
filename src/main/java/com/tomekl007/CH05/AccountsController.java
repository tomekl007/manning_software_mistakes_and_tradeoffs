package com.tomekl007.CH05;

import com.tomekl007.CH05.initial.DefaultNamingService;
import com.tomekl007.CH05.initial.DefaultSupportedAccountsLoader;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountsController {
  private final DefaultSupportedAccountsLoader defaultSupportedAccountsLoader;
  private final DefaultNamingService defaultNamingService;

  public AccountsController() {
    java.nio.file.Path defaultPath =
        Paths.get(
            Objects.requireNonNull(getClass().getClassLoader().getResource("accounts.yaml"))
                .getPath());
    defaultSupportedAccountsLoader = new DefaultSupportedAccountsLoader(defaultPath);
    defaultNamingService = new DefaultNamingService();
  }

  // we are expecting that according to the pareto principle, the majority of traffic will go to
  // this endpoint
  // and our SLA is confirming that
  @GET
  @Path("/all")
  public Response getAllAccounts() {
    return Response.ok(defaultSupportedAccountsLoader.accounts()).build();
  }

  @GET
  @Path("/validate/{id}")
  public Response validateAccount(@PathParam("token") Integer id) {
    Optional<Account> account = defaultSupportedAccountsLoader.account(id);
    if (!account.isPresent()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    defaultNamingService.checkIfNameIsCorrect(account.get().getName());

    return Response.ok().build();
  }
}
