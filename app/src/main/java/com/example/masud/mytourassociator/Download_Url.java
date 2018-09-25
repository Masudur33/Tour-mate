package com.example.masud.mytourassociator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Download_Url {

    public String readUrl(String myUrl) throws IOException {

        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlconnection = null;
        try {
            URL url = new URL(myUrl)
            urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.connect();
            inputStream = urlconnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer sb=new StringBuffer();
            String line="";
            while ( line = br.readLine( ) )!=null)
            {
               sb.append(line);
           }
           data=sb.toString();
           br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
      finally {
            inputStream.close();
            urlconnection.disconnect();
        }
        return data;
    }
}


