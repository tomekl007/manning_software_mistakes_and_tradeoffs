package com.tomekl007.CH07;

public class JsonAlarm {

    private final String type;
    private final String dateTime;
    private final String label;
    
    public JsonAlarm(String type, String dateTime, String label) {
        this.type = type;
        this.dateTime = dateTime;
        this.label = label;                
    }
    
    public String getType() {
        return type;
    }
    
    public String getDateTime() {
        return dateTime;
    }
    
    public String getLabel() {
        return label;
    }
}
