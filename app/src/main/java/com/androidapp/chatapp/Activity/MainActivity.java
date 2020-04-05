package com.androidapp.chatapp.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidapp.chatapp.Adapter.ChatMessageAdapter;
import com.androidapp.chatapp.Model.ChatMessageDetailModel;
import com.androidapp.chatapp.R;
import com.androidapp.chatapp.Utils.CameraUtils;
import com.androidapp.chatapp.Utils.Global;
import com.androidapp.chatapp.Utils.KeyboardUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Global global;

    // Arraylist which is used to insert, fetch chat messgaes
    ArrayList<ChatMessageDetailModel> chatMessageDetailModelArrayList = new ArrayList<>();

    // Adapter and Recyler view to display chat messages
    ChatMessageAdapter chatMessageAdapter;
    RecyclerView recycler_view_chat;

    EditText edit_text_message;
    RelativeLayout relSelectImages,relSendMessage;
    String strEnteredMessage = "";

    String strChatMessageType = "";

    // Following variables used to select Image from Camera or from Gallery
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Bitmap bitmapUpdateProfile = null;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    File fileImage;
    String strFilePath = "";
    private String imageStoragePath = "";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 200;
    public static final String GALLERY_DIRECTORY_NAME = "chatapp";
    public static final String IMAGE_EXTENSION = "jpg";
    public static final int MEDIA_TYPE_IMAGE = 1;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        global = new Global(this);
        // In Meessage Type Paramter - 1 - Text, 2- Image
        // In Chat User Type Parameter - 1 - Receiver User, 2 - Sender User

        // Initially 4 messages has been bind in arraylist which defined in top
        chatMessageDetailModelArrayList.add(new ChatMessageDetailModel("31082018_111" ,
                getResources().getString(R.string.reveived_name),getResources().getString(R.string.reveived_message),
                getResources().getString(R.string.reveived_time),"1","1","",
                0));


        chatMessageDetailModelArrayList.add(new ChatMessageDetailModel("31082018_222" ,
                getResources().getString(R.string.sender_name),getResources().getString(R.string.sender_message),
                getResources().getString(R.string.sender_time),"1","2","",
               0));

        chatMessageDetailModelArrayList.add(new ChatMessageDetailModel("31082018_333" ,
                getResources().getString(R.string.sender_name),getResources().getString(R.string.sender_message),
                getResources().getString(R.string.sender_time),"2","2",
                "", R.drawable.ic_double_tick));

        chatMessageDetailModelArrayList.add(new ChatMessageDetailModel("31082018_444" ,
                getResources().getString(R.string.reveived_name),getResources().getString(R.string.reveived_message),
                getResources().getString(R.string.reveived_time),"2","1",
                "", R.drawable.ic_double_tick));

        chatMessageDetailModelArrayList.add(new ChatMessageDetailModel("31082018_999" ,
                getResources().getString(R.string.sender_name),getResources().getString(R.string.sender_message_two),
                getResources().getString(R.string.sender_time_two),"1","2",
                "", 0));

        try {
            Log.e("chatMessageDList",chatMessageDetailModelArrayList.size()+"_");

            // Bind arralyst to adapter
            chatMessageAdapter = new ChatMessageAdapter(MainActivity.this,chatMessageDetailModelArrayList); //new ArrayList<ChatMessage>()
            recycler_view_chat.setAdapter(chatMessageAdapter);
            recycler_view_chat.scrollToPosition(chatMessageAdapter.getItemCount()-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // A useful class use has been used to detect keyboard open or not to scroll bottom
        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                if(isVisible){
                    Log.e("isVisible","true");
                    if(chatMessageDetailModelArrayList.size() > 0){
                        // When user type, chat list will move to last position
                        recycler_view_chat.scrollToPosition(chatMessageAdapter.getItemCount()-1);
                    }

                }else {
                    Log.e("isVisible","false");
                }

            }
        });

        // Send message and bind to adapter
        relSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEnteredMessage = edit_text_message.getText().toString();

                if(strEnteredMessage.length() > 0){

                    // Constructor has been defined as per our requirement
                    chatMessageDetailModelArrayList.add(new ChatMessageDetailModel("31082018_555" ,
                            getResources().getString(R.string.sender_name),strEnteredMessage,
                            getResources().getString(R.string.sender_time),
                            "1","2","",0));

                 //   chatMessageAdapter.notifyDataSetChanged();
                    chatMessageAdapter.notifyItemInserted(chatMessageDetailModelArrayList.size()+1);
                    recycler_view_chat.scrollToPosition(chatMessageAdapter.getItemCount()-1);
                    edit_text_message.setText("");
                }
            }
        });

        // When user click on Camera icon, it will check whether device is
        // greater then M or not to ask for required permission
        relSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Method to ask for permission
                    checkAndRequestPermissions();
                } else {
                    selectImage();

                }

            }
        });

    }
    // Initialize required components
    private void initComponents() {

        recycler_view_chat = findViewById(R.id.recycler_view_chat);
        recycler_view_chat.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_chat.setHasFixedSize(true);
        edit_text_message = findViewById(R.id.edit_text_message);
        relSelectImages = findViewById(R.id.relSelectImages);
        relSendMessage = findViewById(R.id.relSendMessage);

        edit_text_message.setFilters(new InputFilter[]{new EmojiExcludeFilter()});

    }



    private class EmojiExcludeFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }


    // function to select image from gallery or camera
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        if (file != null) {
                            strFilePath = file.getName();
                            Log.e("CCCCCCCCCC", strFilePath + "_111");
                            fileImage = file;
                            //global.setPrefString(Constants.CUST_SELECTED_IMAGE_PATH_SELL,strFilePath);
                        }

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        if (file != null) {
                            strFilePath = file.getName();
                            //  global.setPrefString(Constants.CUST_SELECTED_IMAGE_PATH_SELL,strFilePath);
                            Log.e("CCCCCCCCCC", strFilePath + "_111");
                            fileImage = file;
                            imageStoragePath = file.getAbsolutePath();
                        }
                        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // onActivityResult is used to fetch image selected by user from camera or gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                try {

                    Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
                    Log.e("AAAAAAAAAAA", strFilePath + "_111");
                    Log.e("imageStoragePath", strFilePath + "_111");


                    ExifInterface ei = new ExifInterface(imageStoragePath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    // Bitmap rotatedBitmap = null;
                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            bitmapUpdateProfile = rotateBitmap(bitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            bitmapUpdateProfile = rotateBitmap(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            bitmapUpdateProfile = rotateBitmap(bitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            bitmapUpdateProfile = bitmap;

                    }
                    bitmapUpdateProfile = Bitmap.createScaledBitmap(bitmapUpdateProfile, 300, 300, false);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapUpdateProfile.compress(Bitmap.CompressFormat.JPEG, 70, stream);

                    byte[] byteArray = stream.toByteArray();
                    stream.flush();
                    stream.close();


                    strChatMessageType = "2";

                    Log.e("AAAAAAAAAAA", strFilePath + "_111");
                    Log.e("imageStoragePath", strFilePath + "_111");

                    chatMessageDetailModelArrayList.add(new ChatMessageDetailModel("31082018_333" ,
                            getResources().getString(R.string.sender_name),getResources().getString(R.string.sender_message),
                            getResources().getString(R.string.sender_time),"2","2",
                            "", R.drawable.ic_double_tick));
                    chatMessageAdapter.notifyItemInserted(chatMessageDetailModelArrayList.size()+1);
                    recycler_view_chat.scrollToPosition(chatMessageAdapter.getItemCount()-1);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                // successfully captured the image
                // display it in image view
                //  previewCapturedImage();


            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null,
                        null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                Log.e("picturePath", picturePath);

                c.close();
                //  Bitmap bitmap;
                bitmapUpdateProfile = global.getSampleBitmapFromFile(picturePath, 300, 300);

                fileImage = new File(picturePath);


                try {
                    ExifInterface exif = null;
                    File pictureFile = new File(picturePath);
                    exif = new ExifInterface(pictureFile.getAbsolutePath());

                    int orientation = ExifInterface.ORIENTATION_NORMAL;
                    if (exif != null)
                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            bitmapUpdateProfile = rotateBitmap(bitmapUpdateProfile, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            bitmapUpdateProfile = rotateBitmap(bitmapUpdateProfile, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            bitmapUpdateProfile = rotateBitmap(bitmapUpdateProfile, 270);
                            break;
                    }
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapUpdateProfile.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    byte[] byteArray = stream.toByteArray();
                    stream.flush();
                    stream.close();

                    if (fileImage != null && fileImage.exists()) {
                        Log.e("GetPath44:", fileImage.getName());
                        strFilePath = fileImage.getName();
                        Log.e("strFilePath:", strFilePath);

                        //   tvFilepathname.setText(strFilePath);
                        strChatMessageType = "2";

                        chatMessageDetailModelArrayList.add(new ChatMessageDetailModel("31082018_333" ,
                                getResources().getString(R.string.sender_name),getResources().getString(R.string.sender_message),
                                getResources().getString(R.string.sender_time),"2","2",
                                "", R.drawable.ic_double_tick));

                        chatMessageAdapter.notifyItemInserted(chatMessageDetailModelArrayList.size()+1);
                        recycler_view_chat.scrollToPosition(chatMessageAdapter.getItemCount()-1);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private boolean checkAndRequestPermissions() {
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }


        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new
                    String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }else {
            selectImage();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        // process the normal flow
                        selectImage();

                    } else {
                        if ( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                &&ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                            showDialogOK("Service Permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:com.androidapp.chatapp")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }


}
