package com.tomekl007.CH05;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

public class HttpApplication extends Application<Configuration> {

  @Override
  public void run(Configuration configuration, Environment environment) {
    WordsController wordsController = new WordsController();
    environment.jersey().register(wordsController);
  }

  // it will be accessible under
  // http://localhost:8080/words
  public static void main(String[] args) throws Exception {
    new HttpApplication().run("server");
  }
}
