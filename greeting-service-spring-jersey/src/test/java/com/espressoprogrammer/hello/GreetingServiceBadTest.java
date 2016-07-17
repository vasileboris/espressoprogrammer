package com.espressoprogrammer.hello;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.core.MediaType;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-applicationContext.xml"})
public class GreetingServiceBadTest extends JerseyTest {
    private static final String JERSEY_TEST_PORT_SYSTEM_PROPERTY = "jersey.test.port";
    private static final Integer JERSEY_TEST_PORT = 8080;

    @BeforeClass
    public static void init() {
        System.setProperty(JERSEY_TEST_PORT_SYSTEM_PROPERTY, JERSEY_TEST_PORT.toString());
    }

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    public GreetingServiceBadTest() {
        super(new WebAppDescriptor.Builder("com.espressoprogrammer.hello")
            .contextPath("greeting-service")
            .contextParam("contextConfigLocation", "classpath:/test-applicationContext.xml")
            .servletClass(SpringServlet.class)
            .contextListenerClass(ContextLoaderListener.class)
            .build());
    }

    @Test
    public void greetingGetWithProvidedContent() throws Exception {
        given()
            .port(JERSEY_TEST_PORT)
            .filter(documentationConfiguration(this.restDocumentation))
            .filter(document("{class-name}/{method-name}",
                requestParameters(parameterWithName("name").description("Greeting's target"))
            ))
            .accept(MediaType.APPLICATION_JSON)
        .when()
            .get("/greeting-service/greeting?name={id}", "Everybody")
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().contentType(equalTo(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    public void greetingGetWithDefaultContent() throws Exception {
        given()
            .port(JERSEY_TEST_PORT)
            .filter(documentationConfiguration(this.restDocumentation))
            .filter(document("{class-name}/{method-name}"))
            .accept(MediaType.APPLICATION_JSON)
        .when()
            .get("/greeting-service/greeting")
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().contentType(equalTo(MediaType.APPLICATION_JSON))
        ;
    }

}
