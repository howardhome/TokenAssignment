package com.ctoboost.cassandra.whoop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("configuration")
public class AppConfiguration {

    private int maxThread = 10;

    private int numVNode = 80;

    private int maxDays = 60; // 60 days

    private int maxUsers = 10000;

    private int startUser = 1;

    public void setStartUser(int startUser) {
        this.startUser = startUser;
    }

    public int getStartUser() {
        return startUser;
    }

    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getMaxDays() {
        return maxDays;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    public void setNumVNode(int numVNode) {
        this.numVNode = numVNode;
    }

    public int getNumVNode() {
        return numVNode;
    }
}
