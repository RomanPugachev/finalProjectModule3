package org.example.finalprojectmodule3.service;

import lombok.Getter;

import java.io.*;
import java.util.Properties;
@Getter
public class PropertyService {
    private Properties properties;
    public static final String PATH_TO_FILE = "resources/config.properties";

    public PropertyService(){
        this.properties = loadProperties();
    }

    public Properties loadProperties(){
        Properties properties = new Properties();
        try(InputStream is = PropertyService.class.getResourceAsStream("/config.properties")){
            properties.load(is);
        } catch (IOException e) {
            System.out.println("Не удалось считать файл configuration.properties");
            return null;
        }
        return properties;
    }
}
