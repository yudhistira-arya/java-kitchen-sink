package org.yudhistiraarya.proxy.dynamicloading;

import java.io.PrintStream;

public class PostmanImpl implements Postman {
    private PrintStream output;

    public PostmanImpl() {
        this.output = System.out;
    }

    @Override
    public void deliverMessage(String msg) {
        output.println("[PosLaju] " + msg);
        output.flush();
    }
}
