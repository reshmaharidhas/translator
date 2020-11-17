package com.example.firebas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView tv1;
    AutoCompleteTextView autoEditText;
    Button btn;
    DatabaseReference reff;
    String[] words = {"yes","no","come","eat"}; //more words to translate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,"Firebase connection success",Toast.LENGTH_LONG).show();

        tv1 = (TextView)findViewById(R.id.textView1);
        btn = (Button)findViewById(R.id.button);
        autoEditText = (AutoCompleteTextView)findViewById(R.id.editText);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item,words);
        autoEditText.setThreshold(2); //will start working from first character
        autoEditText.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = autoEditText.getText().toString();
                final String str = word.toLowerCase();
                reff = FirebaseDatabase.getInstance().getReference().child("words");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean b = snapshot.hasChild(str);
                        if(b==true){
                            String word = snapshot.child(str).getValue().toString();
                            tv1.setTextColor(Color.BLACK);
                            tv1.setText(word);
                        } else{
                            tv1.setTextColor(Color.RED);
                            tv1.setText("WORD NOT FOUND");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this,"Error fetching data",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
