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

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    Context context;
    ArrayList<Task> task_list;

    public DayAdapter(Context context, ArrayList<Task> task_list) {
        this.context = context;
        this.task_list = task_list;
    }

    @NonNull
    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DayAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.today_task_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DayAdapter.ViewHolder holder, int position) {
        holder.tvTask.setText(task_list.get(position).getTodo_task());
        holder.tvTime.setText(task_list.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return task_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTask, tvTime;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTask = itemView.findViewById(R.id.tvTask);
            tvTime = itemView.findViewById(R.id.tvTime);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
