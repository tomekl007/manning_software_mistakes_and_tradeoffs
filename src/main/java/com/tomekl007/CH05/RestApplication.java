package com.tomekl007.CH05;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

public class RestApplication extends Application<Configuration> {

  @Override
  public void run(Configuration configuration, Environment environment) {
    WordsController wordsController = new WordsController();
    environment.jersey().register(wordsController);
  }

  // it will be accessible under
  // http://localhost:8080/accounts/all
  public static void main(String[] args) throws Exception {
    new RestApplication().run("server");
  }
}
