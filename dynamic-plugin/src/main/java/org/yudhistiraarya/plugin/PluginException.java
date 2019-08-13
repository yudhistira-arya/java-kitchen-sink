package org.yudhistiraarya.plugin;

public class PluginException extends RuntimeException {
    public PluginException() {
    }

    public PluginException(final String message) {
        super(message);
    }

    public PluginException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PluginException(final Throwable cause) {
        super(cause);
    }

    public PluginException(final String message, final Throwable cause, final boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
