package org.yudhistiraarya.plugin;

import java.util.function.BiConsumer;

public interface ProcessingBuilder {
    ProcessingBuilder process(BiConsumer<ProcessingContext, ProcessingResult> processing);
    Processing build();
}
