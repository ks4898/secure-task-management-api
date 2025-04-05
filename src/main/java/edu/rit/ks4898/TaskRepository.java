package edu.rit.ks4898;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRepository {
    private static final String STORAGE_FILE = "tasks.dat";
    private static final Map<String, Task> tasks = loadTasks();

    private static Map<String, Task> loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            @SuppressWarnings("unchecked")
            Map<String, Task> loadedTasks = (ConcurrentHashMap<String, Task>) ois.readObject();
            return new ConcurrentHashMap<>(loadedTasks);
        } catch (FileNotFoundException e) {
            return new ConcurrentHashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load tasks from storage", e);
        }
    }

    private static synchronized void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks to storage", e);
        }
    }

    public static List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public static Task getTask(String id) {
        return tasks.get(id);
    }

    public static void addTask(Task task) {
        tasks.put(task.getId(), task);
        saveTasks();
    }

    public static void updateTask(Task task) {
        tasks.put(task.getId(), task);
        saveTasks();
    }

    public static void deleteTask(String id) {
        tasks.remove(id);
        saveTasks();
    }
}