package com.espressoprogrammer.references;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ReferencesController {

    @Autowired
    private ReferencesService referencesService;

    @RequestMapping("/references")
    void references(HttpServletResponse response,
                    @RequestParam(required = false, defaultValue = "10") int referencesCountMax,
                    @RequestParam(required = false, defaultValue = "2") int insertMoreReferencesMax,
                    @RequestParam(required = false, defaultValue = "5000") int millisToKeepInMemoryHardReferences) throws IOException {
        response.setContentType("image/png");
        response.getOutputStream().write(referencesService.execute(referencesCountMax, insertMoreReferencesMax, millisToKeepInMemoryHardReferences));
        response.getOutputStream().close();
    }

}
