package com.example.todolist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.Menu;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;
import android.text.Spanned;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.todolist.Model.AppDatabase;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.todolist.databinding.ActivityMainBinding;


import java.util.TimeZone;



/**
 * The Main activity for the application.
 *  <p>This is the first Screen the user sees</p>
 * @author Jay Stewart, Bryce McNary, Marwa Qureshi, Alyana Barrera, Austin Hwang
 * @version 1.0
 * @see <a href="https://github.com/WSU-DGscheidle/spring23_project-go-team">Visit this project's GitHub repository</a>
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding binding;
    public static AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "tasks-db").allowMainThreadQueries().build();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.btnAddItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ConstraintLayout popupWindow = findViewById(R.id.popup_window);
                popupWindow.setVisibility(View.VISIBLE);
            }

        });

        //This will by default display today's date which is editable
        EditText editText = findViewById(R.id.current_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        editText.setText(dateFormat.format(calendar.getTime()));


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public AppDatabase getDb() {
        return db;
    }
}