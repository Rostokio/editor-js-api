package com.example.demo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static Properties config;

    public static Properties getConfig() {
        if (config == null) {
            config = new Properties();
            try {
                   config.load(new FileInputStream("/Users/u17491378/IdeaProjects/test-demo/src/main/resources/config.properties"));
               } catch (IOException ioe) {
                   ioe.printStackTrace();
            }

        }
        return config;
    }

}
