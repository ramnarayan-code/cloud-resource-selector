package com.cloud.resource.selector.service;

import com.cloud.resource.selector.config.AWSCloudConfiguration;
import com.cloud.resource.selector.exception.RegionNotFoundException;
import com.cloud.resource.selector.model.CloudRegionIPInfo;
import com.cloud.resource.selector.model.CloudRegionIPList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service("aws-ipranges-selector-service")
public class AWSCloudIPRangeSelectorService implements CloudIPRangeSelectorService {

    private static final Logger LOG
            = Logger.getLogger(String.valueOf(AWSCloudIPRangeSelectorService.class));


    @Autowired
    private AWSCloudRegionIPInfoInMemoryDBService awsCloudRegionIPInfoInMemoryDBService;

    @Autowired
    private AWSCloudConfiguration awsCloudConfiguration;

    public AWSCloudIPRangeSelectorService(AWSCloudRegionIPInfoInMemoryDBService awsCloudRegionIPInfoInMemoryDBService, AWSCloudConfiguration awsCloudConfiguration) {
        this.awsCloudRegionIPInfoInMemoryDBService = awsCloudRegionIPInfoInMemoryDBService;
        this.awsCloudConfiguration = awsCloudConfiguration;
    }

    @Override
    public List<CloudRegionIPInfo> getAllRegionIPRanges() {
        List<CloudRegionIPInfo> allCloudRegionIPInfoList = new CloudRegionIPList<>();
        Map<String, List<CloudRegionIPInfo>> cloudAllRegionIPInfoMap = awsCloudRegionIPInfoInMemoryDBService.getCloudAllRegionIPInfoMap();
        awsCloudConfiguration.getValidRegions().forEach(region -> allCloudRegionIPInfoList.addAll(cloudAllRegionIPInfoMap.get(region)));
        return allCloudRegionIPInfoList;
    }

    @Override
    public List<CloudRegionIPInfo> getRegionSpecificIPRanges(String region) throws RegionNotFoundException {
        Map<String, List<CloudRegionIPInfo>> cloudAllRegionIPInfoMap = awsCloudRegionIPInfoInMemoryDBService.getCloudAllRegionIPInfoMap();
        region = region.toLowerCase();
        if (cloudAllRegionIPInfoMap.containsKey(region)) {
            return cloudAllRegionIPInfoMap.get(region);
        } else {
            throw new RegionNotFoundException(region + " does not exist");
        }
    }
}
