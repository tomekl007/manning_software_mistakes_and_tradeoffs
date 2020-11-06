package com.tomekl007.CH05.simulation

import io.gatling.core.Predef.{Simulation, nothingFor, _}
import io.gatling.http.Predef.{http, status}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * to start simulation: mvn gatling:test
 *
 */
class AccountsSimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8080/accounts") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers



  val allAccountsScenario = scenario("all_accounts")
    .exec(AllAccounts.all)


  val validateAccountsScenario = scenario("validate_accounts")
    .exec(ValidateAccounts.validate)


  setUp(
    allAccountsScenario.inject(
      rampUsers(10) during (10 seconds)
    ),
    validateAccountsScenario.inject(
      rampUsers(10) during (10 seconds)
  )).protocols(httpProtocol)


}

object AllAccounts {
  val all = http("all_accounts").get("/all")

}

object ValidateAccounts{
  val feeder = csv("account_ids.csv").random

  val validate = feed(feeder).exec(
    http("validate")
      .get("/validate?id=${id}").check(status is 200)
  )

}