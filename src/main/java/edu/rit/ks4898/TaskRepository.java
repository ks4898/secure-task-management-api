package edu.rit.ks4898;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    private static final Map<String, Task> tasks = new HashMap<>();

    public static void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public static Task getTask(String id) {
        return tasks.get(id);
    }

    public static List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public static void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public static void deleteTask(String id) {
        tasks.remove(id);
    }
}

