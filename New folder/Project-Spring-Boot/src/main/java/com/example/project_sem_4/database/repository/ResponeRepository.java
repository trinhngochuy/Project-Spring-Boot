package com.example.project_sem_4.database.repository;

import java.util.HashMap;
import java.util.Map;

public class ResponeRepository {
    public static Map<String, String> ResponeJsonError(String message) {
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "0");
        map.put("message", message);
        map.put("data", "");
        return map;
    }
    public static Map<String, String> ResponeJsonSusscess(String message, String data) {
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "1");
        map.put("message", message);
        map.put("data", data);
        return map;
    }
}
