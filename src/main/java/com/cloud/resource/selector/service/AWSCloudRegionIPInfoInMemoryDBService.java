package com.cloud.resource.selector.service;

import com.cloud.resource.selector.config.AWSCloudConfiguration;
import com.cloud.resource.selector.model.CloudRegionIPInfo;
import com.cloud.resource.selector.model.CloudRegionIPList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class AWSCloudRegionIPInfoInMemoryDBService {

    private static final Logger LOG
            = Logger.getLogger(String.valueOf(AWSCloudRegionIPInfoInMemoryDBService.class));

    @Autowired
    private AWSCloudConfiguration awsCloudConfiguration;

    @Autowired
    private CloudProviderAPIInvocationService cloudProviderAPIInvocationService;

    private static Map<String, List<CloudRegionIPInfo>> cloudAllRegionIPInfoMap;

    public void loadRegionIPRanges(boolean refresh) {
        if (cloudAllRegionIPInfoMap == null || refresh) {
            cloudAllRegionIPInfoMap = new HashMap<>();
            awsCloudConfiguration.getValidRegions().forEach(region -> cloudAllRegionIPInfoMap.put(region, new CloudRegionIPList<>()));
            try {
                String response = cloudProviderAPIInvocationService.get(awsCloudConfiguration.getAwsDatasourceURL());
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(response);
                List allIPAddressList = (List) jsonObject.get("prefixes");
                allIPAddressList.forEach(record -> {
                    Map ipAddressRecord = (Map) record;

                    // Take only IPV4 and neglects IPV6
                    if (ipAddressRecord.containsKey("ip_prefix")) {
                        String[] regionTokens = ipAddressRecord.get("region").toString().split("-");
                        String region = (regionTokens != null && regionTokens.length != 0) ? regionTokens[0].toLowerCase() : null;
                        if (region != null) {
                            List<CloudRegionIPInfo> regionIPInfoList = cloudAllRegionIPInfoMap.get(region);
                            if (regionIPInfoList != null) {
                                CloudRegionIPInfo cloudRegionIPInfo = new CloudRegionIPInfo();
                                cloudRegionIPInfo.setRegion(region);
                                cloudRegionIPInfo.setIpPrefix(ipAddressRecord.get("ip_prefix").toString());
                                regionIPInfoList.add(cloudRegionIPInfo);
                            }
                        }
                    }
                });
            } catch (IOException e) {
                LOG.warning("IOException: failed to load the AWS Cloud Region based IP ranges Map ::" + e.getMessage());
            } catch (InterruptedException e) {
                LOG.warning("InterruptedException: failed to load the AWS Cloud Region based IP ranges Map::" + e.getMessage());
            } catch (Exception e) {
                LOG.warning("Exception: failed to initialize the AWS Cloud Region based IP ranges Map::" + e.getMessage());
            }
        }
    }

    public Map<String, List<CloudRegionIPInfo>> getCloudAllRegionIPInfoMap() {
        return cloudAllRegionIPInfoMap;
    }
}
