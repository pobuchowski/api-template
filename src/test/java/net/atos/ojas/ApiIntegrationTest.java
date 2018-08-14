package net.atos.ojas;

import io.restassured.RestAssured;
import org.arquillian.cube.DockerUrl;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.net.URL;


@RunWith(Arquillian.class)
public class ApiIntegrationTest extends ApiBaseTest {

  @DockerUrl(containerName = "test*", exposedPort = 1080)
  @ArquillianResource
  private URL url;


  @Before
  public void setUp() throws Exception {
    RestAssured.port = url.getPort();
    RestAssured.baseURI = url.toURI().toString();
    RestAssured.trustStore("ssl/truststore.p12", "password");


    // This config option disables all SSL validation checking
    //RestAssured.useRelaxedHTTPSValidation();
  }


  ///
  /// All Tests Defined in BaseTest class
  ///


}
