package org.example;

import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;

public class TodoService {

    private final SqlClient client;

    public TodoService(SqlClient client) {
        this.client = client;
    }


    public Future<List<Todo>> getAll() {
        return client
                .preparedQuery("select * from todo")
                .mapping(row -> new Todo(
                        row.getString("id"),
                        row.getString("task"),
                        row.getBoolean("completed"),
                        row.getLocalDateTime("created_at")
                ))
                .execute()
                .map(todos -> {
                    List<Todo> todoList = new ArrayList<>();
                    for (Todo todo : todos) {
                        todoList.add(todo);
                    }
                    return todoList;
                });
    }

    public Future<?> addTodo(Todo todo) {
        return client.preparedQuery("insert into todo(id, task) values(?, ?)")
                .execute(Tuple.of(todo.getId(), todo.getTask()))
                .compose(ignored -> Future.succeededFuture());
    }

}
