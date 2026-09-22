package com.example.ghasdemo.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Operator tooling. Demonstrates java/command-line-injection and java/ssrf.
 */
@RestController
public class AdminController {

    /** VULNERABLE: user input reaches a shell. */
    @PostMapping("/admin/ping")
    public String ping(@RequestParam String host) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("cmd /c ping -n 1 " + host);
        String out = read(p.getInputStream());
        p.waitFor();
        return out;
    }

    /** VULNERABLE: server-side request forgery, the URL is fully attacker controlled. */
    @GetMapping("/admin/fetch")
    public String fetch(@RequestParam String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        return read(conn.getInputStream());
    }

    private static String read(InputStream in) {
        try (Scanner s = new Scanner(in, StandardCharsets.UTF_8.name()).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }
}
