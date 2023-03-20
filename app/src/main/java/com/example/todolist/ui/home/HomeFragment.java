package com.example.todolist.ui.home;

import android.app.Dialog;
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
import android.widget.TextView;
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

/**
 * The default activity Fragment that displays the list of To-Do tasks
 * @author Jay Stewart, Bryce McNary, Marwa Qureshi
 * @version 1.0
 * @see android.app.Fragment
 */
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
        setRecyclerVisibility();
    }

    /**
     * Caled to add a task to the database and update the RecyclerView
     * @param task a Task
     * @return updated RecyclerView
     */
    public void addToRecycler(Task task) {
        taskDao.insert(task);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerAdapter(getContext(),taskDao.getAll(), this));
        setRecyclerVisibility();
    }

    /**
     * Called to remove a task from the database and update the RecyclerView
     * @param task a Task
     * @return updated RecyclerView
     */
    public void removeFromRecycler(Task task){
        taskDao.delete(task);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerAdapter(getContext(),taskDao.getAll(), this));
        setRecyclerVisibility();
    }

    public void recycler() {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerAdapter(getContext(),taskDao.getAll(), this));
    }

    /**
     * This method sets the visibility of the RecyclerView
     * <p>
     * Sets the RecyclerView VISIBLE if the database is !empty,
     * otherwise sets the RecyclerView INVISIBLE and
     * the TextView is set VISIBLE
     * </p>
     * @see View
     */
    public void setRecyclerVisibility(){
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        TextView textView = getView().findViewById(R.id.noTaskTextView);
        if (!taskDao.getAll().isEmpty()){
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Called when the user selects a task_container from the RecyclerView
     * @param task a Task Selected
     * @return a bottom_sheet_dialog for the selected Task
     */
    @Override
    //onClick listener for recycler view
    public void onItemClicked(Task task) {
        showBottomDialog(task);
    }

    /**
     * Called to display a bottom Dialog on the screen.
     * <p>
     *     Displays the bottom_sheet_layout ContentView on the screen.
     *     The Dialog has three choices: edit, mark as complete, and delete.
     * </p>
     * @param task the selected Task
     */
    private void showBottomDialog(Task task) {
        //declare Dialog and set dialog view to bottom_sheet_layout
        final Dialog bottomDialog = new Dialog(getContext());
        bottomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomDialog.setContentView(R.layout.bottom_sheet_layout);

        //declare LinearLayouts from bottom_sheet_layout
        LinearLayout markCompleteLayout = bottomDialog.findViewById(R.id.bottom_sheet_markComplete);
        LinearLayout editLayout = bottomDialog.findViewById(R.id.bottom_sheet_edit);
        LinearLayout deleteLayout = bottomDialog.findViewById(R.id.bottom_sheet_delete);

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
                showDeleteDialog(task);
                bottomDialog.dismiss();}
        });

        //start dialog and display on screen with the following settings
        bottomDialog.show();
        bottomDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * Called to display a delete Dialog on the screen.
     * <p>
     *     Displays the delete_dialog ContentView on the screen.
     *     The Dialog has two choices: cancel or delete.
     * </p>
     * @param task the selected Task
     */
    private void showDeleteDialog(Task task){
        final Dialog deleteDialog = new Dialog(getContext());

        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.setContentView(R.layout.delete_dialog);

        //declare the buttons from delete_dialog
        Button cancelButton = deleteDialog.findViewById(R.id.cancel_delete_button);
        Button deleteButton = deleteDialog.findViewById(R.id.delete_button);

        //start dialog and display on screen with the following settings
        deleteDialog.show();
        deleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.getWindow().setGravity(Gravity.CENTER);
        deleteDialog.setCancelable(false);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismiss the dialog and remove it from the screen
                deleteDialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove selected task from recycler and dismiss the dialog
                removeFromRecycler(task);
                deleteDialog.dismiss();
            }
        });

    }
}