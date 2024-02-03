package io.scriptor.sapps;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import io.scriptor.sapps.databinding.ActivityAccountBinding;
import io.scriptor.sapps.firebase.FB;
import io.scriptor.sapps.firebase.User;

public class AccountActivity extends AppCompatActivity {

    private ActivityAccountBinding mBinding;

    private String mUID;
    private DatabaseReference mData;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);

        mUID =
                getIntent().hasExtra("uid")
                        ? getIntent().getStringExtra("uid")
                        : FB.getAuth().getCurrentUser().getUid();

        if (mUID.equals(FB.getAuth().getCurrentUser().getUid())) {
            mBinding.fab.show();
            mBinding.fab.setOnClickListener(v -> goEditAccount());
        } else mBinding.fab.hide();

        mData = FB.getDatabase().getReference("users").child(mUID);
        mData.get().addOnCompleteListener(this::onGetUserComplete);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mData.get().addOnCompleteListener(this::onGetUserComplete);
    }

    private void goEditAccount() {
        Intent intent = new Intent(getApplicationContext(), EditAccountActivity.class);
        startActivity(intent);
    }

    private void onGetUserComplete(Task<DataSnapshot> task) {
        if (task.isSuccessful()) {
            mUser = task.getResult().getValue(User.class);

            if (mUser.name != null) mBinding.toolbarLayout.setTitle(mUser.name);

            if (mUser.profile != null) Glide.with(this).load(mUser.profile).into(mBinding.profile);
            if (mUser.banner != null) Glide.with(this).load(mUser.banner).into(mBinding.banner);
        } else {
            Snackbar.make(
                            mBinding.getRoot(),
                            task.getException().getMessage(),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
