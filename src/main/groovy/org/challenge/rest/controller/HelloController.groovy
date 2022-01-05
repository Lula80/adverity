package org.challenge.rest.controller

import org.challenge.utils.Constants
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @RequestMapping("/")
    String index() {
        return "<h1 style =\"text-align: center;\">Swagger Documentation:</h1><br/>\n" +
                getAbsoluteHyperlink() +
                "<br/>\n\n"
    }

    private String getAbsoluteHyperlink() {
        return "<a href=\"swagger-ui/\">" +
                "<div style=\"text-align: center; font-size: 40px;\">https://" +
                Constants.RFC3986.APPLICATION_NAME + "-" + Constants.RFC3986.API_VERSION +
                "/swagger-ui/</div>" +
                "</a><br/><br/>\n\n\n\n"
    }
}
