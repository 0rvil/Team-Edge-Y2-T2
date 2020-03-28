package com.project.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.annotation.Nullable;

public class weatherActivity extends AppCompatActivity {
    public String api_key = "*********Your api key goes here *********";
    String city,city_firebase;
    EditText city_state;
    ArrayList<String> weather,cityname;
    ListView lvRss;
    Button btn;
    FirebaseFirestore fStore;
    String userID;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        city_state = findViewById(R.id.user_city_state);
        weather = new ArrayList<>();
        cityname = new ArrayList<>();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        lvRss = findViewById(R.id.list_view_weather);
        btn = findViewById(R.id.find_weather_btn);
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                try {  city_firebase = documentSnapshot.getString("City_state");
                    if(city_firebase.equals("null")){
                        btn.setVisibility(View.VISIBLE);
                        city_state.setVisibility(View.VISIBLE);
                    } else {
                        btn.setVisibility(View.INVISIBLE);
                        city_state.setVisibility(View.INVISIBLE);
                        city = city_firebase;
                        new ProcessInBackground().execute();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city = city_state.getText().toString();
                city_firebase = city;
                DocumentReference documentReference = fStore.collection("users").document(userID);
                documentReference.update("City_state",city_firebase);
                new ProcessInBackground().execute();
            }
        });

    }

    public InputStream getInputStream(URL url)
    {
        try
        {
            //openConnection() returns instance that represents a connection to the remote object referred to by the URL
            //getInputStream() returns a stream that reads from the open connection
            return url.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception>
    {
        ProgressDialog progressDialog = new ProgressDialog(weatherActivity.this);
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Dialog Message is displayed
            progressDialog.setMessage("Loading Weather.....Please Wait.....");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... params) {
            // Get the info from XMLPullParser
            try
            {

                URL url = new URL(String.format("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+api_key+"&mode=xml&units=imperial"));


                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");
                boolean insideItem = false;
                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    if (eventType == XmlPullParser.START_TAG)
                    {
                        if (xpp.getName().equalsIgnoreCase("current"))
                        {
                            insideItem = true;
                        }

                        else if (xpp.getName().equalsIgnoreCase("temperature"))
                        {
                            if (insideItem)
                            {
                                // extract the text between <temperature> and </temperature>
                                String weather_in_city = "The weather in "+city +" is "+xpp.getAttributeValue(null,"value") +" degrees fahrenheit.";
                                weather.add(weather_in_city);
                                System.out.println(weather);
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("country"))
                        {
                            if (insideItem)
                            {
                                cityname.add(xpp.getText());
                                System.out.println(cityname);
                            }
                        }
                    }
                    //if we are at an END_TAG and the END_TAG is called "item"
                    else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("current"))
                    {
                        insideItem = false;
                    }

                    eventType = xpp.next(); //move to next element
                }
            }
            // Exceptions
            catch (MalformedURLException e)
            {
                exception = e;
            }
            catch (XmlPullParserException e)
            {
                exception = e;
            }
            catch (IOException e)
            {
                exception = e;
            }

            return exception;
        }
        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);
            //Display the info
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(weatherActivity.this, android.R.layout.simple_list_item_1, weather);
            lvRss.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }

}
