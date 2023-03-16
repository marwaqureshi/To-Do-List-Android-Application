package com.example.todolist;

import android.app.LauncherActivity;

import com.example.todolist.Model.Task;
import com.example.todolist.Model.home.HomeFragment;

import org.junit.Test;

public class RecyclerTest {

    HomeFragment homeFragment = new HomeFragment();

    @Test
    public void testAdd() {
        Task task1 = new Task
                (1, R.drawable.placeholder, true, "task1", "desc1", "1/1/1");
    }

}
