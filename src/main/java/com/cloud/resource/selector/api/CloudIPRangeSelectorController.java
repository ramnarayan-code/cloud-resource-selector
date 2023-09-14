package com.cloud.resource.selector.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cloud")
public class CloudIPRangeSelectorController {

    @GetMapping(value = "/ip-ranges")
    public String getRegionBasedIPRanges(@RequestParam String region) {
        return "ip range list for "+ region;
    }

    @GetMapping("/hello")
    public String testRest() {
        return "hello test";
    }
}
