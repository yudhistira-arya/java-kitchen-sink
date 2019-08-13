package org.yudhistiraarya.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SamplePlugin implements PluginDefinition {
    private static final Logger LOG = LoggerFactory.getLogger(SamplePlugin.class);

    @Override
    public Configuration configure(final ConfigurationBuilder configurationBuilder) {
        return configurationBuilder
                .withName("Sample Plugin")
                .withVersion("0.0.1")
                .build();
    }

    @Override
    public Processing process(final ProcessingBuilder processingBuilder) {
        return processingBuilder
                .process((context, result) -> {
                    Configuration configuration = context.getConfiguration();
                    final String name = configuration.getName();
                    LOG.info("From plugin: {}", name);
                    result.onComplete();
                })
                .build();
    }
}
