package com.example.root.cloudnote;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditorActivity extends AppCompatActivity {
    private TextInputLayout titleWrapper;
    private TextInputLayout notesWrapper;
    private EditText mtitle;
    private EditText mnotes;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mAuth=FirebaseAuth.getInstance();
        titleWrapper= (TextInputLayout) findViewById(R.id.textlayout_title);
        notesWrapper= (TextInputLayout) findViewById(R.id.textlayout_notes);
        titleWrapper.setHint("Title");
        notesWrapper.setHint("Notes");

        mDatabase= FirebaseDatabase.getInstance().getReference().child("User Data");

        mtitle= (EditText) findViewById(R.id.title_edit_text);
        mnotes= (EditText) findViewById(R.id.notes_edit_text);

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
            DatabaseReference currentuser=mDatabase.child(mAuth.getCurrentUser().getUid()).push();
            currentuser.child("Title").setValue(title);
            currentuser.child("Notes").setValue(notes);
            finish();


        }

    }
}
