package com.blackducksoftware.core.properties.configuration;

public enum PropertyConfigurationName {
    SPRING("Spring properties"), SYSTEM("Java system properties"), REGISTRATION_METADATA("Registration metadata properties"),
    ZOOKEEPER("ZooKeeper properties"), ZOOKEEPER_NODE_SPECIFIC("ZooKeeper node-specific properties"), ZOOKEEPER_SHARED("ZooKeeper shared properties");

    private final String name;

    private PropertyConfigurationName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
