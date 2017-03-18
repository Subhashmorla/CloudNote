package com.example.root.cloudnote;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView notesList;
    private DatabaseReference mDatabase;
    private FloatingActionButton fabButton;
    private  String TAG=MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Transition   explode = new Explode();

            explode.setDuration(900);

            explode.excludeTarget(android.R.id.statusBarBackground, true);

            explode.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }


        notesList= (RecyclerView) findViewById(R.id.recycler_view);
        notesList.setHasFixedSize(true);
        notesList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("User Data").child(mAuth.getCurrentUser().getUid());
        mDatabase.keepSynced(true);

        fabButton= (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editorIntent=new Intent(MainActivity.this,EditorActivity.class);
                startActivity(editorIntent);
            }
        });




    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Note,NoteViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Note, NoteViewHolder>(
                Note.class,R.layout.notesrow,NoteViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(NoteViewHolder viewHolder, Note model,final int position) {
                final String note_key=getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setNotes(model.getNotes());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent editorIntent=new Intent(MainActivity.this,EditorActivity.class);
                        editorIntent.putExtra("note key",note_key);
                        startActivity(editorIntent);
                    }
                });

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
            case  (R.id.add_event):

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Calendar  cal = Calendar.getInstance();

                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", cal.getTimeInMillis());
                    intent.putExtra("allDay", true);
                    intent.putExtra("rrule", "FREQ=YEARLY");
                    intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                    intent.putExtra("title", "A Test Event from android app");
                    startActivity(intent);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}
