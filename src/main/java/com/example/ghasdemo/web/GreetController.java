package com.example.ghasdemo.web;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demonstrates java/xss and the commons-text CVE-2022-42889 sink.
 */
@RestController
public class GreetController {

    /** VULNERABLE: reflected XSS, user input written into an HTML response unescaped. */
    @GetMapping("/greet")
    public void greet(@RequestParam String name, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().write("<h1>Hello, " + name + "!</h1>");
    }

    /**
     * VULNERABLE: StringSubstitutor in commons-text 1.9 interpolates script: and dns:
     * lookups, so this is remote code execution (Text4Shell).
     */
    @GetMapping("/template")
    public String template(@RequestParam String text) {
        return new StringSubstitutor().replace(text);
    }
}
