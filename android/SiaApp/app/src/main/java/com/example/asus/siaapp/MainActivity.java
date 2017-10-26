package com.example.asus.siaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

//import android.graphics.Camera;

public class MainActivity extends AppCompatActivity {

    private WebSocketClient mWebSocketClient;
    static Camera camera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        connectWebSocket();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//
//                Thread thread = new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        try  {
//                            sendRequest(null);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
////
//                thread.start();

                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("wss://echo.websocket.org:80");
//            uri = new URI("wss://54.251.191.230");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("before openning");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                System.out.println("after openning");
//                PrintWriter toServer = new  PrintWriter(clientSocket.getOutputStream(), true);     // write user input to the socket
//                toServer.println(fromKeyboard);     // create input stream from server
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.message);
                        textView.setText(textView.getText() + "\n" + message);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public void sendMessage(View view) {
        EditText editText = (EditText)findViewById(R.id.message);
        mWebSocketClient.send(editText.getText().toString());
        editText.setText("");
    }

    public void sendRequest(Bitmap bm){
        try {
            String charset = "UTF-8";
            String requestURL = "http://54.251.191.230/api/dims"; //"http://localhost:8000/api/dims";
//           String requestURL = "https://www.google.com.sg";
//            String file_path = "/home/anh_huynh2111/Desktop/Object_size/akjdf.png";
//            File uploadedFile = new File(file_path);

            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            System.out.println("1");
            multipart.addFormField("width", "1");
            multipart.addHeaderField("width", "1");
//            multipart.addFilePart("image", uploadedFile);
            multipart.addFilePart("image", bm);
            String response = multipart.finish(); // response from server.
            System.out.println("2");
            System.out.println("The response is " + response);
//            File myFile = new File("word.txt");
//            System.out.println("Attempting to read from file in: "+myFile.getCanonicalPath());
        } catch(Exception e) {
            System.out.println("the error is "+ e.getMessage());
            e.printStackTrace();
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;



//
    Camera.PictureCallback jpegCallBack=new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            final Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);;

            System.out.println("Take picture already ");
            ImageView iv = (ImageView) findViewById(R.id.ReturnedImage);
            iv.setImageBitmap(bmp);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        sendRequest(bmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
//
            thread.start();
            camera.release();

        }
    };


    //TODO: clean up
    private void dispatchTakePictureIntent() {
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(CodeScanner.this, new String[]{android.Manifest.permission.CAMERA}, 50);
//        }
//        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//
//            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
//                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
//        }
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//        String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
//        File imageFile = new File(imageFilePath);
//        Uri imageFileUri = Uri.fromFile(imageFile); // convert path to Uri
//
//        Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////        it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
//        startActivityForResult(it, 1);
//        Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//        startActivityForResult(it, 0);
        System.out.println("About to open");
        camera = Camera.open(0);
        camera.takePicture(null, null, null, jpegCallBack);
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri imageUri = getOutputMediaFile();
//                //Uri.fromFile(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"fname_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
//        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
//        startActivity(intent);


//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                ex.printStackTrace();
//            }
//            System.out.println("CREATED");
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            // Get Extra from the intent
            Bundle extras = data.getExtras();
            // Get the returned image from extra
            final Bitmap bmp = (Bitmap) extras.get("data");

            System.out.println("Take picture already ");
            ImageView iv = (ImageView) findViewById(R.id.ReturnedImage);
            iv.setImageBitmap(bmp);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        sendRequest(bmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
//
            thread.start();
        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            ImageView mImageView = new ImageView();
//            mImageView.setImageBitmap(imageBitmap);
//        }
//    }

//    String mCurrentPhotoPath;
//
//    private Uri getOutputMediaFile() {
//
//        File mediaStorageDir = Environment.getExternalStorageDirectory();
//
//                //new File(
////                Environment.getExternalStorageDirectory(), "." + SyncStateContract.Constants.CONTAINER);
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            mediaStorageDir.mkdirs();
//        }
//
//        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                + "IMG_" + System.currentTimeMillis() + ".png");
//        Uri uri = null;
//        if (mediaFile != null) {
//            uri = Uri.fromFile(mediaFile);
//
//        }
//        return uri;
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

//    public void capturePhoto() {
//
//    }
}
