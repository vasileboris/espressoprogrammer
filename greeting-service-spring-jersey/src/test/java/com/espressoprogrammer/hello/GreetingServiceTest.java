package com.espressoprogrammer.hello;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
    private static final String JERSEY_TEST_PORT_SYSTEM_PROPERTY = "jersey.test.port";
    private static final Integer JERSEY_TEST_PORT = 8080;

    @BeforeClass
    public static void init() {
        System.setProperty(JERSEY_TEST_PORT_SYSTEM_PROPERTY, JERSEY_TEST_PORT.toString());
    }

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private static ClientConfig createClientConfig() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);
        return clientConfig;
    }

    public GreetingServiceTest() {
        super(new WebAppDescriptor.Builder("com.espressoprogrammer.hello;org.codehaus.jackson.jaxrs")
            .contextPath("greeting-service")
            .contextParam("contextConfigLocation", "classpath:/test-applicationContext.xml")
            .servletClass(SpringServlet.class)
            .contextListenerClass(ContextLoaderListener.class)
            .clientConfig(createClientConfig())
            .build());
    }

    @Test
    public void greetingGetWithProvidedContentTestedWithRestassured() throws Exception {
        FieldDescriptor[] greeting = new FieldDescriptor[] {
            fieldWithPath("id").description("Greeting's generated id"),
            fieldWithPath("content").description("Greeting's content"),
            fieldWithPath("optionalContent").description("Greeting's optional content").type(JsonFieldType.STRING).optional()
        };
        given()
            .port(JERSEY_TEST_PORT)
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
    public void greetingGetWithProvidedContentTestedWithJersey() throws Exception {
        WebResource webResource = resource().path("greeting");
        ClientResponse response = webResource
            .queryParam("name", "Everybody")
            .accept(MediaType.APPLICATION_JSON)
            .get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Greeting[] greetings = response.getEntity(new GenericType<Greeting[]>(){});
        assertThat(greetings).hasSize(1);
        assertThat(greetings[0].getContent()).isEqualTo("Hello, Everybody!");
    }

    @Test
    public void greetingGetWithDefaultContentTestedWithRestassured() throws Exception {
        FieldDescriptor[] greeting = new FieldDescriptor[] {
            fieldWithPath("id").ignored(),
            fieldWithPath("content").description("When name is not provided, this field contains the default value")
        };
        given()
            .port(JERSEY_TEST_PORT)
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

    @Test
    public void greetingGetWithDefaultContentTestedWithJersey() throws Exception {
        WebResource webResource = resource().path("greeting");
        ClientResponse response = webResource
            .accept(MediaType.APPLICATION_JSON)
            .get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Greeting[] greetings = response.getEntity(new GenericType<Greeting[]>(){});
        assertThat(greetings).hasSize(1);
        assertThat(greetings[0].getContent()).isEqualTo("Hello, World!");
    }

}
