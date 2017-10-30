package com.example.administrador.photointentactivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;

public class SenderReceiver {
    String stringStream;

    // "http://192.168.134.204/orders/index.php/services/add_product"
    public String getJSONfromURL(String stringURL, Hashtable params) {
        try {
            URL url = new URL(stringURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());

            writer.write( getWrappedToURL(params) );
            writer.flush();

            // Check the connection status
            if(urlConnection.getResponseCode() == 200) {
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                stringStream = sb.toString();

                reader.close();
                urlConnection.disconnect();
            }
            else {
                // Do something
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally{
        }
        return stringStream;
    }
    //////////////////////////////////////////////////////////////////////////////////////
    // "http://192.168.134.204/orders/index.php/services/add_product"
    public String getMessage(String stringURL, Hashtable params) {
        try {
            URL url = new URL(stringURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());

            writer.write( getWrappedToURL(params) );
            writer.flush();

            // Check the connection status
            if(urlConnection.getResponseCode() == 200) {
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                stringStream = sb.toString();

                reader.close();
                urlConnection.disconnect();
            }
            else {
                // Do something
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally{
        }
        return stringStream;
    }

    public String getWrappedToURL(Hashtable params) {
        String result="";
        int counter=0;

        Enumeration keys = params.keys();

        while(keys.hasMoreElements()) {
            if(counter>0){ result += "&"; }

            String key = (String) keys.nextElement();
            String value = (String) params.get(key);

            try {
                result += URLEncoder.encode(key, "UTF-8");
                result += "="+URLEncoder.encode(value,"UTF-8");
            }
            catch(UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            counter++;
        }
        return result;
    }
}
