package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try (var resourceAsStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (resourceAsStream == null) {
                System.err.println("File not found");
                throw new FileProcessException("File not found");
            }
            byte[] bytes = resourceAsStream.readAllBytes();
            if (bytes.length == 0) {
                System.err.println("Empty file");
                throw new FileProcessException("Empty file");
            }
            return new Gson().fromJson(new String(bytes), new TypeToken<List<Measurement>>() {
            }.getType());
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}
