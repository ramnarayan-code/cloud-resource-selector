package com.cloud.resource.selector.api;

import com.cloud.resource.selector.model.CloudRegionIPInfo;
import com.cloud.resource.selector.service.CloudIPRangeSelectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/aws/")
public class CloudIPRangeSelectorController {

    @Autowired
    @Qualifier("aws-ipranges-selector-service")
    private CloudIPRangeSelectorService cloudIPRangeSelectorService;


    @GetMapping(value = "/ip-ranges", produces = "text/plain")
    public String getRegionBasedIPRanges(@RequestParam String region) {
        List<CloudRegionIPInfo> cloudRegionIPInfoList;
        cloudRegionIPInfoList = region.equalsIgnoreCase("all") ? cloudIPRangeSelectorService.getAllRegionIPRanges() : cloudIPRangeSelectorService.getRegionSpecificIPRanges(region);
        return cloudRegionIPInfoList != null ? cloudRegionIPInfoList.toString() : null;
    }
}
