package com.example.root.cloudnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView notesList;
    private DatabaseReference mDatabase;
    private  String TAG=MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesList= (RecyclerView) findViewById(R.id.recycler_view);
        notesList.setHasFixedSize(true);
        notesList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("User Data").child(mAuth.getCurrentUser().getUid());
        mDatabase.keepSynced(true);




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Note,NoteViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Note, NoteViewHolder>(
                Note.class,R.layout.notesrow,NoteViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(NoteViewHolder viewHolder, Note model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setNotes(model.getNotes());

            }
        };
        notesList.setAdapter(firebaseRecyclerAdapter);


    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public NoteViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setTitle(String title){
            TextView titleView= (TextView) mView.findViewById(R.id.title_text_view);
            titleView.setText(title);
        }

        public void  setNotes(String notes){
            TextView notesView= (TextView) mView.findViewById(R.id.notes_text_view);
            notesView.setText(notes);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.logout_menu_item):
                mAuth.signOut();
                Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.add_menu_item:
                Intent editorIntent=new Intent(MainActivity.this,EditorActivity.class);
                startActivity(editorIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}
