package com.cloud.resource.selector.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AWSCloudConfiguration {
    @Value("${awsDatasourceURL}")
    private String awsDatasourceURL;
    @Value("#{'${validRegions}'.split(',')}")
    private List<String> validRegions;

    @PostConstruct
    public void init() {
        setValidRegions(validRegions);
    }

    public String getAwsDatasourceURL() {
        return awsDatasourceURL;
    }

    public void setAwsDatasourceURL(String awsDatasourceURL) {
        this.awsDatasourceURL = awsDatasourceURL;
    }

    public List<String> getValidRegions() {
        return validRegions;
    }

    public void setValidRegions(List<String> validRegions) {
        List<String> lowerCaseRegionList = new ArrayList<>();
        validRegions.forEach(region -> lowerCaseRegionList.add(region.toLowerCase()));
        this.validRegions = lowerCaseRegionList;
    }
}
