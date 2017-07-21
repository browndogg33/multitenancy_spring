package com.brandonscottbrown.multitenant.config;

public class Tenant {

    private String name;
    private Boolean isDefault;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Boolean separateDatabase;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

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

    public Boolean getDefault() {
        return isDefault;
    }

    public Boolean getSeparateDatabase() {
        return separateDatabase;
    }

    public void setSeparateDatabase(Boolean separateDatabase) {
        this.separateDatabase = separateDatabase;
    }
}
