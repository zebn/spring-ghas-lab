package com.example.ghasdemo.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Report download. Demonstrates java/path-injection.
 */
@RestController
public class FileController {

    private static final Path REPORT_DIR = Paths.get("reports");

    /** VULNERABLE: the file name is user controlled, so ../../ escapes REPORT_DIR. */
    @GetMapping(value = "/reports/download", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> download(@RequestParam String file) throws IOException {
        Path target = REPORT_DIR.resolve(file);
        if (!Files.exists(target)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new String(Files.readAllBytes(target)));
    }

    /** SAFE: normalise, then prove the result is still inside the base directory. */
    @GetMapping(value = "/reports/download-safe", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> downloadSafe(@RequestParam String file) throws IOException {
        Path base = REPORT_DIR.toAbsolutePath().normalize();
        Path target = base.resolve(file).normalize();
        if (!target.startsWith(base) || !Files.exists(target)) {
            return ResponseEntity.badRequest().body("rejected");
        }
        return ResponseEntity.ok(new String(Files.readAllBytes(target)));
    }
}
