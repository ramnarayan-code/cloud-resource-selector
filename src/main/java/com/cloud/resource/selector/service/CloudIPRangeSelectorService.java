package com.cloud.resource.selector.service;

import com.cloud.resource.selector.exception.RegionNotFoundException;
import com.cloud.resource.selector.model.CloudRegionIPInfo;

import java.util.List;

/*
 Interface for cloud provider based sub-classes to implement the retrieval of IP Ranges
 */
public interface CloudIPRangeSelectorService {
    List<CloudRegionIPInfo> getAllRegionIPRanges();

    List<CloudRegionIPInfo> getRegionSpecificIPRanges(String region) throws RegionNotFoundException;
}
