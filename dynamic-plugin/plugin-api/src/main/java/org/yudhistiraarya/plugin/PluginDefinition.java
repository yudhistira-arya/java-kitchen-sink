package org.yudhistiraarya.plugin;

public interface PluginDefinition {
    Configuration configure(ConfigurationBuilder configurationBuilder);
    Processing process(ProcessingBuilder context);
}
