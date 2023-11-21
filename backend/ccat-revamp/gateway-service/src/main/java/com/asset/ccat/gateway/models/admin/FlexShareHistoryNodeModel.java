package com.asset.ccat.gateway.models.admin;

public class FlexShareHistoryNodeModel {
    private Integer id;
    private String address;
    private Integer port;
    private String username;
    private String password;
    private Integer numberOfSessions;
    private Integer concurrentCalls;
    private Integer connectionTimeout;
    private String extraConf;
    private String schema;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public Integer getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(Integer numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public Integer getConcurrentCalls() {
        return concurrentCalls;
    }

    public void setConcurrentCalls(Integer concurrentCalls) {
        this.concurrentCalls = concurrentCalls;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getExtraConf() {
        return extraConf;
    }

    public void setExtraConf(String extraConf) {
        this.extraConf = extraConf;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
