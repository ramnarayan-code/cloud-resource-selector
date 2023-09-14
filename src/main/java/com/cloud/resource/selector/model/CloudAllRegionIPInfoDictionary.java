package com.cloud.resource.selector.model;

import java.util.List;
import java.util.Map;

/**
 * CloudAllRegionIPInfoDictionary contains a map of Region based IP List
 *
 */
public class CloudAllRegionIPInfoDictionary {
    private Map<String, List<CloudRegionIPInfo>> cloudAllRegionIPInfoMap;

    public Map<String, List<CloudRegionIPInfo>> getCloudAllRegionIPInfoMap() {
        return cloudAllRegionIPInfoMap;
    }

    public void setCloudAllRegionIPInfoMap(Map<String, List<CloudRegionIPInfo>> cloudAllRegionIPInfoMap) {
        this.cloudAllRegionIPInfoMap = cloudAllRegionIPInfoMap;
    }
}
