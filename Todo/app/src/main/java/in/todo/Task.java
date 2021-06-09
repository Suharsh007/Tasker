package in.todo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Task {
    @NonNull
   @PrimaryKey
   @ColumnInfo(name = "Task")
   String todo_task;
   @ColumnInfo(name = "Date")
   String date;
   @ColumnInfo(name = "Time")
   String time;

    public Task() {
    }

    public Task( String todo_task, String date, String time) {
        this.todo_task = todo_task;
        this.date = date;
        this.time = time;
    }


    public String getTodo_task() {
        return todo_task;
    }

    public void setTodo_task( String todo_task) {
        this.todo_task = todo_task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
