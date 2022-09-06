package bdd;

import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;

//  lsof -i tcp:9394
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {DummyApplication.class})
public class KarateTests {
    public static final String BaseClasspath = "classpath:bdd/";

    @Karate.Test
    Karate clusterTest() {
        return Karate.run(BaseClasspath + "kaviewer.feature");
    }
}
