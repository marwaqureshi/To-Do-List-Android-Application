package com.example.todolist.Model.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.todolist.ui.home.SelectListener;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/**
 * The default activity Fragment that displays the list of To-Do tasks
 * @author Jay Stewart, Bryce McNary, Marwa Qureshi
 * @version 1.0
 * @see android.app.Fragment
 */
public class HomeFragment extends Fragment implements SelectListener {

    AppDatabase db;
    TaskDao taskDao;

    private FragmentHomeBinding binding;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    /**
     * called on initial creation of the fragment.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * called when the fragment returns to the foreground
     */
    @Override
    public void onStart() {
        super.onStart();
        db = MainActivity.db;
        taskDao = db.taskDao();
        recycler();
        setRecyclerVisibility();
    }

    /**
     * Called to add a task to the database and update the RecyclerView
     * @param task a Task
     * @return updated RecyclerView
     */
    public void addToRecycler(Task task) {
        taskDao.insert(task);
        recycler();
        setRecyclerVisibility();
    }

    /**
     * Called to remove a task from the database and update the RecyclerView
     * @param task a Task
     * @return updated RecyclerView
     */
    public void removeFromRecycler(Task task){
        taskDao.delete(task);
        recycler();
        setRecyclerVisibility();
    }

    /**
     * Called to refresh the RecyclerView
     * @return updated RecyclerView
     */
    public void recycler() {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Task> taskList = taskDao.getIncomplete();
        Collections.sort(taskList);
        recyclerView.setAdapter(new RecyclerAdapter(getContext(),taskList, this));
    }

    /**
     * This method sets the visibility of the RecyclerView
     * <p>
     * Sets the RecyclerView VISIBLE if the database is storing tasks WHERE isComplete = 0,
     * otherwise sets the RecyclerView INVISIBLE and
     * the TextView is set VISIBLE
     * </p>
     * @see View
     */
    public void setRecyclerVisibility(){
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        TextView textView = getView().findViewById(R.id.noTaskTextView);
        if (!taskDao.getIncomplete().isEmpty()){
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
                // Update task to complete
                taskDao.setComplete(task.getTaskId());
                // create Snackbar msg
                Snackbar.make(getView(), task.getTaskName() + " Marked As Complete", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            taskDao.setIncomplete(task.getTaskId());
                            recycler();
                            setRecyclerVisibility();
                        })
                        .show();

                // dismiss dialog and update recycler
                recycler();
                setRecyclerVisibility();
                bottomDialog.dismiss();
            }
        });

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(task);
                bottomDialog.dismiss();
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

    /**
     * Called to display an edit Dialog on the screen.
     * <p>
     *     Displays the edit_dialog ContentView on the screen.
     *     The Dialog allows user to change task details.
     * </p>
     * @param task the selected Task
     */
     private void showEditDialog(Task task){
         //initialize the dialog
         final Dialog editDialog = new Dialog(getContext());
         editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         editDialog.setContentView(R.layout.edit_dialog);

         //initialize everything
         EditText taskName = editDialog.findViewById(R.id.task_name);
         EditText cDate = editDialog.findViewById(R.id.current_date);
         EditText dueDate = editDialog.findViewById(R.id.due_date);
         EditText reminder = editDialog.findViewById(R.id.reminder_time);
         EditText taskDescription = editDialog.findViewById(R.id.description_task);
         Button cancelButton = editDialog.findViewById(R.id.cancelButton);
         Button updateButton = editDialog.findViewById(R.id.update_button);

         cDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View v, boolean hasFocus) {
                 if (hasFocus) {
                     cDate.setEnabled(false);
                 }
             }
         });
         cancelButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 editDialog.dismiss();
             }
         });

         dueDate.addTextChangedListener(new TextWatcher() {
             private String current = "";
             private String mmddyyyy = "MMDDYYYY";
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if (!s.toString().equals(current)) {
                     String clean = s.toString().replaceAll("[^\\d.]", "");
                     String cleanC = current.replaceAll("[^\\d.]", "");

                     int cl = clean.length();
                     int sel = cl;
                     for (int i = 2; i <= cl && i < 6; i += 2) {
                         sel++;
                     }
                     //Fix for pressing delete next to a forward slash
                     if (clean.equals(cleanC)) sel--;

                     if (clean.length() < 8) {
                         clean = clean + mmddyyyy.substring(clean.length());
                     } else {
                         //This part makes sure that when we finish entering numbers
                         //the date is correct, fixing it otherwise
                         int month = Integer.parseInt(clean.substring(0, 2));
                         int day = Integer.parseInt(clean.substring(2, 4));
                         int year = Integer.parseInt(clean.substring(4, 8));

                         month = month < 1 ? 1 : month > 12 ? 12 : month;
                         Calendar cal = Calendar.getInstance();
                         cal.set(Calendar.MONTH, month - 1);
                         year = (year < 1900) ? 1900 : (year > cal.get(Calendar.YEAR)) ? cal.get(Calendar.YEAR) : year;
                         cal.set(Calendar.YEAR, year);
                         day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                         clean = String.format("%02d%02d%02d", month, day, year);
                     }

                     clean = String.format("%s/%s/%s", clean.substring(0, 2),
                             clean.substring(2, 4),
                             clean.substring(4, 8));

                     sel = sel < 0 ? 0 : sel;
                     current = clean;
                     dueDate.setText(current);
                     dueDate.setSelection(sel < current.length() ? sel : current.length());
                 }
             }

             @Override
             public void afterTextChanged(Editable s) {}
         });

         updateButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 addToRecycler(new Task(task.getTaskId(), task.getTaskImage(), task.getIsComplete(), taskName.getText().toString(), taskDescription.getText().toString(), dueDate.getText().toString()));
                 editDialog.dismiss();
             }
         });

         //populate page with stored values
         taskName.setText(task.getTaskName());
         SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
         Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
         cDate.setText(dateFormat.format(calendar.getTime()));
         dueDate.setText(task.getTaskDate());
         taskDescription.setText(task.getTaskDescription());

         //start the dialog
         editDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
         editDialog.show();

     }
}