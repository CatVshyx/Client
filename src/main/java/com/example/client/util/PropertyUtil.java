package com.example.client.util;

import java.util.HashMap;
import java.util.Map;

public class PropertyUtil {
    private static HashMap<String,String> map = new HashMap<>(Map.of(
            "apple.link", "https://www.youtube.com/watch?v=C9h6M7ELSCE",
            "server.link","https://test-back-ncm8.onrender.com/"
    ));
    private PropertyUtil()  {
    }
    public static String read(String property){

        return map.get(property);
    }
}
