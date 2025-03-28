package edu.rit.ks4898;

import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
            config.jetty.modifyServer(server -> {
                ServerConnector sslConnector = new ServerConnector(server, getSslContextFactory());
                sslConnector.setPort(7000);
                server.addConnector(sslConnector);
            });
        }).start(7000);

        app.get("/", ctx -> ctx.redirect("index.html"));

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

    private static SslContextFactory.Server getSslContextFactory() {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath("keystore.jks");
        sslContextFactory.setKeyStorePassword("javalinhttps");
        sslContextFactory.setEndpointIdentificationAlgorithm(null);
        sslContextFactory.setSniRequired(false);
        return sslContextFactory;
    }
    
}