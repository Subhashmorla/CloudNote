package com.example.root.cloudnote;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class EditorActivity extends AppCompatActivity {
    private EditText mtitle;
    private EditText mnotes;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabasekey;
    private String noteKey=null;
    private boolean hasCurrentNote=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mAuth=FirebaseAuth.getInstance();




        mDatabase= FirebaseDatabase.getInstance().getReference().child("User Data").child(mAuth.getCurrentUser().getUid());



        mtitle= (EditText) findViewById(R.id.title_edit_text);
        mnotes= (EditText) findViewById(R.id.notes_edit_text);
        noteKey=getIntent().getStringExtra("note key");

        if (noteKey!=null){
            mDatabasekey=FirebaseDatabase.getInstance().getReference().child("User Data").child(mAuth.getCurrentUser().getUid()).child(noteKey);
            hasCurrentNote=true;
            setTitle("Edit Note");
            mtitle.setCursorVisible(false);
            mnotes.setCursorVisible(false);
            mDatabasekey.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Note map=dataSnapshot.getValue(Note.class);
                    String Title=map.getTitle();
                    String Notes=map.getNotes();

                    mtitle.setText(Title);
                    mnotes.setText(Notes);
                    mtitle.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            mtitle.setCursorVisible(true);
                            return false;
                        }
                    });
                    mnotes.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            mnotes.setCursorVisible(true);
                            return false;
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            setTitle("Add Note");
            mtitle.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu_item:
                saveNotes();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNotes() {
        String title=mtitle.getText().toString();
        String notes=mnotes.getText().toString();

        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(notes)){
            if (hasCurrentNote==false) {
                DatabaseReference currentuser = mDatabase.push();
                currentuser.child("Title").setValue(title);
                currentuser.child("Notes").setValue(notes);
            }
            else {
                mDatabase.child(noteKey).child("Notes").setValue(notes);
                mDatabase.child(noteKey).child("Title").setValue(title);
            }
            finish();


        }

    }
}
