package in.todo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void taskInsertion(Task task);

    @Query("Select * from Task ORDER BY date,time ASC")
    List<Task> getTask();

   @Query("Select * from Task WHERE date IN (:d)")
    List<Task> getTodayTask(String d);

}
