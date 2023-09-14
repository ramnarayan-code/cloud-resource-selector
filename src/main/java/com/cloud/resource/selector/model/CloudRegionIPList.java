package com.cloud.resource.selector.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CloudRegionIPList<T> extends ArrayList<CloudRegionIPInfo> {

    @Override
    public String toString() {
        List<String> ipAddressList = new ArrayList<>();
        this.forEach(cloudRegionIPInfo -> ipAddressList.add(cloudRegionIPInfo.getIpPrefix()));
        return ipAddressList.stream().collect(Collectors.joining(", "));
    }
}
