package com.example.todolist;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import android.view.View;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;

import android.text.Spanned;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.todolist.Model.AppDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.todolist.Model.ui.settings.SettingsActivity;

import com.example.todolist.Model.Task;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import com.example.todolist.databinding.ActivityMainBinding;

import java.util.List;
import java.util.TimeZone;

/**
 * The Main activity for the application.
 * <p>This is the first Screen the user sees</p>
 *
 * @author Jay Stewart, Bryce McNary, Marwa Qureshi, Alyana Barrera, Austin Hwang
 * @version 1.0
 * @see <a href="https://github.com/WSU-DGscheidle/spring23_project-go-team">Visit this project's GitHub repository</a>
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding binding;
    public static AppDatabase db;

    public static SharedPreferences spf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spf = PreferenceManager.getDefaultSharedPreferences(this);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "tasks-db").allowMainThreadQueries().build();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout popupWindow = findViewById(R.id.popup_window);
                FloatingActionButton btnAddItem = findViewById(R.id.btnAddItem);
                //the btnAddItem floating action button will disappear once pop-up window opens
                btnAddItem.setVisibility(View.GONE);
                popupWindow.setVisibility(View.VISIBLE);
            }
        });


        //Return to Main Screen when cancel button is clicked
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to return to the main activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



        //This will by default display today's date which is editable
        EditText editText = findViewById(R.id.current_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        editText.setText(dateFormat.format(calendar.getTime()));

        // Disable EditText when it gains focus
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText.setEnabled(false);
                }
            }
        });

        // Set up the due date format and restriction
        EditText dueDateEditText = findViewById(R.id.due_date);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        dueDateEditText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "MMDDYYYY";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Get current year
                int currentYear = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    currentYear = LocalDate.now().getYear();
                }

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

                        //The line of code below sets minimum year to current year and max year of due date as next year
                        //This auto-corrects when year is for example, 1800 or 2028
                        year = (year < currentYear) ? currentYear : (year > cal.get(Calendar.YEAR) + 1) ? cal.get(Calendar.YEAR) + 1 : year;

                        cal.set(Calendar.YEAR, year);
                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", month, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dueDateEditText.setText(current);
                    dueDateEditText.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_complete)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * inflates the menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_navigation) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public AppDatabase getDb() {
        return db;
    }

    /**
     * Retrieves the smallest primary Key not in use by the database
     * @return primaryKey a primary key value not in use by the database
     */
    private int getNextPrimaryKey(){
        int primaryKey = 1;
        for (Task task: db.taskDao().getAll()) {
            if (primaryKey != task.getTaskId()){
                return primaryKey;
            }
            else primaryKey += 1;
        }
        return primaryKey;
    }
}