package net.atos.ojas;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiBaseTest {

  @Test
  public void trivial() {
    assert(true);
  }

  @Test
  public void echoSuccess() {
  // formatter:off
    given()
      .log().uri()
    .when()
      .get("/dummy/echo/Hello")
    .then()
      .log().all()
      .statusCode(HttpStatus.SC_OK)
      .contentType(MediaType.TEXT_PLAIN)
      .body(equalTo("Hello\n"));
  // formatter:on
  }

  @Test
  public void echoNullNotFound() {
    // formatter:off
    ValidatableResponse result =
    given()
      .log().uri()
    .when()
      .get("/dummy/echo/")
    .then()
      .log().all()
      .statusCode(HttpStatus.SC_NOT_FOUND)
      .contentType(MediaType.TEXT_HTML);
    // formatter:on
  }

  @Test
  public void reverseSuccess() {
  // formatter:off
    given()
      .log().uri()
    .when()
      .get("/dummy/reverse/Hello")
    .then()
      .log().all()
      .statusCode(HttpStatus.SC_OK)
      .contentType(MediaType.TEXT_PLAIN)
      .body(equalTo("olleH\n"));
  // formatter:on
  }

  @Test
  public void reverseNullNotFound() {
    // formatter:off
    given()
      .log().uri()
    .when()
      .get("/dummy/reverse/")
    .then()
      .log().all()
      .statusCode(HttpStatus.SC_NOT_FOUND)
      .contentType(MediaType.TEXT_HTML);
    // formatter:on
  }

  @Test
  public void timeSuccess() {
    // formatter:off
    ValidatableResponse result =
    given()
      .log().uri()
    .when()
      .get("/dummy/time/")
    .then()
      .log().all()
      .statusCode(HttpStatus.SC_OK)
      .contentType(MediaType.TEXT_PLAIN)
      .body(notNullValue());
    // formatter:on

    // Assert that we can parse the response
    // as a valid ISO Date Time
    String resultStr = result.extract().body().asString().trim();
    DateTimeFormatter parser=ISODateTimeFormat.dateTimeParser();
    DateTime dt=parser.parseDateTime(resultStr);
  }

  @Test
  public void dataSuccess() {
    // formatter:off
    given()
      .log().uri()
      .log().headers()
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
      .header("X-CorrelationId","ABC123")
    .when()
      .post("/dummy/data/")
    .then()
      .log().all()
      .statusCode(HttpStatus.SC_OK)
      .contentType(MediaType.APPLICATION_JSON)
      .body("id",instanceOf(Integer.class))
      .body("tags", hasSize(4))
      .body("data.c", is("d"));
    // formatter:on
  }

  @Test
  public void dataNoCorrelationId() {
    // formatter:off
    given()
      .log().uri()
      .log().headers()
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
    .when()
      .post("/dummy/data/")
    .then()
      .log().all()
      .statusCode(HttpStatus.SC_BAD_REQUEST);
    // formatter:on
  }

  @Test
  public void dataBadAcceptMediaType() {
    // formatter:off
    given()
      .log().uri()
      .log().headers()
      .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
      .header("X-CorrelationId","ABC123")
    .when()
      .post("/dummy/data/")
    .then()
      .log().all()
      .statusCode(HttpStatus.SC_NOT_ACCEPTABLE);
    // formatter:on
  }

}
