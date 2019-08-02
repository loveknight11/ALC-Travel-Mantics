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

        FirebaseUtil.openReference();
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
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveDeal();
                Toast.makeText(this,"Deal Saved",Toast.LENGTH_LONG).show();
                cleanAll();
        }
        return super.onOptionsItemSelected(item);
    }

    private void cleanAll() {
        etTitle.setText("");
        etPrice.setText("");
        etDescription.setText("");
        etTitle.requestFocus();
    }

    private void saveDeal() {
        String title = etTitle.getText().toString();
        String price = etPrice.getText().toString();
        String description = etDescription.getText().toString();

        TravelDeal deal = new TravelDeal(title,price,description,"");
        mDatabaseReference.push().setValue(deal);
    }
}
