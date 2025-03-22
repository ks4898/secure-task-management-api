package edu.rit.ks4898;

import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.NotFoundResponse;

public class Main {
    public static void main(String[] args) {
        SslPlugin sslPlugin = new SslPlugin(ssl -> {
            ssl.keystoreFromPath("keystore.p12", "your_keystore_password");
        });

        Javalin app = Javalin.create(config -> {
            config.plugins.register(sslPlugin);
        }).start(7000);

        // Your existing route definitions here
        app.get("/tasks", ctx -> ctx.json(TaskRepository.getAllTasks()));

        app.get("/tasks/{id}", ctx -> {
            Task task = TaskRepository.getTask(ctx.pathParam("id"));
            if (task != null) ctx.json(task);
            else throw new NotFoundResponse("Task not found");
        });

        app.post("/tasks", ctx -> {
            Task task = ctx.bodyAsClass(Task.class);
            TaskRepository.addTask(task);
            ctx.status(201).json(task);
        });

        app.put("/tasks/{id}", ctx -> {
            String taskId = ctx.pathParam("id");
            Task updatedTask = ctx.bodyAsClass(Task.class);
            if (!taskId.equals(updatedTask.getId())) throw new BadRequestResponse("Task ID mismatch");
            TaskRepository.updateTask(updatedTask);
            ctx.json(updatedTask);
        });

        app.delete("/tasks/{id}", ctx -> {
            TaskRepository.deleteTask(ctx.pathParam("id"));
            ctx.status(204);
        });
    }
}