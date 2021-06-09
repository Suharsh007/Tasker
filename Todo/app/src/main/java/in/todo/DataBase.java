package in.todo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Task.class,version =1,exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    public abstract TaskDao dao();
}
