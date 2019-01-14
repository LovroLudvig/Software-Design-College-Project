package com.example.lovro.myapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lovro.myapplication.R;
import com.example.lovro.myapplication.domain.Story;
import com.example.lovro.myapplication.domain.User;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lovro.myapplication.network.InitApiService.apiService;

public class SuggestStoryActivity extends BasicActivity {

    private static final int REQUEST_CODE_PERMISSION_GALLERY = 1;
    private static final int REQUEST_CODE_PICTURE_FROM_GALLERY = 2;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 4;
    private static final int SELECT_VIDEO = 5;

    private ImageView storyPhoto;
    private Uri storyUriPicture=null;

    private EditText storyStatusEditText;
    private Button suggestStoryButton;

    private ImageView storyVideo;
    private Uri storyUriVideo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_story);
        storyPhoto=findViewById(R.id.imageHolder);

        storyStatusEditText=findViewById(R.id.storyStatusEditText);
        suggestStoryButton=findViewById(R.id.suggestStoryButton);

        storyVideo = findViewById(R.id.videoHolder);


        initListeners();
    }

    private void initListeners() {
        suggestStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storyStatusEditText.getText().toString().equals("")){
                    Toast.makeText(SuggestStoryActivity.this, "Please enter story text", Toast.LENGTH_SHORT).show();
                }else if(storyUriPicture==null){
                    Toast.makeText(SuggestStoryActivity.this, "Please add picture", Toast.LENGTH_SHORT).show();
                }else{
                    show_loading("Getting you details...");
                    apiService.getUserByUsername2(getUserAuth(),getSharedPreferences("UserData", MODE_PRIVATE).getString("username", "")).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            stop_loading();
                            if(response.isSuccessful()){
                                suggestStory(response.body());
                            }else{
                                try {
                                    showError(response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            stop_loading();
                            showError(t.getMessage());
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
        storyPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SuggestStoryActivity.this);
                dialog.setContentView(R.layout.camra_gallery_picker);
                Button galleryButton = dialog.findViewById(R.id.galleryButton);
                Button cameraButton = dialog.findViewById(R.id.cameraButton);

                cameraButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (ActivityCompat.checkSelfPermission(SuggestStoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                        if (ActivityCompat.checkSelfPermission(SuggestStoryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_GALLERY);
                        } else {
                            loadFromGallery();
                        }
                    }
                });
                dialog.show();
            }
        });

        storyVideo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SuggestStoryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_GALLERY); }
                    else {
                            loadVideo();
                }

            }
        });


    }

    private void suggestStory(User user) {
        show_loading("Suggesting story");
        apiService.suggestStory(getUserAuth(), new Story(storyStatusEditText.getText().toString(),user)).enqueue(new Callback<Story>() {
            @Override
            public void onResponse(Call<Story> call, Response<Story> response) {
                stop_loading();
                if(response.isSuccessful()){
                    uploadMedia(response.body());
                }else{
                    try {
                        showError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Story> call, Throwable t) {
                stop_loading();
                showError(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void uploadMedia(final Story story) {
        show_loading("Uploading image");
        File file = new File(decodeFile(storyUriPicture.getPath(),500,500));
        apiService.uploadStoryImage(getUserAuth(),String.valueOf(story.getId()),RequestBody.create(MediaType.parse("image/jpg"), file)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                stop_loading();
                if(response.isSuccessful()){
                    compressVideo();
                    if(storyUriVideo != null){
                        uploadVideo(story);
                    }else{
                        Toast.makeText(SuggestStoryActivity.this, "Story suggested", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    try {
                        showError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                stop_loading();
                showError(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private boolean checkSize(){
        File file = new File(storyUriVideo.getPath());
        Long size = file.length();
        Long sizeInKB = size/1024;
        Long sizeInMB = sizeInKB/1024;
        if(sizeInMB > 150){
            return false;
        }else{
            return true;
        }
    }

    private void compressVideo(){

    }

    private void uploadVideo(Story story){
        show_loading("Uploading video...");
        File video = new File(storyUriVideo.getPath());
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"),video);
        apiService.uploadStoryVideo(getUserAuth(),String.valueOf(story.getId()),videoBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                stop_loading();
                if(response.isSuccessful()){
                    Toast.makeText(SuggestStoryActivity.this, "Story suggested", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    try {
                        showError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                stop_loading();
                showError(t.getMessage());
                t.printStackTrace();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICTURE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri result = data.getData();
            Picasso.get().load(result).into(storyPhoto);
            storyUriPicture = Uri.fromFile(new File(getRealPathFromUri(SuggestStoryActivity.this, result)));
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Picasso.get().load(storyUriPicture).into(storyPhoto);
        }else if(requestCode == SELECT_VIDEO && resultCode == Activity.RESULT_OK){
            Uri selectedVideoUri = data.getData();
            storyUriVideo = Uri.fromFile(new File(getRealPathFromUri(SuggestStoryActivity.this,selectedVideoUri)));

            if(checkSize()){
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(storyUriVideo.getPath(),MediaStore.Images.Thumbnails.MINI_KIND);
                storyVideo.setImageBitmap(thumb);
            }else{
                storyUriVideo = null;
                Toast.makeText(this,"The video is too large!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,"Select a Video"),SELECT_VIDEO);
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
        if (pictureIntent.resolveActivity(SuggestStoryActivity.this.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(SuggestStoryActivity.this, "com.example.android.crafteryFileProvider", photoFile);
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
        storyUriPicture = Uri.fromFile(image);
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
            Toast.makeText(SuggestStoryActivity.this, "Need permission to do that action", Toast.LENGTH_SHORT);
        }
    }
}
