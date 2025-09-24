package cloudflight.integra.backend;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("openapi-export")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OpenApiExportTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void exportOpenApi() throws Exception {
        String spec = restTemplate.getForObject("/v3/api-docs.yaml", String.class);
        Files.write(Paths.get("openapi.yaml"), spec.getBytes(StandardCharsets.UTF_8));
    }
}
