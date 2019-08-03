package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class DealActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etPrice;
    private EditText etDescription;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    TravelDeal mDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        etTitle = findViewById(R.id.et_title);
        etPrice = findViewById(R.id.et_price);
        etDescription = findViewById(R.id.et_description);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.insert_menu,menu);

        if (FirebaseUtil.isAdmin){
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_save).setVisible(true);
            enableEditTexts(true);
        } else {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(false);
            enableEditTexts(false);
        }
        return true;
    }

    private void enableEditTexts(boolean isEnabled) {
        etPrice.setEnabled(isEnabled);
        etDescription.setEnabled(isEnabled);
        etTitle.setEnabled(isEnabled);
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
        Toast.makeText(this,"Deal deleted",Toast.LENGTH_LONG).show();
    }

    private void backToList(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);

    }
}
