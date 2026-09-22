package com.example.ghasdemo.web;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Product lookup. Demonstrates java/sql-injection.
 */
@RestController
public class ProductController {

    private final JdbcTemplate jdbc;

    public ProductController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** VULNERABLE: request parameter concatenated straight into SQL. */
    @GetMapping("/products/search")
    public List<Map<String, Object>> search(@RequestParam String name) {
        String sql = "SELECT id, name, price FROM products WHERE name LIKE '%" + name + "%'";
        return jdbc.queryForList(sql);
    }

    /** SAFE: same feature, parameterised. Used to compare the two in the alert view. */
    @GetMapping("/products/search-safe")
    public List<Map<String, Object>> searchSafe(@RequestParam String name) {
        return jdbc.queryForList(
                "SELECT id, name, price FROM products WHERE name LIKE ?", "%" + name + "%");
    }

    /**
     * Sorting by a column name cannot be parameterised, so it is allow-listed instead.
     * CodeQL may still flag this: it is the false positive we will triage.
     */
    @GetMapping("/products")
    public List<Map<String, Object>> list(@RequestParam(defaultValue = "id") String sort) {
        String column = "id";
        if ("name".equals(sort) || "price".equals(sort)) {
            column = sort;
        }
        return jdbc.queryForList("SELECT id, name, price FROM products ORDER BY " + column);
    }
}
