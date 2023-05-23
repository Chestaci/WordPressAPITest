package com.github.Chestaci.util;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Утилитный класс для работы с файлом настроек.
 */
public class ConfProperties {

    protected static FileInputStream fileInputStream;
    protected static Properties PROPERTIES;

    /*
     Инициализация утилитного класса
    */
    static {
        try {
            FileReader fileReader = new FileReader("src/test/resources/conf.properties");
            //указание пути до файла с настройками
            PROPERTIES = new Properties();
            PROPERTIES.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
            //обработка возможного исключения (нет файла и т.п.)
        } finally {
            if (fileInputStream != null) try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод для возврата строки со значением из файла с настройками
     *
     * @param key ключ параметра из файла с настройками
     * @return возвращает значение ключа
     */
    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}
