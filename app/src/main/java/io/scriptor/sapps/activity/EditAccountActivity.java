package io.scriptor.sapps.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.scriptor.sapps.FB;
import io.scriptor.sapps.databinding.ActivityEditAccountBinding;
import io.scriptor.sapps.model.UserModel;

public class EditAccountActivity extends AppCompatActivity {

    private ActivityEditAccountBinding mBinding;

    private String mUID;
    private DatabaseReference mData;
    private UserModel mUser;

    private Uri mProfile;
    private Uri mBanner;

    private StorageReference mProfileRef;
    private StorageReference mBannerRef;

    private ActivityResultLauncher<Intent> mPickProfileLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            final var uri = result.getData().getData();
                            mProfile = uri;
                            mBinding.profile.setImageURI(uri);
                        }
                    });
    private ActivityResultLauncher<Intent> mPickBannerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            final var uri = result.getData().getData();
                            mBanner = uri;
                            mBinding.banner.setImageURI(uri);
                        }
                    });

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mUID =
                getIntent().hasExtra("uid")
                        ? getIntent().getStringExtra("uid")
                        : FB.getAuth().getCurrentUser().getUid();

        mBinding.fab.setOnClickListener(
                v -> {
                    final var name = mBinding.name.getText().toString().trim();
                    if (!name.isEmpty()) {
                        mUser.name = name;
                        mData.setValue(mUser);
                    }

                    if (mProfile != null) {
                        mProfileRef
                                .putFile(mProfile)
                                .continueWithTask(this::onContinueGetProfileUri)
                                .addOnCompleteListener(this::onGetProfileUriComplete);
                    }

                    if (mBanner != null) {
                        mBannerRef
                                .putFile(mBanner)
                                .continueWithTask(this::onContinueGetBannerUri)
                                .addOnCompleteListener(this::onGetBannerUriComplete);
                    }
                });

        mBinding.profile.setOnClickListener(
                v -> {
                    final var intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");

                    mPickProfileLauncher.launch(intent);
                });

        mBinding.banner.setOnClickListener(
                v -> {
                    final var intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");

                    mPickBannerLauncher.launch(intent);
                });

        mData = FB.getDatabase().getReference("users").child(mUID);
        mData.get().addOnCompleteListener(this::onGetUserComplete);

        mProfileRef = FB.getStorage().getReference("users/profile").child(mUID);
        mBannerRef = FB.getStorage().getReference("users/banner").child(mUID);
    }

    private Task<Uri> onContinueGetProfileUri(final Task<UploadTask.TaskSnapshot> task)
            throws Exception {
        if (task.isSuccessful()) {
            return mProfileRef.getDownloadUrl();
        } else {
            throw task.getException();
        }
    }

    private void onGetProfileUriComplete(final Task<Uri> task) {
        if (task.isSuccessful()) {
            mProfile = null;
            final var uri = task.getResult();
            mUser.profile = uri.toString();
            mData.setValue(mUser);
        } else {
            Snackbar.make(
                            mBinding.getRoot(),
                            task.getException().getMessage(),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private Task<Uri> onContinueGetBannerUri(final Task<UploadTask.TaskSnapshot> task)
            throws Exception {
        if (task.isSuccessful()) {
            return mBannerRef.getDownloadUrl();
        } else {
            throw task.getException();
        }
    }

    private void onGetBannerUriComplete(final Task<Uri> task) {
        if (task.isSuccessful()) {
            mBanner = null;
            final var uri = task.getResult();
            mUser.banner = uri.toString();
            mData.setValue(mUser);
        } else {
            Snackbar.make(
                            mBinding.getRoot(),
                            task.getException().getMessage(),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void onGetUserComplete(final Task<DataSnapshot> task) {
        if (task.isSuccessful()) {
            mUser = task.getResult().getValue(UserModel.class);
            mBinding.name.setText(mUser.name);

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
