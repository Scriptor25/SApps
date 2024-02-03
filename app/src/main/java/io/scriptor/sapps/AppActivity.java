package io.scriptor.sapps;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import io.scriptor.sapps.databinding.ActivityAppBinding;
import io.scriptor.sapps.firebase.App;
import io.scriptor.sapps.firebase.FB;
import java.util.UUID;

public class AppActivity extends AppCompatActivity {

    private ActivityAppBinding mBinding;

    private App mApp;
    private String mAID;
    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);

        mAID =
                getIntent().hasExtra("aid")
                        ? getIntent().getStringExtra("aid")
                        : UUID.randomUUID().toString();

        mData = FB.getDatabase().getReference("sapps/apps").child(mAID);
        mData.get().addOnCompleteListener(this::onGetAppComplete);
    }

    private void onGetAppComplete(Task<DataSnapshot> task) {
        if (task.isSuccessful()) {
            mApp = task.getResult().getValue(App.class);

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
        Intent intent = new Intent(getApplicationContext(), EditAppActivity.class);
        startActivity(intent);
    }

    private void goAccount() {
        Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(intent);
    }
}
