package com.example.todolist.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.todolist.Model.Task;
import com.example.todolist.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    Context context;
    List<Task> taskList;

    public RecyclerAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.task_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        holder.taskImage.setImageResource(taskList.get(position).getTaskImage());
        holder.taskName.setText(taskList.get(position).getTaskName());
        holder.taskDesc.setText(taskList.get(position).getTaskDescription());
        holder.taskDate.setText(taskList.get(position).getTaskDate());

        // Completion status for task
        if (taskList.get(position).getIsComplete()) {
            holder.isComplete.setText("Yes");
        }
        else  {
            holder.isComplete.setText("No");
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
