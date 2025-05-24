package io.scriptor.sapps.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.scriptor.sapps.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding mBinding;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);

        final var query =
                getIntent().hasExtra("query")
                        ? getIntent().getStringArrayExtra("query")
                        : new String[0];
    }

    private void goApp(final String aid) {
        final var intent = new Intent(getApplicationContext(), AppActivity.class);
        intent.putExtra("aid", aid);
        startActivity(intent);
    }
}
