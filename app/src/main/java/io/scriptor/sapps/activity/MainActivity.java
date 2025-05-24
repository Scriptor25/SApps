package io.scriptor.sapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import io.scriptor.sapps.FB;
import io.scriptor.sapps.R;
import io.scriptor.sapps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FB.getAuth().getCurrentUser() == null) {
            goLogin();
            return;
        }

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);

        mBinding.fab.setOnClickListener(v -> goPublishApp());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                goSearch();
                break;

            case R.id.downloads:
                goUpdate();
                break;

            case R.id.settings:
                goSettings();
                break;

            case R.id.account:
                goAccount();
                break;

            case R.id.logout:
                FB.getAuth().signOut();
                goLogin();
                break;
        }

        return true;
    }

    private void goLogin() {
        final var intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goSettings() {
        final var intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void goAccount() {
        final var intent = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(intent);
    }

    private void goSearch() {
        final var intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
    }

    private void goUpdate() {
        final var intent = new Intent(getApplicationContext(), UpdateActivity.class);
        startActivity(intent);
    }

    private void goPublishApp() {
        final var intent = new Intent(getApplicationContext(), EditAppActivity.class);
        startActivity(intent);
    }
}
