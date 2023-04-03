package com.example.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static boolean isLoaded = false;
    private final static Properties properties = new Properties();
    private PropertyUtil()  {
    }
    public static String read(String property){
        if (!isLoaded){
            try {
                FileInputStream fileInputStream = new FileInputStream("src/main/resources/com/example/examples/application.properties");
                properties.load(fileInputStream);
                isLoaded = true;
            } catch (IOException ignored) {

            }

        }
        return properties.getProperty(property);
    }
}
