package com.koy.kaviewer.runner;

import com.koy.kaviewer.app.KaViewerApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * For build a dependency jar to use KaViewer as import component and extensions for persist.
 * Import runner.jar in pom like.
 *<dependency>
 * 			<groupId>com.koy</groupId>
 * 			<artifactId>runner</artifactId>
 * 			<version>0.0.1</version>
 * 		</dependency>
 * Run the KaViewerRunnerApplication to start the KaViewer.
 *
 * @SpringBootApplication
 * public class DemoApplication {
 *     public static void main(String[] args) {
 *         KaViewerApplication.main(args);
 *     }
 * }
 */
@SpringBootApplication
public class KaViewerRunnerApplication {
    public static void main(String[] args) {
        KaViewerApplication.main(args);
    }
}
