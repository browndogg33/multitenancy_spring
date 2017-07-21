package com.brandonscottbrown.multitenant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "multitenancy")
public class MultitenancyConfigurationProperties {
    
    private Tenant defaultTenant;

    private List<Tenant> tenants = new ArrayList<Tenant>();
    
    @PostConstruct
    public void init() {
        List<Tenant> tcs = tenants.stream().filter(Tenant::isDefault).collect(Collectors.toCollection(ArrayList::new));
        if (tcs.size() > 1) {
            throw new IllegalStateException("Only can be configured as default one data source. Review your configuration");
        }
        this.defaultTenant = tcs.get(0);
    }
       
    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    public Tenant getDefaultTenant() {
        return defaultTenant;
    }

}
