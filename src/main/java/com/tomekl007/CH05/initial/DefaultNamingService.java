package com.tomekl007.CH05.initial;

import com.tomekl007.CH05.NamingService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultNamingService implements NamingService {

  private static Pattern NAME_PATTERN = Pattern.compile("^[A-Z][a-z]{2,}(?: [A-Z][a-z]*)*$");

  @Override
  public boolean checkIfNameIsCorrect(String name) {
    Matcher matcher = NAME_PATTERN.matcher(name);
    return matcher.matches();
  }
}
