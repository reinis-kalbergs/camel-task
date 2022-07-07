package com.example.cameltask.demo;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CurrentTime {
    public String getCurrentTime(){
        return "Time now is: " + LocalDateTime.now();
    }
}
