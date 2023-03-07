package com.idontknow.anochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatActivity extends AppCompatActivity {

    String ReciverImage, ReciverUid, ReciverName, SenderUid;
    CircleImageView profile_image;
    TextView reciverName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String simage;
    public static String rimage;
    CardView send_btn;
    EditText edtMessage;

    String senderRoom, reciverRoom;
    RecyclerView messageAdapter;
    ArrayList<messages> messagesArrayList;

    messagesAdapter msgAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ReciverImage = getIntent().getStringExtra("ReciverImage");
        ReciverUid = getIntent().getStringExtra("uid");
        ReciverName = getIntent().getStringExtra("name");
        messagesArrayList = new ArrayList<>();

        profile_image = findViewById(R.id.profile_image);
        reciverName = findViewById(R.id.reciverName);
        messageAdapter = findViewById(R.id.messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        msgAdapter = new messagesAdapter(chatActivity.this, messagesArrayList);
        messageAdapter.setAdapter(msgAdapter);

        send_btn = findViewById(R.id.send_btn);
        edtMessage = findViewById(R.id.edtMessage);


        Picasso.get().load(ReciverImage).into(profile_image);
        reciverName.setText(""+ReciverName);

        SenderUid = firebaseAuth.getUid();
        senderRoom = SenderUid+ReciverUid;
        reciverRoom = ReciverUid+SenderUid;


        DatabaseReference reference =database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference Chatreference =database.getReference().child("chats").child(senderRoom).child("messages");

        Chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesArrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    messages messages=dataSnapshot.getValue(messages.class);
                    messagesArrayList.add(messages);
                }
                msgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                simage = snapshot.child("imageUri").getValue().toString();
                rimage = ReciverImage;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(chatActivity.this, "Message Not Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                edtMessage.setText("");
                Date date = new Date();

                messages messages = new messages(message, SenderUid, date.getTime());

                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .push()
                                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });

            }
        });


    }
}