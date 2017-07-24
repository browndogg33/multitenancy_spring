package com.brandonscottbrown.multitenant.tenantdb.config;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;
import java.util.Map;

public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private String defaultTenant;

    private Map<String, DataSource> map;

    public DataSourceBasedMultiTenantConnectionProviderImpl(String defaultTenant, Map<String, DataSource> map) {
        super();
        this.defaultTenant = defaultTenant;
        this.map = map;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(defaultTenant);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return map.containsKey(tenantIdentifier) ? map.get(tenantIdentifier) : getDefaultDataSource();
    }

    public DataSource getDefaultDataSource() {
        return map.get(defaultTenant);
    }

    
}
