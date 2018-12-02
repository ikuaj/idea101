package com.example.prideland.idea.ui;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.prideland.idea.MainActivity;
import com.example.prideland.idea.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

public class AddActivity extends AppCompatActivity {
    @BindView(R.id.add) EditText mAdd;
    @BindView(R.id.post) Button mPost;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        databaseReference = FirebaseDatabase.getInstance().getReference("idea");

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();

                String comment = mAdd.getText().toString();

                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.putExtra("comment", comment);
                startActivity(intent);
            }
        });
    }

    public void addComment(){
        String comment = mAdd.getText().toString();

        if (TextUtils.isEmpty(comment)){
            String id = databaseReference.push().getKey();
            Comment comment1 = new Comment(id, comment);

            databaseReference.child(id).setValue(comment1);
            comment.setText("");

        }
        else {
            Toast.makeText(AddActivity.this, "Please give an idea",Toast.LENGTH_LONG).show();
        }
    }
}
