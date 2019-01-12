package com.example.lovro.myapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Offer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.view.View.GONE;

public class AddOfferActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_GALLERY = 1;
    private static final int REQUEST_CODE_PICTURE_FROM_GALLERY = 2;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 4;
    private ImageView offerPhoto;
    private Uri offerUriPicture=null;
    private Offer currentOffer=new Offer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        offerPhoto=findViewById(R.id.imageHolder);

        initListeners();


    }

    private void initListeners() {
        offerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddOfferActivity.this);
                dialog.setContentView(R.layout.camra_gallery_picker);
                Button galleryButton = dialog.findViewById(R.id.galleryButton);
                Button cameraButton = dialog.findViewById(R.id.cameraButton);

                cameraButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (ActivityCompat.checkSelfPermission(AddOfferActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_CAMERA);
                        } else {
                            loadFromCamera();
                        }
                    }
                });

                galleryButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (ActivityCompat.checkSelfPermission(AddOfferActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_GALLERY);
                        } else {
                            loadFromGallery();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICTURE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri result = data.getData();
            Picasso.get().load(result).into(offerPhoto);
            offerUriPicture = Uri.fromFile(new File(getRealPathFromUri(AddOfferActivity.this, result)));

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Picasso.get().load(offerUriPicture).into(offerPhoto);
        }
    }

    private void loadFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);

        String picturesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        Uri data = Uri.parse(picturesPath);

        i.setDataAndType(data, "image/*");
        startActivityForResult(i, REQUEST_CODE_PICTURE_FROM_GALLERY);
    }

    private void loadFromCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(AddOfferActivity.this.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(AddOfferActivity.this, "com.example.android.crafteryFileProvider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        offerUriPicture = Uri.fromFile(image);
        return image;
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CODE_PERMISSION_GALLERY) {
                loadFromGallery();
            } else if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
                loadFromCamera();
            }
        } else {
            Toast.makeText(AddOfferActivity.this, "Need permission to do that action", Toast.LENGTH_SHORT);
        }
    }


}
