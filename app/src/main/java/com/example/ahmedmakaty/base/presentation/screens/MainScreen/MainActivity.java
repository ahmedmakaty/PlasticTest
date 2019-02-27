package com.example.ahmedmakaty.base.presentation.screens.MainScreen;

import com.example.ahmedmakaty.base.presentation.BaseActivity;
import com.example.ahmedmakaty.base.presentation.BaseFragment;

public class MainActivity extends BaseActivity {

    @Override
    public BaseFragment getFragment() {
        return MainFragment.newInstance();
    }
}
