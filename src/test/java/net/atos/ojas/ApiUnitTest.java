package net.atos.ojas;

import io.restassured.RestAssured;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.EnableRouteCoverage;
import org.arquillian.cube.docker.junit.rule.ContainerDslRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = ApiUnitTest.Setup.class)
@EnableRouteCoverage
public class ApiUnitTest extends ApiBaseTest {

  @ClassRule
  public static ContainerDslRule stub = new ContainerDslRule("stub-template:1.0-SNAPSHOT", "stub_"+ UUID.randomUUID());

  @Value("${local.server.port}")
  int port;

  @Before
  public void setUp() throws Exception{
    RestAssured.port = port;
    RestAssured.baseURI = "http://localhost";
    //RestAssured.trustStore("ssl/truststore.p12", "password");

    // This config option disables all SSL validation checking
    //RestAssured.useRelaxedHTTPSValidation();

  }

  ///
  /// All Tests Defined in BaseTest class
  ///



  public static class Setup implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      //*******************************************
      // Use this option to disable SSL for testing
      //*******************************************
      /*
      EnvironmentTestUtils.addEnvironment("test", configurableApplicationContext.getEnvironment(),
        "server.ssl.enabled=false"
      );
      */

      //
      //  Get IP Address of Docker Container
      //
      String stubIp = stub.exec("/bin/sh","-c","hostname -i").getStandard();

      EnvironmentTestUtils.addEnvironment("test", configurableApplicationContext.getEnvironment(),
              "target.endpoint.ip=" + stubIp, "ssl.hostname.verify=false"
      );
    }
  }

}
