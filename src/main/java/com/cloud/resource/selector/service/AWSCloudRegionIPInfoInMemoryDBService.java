package com.cloud.resource.selector.service;

import com.cloud.resource.selector.config.AWSCloudConfiguration;
import com.cloud.resource.selector.model.CloudRegionIPInfo;
import com.cloud.resource.selector.model.CloudRegionIPList;
import jakarta.annotation.PostConstruct;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class AWSCloudRegionIPInfoInMemoryDBService {

    private static final Logger LOG
            = Logger.getLogger(String.valueOf(AWSCloudRegionIPInfoInMemoryDBService.class));

    @Autowired
    private Environment environment;

    @Autowired
    private AWSCloudConfiguration awsCloudConfiguration;

    @Autowired
    private CloudProviderAPIInvocationService cloudProviderAPIInvocationService;

    private static Map<String, List<CloudRegionIPInfo>> cloudAllRegionIPInfoMap;

    @PostConstruct
    private void init() {
        boolean isTestProfile = Arrays.stream(this.environment.getActiveProfiles()).anyMatch(record -> record.equals("test"));
        if (!isTestProfile) {
            LOG.info("Initial loading of AWS Cloud Region based IP ranges Map started...");
            loadRegionIPRanges();
            LOG.info("Initial loading of AWS Cloud Region based IP ranges Map completed...");
        }
    }

    @Scheduled(fixedDelay = 5, initialDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void scheduledLoadRegionIPRanges() {
        LOG.info("Scheduled loading of AWS Cloud Region based IP ranges Map started...");
        loadRegionIPRanges();
        LOG.info("Scheduled loading of AWS Cloud Region based IP ranges Map completed...");
    }

    public void loadRegionIPRanges() {
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

    public Map<String, List<CloudRegionIPInfo>> getCloudAllRegionIPInfoMap() {
        return cloudAllRegionIPInfoMap;
    }
}
