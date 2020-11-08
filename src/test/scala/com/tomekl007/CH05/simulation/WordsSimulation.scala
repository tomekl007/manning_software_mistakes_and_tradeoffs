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
class WordsSimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8080/words") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers



  val wordOfTheDayScenario = scenario("word-of-the-day")
    .exec(WordOfTheDay.get)


  val validateScenario = scenario("word-exists")
    .exec(ValidateWord.validate)


  setUp(
    wordOfTheDayScenario.inject(
      constantUsersPerSec(40) during (2 minutes)
    ),
    validateScenario.inject(
      constantUsersPerSec(10) during (2 minutes)
  )).protocols(httpProtocol)


}

object WordOfTheDay {
  val get = http("word-of-the-day").get("/word-of-the-day")

}

object ValidateWord{
  val feeder = csv("words.csv").random

  val validate = feed(feeder).exec(
    http("word-exists")
      .get("/word-exists?word=${word}").check(status is 200)
  )

}