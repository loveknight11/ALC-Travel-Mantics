package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealActivity extends AppCompatActivity {

    public static final int IMAGE_RESULT = 11;
    private EditText etTitle;
    private EditText etPrice;
    private EditText etDescription;
    private ImageView ivImage;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    TravelDeal mDeal;
    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        etTitle = findViewById(R.id.et_title);
        etPrice = findViewById(R.id.et_price);
        etDescription = findViewById(R.id.et_description);
        ivImage = findViewById(R.id.iv_image);

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("deal");
        if (deal == null) {
            deal = new TravelDeal();
        }
        mDeal = deal;
        etTitle.setText(mDeal.getTitle());
        etDescription.setText(mDeal.getDescription());
        etPrice.setText(mDeal.getPrice());
        showImage(mDeal.getImageUrl());

        btnUpload = findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), IMAGE_RESULT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_RESULT && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            final StorageReference ref = FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());
//            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                    mDeal.setImageUrl(url);
//                    showImage(url);
//                }
//            });
            UploadTask uploadTask = ref.putFile(imageUri);
            uploadTask.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String imageName = taskSnapshot.getStorage().getPath();
                    mDeal.setImageName(imageName);
                }
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();
//                        String imageName = task.getClass().get
                        mDeal.setImageUrl(url);
                        showImage(url);
                    }
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.insert_menu,menu);

        if (FirebaseUtil.isAdmin){
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_save).setVisible(true);
            enableInterfaceItems(true);
        } else {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(false);
            enableInterfaceItems(false);
        }
        return true;
    }

    private void enableInterfaceItems(boolean isEnabled) {
        etPrice.setEnabled(isEnabled);
        etDescription.setEnabled(isEnabled);
        etTitle.setEnabled(isEnabled);
        btnUpload.setEnabled(isEnabled);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveDeal();
                Toast.makeText(this,"Deal Saved",Toast.LENGTH_LONG).show();
                backToList();
                return true;
            case R.id.action_delete:
                deleteDeal();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void cleanAll() {
        etTitle.setText("");
        etPrice.setText("");
        etDescription.setText("");
        etTitle.requestFocus();
    }

    private void saveDeal() {
        mDeal.setTitle(etTitle.getText().toString());
        mDeal.setPrice(etPrice.getText().toString());
        mDeal.setDescription(etDescription.getText().toString());

        if (mDeal.getId() == null){
            mDatabaseReference.push().setValue(mDeal);
        } else {
            mDatabaseReference.child(mDeal.getId()).setValue(mDeal);
        }
    }

    private void deleteDeal(){
        if (mDeal.getId() == null){
            Toast.makeText(this,"empty deal, can not delete",Toast.LENGTH_LONG).show();
            return ;
        }
        mDatabaseReference.child(mDeal.getId()).removeValue();

        if (mDeal.getImageName() != null && mDeal.getImageName().isEmpty() == false){
            StorageReference ref = FirebaseUtil.mStorage.getReference().child(mDeal.getImageName());
            ref.delete();
        }
        Toast.makeText(this,"Deal deleted",Toast.LENGTH_LONG).show();
    }

    private void backToList(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);

    }

    private void showImage(String url){
        if (url != null && url.isEmpty() != true) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(width,width*2/3)
                    .centerCrop()
                    .into(ivImage);
        }
    }
}
