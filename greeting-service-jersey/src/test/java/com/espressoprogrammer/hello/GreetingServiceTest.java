package com.espressoprogrammer.hello;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class GreetingServiceTest extends JerseyTest {
    private static final Integer JERSEY_CONTAINER_PORT = 8080;

    @Rule
    public final ProvideSystemProperty myPropertyHasMyValue = new ProvideSystemProperty("jersey.config.test.container.port", JERSEY_CONTAINER_PORT.toString());

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Test
    public void greetingGetWithProvidedContent() throws Exception {
        given()
                .port(JERSEY_CONTAINER_PORT)
                .filter(documentationConfiguration(this.restDocumentation))
                .filter(document("{class-name}/{method-name}",
                        requestParameters(parameterWithName("name").description("Greeting's target")),
                        responseFields(fieldWithPath("id").description("Greeting's generated id"),
                                fieldWithPath("content").description("Greeting's content"),
                                fieldWithPath("optionalContent").description("Greeting's optional content").type(JsonFieldType.STRING).optional()
                        )))
                .accept(MediaType.APPLICATION_JSON.toString())
                .get("/greeting?name={id}", "Everybody")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().contentType(equalTo(MediaType.APPLICATION_JSON.toString()))
                .assertThat().body("content", equalTo("Hello, Everybody!"));
    }

    @Test
    public void greetingGetWithDefaultContent() throws Exception {
        given()
                .port(JERSEY_CONTAINER_PORT)
                .filter(documentationConfiguration(this.restDocumentation))
                .filter(document("{class-name}/{method-name}",
                        responseFields(fieldWithPath("id").ignored(),
                                fieldWithPath("content").description("When name is not provided, this field contains the default value"))))
                .accept(MediaType.APPLICATION_JSON.toString())
                .get("/greeting")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().contentType(equalTo(MediaType.APPLICATION_JSON.toString()))
                .assertThat().body("content", equalTo("Hello, World!"));
    }

    @Override
    public ResourceConfig configure() {
        return new ResourceConfig(GreetingService.class);
    }

}
