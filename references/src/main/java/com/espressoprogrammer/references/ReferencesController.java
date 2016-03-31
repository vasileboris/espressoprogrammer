package com.espressoprogrammer.references;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ReferencesController {

    @Autowired
    private ReferencesComponent classSoft;

    @RequestMapping("/references")
    void home(HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        response.getOutputStream().write(classSoft.execute());
        response.getOutputStream().close();
    }

}
