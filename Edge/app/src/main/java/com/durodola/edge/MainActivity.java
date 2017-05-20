package com.durodola.edge;

import android.os.Bundle;

import com.durodola.edge.BaseActivity.BaseActivity;
import com.durodola.edge.Fragments.SignUpFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(SignUpFragment.newInstance() , R.id.container);
    }
}
