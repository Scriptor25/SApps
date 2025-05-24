package io.scriptor.sapps.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import io.scriptor.sapps.FB;
import io.scriptor.sapps.databinding.ActivityAppBinding;
import io.scriptor.sapps.model.AppModel;

public class AppActivity extends AppCompatActivity {

    private ActivityAppBinding mBinding;

    private AppModel mApp;
    private String mAID;
    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);

        mAID = getIntent().hasExtra("aid") ? getIntent().getStringExtra("aid") : null;

        mData = FB.getDatabase().getReference("sapps/apps").child(mAID);
        mData.get().addOnCompleteListener(this::onGetAppComplete);
    }

    private void onGetAppComplete(Task<DataSnapshot> task) {
        if (task.isSuccessful()) {
            mApp = task.getResult().getValue(AppModel.class);

            if (mApp.name != null) mBinding.toolbar.setTitle(mApp.name);

        } else {
            Snackbar.make(
                            mBinding.getRoot(),
                            task.getException().getMessage(),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void goEditApp() {
        var intent = new Intent(getApplicationContext(), EditAppActivity.class);
        startActivity(intent);
    }

    private void goAccount() {
        var intent = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(intent);
    }
}
