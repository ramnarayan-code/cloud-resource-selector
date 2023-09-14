package com.cloud.resource.selector.service;

import com.cloud.resource.selector.model.CloudRegionIPInfo;

import java.util.List;

public class AWSCloudIPRangeSelectorService implements CloudIPRangeSelectorService{
    @Override
    public List<CloudRegionIPInfo> getAllRegionIPRanges() {
        return null;
    }

    @Override
    public List<CloudRegionIPInfo> getRegionSpecificIPRanges(String region) {
        return null;
    }
}
