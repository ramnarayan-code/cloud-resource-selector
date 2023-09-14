package com.cloud.resource.selector.service;

import com.cloud.resource.selector.config.AWSCloudConfiguration;
import com.cloud.resource.selector.exception.RegionNotFoundException;
import com.cloud.resource.selector.model.CloudRegionIPInfo;
import jakarta.annotation.PostConstruct;
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


    @PostConstruct
    private void init() {
        LOG.info("Initial loading of AWS Cloud Region based IP ranges Map started...");
        awsCloudRegionIPInfoInMemoryDBService.loadRegionIPRanges(false);
        LOG.info("Initial loading of AWS Cloud Region based IP ranges Map completed...");
    }


    @Override
    public List<CloudRegionIPInfo> getAllRegionIPRanges() {
        List<CloudRegionIPInfo> allCloudRegionIPInfoList = new ArrayList<>();
        Map<String, List<CloudRegionIPInfo>> cloudAllRegionIPInfoMap = awsCloudRegionIPInfoInMemoryDBService.getCloudAllRegionIPInfoMap();
        awsCloudConfiguration.getValidRegions().forEach(region -> allCloudRegionIPInfoList.addAll(cloudAllRegionIPInfoMap.get(region)));
        return allCloudRegionIPInfoList;
    }

    @Override
    public List<CloudRegionIPInfo> getRegionSpecificIPRanges(String region) throws RegionNotFoundException {
        Map<String, List<CloudRegionIPInfo>> cloudAllRegionIPInfoMap = awsCloudRegionIPInfoInMemoryDBService.getCloudAllRegionIPInfoMap();
        if (cloudAllRegionIPInfoMap.containsKey(region)) {
            return cloudAllRegionIPInfoMap.get(region);
        } else {
            throw new RegionNotFoundException(region + " does not exist");
        }
    }
}
