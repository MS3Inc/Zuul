package org.devnull.zuul.web

import org.devnull.zuul.web.test.ZuulWebIntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.mock.web.MockHttpServletResponse

public class SettingsControllerIntegrationTest extends ZuulWebIntegrationTest {

    @Autowired
    SettingsController controller

    @Test
    void listJsonShouldReturnValidResults() {
        def environments = ["dev", "qa", "prod"]
        def results = controller.listJson()
        assert results.size() == environments.size()
        environments.each { env ->
            def group = results.find { it.environment.name == env }
            loadTestProperties("/test-app-data-config-${env}.properties") == group as Properties
        }
    }


    @Test
    void renderPropertiesByNameAndEnvShouldReturnValidResults() {
        def response = new MockHttpServletResponse()
        controller.renderPropertiesByNameAndEnv(response, "app-data-config", "dev")
        def properties = new Properties()
        properties.load(new StringReader(response.contentAsString))
        assert properties == loadTestProperties("/test-app-data-config-dev.properties")
    }

    Properties loadTestProperties(String path) {
        def resource = new ClassPathResource(path)
        def properties = new Properties()
        properties.load(resource.inputStream)
        return properties
    }
}
