package io.scriptor.sapps.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import io.scriptor.sapps.FB;
import io.scriptor.sapps.adapter.SlideAdapter;
import io.scriptor.sapps.databinding.ActivityEditAppBinding;
import io.scriptor.sapps.model.AppModel;

import java.util.UUID;

public class EditAppActivity extends AppCompatActivity {

    private ActivityEditAppBinding mBinding;

    private String mAID;
    private AppModel mApp;
    private DatabaseReference mData;
    private StorageReference mApkStorage;
    private StorageReference mIconStorage;
    private StorageReference mSlideStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityEditAppBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.fab.setOnClickListener(v -> {});

        mAID =
                getIntent().hasExtra("aid")
                        ? getIntent().getStringExtra("aid")
                        : UUID.randomUUID().toString();

        mData = FB.getDatabase().getReference("sapps/apps").child(mAID);
        mData.get().addOnCompleteListener(this::onGetAppComplete);

        mApkStorage = FB.getStorage().getReference("sapps/apps/apk").child(mAID);
        mIconStorage = FB.getStorage().getReference("sapps/apps/icon").child(mAID);
        mSlideStorage = FB.getStorage().getReference("sapps/apps/slide").child(mAID);

        mBinding.icon.setOnClickListener(v -> {});
        mBinding.slide.setAdapter(new SlideAdapter());
    }

    private void onGetAppComplete(Task<DataSnapshot> task) {
        if (task.isSuccessful()) {
            mApp = task.getResult().getValue(AppModel.class);
            if (mApp == null) {
                mApp = new AppModel();
                mApp.aid = mAID;
            }

            if (mApp.icon != null) Glide.with(this).load(mApp.icon).into(mBinding.icon);
            mBinding.description.setText(mApp.description);
        } else {
            Snackbar.make(
                            mBinding.getRoot(),
                            task.getException().getMessage(),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
