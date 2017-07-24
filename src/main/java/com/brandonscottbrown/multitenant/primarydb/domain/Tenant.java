package com.brandonscottbrown.multitenant.primarydb.domain;

import javax.persistence.*;

@Entity
public class Tenant {

    private Long id;
    private String name;
    private Boolean defaultTenant;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Boolean separateDatabase;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "default_tenant")
    public Boolean getDefaultTenant() {
        return defaultTenant;
    }

    public void setDefaultTenant(Boolean defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    @Column(name = "driver_class_name")
    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "separate_database")
    public Boolean getSeparateDatabase() {
        return separateDatabase;
    }

    public void setSeparateDatabase(Boolean separateDatabase) {
        this.separateDatabase = separateDatabase;
    }
}
