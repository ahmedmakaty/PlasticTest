package com.example.ahmedmakaty.base.presentation;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmedmakaty.base.R;
import com.example.ahmedmakaty.base.presentation.BaseFragment;

abstract public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (this.getSupportFragmentManager().findFragmentById(R.id.main_content) == null) {
            BaseFragment f = getFragment();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, (Fragment) f).addToBackStack(f.getClass().getSimpleName()).commit();
        }
    }

    public abstract BaseFragment getFragment();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
