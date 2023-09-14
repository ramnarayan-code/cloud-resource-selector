package com.cloud.resource.selector.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConfigurationValueLoadTest {

    @Value("${awsDatasourceURL}")
    private String awsDatasourceURL;

    @Value("#{'${validRegions}'.split(',')}")
    private List<String> validRegions;

    @Test
    public void testAWSDatasourceURLNotNull() {
        assertNotNull(awsDatasourceURL);
    }

    @Test
    public void testAWSDatasourceURLNotEmptyString() {
        assertNotEquals(awsDatasourceURL, "");
    }

    @Test
    public void testValidRegionsNotNull() {
        assertNotNull(validRegions);
    }

    @Test
    public void testValidRegionsWithValidValues() {
        assertEquals(Arrays.asList("EU", "US", "AP", "CN", "SA", "AF", "CA"), validRegions);
    }
}
