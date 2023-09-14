package com.cloud.resource.selector.model;

/**
 * CloudRegionIPInfo class contains a Region and an IP hosted from that region
 */
public class CloudRegionIPInfo {

    private String region;
    private String ipPrefix;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIpPrefix() {
        return ipPrefix;
    }

    public void setIpPrefix(String ipPrefix) {
        this.ipPrefix = ipPrefix;
    }
}
