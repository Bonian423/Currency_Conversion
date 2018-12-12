package com.example.rober.currencyconversion;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;
import java.text.DecimalFormat;

/**
 * Main screen for our API testing app.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Currency Conversion";

    /** Request queue for our network requests. */
    private static RequestQueue requestQueue;
    /** Data.
     */
    private Computation data;
    /** the initial value of the string.
     */
    /**
     * Run when our activity comes into view.
     *
     * @param savedInstanceState state that was saved by the activity last time it was paused
     */
    private Button go;
    private Button refresh;
    private Spinner from;
    private Spinner to;
    private EditText number;
    private TextView display;
    private TextView display2;
    private FloatingActionButton info;
    private TextView result;
    private String[] abbreviations = new String[] {
            "Euro (EUR)",
            "US dollar (USD)",
            "Chinese Renminbi (CNY)",
            "Canadian dollar (CAD)",
            "Swiss franc (CHF)",
            "Pound sterling (GBP)",
            "Japanese yen (JPY)",
            "Hong Kong dollar (HKD)",
            "New Zealand dollar (NZD)",
            "Swedish krona (SEK)",
            "Danish krone (DKK)",
            "Norwegian krone (NOK)",
            "Singapore dollar (SGD)",
            "Czech koruna (CZK)",
            "Mexican peso (MXN)",
            "Polish zloty (PLN)",
            "Bulgarian lev (BGN))",
            "Russian rouble (RUB)",
            "Turkish lira (TRY)",
            "South African rand (ZAR)",
            "Hungarian forint (HUF)",
            "Romanian leu (RON)",
            "Icelandic krona (ISK)",
            "Australian dollar (AUD)",
            "Brazilian real (BRL)",
            "Indonesian rupiah (IDR)",
            "Israeli shekel (ILS)",
            "Indian rupee (INR)",
            "South Korean (KRW)",
            "Malaysian ringgit (MYR)",
            "Philippine piso (PHP)",
            "Thai baht (THB)",
            "Croatian kuna (HRK)"};
    private String[] names = new String[]{
            "EUR",
            "USD",
            "CNY",
            "CAD",
            "CHF",
            "GBP",
            "JPY",
            "HKD",
            "NZD",
            "SEK",
            "DKK",
            "NOk",
            "SGD",
            "CZK",
            "MXN",
            "PLN",
            "BGN",
            "RUB",
            "TRY",
            "ZAR",
            "HUF",
            "RON",
            "ISK",
            "AUD",
            "BRL",
            "IDR",
            "ILS",
            "INR",
            "KRW",
            "MYR",
            "PHP",
            "THB",
            "HRK"};
    private ArrayList<String> countrynames = new ArrayList<>(Arrays.asList(names));
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set up a queue for our Volley requests
        // Load the main layout for our activity
        setContentView(R.layout.mainactivity);
        requestQueue = Volley.newRequestQueue(this);
        // Attach the handler to our UI button
        //Component initiation
        go = (Button) findViewById(R.id.go);
        refresh = (Button) findViewById(R.id.refresh);
        from = (Spinner) findViewById(R.id.from);
        to = (Spinner) findViewById(R.id.to);
        number = (EditText) findViewById(R.id.enter);
        display = (TextView) findViewById(R.id.display);
        display2 = (TextView) findViewById(R.id.display2);
        info = (FloatingActionButton) findViewById(R.id.info);
        result = (TextView) findViewById(R.id.result);
        //Spinner initiation;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(adapter);
        from.setOnItemSelectedListener(this);
        to.setAdapter(adapter);
        to.setOnItemSelectedListener(this);
        startAPICall();
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InfoPanel.class));
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(getBaseContext(),"Refreshing API", Toast.LENGTH_LONG);
                startAPICall();
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (from.getSelectedItem() == null
                        || to.getSelectedItem().toString() == null
                        || from.getSelectedItem().toString() == "select"
                        || to.getSelectedItem().toString() == "select") {
                    Toast.makeText(getBaseContext(), "Please choose a Currency of preference" , Toast.LENGTH_LONG);
                }
                if (number.getText().toString().equals("") || Double.valueOf(number.getText().toString()) < 0) {
                    Toast.makeText(getBaseContext(), "Please enter a valid number" , Toast.LENGTH_LONG);

                }
                else {
                    Toast.makeText(getBaseContext(), data.setBaseCase(String.valueOf(to.getSelectedItem())), Toast.LENGTH_LONG);
                    Toast.makeText(getBaseContext(), data.setTarget(String.valueOf(to.getSelectedItem())), Toast.LENGTH_LONG);
                    data.calc(Double.valueOf(number.getText().toString()));
                    result.setText(new DecimalFormat("#0.00").format(data.bcrate.get(String.valueOf(from.getSelectedItem()))));
                }
            }
        });
    }

    /**
     * Make an API call.
     */
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://data.fixer.io/api/latest?access_key=5780d1e929c01e066d3c8b600e87827d",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            /** creating a computation instance to store all the calculation stuff*/
                            data = new Computation(response);
                            for (String key: data.bcrate.keySet()) {
                                if(countrynames.contains(key)) {
                                    Log.d("d", abbreviations[countrynames.indexOf(key)]);
                                    display.append(abbreviations[countrynames.indexOf(key)] + "\n");
                                    display2.append(data.getRate().get(key) + "\n");
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getBaseContext(), "From " + parent.getItemIdAtPosition(position), Toast.LENGTH_LONG);
        String ch = (String) parent.getItemAtPosition(position);
        if(view == from) {
            data.setBaseCase(ch);
        }
        if(view == to) {
            data.setTarget(ch);
        }
    }
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
