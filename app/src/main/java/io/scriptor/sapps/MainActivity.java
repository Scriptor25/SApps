package io.scriptor.sapps;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.scriptor.sapps.databinding.ActivityMainBinding;
import io.scriptor.sapps.firebase.FB;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void goAccount() {
        Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(intent);
    }

    private void goSearch() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
    }

    private void goUpdate() {
        Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
        startActivity(intent);
    }

    private void goPublishApp() {
        Intent intent = new Intent(getApplicationContext(), EditAppActivity.class);
        startActivity(intent);
    }
}
