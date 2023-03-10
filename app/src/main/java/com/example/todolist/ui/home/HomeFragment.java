package com.example.todolist.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.Model.AppDatabase;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TaskDao;
import com.example.todolist.R;
import com.example.todolist.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements SelectListener{
    AppDatabase db;
    TaskDao taskDao;

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        db = MainActivity.db;
        taskDao = db.taskDao();

        recycler();
    }

    public void addToRecycler(Task task) {
        taskDao.insert(task);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerAdapter(getContext(),taskDao.getAll(), this));
    }

    public void removeFromRecycler(Task task){
        taskDao.delete(task);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerAdapter(getContext(),taskDao.getAll(), this));
    }

    public void recycler() {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerAdapter(getContext(),taskDao.getAll(), this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    //onClick listener for recycler view
    public void onItemClicked(Task task) {
        showDialog(task);
    }

    private void showDialog(Task task) {
        //declare Dialog and set dialog view to bottom_sheet_layout
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        //declare LinearLayouts from bottom_sheet_layout
        LinearLayout markCompleteLayout = dialog.findViewById(R.id.bottom_sheet_markComplete);
        LinearLayout editLayout = dialog.findViewById(R.id.bottom_sheet_edit);
        LinearLayout deleteLayout = dialog.findViewById(R.id.bottom_sheet_delete);

        // create onClickListener for each LinearLayout
        markCompleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Update the selected task to present as completed
                Toast.makeText(getContext(), "Mark " + task.getTaskName() + " as complete", Toast.LENGTH_SHORT).show();
            }
        });

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Make this ReOpen the ADD Task view and allow user to modify/update task details (or cancel)
                Toast.makeText(getContext(), "edit " + task.getTaskName(), Toast.LENGTH_SHORT).show();
            }
        });

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Make this open a pop-up "are you sure?" window and if confirm, delete the task
                Toast.makeText(getContext(), "delete " + task.getTaskName(), Toast.LENGTH_SHORT).show();
            }
        });

        //start dialog and display on screen
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}