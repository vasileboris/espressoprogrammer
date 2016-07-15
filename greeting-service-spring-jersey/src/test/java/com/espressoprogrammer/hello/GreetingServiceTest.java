package com.espressoprogrammer.hello;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.core.MediaType;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-applicationContext.xml"})
public class GreetingServiceTest extends JerseyTest {
    private static final Integer JERSEY_CONTAINER_PORT = 9998;

    @Rule
    public final ProvideSystemProperty myPropertyHasMyValue = new ProvideSystemProperty("jersey.config.test.container.port", JERSEY_CONTAINER_PORT.toString());

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    public GreetingServiceTest() {
        super(new WebAppDescriptor.Builder("com.espressoprogrammer.hello;org.codehaus.jackson.jaxrs")
            .contextPath("greeting-service")
            .contextParam("contextConfigLocation", "classpath:/test-applicationContext.xml")
            .servletClass(SpringServlet.class)
            .contextListenerClass(ContextLoaderListener.class)
            .build());
    }

    @Test
    public void greetingGetWithProvidedContent() throws Exception {
        FieldDescriptor[] greeting = new FieldDescriptor[] {
            fieldWithPath("id").description("Greeting's generated id"),
            fieldWithPath("content").description("Greeting's content"),
            fieldWithPath("optionalContent").description("Greeting's optional content").type(JsonFieldType.STRING).optional()
        };
        given()
            .port(JERSEY_CONTAINER_PORT)
            .filter(documentationConfiguration(this.restDocumentation))
            .filter(document("{class-name}/{method-name}",
                requestParameters(parameterWithName("name").description("Greeting's target")),
                responseFields(fieldWithPath("[]").description("An array of greetings")).andWithPrefix("[].", greeting)))
            .accept(MediaType.APPLICATION_JSON)
            .get("/greeting-service/greeting?name={id}", "Everybody")
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().contentType(equalTo(MediaType.APPLICATION_JSON))
            .assertThat().body("content", hasItems("Hello, Everybody!"))
        ;
    }

    @Test
    public void greetingGetWithDefaultContent() throws Exception {
        FieldDescriptor[] greeting = new FieldDescriptor[] {
            fieldWithPath("id").ignored(),
            fieldWithPath("content").description("When name is not provided, this field contains the default value")
        };
        given()
            .port(JERSEY_CONTAINER_PORT)
            .filter(documentationConfiguration(this.restDocumentation))
            .filter(document("{class-name}/{method-name}",
                responseFields(fieldWithPath("[]").description("An array of greetings")).andWithPrefix("[].", greeting)))
            .accept(MediaType.APPLICATION_JSON)
            .get("/greeting-service/greeting")
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().contentType(equalTo(MediaType.APPLICATION_JSON))
            .assertThat().body("content", hasItems("Hello, World!"))
        ;
    }

    @Override
    protected AppDescriptor configure() {
        return super.configure();
    }
}
