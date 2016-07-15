package com.espressoprogrammer.hello;

import org.springframework.stereotype.Component;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Path("/greeting")
public class GreetingService {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Greeting> greeting(@QueryParam("name") @DefaultValue("World") String name) {
        return Arrays.asList(new Greeting(counter.incrementAndGet(), String.format(template, name)));
    }

}
