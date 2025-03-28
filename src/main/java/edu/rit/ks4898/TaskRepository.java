package edu.rit.ks4898;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskRepository {
    private static final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public static void addTask(Task task) {
        String id = String.valueOf(idCounter.incrementAndGet());
        task.setId(id);
        tasks.put(id, task);
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

