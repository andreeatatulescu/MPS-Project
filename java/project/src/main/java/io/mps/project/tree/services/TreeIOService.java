package io.mps.project.tree.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Optional;

public class TreeIOService {
    private static final String DIRECTORY = "src/main/resources/tree/";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> void save(T tree, String fileName) {
        try {
            objectMapper.writeValue(new File(DIRECTORY + fileName), tree);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Optional<T> find(String fileName, Class<T> clazz) {
        File file = new File(DIRECTORY + fileName);
        if (!file.exists()) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(file, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TreeIOService() {}

    private static final TreeIOService INSTANCE = new TreeIOService();

    public static TreeIOService getInstance() {
        return INSTANCE;
    }
}
