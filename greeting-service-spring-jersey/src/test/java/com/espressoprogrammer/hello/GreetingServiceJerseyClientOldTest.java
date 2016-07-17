package com.espressoprogrammer.hello;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-applicationContext.xml"})
public class GreetingServiceJerseyClientOldTest extends JerseyTest {
    private static final String JERSEY_TEST_PORT_SYSTEM_PROPERTY = "jersey.test.port";
    private static final Integer JERSEY_TEST_PORT = 8080;

    @BeforeClass
    public static void init() {
        System.setProperty(JERSEY_TEST_PORT_SYSTEM_PROPERTY, JERSEY_TEST_PORT.toString());
    }

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    public GreetingServiceJerseyClientOldTest() {
        super(new WebAppDescriptor.Builder("com.espressoprogrammer.hello")
            .contextPath("greeting-service")
            .contextParam("contextConfigLocation", "classpath:/test-applicationContext.xml")
            .servletClass(SpringServlet.class)
            .contextListenerClass(ContextLoaderListener.class)
            .build());
    }

    @Test
    public void greetingGetWithProvidedContent() throws Exception {
        WebResource webResource = resource().path("greeting");
        ClientResponse response = webResource
            .queryParam("name", "Everybody")
            .accept(MediaType.APPLICATION_JSON)
            .get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        List<Greeting> greetings = response.getEntity(new GenericType<List<Greeting>>(){});
        assertThat(greetings).hasSize(1);
        assertThat(greetings.get(0).getContent()).isEqualTo("Hello, Everybody!");
    }

    @Test
    public void greetingGetWithDefaultContent() throws Exception {
        WebResource webResource = resource().path("greeting");
        ClientResponse response = webResource
            .accept(MediaType.APPLICATION_JSON)
            .get(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        List<Greeting> greetings = response.getEntity(new GenericType<List<Greeting>>(){});
        assertThat(greetings).hasSize(1);
        assertThat(greetings.get(0).getContent()).isEqualTo("Hello, World!");
    }

}
