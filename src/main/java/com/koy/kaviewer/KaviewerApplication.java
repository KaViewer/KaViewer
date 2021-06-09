package com.koy.kaviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class KaviewerApplication {


    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(KaviewerApplication.class, args);
    }

}
