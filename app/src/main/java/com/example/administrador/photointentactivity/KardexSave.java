package com.example.administrador.photointentactivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class KardexSave extends Activity {



    /** Items entered by the user is stored in this ArrayList variable */
    ArrayList<SpinnerModel> list = new ArrayList<SpinnerModel>();

    /** Declaring an ArrayAdapter to set items to ListView */
    SpinnerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kardex_save);
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

                JSONObject jsonRootObject = new JSONObject(jsonstring);

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




}
