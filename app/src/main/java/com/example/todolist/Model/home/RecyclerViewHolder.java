package com.example.todolist.Model.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    ImageView taskImage;
    TextView taskName;
    TextView isComplete;
    TextView taskDate;
    TextView taskDesc;
    RelativeLayout relativeLayout;


    public RecyclerViewHolder(@NonNull View taskView) {
        super(taskView);
        taskImage = taskView.findViewById(R.id.taskImage);
        taskName = taskView.findViewById(R.id.taskName);
        taskDate = taskView.findViewById(R.id.taskDate);
        taskDesc = taskView.findViewById(R.id.taskDesc);
        isComplete = taskView.findViewById(R.id.isComplete);
        relativeLayout = taskView.findViewById(R.id.task_container);
    }
}
