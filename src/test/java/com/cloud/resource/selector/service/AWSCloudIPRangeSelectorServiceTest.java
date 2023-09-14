package com.cloud.resource.selector.service;

import com.cloud.resource.selector.config.AWSCloudConfiguration;
import com.cloud.resource.selector.model.CloudRegionIPInfo;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AWSCloudIPRangeSelectorServiceTest {

    @Value("${awsDatasourceURL}")
    private String awsDatasourceURL;

    @Mock
    AWSCloudConfiguration awsCloudConfiguration;

//    @Mock
//    AWSCloudRegionIPInfoInMemoryDBService awsCloudRegionIPInfoInMemoryDBService;

    @Mock
    CloudProviderAPIInvocationService cloudProviderAPIInvocationService;

    @InjectMocks
    AWSCloudRegionIPInfoInMemoryDBService awsCloudRegionIPInfoInMemoryDBService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRegionIPRanges() throws IOException, InterruptedException {
        when(cloudProviderAPIInvocationService.get(awsDatasourceURL)).thenReturn("""
                {
                  "syncToken": "1694724189",
                  "createDate": "2023-09-14-20-43-09",
                  "prefixes": [
                    {
                      "ip_prefix": "3.2.34.0/26",
                      "region": "af-south-1",
                      "service": "AMAZON",
                      "network_border_group": "af-south-1"
                    },
                    {
                      "ip_prefix": "3.5.140.0/22",
                      "region": "ap-northeast-2",
                      "service": "AMAZON",
                      "network_border_group": "ap-northeast-2"
                    },
                    {
                      "ip_prefix": "13.34.37.64/27",
                      "region": "ap-southeast-4",
                      "service": "AMAZON",
                      "network_border_group": "ap-southeast-4"
                    },
                    {
                      "ip_prefix": "13.34.65.64/27",
                      "region": "il-central-1",
                      "service": "AMAZON",
                      "network_border_group": "il-central-1"
                    }]
                    }
                """);


        System.out.println(awsCloudRegionIPInfoInMemoryDBService.getCloudAllRegionIPInfoMap());
    }

    @Test
    void getRegionSpecificIPRanges() {
    }
}