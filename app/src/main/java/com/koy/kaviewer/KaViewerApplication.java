package com.koy.kaviewer;


import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.rest"})
public class KaViewerApplication {
    public static void main(String[] args) {
        SpringApplicationBuilder parentBuilder =
                new SpringApplicationBuilder(KaViewerApplication.class)
                        .web(WebApplicationType.NONE);
        parentBuilder.run(args);
        parentBuilder.child(ConsumerApplication.class)
                .profiles("rest").run(args);
        parentBuilder.child(ClientApplication.class)
                .properties("kafka").run(args);
    }

}
