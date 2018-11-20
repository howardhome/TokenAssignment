package com.ctoboost.cassandra.whoop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("configuration")
public class AppConfiguration {

    private int maxThread = 10;

    private int numVNode = 10;

    private int maxKeys = 1000000; // 1M keys

    private int maxUsers = 10000;

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getMaxKeys() {
        return maxKeys;
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
