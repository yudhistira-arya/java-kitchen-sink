package org.yudhistiraarya.proxy.dynamicloading;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class PostmanApp {

    public static void main(String[] args) throws Exception {
        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));

        Postman postman = getPostman();

        while (true) {
            System.out.print("Enter a message: ");
            String msg = sysin.readLine();

            postman.deliverMessage(msg);
        }
    }

    private static Postman getPostman() {
        DynaCode dynacode = new DynaCode();
        dynacode.addSourceDir(new File("proxy/src/main/java"));
        return (Postman) dynacode.newProxyInstance(Postman.class,
                "org.yudhistiraarya.proxy.dynamicloading.PostmanImpl");
    }
}
