# spring-ghas-lab

> ⚠️ **Vulnerable by design. Do not deploy, do not reuse this code.**
> This is a training sandbox for GitHub Advanced Security (code scanning with
> CodeQL, Dependabot, secret scanning). Every vulnerability here is deliberate
> and documented. The "secrets" in the source are fake.

A small Spring Boot 2.7 REST service (product catalogue plus some operator
endpoints) used to practise the security champion workflow: triaging code
scanning alerts, reading CodeQL data flow paths, writing custom QL queries, and
setting up the CI/CD policies around them.

## Build and run

Requires JDK 11 and Maven.

```
mvn clean package
java -jar target/ghas-demo-0.0.1-SNAPSHOT.jar
```

## The planted vulnerabilities

| Endpoint | Issue | CodeQL query | CWE |
|---|---|---|---|
| `GET /products/search` | SQL injection | `java/sql-injection` | CWE-89 |
| `GET /products` | allow-listed `ORDER BY` — expected false positive | `java/sql-injection` | CWE-89 |
| `GET /reports/download` | Path traversal | `java/path-injection` | CWE-22 |
| `GET /greet` | Reflected XSS | `java/xss` | CWE-79 |
| `GET /template` | Text4Shell (commons-text 1.9) | CVE-2022-42889 | CWE-94 |
| `POST /admin/ping` | Command injection | `java/command-line-injection` | CWE-78 |
| `GET /admin/fetch` | SSRF | `java/ssrf` | CWE-918 |
| `UserService#hashPassword` | MD5 as a password hash | `java/weak-cryptographic-algorithm` | CWE-327 |
| `UserService#newResetToken` | Predictable token seed | `java/predictable-seed` | CWE-330 |

Each vulnerable endpoint has a `-safe` counterpart where one exists, so the two
can be compared side by side in the alert view.

Outdated dependencies are pinned on purpose in `pom.xml` so Dependabot has
something to report.
