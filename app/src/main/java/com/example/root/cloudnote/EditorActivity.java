package com.example.root.cloudnote;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Transition;
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
    private String Title;
    private String Notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mAuth=FirebaseAuth.getInstance();
        startAnim();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Transition   explode = new Explode();

            explode.setDuration(900);

            explode.excludeTarget(android.R.id.statusBarBackground, true);

            explode.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar_editor);
        setSupportActionBar(toolbar);
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
            mDatabasekey.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Note map=dataSnapshot.getValue(Note.class);
                     Title=map.getTitle();
                     Notes=map.getNotes();

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
                    Toast.makeText(EditorActivity.this,"Database error",Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            setTitle("Add Note");
            invalidateOptionsMenu();
            mtitle.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }


    }

    private void startAnim() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (noteKey==null){
            MenuItem menuItem= menu.findItem(R.id.delete_menu_item);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu_item:
                saveNotes();
                break;
            case R.id.delete_menu_item:
                deleteNotes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteNotes() {
mDatabasekey.removeValue();
        finish();
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
