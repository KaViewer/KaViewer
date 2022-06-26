package com.koy.kaviewer.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI(@Value("v${kaviewer.version:x,y.z}") String kaViewerVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("KaViewer")
                        .version(kaViewerVersion)
                        .description("KaViewer Swagger API docs.")
                        .contact(new Contact().name("@Koy").url("https://github.com/Koooooo-7"))
                        .license(new License().name("Apache License Version 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                );
    }
}
