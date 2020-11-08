package com.tomekl007.CH05;

import com.tomekl007.CH05.initial.DefaultWordsService;
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
    wordsService = new DefaultWordsService(defaultPath);
  }

  // we are expecting that according to the pareto principle, the majority of traffic will go to
  // this endpoint and our SLA is confirming that

  // Word of the day. It returns the one word from the file, and it is called once per day.
  // It is very naive - gets the date as number, add Fixed offset And returns the same word.
  // It loads the file and iterate over every line until it met the line with number.
  // Not optimal, easy to optimize but not hot path! We don’t need to prematurely optimize it.
  @GET
  @Path("/word-of-the-day")
  public Response getAllAccounts() {
    return Response.ok(wordsService.getWordOfTheDay()).build();
  }

  // Validate if word exists in the dictionary.
  // Called hundred times per second.
  // First version iterates over file and check if the line exists.
  // It does it every time. It is on the hot path!
  // It should be optimized.
  // Add metrics around loading file and iterating over results.
  // Show optimizations using cache. whole file by loading it every n second.
  // Also per lines. Key is word value info if it does exists.
  // 3rd optimization - add regrxp that pre checks if the provided line has alpha numeric.
  // Jmh every variant. Finally show the Gatling benchmark that shows how hot path was optimized.

  @GET
  @Path("/word-exists")
  public Response validateAccount(@QueryParam("word") String word) {
    boolean exists = wordsService.wordExists(word);
    return Response.status(Status.OK.getStatusCode(), String.valueOf(exists)).build();
  }
}
