package com.cloud.resource.selector.service;

import com.cloud.resource.selector.exception.RegionNotFoundException;
import com.cloud.resource.selector.model.CloudRegionIPInfo;
import com.cloud.resource.selector.model.CloudRegionIPList;
import jakarta.annotation.PostConstruct;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component("aws-ipranges-selector-service")
public class AWSCloudIPRangeSelectorService implements CloudIPRangeSelectorService {

    private static final Logger LOG
            = Logger.getLogger(String.valueOf(AWSCloudIPRangeSelectorService.class));

    private static Map<String, List<CloudRegionIPInfo>> cloudAllRegionIPInfoMap;

    @Autowired
    private CloudProviderAPIInvocationService cloudProviderAPIInvocationService;

    @Value("${awsDatasourceURL}")
    private String awsDatasourceURL;
    @Value("#{'${validRegions}'.split(',')}")
    private List<String> validRegions;


    @PostConstruct
    private void init() {
        List<String> lowerCaseRegionList = new ArrayList<>();
        validRegions.forEach(region -> lowerCaseRegionList.add(region.toLowerCase()));
        validRegions = lowerCaseRegionList;
        LOG.info("Initial loading of AWS Cloud Region based IP ranges Map started...");
        loadRegionIPRanges(false);
        LOG.info("Initial loading of AWS Cloud Region based IP ranges Map completed...");
    }

    private void loadRegionIPRanges(boolean refresh) {
        if (cloudAllRegionIPInfoMap == null || refresh) {
            cloudAllRegionIPInfoMap = new HashMap<>();
            validRegions.forEach(region -> cloudAllRegionIPInfoMap.put(region, new CloudRegionIPList<>()));
            try {
                String response = cloudProviderAPIInvocationService.get(awsDatasourceURL);
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
//                validRegions.forEach(region -> LOG.info(region.toLowerCase() + " size:"+ cloudAllRegionIPInfoMap.get(region.toLowerCase()).size()));
            } catch (IOException e) {
                LOG.warning("IOException: failed to load the AWS Cloud Region based IP ranges Map ::" + e.getMessage());
            } catch (InterruptedException e) {
                LOG.warning("InterruptedException: failed to load the AWS Cloud Region based IP ranges Map::" + e.getMessage());
            } catch (Exception e) {
                LOG.warning("Exception: failed to initialize the AWS Cloud Region based IP ranges Map::" + e.getMessage());
            }
        }
    }

    @Override
    public List<CloudRegionIPInfo> getAllRegionIPRanges() {
        List<CloudRegionIPInfo> allCloudRegionIPInfoList = new ArrayList<>();
        validRegions.forEach(region -> allCloudRegionIPInfoList.addAll(cloudAllRegionIPInfoMap.get(region)));
        return allCloudRegionIPInfoList;
    }

    @Override
    public List<CloudRegionIPInfo> getRegionSpecificIPRanges(String region) throws RegionNotFoundException {
        if (cloudAllRegionIPInfoMap.containsKey(region)) {
            return cloudAllRegionIPInfoMap.get(region);
        } else {
            throw new RegionNotFoundException(region + " does not exist");
        }
    }
}
