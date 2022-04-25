package com.koy.kaviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer"})
public class KaViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KaViewerApplication.class, args);
    }

}
