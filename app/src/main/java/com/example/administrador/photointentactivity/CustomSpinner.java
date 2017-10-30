package com.example.administrador.photointentactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrador on 20/10/2017.
 */

public class CustomSpinner extends Activity implements View.OnClickListener{
    /** Items entered by the user is stored in this ArrayList variable */
    ArrayList<SpinnerModel> list = new ArrayList<SpinnerModel>();

    /** Declaring an ArrayAdapter to set items to ListView */
    SpinnerAdapter adapter;



    private static final int ACTION_TAKE_PHOTO_S = 2;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

    private ImageView mImageView;

    private Bitmap mImageBitmap;

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    /******************************************************************/
    public String SERVER = "http://192.168.132.6/test_android/file_upload.php",
            timestamp;
    private static final String TAG = PhotoIntentActivity.class.getSimpleName();
    /******************************************************************/

    private String mCurrentPhotoPath;


    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";


    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_spinner);

        /** Defining the ArrayAdapter to set items to Spinner Widget */
        adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, list);

        /** Defining click event listener for the button */
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText txtItem = (EditText) findViewById(R.id.editText);

                SpinnerModel newSpinnerModel = new SpinnerModel(0, txtItem.getText().toString(), 0);

                list.add(newSpinnerModel);




                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
                Date date = new Date();

                txtItem.setText(dateFormat.format(date));
                adapter.notifyDataSetChanged();
            }
        };





        /** Getting a reference to button object of the resource activity_main */
        Button btnAdd = (Button) findViewById(R.id.button);
        Button btnPopulate = (Button) findViewById(R.id.buttonPopulate);

        /** Setting click listener for the button */
        btnAdd.setOnClickListener(listener);
        btnPopulate.setOnClickListener(this);

        /** Getting a reference to Spinner object of the resource activity_main */
        spinner = (Spinner) findViewById(R.id.my_spinner);

        /** Setting the adapter to the ListView */
        spinner.setAdapter(adapter);

        /** Adding radio buttons for the spinner items */
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);













        mImageView = (ImageView) findViewById(R.id.imageView);

        mImageBitmap = null;


        Button picBtn = (Button) findViewById(R.id.btnIntend);


        Button picSBtn = (Button) findViewById(R.id.btnIntendS);
        setBtnListenerOrDisable(
                picSBtn,
                mTakePicSOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        /******************************************/
        Button coppyBtn = findViewById(R.id.buttonCoppy);


        coppyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maintenanceAdd();
            }
        });

        /******************************************/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }



    }

    @Override
    public void onClick(View v) {
        String params[] = {"location_id"};
        new ProcessJSON().execute(params);
                /*list.add("load from thread");
                adapter.notifyDataSetChanged();*/
    }






    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String ... params){


            String text = "";
            BufferedReader reader=null;

            String stringURL = "http://192.168.132.6/projecttaskmanager_web/index.php/services/list_locations";

            Hashtable hashparams =new Hashtable();

            hashparams.put("post_content",params[0]);

            SenderReceiver sender = new SenderReceiver();

            String  jsonstring= sender.getMessage(stringURL, hashparams);



            try{

                JSONObject  jsonRootObject = new JSONObject(jsonstring);

                JSONArray jsonArray = jsonRootObject.optJSONArray("list_locations");

                String strAdd = "";

                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id_location = Integer.parseInt(jsonObject.optString("id_location").toString());
                    String location_name = jsonObject.optString("location_name").toString();
                    String level = jsonObject.optString("level").toString();

                    SpinnerModel newSpinnerModel = new SpinnerModel(id_location, location_name, Integer.parseInt(level));

                    list.add(newSpinnerModel);
                }
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }

                /*JSONArray st = new JSONArray(strjson);
                for (int i = 0; i < st.length(); i++) {

                    JSONObject obj = st.getJSONObject(i);

                    list.add(obj.getString("pais"));
                    // loop and add it to array or arraylist
                }*/
            //DEFAULT not important
            return "";

        }


        protected void onPostExecute(String stream) {
            //startActivity(new Intent(CustomSpinner.this, PhotoIntentActivity.class));

            adapter.notifyDataSetChanged();
        }

        /* protected void onPostExecute(String stream) {
            startActivity(new Intent(CustomSpinner.this, PhotoIntentActivity.class));
        }*/



       // onPostExecute() end
    } // ProcessJSON class end





















    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }


    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);

        mImageView.setVisibility(View.VISIBLE);

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



        startActivityForResult(takePictureIntent, actionCode);
    }


    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);

        mImageView.setVisibility(View.VISIBLE);

    }




    Button.OnClickListener mTakePicSOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                }
            };



    /**********************************************************************/
    public void maintenanceAdd(){
        Bitmap bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();

        //SERVER = "http://192.168.132.6/index.php/services/maintenance_add";
        SERVER = "http://192.168.132.6/projecttaskmanager_web/index.php/services/add_support";

        SpinnerModel model = (SpinnerModel) spinner.getSelectedItem();

        String params[] = {""+model.getId()};
        new CustomSpinner.Upload(bitmap,"IMG_"+timestamp).execute(params);

    }
    /***********************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case ACTION_TAKE_PHOTO_S: {
                if (resultCode == RESULT_OK) {
                    handleSmallCameraPhoto(data);
                }
                break;
            } // ACTION_TAKE_PHOTO_S


        } // switch
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);

        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);

        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );

    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }





    /********************************************************************/

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    //async task to upload image
    private class Upload extends AsyncTask<String,Void,String> {
        private Bitmap image;
        private String name;

        public Upload(Bitmap image, String name) {
            this.image = image;
            this.name = name;
        }

        @Override
        protected String doInBackground(String... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            //generate hashMap to store encodedImage and the name
            HashMap<String, String> detail = new HashMap<>();


            detail.put("location_id", (String) params[0]);
            //detail.put("location_id", "2");


            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
            Date date = new Date();
            detail.put("name", dateFormat.format(date));



            detail.put("image", encodeImage);

            try {
                //convert this HashMap to encodedUrl to send to php file
                String dataToSend = hashMapToUrl(detail);
                //make a Http request and send data to saveImage.php file
                String response = Request.post(SERVER, dataToSend);

                //return the response
                return response;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ERROR  " + e);
                return null;
            }
        }
    }
}