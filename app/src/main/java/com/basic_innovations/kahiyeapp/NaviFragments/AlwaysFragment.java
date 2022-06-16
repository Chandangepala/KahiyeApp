package com.basic_innovations.kahiyeapp.NaviFragments;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.basic_innovations.kahiyeapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlwaysFragment extends Fragment implements View.OnClickListener {

    RecyclerView recycVwAlways;
    ArrayList<AlwaysModel> arrAlways = new ArrayList<>();
    FloatingActionButton alwUpload;
    Dialog dialog;
    Button btnUploadDia, btnCancelDia;
    ImageView imgAddPhoto;

    ///////////
    public static final String ANONYMOUS = "anonymous";

    private static final int RC_PHOTO_PICKER = 2;
    String mUserName;
    private FirebaseDatabase mFirebaseeDatabaseTL;

    private DatabaseReference alwaysDatabaseReferenceTL;
    private ChildEventListener mChildEventListenerTL;
    private String mUsername;
    private FirebaseAuth mFirebaseAuthTL;
    private FirebaseStorage mFirebaseStorageTL;
    private StorageReference alwaysStorageReference;
    RecycVwAlwaysAdapter alwaysAdapter;
    Uri imgUrl;


    public AlwaysFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_always, container, false);
        recycVwAlways  = root.findViewById(R.id.recy_vw_always);
        alwUpload = root.findViewById(R.id.alw_upload);
        alwUpload.setOnClickListener(this);

        ///////////////////
        mUsername = ANONYMOUS;
        mFirebaseeDatabaseTL = FirebaseDatabase.getInstance();
        mFirebaseAuthTL = FirebaseAuth.getInstance();
        mFirebaseStorageTL = FirebaseStorage.getInstance();

        alwaysDatabaseReferenceTL = mFirebaseeDatabaseTL.getReference().child("Always");
        alwaysDatabaseReferenceTL.keepSynced(true);

        alwaysStorageReference = mFirebaseStorageTL.getReference().child("Always");
        alwaysDatabaseReferenceTL.keepSynced(false);
        alwaysDatabaseReferenceTL.keepSynced(true);


        addAlwaysItems("Mayank", "Today", "https://cdn.pixabay.com/photo/2016/11/22/23/14/adorable-1851108__340.jpg", R.mipmap.ic_launcher);
        addAlwaysItems("Chandan", "Today", "https://cdn.pixabay.com/photo/2016/11/22/23/14/adorable-1851108__340.jpg", R.mipmap.ic_launcher);
        addAlwaysItems("Rudra", "Today", "https://cdn.pixabay.com/photo/2016/11/22/23/14/adorable-1851108__340.jpg", R.mipmap.ic_launcher);
        addAlwaysItems("Sona", "Today", "https://cdn.pixabay.com/photo/2016/11/22/23/14/adorable-1851108__340.jpg", R.mipmap.ic_launcher);
        addAlwaysItems("Kuki", "Yesterday", "https://cdn.pixabay.com/photo/2016/11/22/23/14/adorable-1851108__340.jpg", R.mipmap.ic_launcher);

        onSignedInInitilize(mUserName);

        recycVwAlways.setHasFixedSize(true);
        recycVwAlways.setLayoutManager( new GridLayoutManager(getContext(),2));
        alwaysAdapter = new RecycVwAlwaysAdapter(getContext(), arrAlways);
        recycVwAlways.setAdapter(alwaysAdapter);
        return root;
    }

    public void addAlwaysItems(String name, String time, String img, int profImg){
        AlwaysModel alwaysModel = new AlwaysModel();
        alwaysModel.alwName = name;
        alwaysModel.alwTime = time;
        alwaysModel.alwImg = img;
        alwaysModel.alwProfImg = profImg;
        arrAlways.add(alwaysModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alw_upload:
                    //Toast.makeText(getContext(), "You Floating", Toast.LENGTH_SHORT).show();
                    showUploadDialog();
                    break;
            case R.id.btn_uploadDia:
                uploadFile(0);
                dialog.dismiss();
                //Toast.makeText(getContext(), "Upload", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.btn_cancelDia:
                dialog.dismiss();
               // Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.img_dialog:
                    imagePicker();
                   // Toast.makeText(getContext(), "add Image", Toast.LENGTH_SHORT).show();
                    break;
        }
    }

    void showUploadDialog(){
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.alw_upload_dialog);

        dialog.setCancelable(true);

        btnUploadDia = dialog.findViewById(R.id.btn_uploadDia);
        btnCancelDia = dialog.findViewById(R.id.btn_cancelDia);
        imgAddPhoto = dialog.findViewById(R.id.img_dialog);

        btnUploadDia.setOnClickListener(this);
        btnCancelDia.setOnClickListener(this);
        imgAddPhoto.setOnClickListener(this);

        dialog.show();
    }

    private void onSignedInInitilize(String username){
        mUsername = username;
        attachDatabaseReadListener();
        //Toast.makeText(getContext(),"here 11",Toast.LENGTH_SHORT).show();
    }

    private void onSignedOutCleanup(){
        mUsername = ANONYMOUS;
        //mMessageAdapter.clear();
        arrAlways.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){
        if(mChildEventListenerTL == null) {
            mChildEventListenerTL = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    AlwaysModel alwaysModel = dataSnapshot.getValue(AlwaysModel.class);
                    arrAlways.add(0,alwaysModel);
                    alwaysAdapter.notifyDataSetChanged();
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
            alwaysDatabaseReferenceTL.addChildEventListener(mChildEventListenerTL);
        }
    }

    private  void detachDatabaseReadListener(){
        if(mChildEventListenerTL != null){
            alwaysDatabaseReferenceTL.removeEventListener(mChildEventListenerTL);
            mChildEventListenerTL = null;
        }
    }


    public void imagePicker(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.setType("image/png");
        intent.setType("image/gif");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Toast.makeText(getContext()," " +requestCode + "  "+ resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == RC_PHOTO_PICKER && data!= null && resultCode == RESULT_OK
                && data.getData()!=null){
            imgUrl = data.getData();

            Picasso.with(getContext()).load(imgUrl).into(imgAddPhoto);
            imgAddPhoto.setImageURI(imgUrl);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFile(int flag){

        final String dateUpload = DateFormat.getDateTimeInstance().format(new Date());
        if(imgUrl!=null && flag == 0){
            final StorageReference fileReference = alwaysStorageReference.child(System.currentTimeMillis()+"."
                    + getFileExtension(imgUrl));

            fileReference.putFile(imgUrl)
                    .continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {   //Toast.makeText(MainActivity.this,"Here One",Toast.LENGTH_SHORT).show();
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        // Toast.makeText(this,"Here One",Toast.LENGTH_SHORT).show();
                        Uri downloadUri = task.getResult();
                        String myUri = downloadUri.toString();

                        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        String time = dateUpload;

                        AlwaysModel uploadPost = new AlwaysModel(name,
                                time,
                                downloadUri.toString(),R.drawable.always_logo);
                        String uploadId = alwaysDatabaseReferenceTL.push().getKey();
                        alwaysDatabaseReferenceTL.child(uploadId).setValue(uploadPost);
                        //progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(),"Bingo! Upload Successful.",Toast.LENGTH_LONG).show();
                    }

                    //Picasso.with(getContext()).load(R.drawable.add_photo_img).resize(300,300).onlyScaleDown().into(imgAlw);

                }
            });
        }

        else{
            Toast.makeText(getContext(),"No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

}
