package in.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<Task> task_list;


    public RecyclerAdapter(Context cb, ArrayList<Task> t) {
        context = cb;
        task_list = t;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTask.setText(task_list.get(position).getTodo_task());
        holder.tvDate.setText(task_list.get(position).getDate());
        holder.tvTime.setText(task_list.get(position).getTime());

    }


    @Override
    public int getItemCount() {
        return task_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTask, tvDate, tvTime;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTask = itemView.findViewById(R.id.tvTask);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
