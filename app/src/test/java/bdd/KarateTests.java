package bdd;

import com.intuit.karate.junit5.Karate;
import com.koy.kaviewer.app.KaViewerApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {KaViewerApplication.class})
public class KarateTests {
    public static final String BaseClasspath = "classpath:bdd/";

    @Karate.Test
    Karate clusterTest() {
        return Karate.run(BaseClasspath + "kaviewer.feature");
    }
}
