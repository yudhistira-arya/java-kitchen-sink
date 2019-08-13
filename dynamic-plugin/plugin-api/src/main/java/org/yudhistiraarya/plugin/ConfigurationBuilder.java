package org.yudhistiraarya.plugin;

public interface ConfigurationBuilder {
    ConfigurationBuilder withName(String pluginName);
    ConfigurationBuilder withVersion(String pluginSemanticVersion);
    Configuration build();
}
