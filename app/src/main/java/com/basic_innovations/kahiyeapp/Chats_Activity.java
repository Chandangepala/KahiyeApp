package com.basic_innovations.kahiyeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;

public class Chats_Activity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerVwChat;
    String recieverID, senderID, recieverName;
    //////
    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;
    private static boolean isPersistenceEnabled = false;
    String token;
    private FirebaseDatabase mFirebaseeDatabase;
    private DatabaseReference mMessageDBRef, mMsgRecieverDBRef;
    private ImageButton mSendButton;
    private EditText mMessageEditText;
    private ChildEventListener mChildEventListener;
    private RecyMessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private String mUsername;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    // private UploadTask uploadTask;
    private ImageView imageView;
    private StorageTask uploadTask;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String defualtMessage;

    final ArrayList<ChatMessage> chatMessages = new ArrayList<>();

    Toolbar toolbar;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_);

        //fetch reciever Data
        fetchUserIds();

        //for custom toolbar
        showCustomToolbar();

        // function which initializes all firebase ref. and obj
        requiredFirebaseInit();

        mProgressBar = findViewById(R.id.progressBar);
        recyclerVwChat = findViewById(R.id.recy_vwChatsAct);  ///check this
        mPhotoPickerButton = findViewById(R.id.photoPickerButton);
        mMessageEditText = findViewById(R.id.messageEditText);
        mSendButton = findViewById(R.id.sendButton);
        imageView = findViewById(R.id.photoImageView);

        //Setup function for recyclerview and it's adapter
        recyclerViewSetup();

        mUsername = mFirebaseAuth.getCurrentUser().getDisplayName();
        onSignedInInitilize(mUsername);
        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                    mSendButton.setImageResource(R.drawable.ic_send_);
                    defualtMessage = mMessageEditText.getText().toString();
                } else {
                    //mSendButton.setEnabled(false);
                    mSendButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mUsername = user.getDisplayName();
                ChatMessage chatMessage = new ChatMessage(defualtMessage, mUsername, null, token);
                mMessageDBRef.push().setValue(chatMessage);
                mMsgRecieverDBRef.push().setValue(chatMessage);
                mMessageEditText.setText("");
                defualtMessage = "";
            }
        });

        //To pick an image from gallery
        imagePickerFunc();

    }

    @Override
    public void onActivityResult(int reqeuestCode, int resultCode, Intent data) {
        super.onActivityResult(reqeuestCode, resultCode, data);
        if (reqeuestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign In Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (reqeuestCode == RC_PHOTO_PICKER && data != null) {

            //Toast.makeText(MainActivity.this,"Here One",Toast.LENGTH_SHORT).show();
            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            final StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            uploadTask = photoRef.putFile(selectedImageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {   //Toast.makeText(MainActivity.this,"Here One",Toast.LENGTH_SHORT).show();
                        throw task.getException();
                    }
                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        // Toast.makeText(this,"Here One",Toast.LENGTH_SHORT).show();
                        Uri downloadUri = task.getResult();
                        String myUri = downloadUri.toString();

                        // Set the download URL to the message box, so that the user can send it to the database
                        ChatMessage chatMessage = new ChatMessage(null, mUsername, downloadUri.toString(), null);
                        mMessageDBRef.push().setValue(chatMessage);
                        mMsgRecieverDBRef.push().setValue(chatMessage);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedInInitilize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    chatMessages.add(chatMessage);
                    mMessageAdapter.notifyDataSetChanged();
                    int position = (mMessageAdapter.getItemCount());
                    recyclerVwChat.smoothScrollToPosition(position);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };

            //mMessageDBRef.addChildEventListener(mChildEventListener);
            mMsgRecieverDBRef.addChildEventListener(mChildEventListener);

        }
    }


    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            //mMessageDBRef.removeEventListener(mChildEventListener);
            mMsgRecieverDBRef.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    public void imagePickerFunc(){
        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.setType("image/png");
                intent.setType("image/gif");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                // Toast.makeText(MainActivity.this,"here picker",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // initialization for all firebase obj and references
    public void requiredFirebaseInit(){
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //Toast.makeText(this, ""+user.getDisplayName(), Toast.LENGTH_SHORT).show();

        mUsername = ANONYMOUS;
        mFirebaseeDatabase = FirebaseDatabase.getInstance();
        if (!isPersistenceEnabled) {
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            isPersistenceEnabled = true;
        }

        reference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mMessageDBRef = mFirebaseeDatabase.getReference().child("Messages/"+senderID+recieverID+"/");
        mMsgRecieverDBRef = mFirebaseeDatabase.getReference().child("Messages/"+recieverID+senderID+"/");
        mMessageDBRef.keepSynced(true);
        mMsgRecieverDBRef.keepSynced(true);
        StorageReference storageRef = mFirebaseStorage.getReference();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");
    }

    // all setup for recyclerview and it's adapter
    public void recyclerViewSetup(){
        recyclerVwChat.setHasFixedSize(true);
        recyclerVwChat.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerVwChat.setLayoutManager(linearLayoutManager);
        recyclerVwChat.smoothScrollToPosition(chatMessages.size());
        mMessageAdapter = new RecyMessageAdapter(this, chatMessages);
        recyclerVwChat.setAdapter(mMessageAdapter);
    }
    public void fetchUserIds() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        senderID = user.getUid();

        recieverID = getIntent().getStringExtra("userID");
        recieverName = getIntent().getStringExtra("recieverName");
        //Toast.makeText(this, recieverID, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, recieverName, Toast.LENGTH_SHORT).show();
    }

    public void showCustomToolbar(){
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.toolbar_title);
        // to set the custom toolbar to function as actionbar
        setSupportActionBar(toolbar);
        //to change the custom toolbar title
        title.setText(recieverName);

        if(getSupportActionBar() != null){
            //to apply back arrow button to toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
