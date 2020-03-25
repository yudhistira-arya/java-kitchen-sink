package org.yudhistiraarya.newfeatures;

import java.util.ArrayList;
import java.util.HashMap;

class LocalVariableInference {

    static void main(String[] args) {
     // String fruit = "Banana";
        var fruit = "Banana";

     // For HashMap declaration, you literally just saving 1 character, lol.
     // was:
     // Map<Integer,String> map = new HashMap<>();
        var map = new HashMap<Integer, String>();

        // var cannot be initialized to null
        // var z = null;

        // beware this turn into ArrayList<Object>
        var empList = new ArrayList<>();
    }
}
