package io.scriptor.sapps;

import android.os.Bundle;

import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import io.scriptor.sapps.adapter.SlideAdapter;
import io.scriptor.sapps.databinding.ActivityEditAppBinding;

import io.scriptor.sapps.firebase.App;
import io.scriptor.sapps.firebase.FB;
import java.util.Arrays;
import java.util.UUID;

public class EditAppActivity extends AppCompatActivity {

    private ActivityEditAppBinding mBinding;

    private String mAID;
    private App mApp;
    private DatabaseReference mData;
    private StorageReference mApkStorage;
    private StorageReference mIconStorage;
    private StorageReference mSlideStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityEditAppBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mAID =
                getIntent().hasExtra("aid")
                        ? getIntent().getStringExtra("aid")
                        : UUID.randomUUID().toString();

        mData = FB.getDatabase().getReference("sapps/apps").child(mAID);
        mData.get().addOnCompleteListener(this::onGetAppComplete);

        mApkStorage = FB.getStorage().getReference("sapps/apps/apk").child(mAID);
        mIconStorage = FB.getStorage().getReference("sapps/apps/icon").child(mAID);
        mSlideStorage = FB.getStorage().getReference("sapps/apps/slide").child(mAID);

        mBinding.app.setOnClickListener(v -> {});
        mBinding.slide.setAdapter(new SlideAdapter(this, Arrays.asList(mApp.slide)));
    }

    private void onGetAppComplete(Task<DataSnapshot> task) {
        if (task.isSuccessful()) {
            mApp = task.getResult().getValue(App.class);

            if (mApp.icon != null) Glide.with(this).load(mApp.icon).into(mBinding.app);
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
