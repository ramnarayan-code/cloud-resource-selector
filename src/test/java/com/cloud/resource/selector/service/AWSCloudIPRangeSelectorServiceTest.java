package com.cloud.resource.selector.service;

import com.cloud.resource.selector.config.AWSCloudConfiguration;
import com.cloud.resource.selector.exception.RegionNotFoundException;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class AWSCloudIPRangeSelectorServiceTest {

    private static final Logger LOG
            = Logger.getLogger(String.valueOf(AWSCloudIPRangeSelectorServiceTest.class));
    @Value("${awsDatasourceURL}")
    private String awsDatasourceURL;

    @Mock
    private AWSCloudConfiguration awsCloudConfiguration;

    @Mock
    private CloudProviderAPIInvocationService cloudProviderAPIInvocationService;

    @InjectMocks
    private AWSCloudRegionIPInfoInMemoryDBService awsCloudRegionIPInfoInMemoryDBService;

    private AWSCloudIPRangeSelectorService awsCloudIPRangeSelectorService;

    @PostConstruct
    private void initLoad() {
        LOG.info("Initial loading of the test");

    }

    @BeforeEach
    public void init() throws IOException, InterruptedException {

        MockitoAnnotations.openMocks(this);

        // Set the Region based IP Range json
        when(cloudProviderAPIInvocationService.get(awsCloudConfiguration.getAwsDatasourceURL())).thenReturn("""
                {
                  "syncToken": "1694724189",
                  "createDate": "2023-09-14-20-43-09",
                  "prefixes": [
                    {
                      "ip_prefix": "3.2.34.0/26",
                      "region": "eu-south-1",
                      "service": "AMAZON",
                      "network_border_group": "eu-south-1"
                    },
                    {
                      "ip_prefix": "3.5.140.0/22",
                      "region": "us-northeast-2",
                      "service": "AMAZON",
                      "network_border_group": "us-northeast-2"
                    },
                    {
                      "ip_prefix": "13.34.37.64/27",
                      "region": "cn-southeast-4",
                      "service": "AMAZON",
                      "network_border_group": "cn-southeast-4"
                    },
                    {
                      "ip_prefix": "13.34.37.66/27",
                      "region": "ap-southeast-4",
                      "service": "AMAZON",
                      "network_border_group": "ap-southeast-4"
                    },
                    {
                      "ip_prefix": "13.34.65.64/27",
                      "region": "sa-central-1",
                      "service": "AMAZON",
                      "network_border_group": "sa-central-1"
                    },
                    {
                      "ip_prefix": "13.34.65.66/27",
                      "region": "af-central-1",
                      "service": "AMAZON",
                      "network_border_group": "af-central-1"
                    },
                    {
                      "ip_prefix": "13.34.65.68/27",
                      "region": "ca-central-1",
                      "service": "AMAZON",
                      "network_border_group": "ca-central-1"
                    },
                    {
                      "ip_prefix": "13.34.65.70/27",
                      "region": "GLOBAL",
                      "service": "AMAZON",
                      "network_border_group": "GLOBAL"
                    }]
                    }
                """);
        when(awsCloudConfiguration.getValidRegions()).thenReturn(List.of("eu", "us", "ap", "cn", "sa", "af", "ca", "global"));
        awsCloudRegionIPInfoInMemoryDBService.loadRegionIPRanges();
        awsCloudIPRangeSelectorService = new AWSCloudIPRangeSelectorService(awsCloudRegionIPInfoInMemoryDBService, awsCloudConfiguration);
    }


    @Test
    void testGetAllRegionIPRangesNotNull() {
        assertNotNull(awsCloudIPRangeSelectorService.getAllRegionIPRanges());
    }

    @Test
    void testGetAllRegionIPRangesWithObjects() {
        assertEquals(awsCloudIPRangeSelectorService.getAllRegionIPRanges().size(), 8);
    }

    @Test
    void getRegionSpecificIPRangesForEU() {
        assertEquals(awsCloudIPRangeSelectorService.getRegionSpecificIPRanges("eu").size(), 1);
    }

    @Test
    void getRegionSpecificIPRangesForGlobal() {
        assertEquals(awsCloudIPRangeSelectorService.getRegionSpecificIPRanges("global").size(), 1);
    }

    @Test
    void getRegionSpecificIPRangesRegionNotFound() {
        assertThrowsExactly(RegionNotFoundException.class, () -> awsCloudIPRangeSelectorService.getRegionSpecificIPRanges("region-not-exist"));
    }
}