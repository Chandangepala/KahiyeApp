package com.basic_innovations.kahiyeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basic_innovations.kahiyeapp.NaviFragments.AlwaysFragment;
import com.basic_innovations.kahiyeapp.NaviFragments.ChatFragment;
import com.basic_innovations.kahiyeapp.NaviFragments.ContactsFragment;
import com.basic_innovations.kahiyeapp.NaviFragments.ContactsModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView btmNaviView;
    ////
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseeDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String mUsername;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private DatabaseReference reference;
    private DatabaseReference usersDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;
        mFirebaseeDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mMessageDatabaseReference = mFirebaseeDatabase.getReference().child("messages");
        mMessageDatabaseReference.keepSynced(true);
        StorageReference storageRef = mFirebaseStorage.getReference();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        usersDatabaseRef = mFirebaseeDatabase.getReference().child("Users");

        btmNaviView = findViewById(R.id.navigation_bottom);
        btmNaviView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    /*case R.id.always_menu:
                        loadFragment( new AlwaysFragment(), false);
                        break;*/
                    case R.id.chat_menu:
                        loadFragment(new ChatFragment(), true);
                        break;
                    case R.id.contacts_menu:
                        loadFragment(new ContactsFragment(), false);
                        break;
                }
                return true;
            }
        });
        btmNaviView.setSelectedItemId(R.id.chat_menu);

        firebaseAuthFunction();
    }

    public void loadFragment(Fragment fragment, Boolean b){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragTrans = fm.beginTransaction();
        if(b == true) {
            fragTrans.add(R.id.frag_container, fragment);
        }
        else {
            fragTrans.replace(R.id.frag_container, fragment);
        }
        fragTrans.commit();
    }

    public void firebaseAuthFunction(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String userId = mFirebaseAuth.getUid();
                    usersDatabaseRef.child(userId).setValue("");

                    /*Map<String, String> profileData = new HashMap<>();
                    profileData.put("name", user.getDisplayName());
                    profileData.put("email", user.getEmail());
                    profileData.put("uid",user.getUid());
                    profileData.put("phoneNumber",user.getPhoneNumber())*/;
                    ContactsModel profileData = new ContactsModel(user.getDisplayName(),
                            "Always Cool", user.getEmail(), user.getUid(),0);
                    usersDatabaseRef.child(userId).setValue(profileData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           // Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          //  Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    });

                    //user is signed in
                    onSignedInInitilize(user.getDisplayName());
                    // mMessageDatabaseReference.push().setValue(friendlyMessage);
                    //Toast.makeText(MainActivity.this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
                } else {
                    //user is signed out
                    onSignedOutCleanup();
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setLogo(R.drawable.always_logo)
                                    .setTheme(R.style.AppTheme)
                                    .setAvailableProviders(providers)
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedInInitilize(String username) {
        mUsername = username;
    }

    private void onSignedOutCleanup() {

        mUsername = ANONYMOUS;
        //mMessageAdapter.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessageDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

}