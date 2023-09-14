package com.cloud.resource.selector.service;

import com.cloud.resource.selector.model.CloudRegionIPInfo;

import java.util.List;

public interface CloudIPRangeSelectorService {
    List<CloudRegionIPInfo> getAllRegionIPRanges();
    List<CloudRegionIPInfo> getRegionSpecificIPRanges(String region);
}
