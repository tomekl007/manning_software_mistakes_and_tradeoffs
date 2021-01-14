package com.tomekl007.CH07;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DailyAlarm {

  public LocalTime getTimeOfDay(JsonAlarm json) {
    String jsonDateTime = json.getDateTime();
    LocalDateTime dateTime =
        LocalDateTime.parse(jsonDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    LocalTime time = dateTime.toLocalTime();
    return time;
  }
}
