package net.atos.ojas;

import io.restassured.RestAssured;
import org.arquillian.cube.docker.impl.client.containerobject.dsl.*;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.UUID;


@RunWith(Arquillian.class)
public class ApiContainerObjectTest extends ApiBaseTest {

  @DockerNetwork
  private static final Network network = NetworkBuilder.withDefaultDriver("network_"+UUID.randomUUID()).build();

  @DockerContainer
  private static Container stub = Container.withContainerName("stub_"+UUID.randomUUID())
                                                  .fromImage("stub-template:1.0-SNAPSHOT")
                                                  .withAwaitStrategy(AwaitBuilder.logAwait("Started Application"))
                                                  .withExposedPorts(8443)
                                                  .withNetworkMode(network)
                                                  .build();

  @DockerContainer
  private static Container test = Container.withContainerName("test_"+UUID.randomUUID())
                                                  .fromBuildDirectory(".")  //.fromImage("api-template:1.0-SNAPSHOT")
                                                  .withAwaitStrategy(AwaitBuilder.logAwait("Started Application"))
                                                  .withExposedPorts(1080)
                                                  .withNetworkMode(network)
                                                  .withLink(stub.getContainerName(),"stub.stub.io")
                                                  .build();

  @Before
  public void setUp() {
    // We set the 'kill' flag to agressively
    // terminate the containers after testing
    stub.getCubeContainer().setKillContainer(true);
    test.getCubeContainer().setKillContainer(true);

    // Get the 'internal' IP of the Docker container we want to test
    final String testIp = test.exec("/bin/sh","-c","hostname -i").getStandard().trim();

    RestAssured.port = 1080;
    RestAssured.baseURI =  "http://"+testIp;
    //RestAssured.trustStore("ssl/truststore.p12", "password");

    // This config option disables all SSL validation checking
    //RestAssured.useRelaxedHTTPSValidation();
  }


  ///
  /// All Tests Defined in BaseTest class
  ///

}
