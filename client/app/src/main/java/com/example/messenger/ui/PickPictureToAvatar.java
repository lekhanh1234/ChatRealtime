package com.example.messenger.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTListPictureGallary;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class PickPictureToAvatar extends AppCompatActivity {
    private RecyclerView RV_listAllPictureGallary;
    ArrayList<String> allURLPictureGallary=new ArrayList<>();
    ADTListPictureGallary ADTListPictureGallary;
    Button BTN_savePictureforProfile;
    RoundedImageView IMG_ImageProfilePreview;
    private String URL_imageProfile=null;



    public String getURL_imageProfile() {
        return URL_imageProfile;
    }

    public void setURL_imageProfile(String URL_imageProfile) {
        this.URL_imageProfile = URL_imageProfile;
    }


    private final int REPONSE_OK=1;
    private final int REPONSE_FAIL=0;
    private final String URL_profile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture_for_avatar);
        RV_listAllPictureGallary=findViewById(R.id.RV_listpiture);
        ConstraintLayout CL_setPictureProfile=findViewById(R.id.CL_setPitureProfile);
        BTN_savePictureforProfile=findViewById(R.id.BTN_save);
        IMG_ImageProfilePreview=findViewById(R.id.IMG_ImageProfilePreview);
        CL_setPictureProfile.setVisibility(View.GONE);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Chọn ảnh");
        allURLPictureGallary=getAllUrlPictureGellary();
        ADTListPictureGallary =new ADTListPictureGallary(this,allURLPictureGallary);
        RV_listAllPictureGallary.setAdapter(ADTListPictureGallary);
        RV_listAllPictureGallary.setLayoutManager(new GridLayoutManager(this,3));

        BTN_savePictureforProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tai day. sau khi co duong dan. chung ta se tien hanh lay toan bo cac byte va dua vao infoMyAccount.
                // kiem tra ket noi internet. neu khong ton tai in ra thong bao loi
                if(!CheckInternet.isNetworkAvailable(PickPictureToAvatar.this)){
                    Toast.makeText(PickPictureToAvatar.this,"kiểm tra internet của bạn",Toast.LENGTH_LONG).show();
                    return;
                }
                // tien hanh thay doi thong tin tren  may chu
                byte[] avata = getAvataByteArray(getURL_imageProfile());
                String[] urlInfo =getURL_imageProfile().split("\\.");
                new OperationServer().changeAvatar(avata,urlInfo[urlInfo.length-1]);
                // thay doi luon trong app cua chung ta
                InfoMyAccount.setAvata(avata);
                setResult(200,null);
                finish();
            }
        });
    }
    public ArrayList<String> getAllUrlPictureGellary(){

        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        ArrayList<String> resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null) {
            c = managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst())) {
            do {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try {
                    dirList.add(tempDir);
                } catch (Exception e) {

                }

            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for (int i = 0; i < dirList.size(); i++) {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if (imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {

                    if (imagePath.isDirectory()) {
                        imageList = imagePath.listFiles();

                    }
                    if (imagePath.getName().contains(".jpg") || imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg") || imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                    ) {
                        String path = imagePath.getAbsolutePath();
                        resultIAV.add(path);


                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resultIAV;
    }
    public byte[] getAvataByteArray(String URL){
        File file = new File(URL);
        byte[] byteImage = null;
        try {
            FileInputStream is = new FileInputStream(file);
            byteImage =new byte[is.available()];
            is.read(byteImage);
        } catch (IOException e) {
           e.getMessage();
        }
        return byteImage;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.CL_setPitureProfile).getVisibility()==View.VISIBLE){
            findViewById(R.id.CL_setPitureProfile).setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}