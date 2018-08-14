package net.atos.ojas;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

@Component
public class ApiRoute extends RouteBuilder {

  @Autowired
  CamelContext ctx;

  @Value("${ssl.hostname.verify}")
  String isSslHostnameValidation;

  @Override
  public void configure() throws Exception {
    // DEBUG
    //ctx.setTracing(true);
    // SSL Config
    configureSslForHttp4();

    // @formatter:off
    restConfiguration()
      .component("servlet")
      .contextPath("/")
      .endpointProperty("matchOnUriPrefix", "true")
      /*.dataFormatProperty("prettyPrint", "true")*/
      .bindingMode(RestBindingMode.off)
      .apiContextPath("/api-doc")
        .apiProperty("api.title", "User API").apiProperty("api.version", "1.0.0")
        .apiProperty("cors", "true");


    rest("/")
      .get("/")
      .consumes(MediaType.MEDIA_TYPE_WILDCARD)
      .produces(MediaType.TEXT_PLAIN)
        .to("direct:in")

      .post("/")
        .consumes(MediaType.MEDIA_TYPE_WILDCARD)
        .produces(MediaType.APPLICATION_JSON)
        .to("direct:in");

    from("direct:in").routeId("route-1")
      .log(LoggingLevel.INFO, "Message Trace Id: ${header.breadcrumbId}")
      .log(LoggingLevel.DEBUG,"***** {{target.endpoint.ip}}")
      .to("https4://{{target.endpoint.ip}}:8443/?bridgeEndpoint=true&throwExceptionOnFailure=false")
      .to("mock:out");
    // @formatter:on

  }

  private void configureSslForHttp4() {

    KeyStoreParameters trustStoreParameters = new KeyStoreParameters();
    trustStoreParameters.setResource("ssl/truststore.p12");
    trustStoreParameters.setPassword("password");

    TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
    trustManagersParameters.setKeyStore(trustStoreParameters);

    SSLContextParameters sslContextParameters = new SSLContextParameters();
    sslContextParameters.setTrustManagers(trustManagersParameters);

    HttpComponent http4Component = getContext().getComponent("http4", HttpComponent.class);
    http4Component.setSslContextParameters(sslContextParameters);

    if ( isSslHostnameValidation != null || isSslHostnameValidation.equals("false")) {
      http4Component.setX509HostnameVerifier(new AllowAllHostnameVerifier());
    }
  }

}

