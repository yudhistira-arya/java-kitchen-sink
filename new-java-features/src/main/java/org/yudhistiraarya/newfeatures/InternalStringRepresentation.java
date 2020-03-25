package org.yudhistiraarya.newfeatures;

import java.nio.charset.StandardCharsets;

class InternalStringRepresentation {
    public static void main(String[] args) {
        // in Java 9 string representation will be byte array + encoding field.
        // encoding field will indicate whether ISO-8859-1/Latin-1 or UTF-16 to be used based on the content of the
        // String.

        var def = "banana".getBytes();
        var iso8859 = "banana".getBytes(StandardCharsets.ISO_8859_1); // 1 byte per char
        var utf16 = "banana".getBytes(StandardCharsets.UTF_16); // 2 byte per char plus bom
    }
}
