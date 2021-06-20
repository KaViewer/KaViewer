package com.koy.kaviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.jline.JLineShellAutoConfiguration;

@SpringBootApplication
public class KaViewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KaViewerApplication.class, args);
    }

}
