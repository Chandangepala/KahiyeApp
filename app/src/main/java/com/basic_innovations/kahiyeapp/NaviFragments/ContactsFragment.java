package com.basic_innovations.kahiyeapp.NaviFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basic_innovations.kahiyeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    RecyclerView recyVwContact;
    ArrayList<ContactsModel> arrContacts = new ArrayList<>();
    RecycVwContactAdapter contactAdapter;
    private DatabaseReference usersDataRef;

    /////

    String mUsername;
    public static final String ANONYMOUS = "anonymous";

    private FirebaseDatabase contactsDatabase;
    private DatabaseReference mcontactsDBRef;
    private ChildEventListener mChildEventListenerTL;
    private FirebaseAuth mFirebaseAuthTL;
    private FirebaseStorage mFirebaseStorageTL;



    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyVwContact = root.findViewById(R.id.recy_vw_contacts);

        usersDataRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //////////
        //mUsername = ANONYMOUS;

        contactsDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuthTL = FirebaseAuth.getInstance();
        mFirebaseStorageTL = FirebaseStorage.getInstance();

        mcontactsDBRef = contactsDatabase.getReference().child("Users");
        mcontactsDBRef.keepSynced(true);

        String mUserName = mFirebaseAuthTL.getCurrentUser().getDisplayName();

        onSignedInInitilize(mUserName);
      ////

        addContacts("Aman", "Eating out Loud!!", R.mipmap.ic_launcher_round);
        addContacts("Ban", "Eating out Loud!!", R.mipmap.ic_launcher_round);
        addContacts("Chan", "Eating out Loud!!", R.mipmap.ic_launcher_round);


        recyVwContact.setHasFixedSize(true);
        recyVwContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        contactAdapter = new RecycVwContactAdapter(getContext(), arrContacts);
        recyVwContact.setAdapter(contactAdapter);

        mcontactsDBRef.keepSynced(false);
        mcontactsDBRef.keepSynced(true);


        return root;
    }

    public void addContacts(String name, String status, int img ){
        ContactsModel contactsModel = new ContactsModel();
        contactsModel.contactName = name;
        contactsModel.contactStatus = status;
        contactsModel.contactImg = img;
        arrContacts.add(contactsModel);
    }

    private void onSignedInInitilize(String username){
        mUsername = username;
        attachDatabaseReadListener();
        //Toast.makeText(getContext(),"here 11",Toast.LENGTH_SHORT).show();
    }

    private void onSignedOutCleanup(){
        //mUsername = ANONYMOUS;
        //mMessageAdapter.clear();
        arrContacts.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){
        if(mChildEventListenerTL == null) {
            mChildEventListenerTL = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ContactsModel contactsModel = dataSnapshot.getValue(ContactsModel.class);
                    //ContactsModel contactsModel = new ContactsModel();

                    arrContacts.add(0,contactsModel);
                    contactAdapter.notifyDataSetChanged();
                    //Toast.makeText(getContext(),"Here 21",Toast.LENGTH_SHORT).show();
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
            mcontactsDBRef.addChildEventListener(mChildEventListenerTL);
        }
    }

    private  void detachDatabaseReadListener(){
        if(mChildEventListenerTL != null){
            mcontactsDBRef.removeEventListener(mChildEventListenerTL);
            mChildEventListenerTL = null;
        }
    }

}
