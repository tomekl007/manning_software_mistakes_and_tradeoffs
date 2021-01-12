package com.tomekl007.CH05;

import com.tomekl007.CH05.cache.CachedWordsService;
import java.nio.file.Paths;
import java.util.Objects;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/words")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WordsController {
  private final WordsService wordsService;

  public WordsController() {
    java.nio.file.Path defaultPath =
        Paths.get(
            Objects.requireNonNull(getClass().getClassLoader().getResource("words.txt")).getPath());
    wordsService = new CachedWordsService(defaultPath);
  }

  // Word of the day. It returns the one Word from the file, and it is called once per day.
  // It is very naive - gets the date as a number, adds fixed offset, and returns the same Word.
  // It loads the file and iterates over every line until it met the line with a number.
  // Not optimal, easy to optimize but not a hot path! We don’t need to optimize it prematurely.
  @GET
  @Path("/word-of-the-day")
  public Response getAllAccounts() {
    return Response.ok(wordsService.getWordOfTheDay()).build();
  }

  // we are expecting that according to the Pareto principle, the majority of traffic will go to
  // this endpoint and our SLA is confirming that

  // Validate if a word exists in the dictionary.
  // Called hundred times per second.
  // First version iterates over a file and checks if the line exists.
  // It does it every time. It is on the hot path!
  // It should be optimized.
  @GET
  @Path("/word-exists")
  public Response validateAccount(@QueryParam("word") String word) {
    boolean exists = wordsService.wordExists(word);
    return Response.status(Status.OK.getStatusCode(), String.valueOf(exists)).build();
  }
}
