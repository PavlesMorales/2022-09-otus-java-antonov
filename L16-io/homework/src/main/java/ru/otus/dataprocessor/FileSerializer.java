package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл

        try (var fileWriter = new FileWriter(fileName)) {
            var json = new Gson().toJson(data);
            fileWriter.write(json);
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}
