package com.example.rober.currencyconversion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

public class InfoPanel extends AppCompatActivity {
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoscreen);
        String[] info = {
                "Welcome to the help Section",
                "Button functions",
                "   Refresh",
                "       get the latest currency exchange rate from the European",
                "       Central Bank.",
                "   Go",
                "       Convert the amount of your entered currency into your",
                "       desired currency.",
                "   From",
                "       Selected the currency to be converted from.",
                "   To",
                "       Selected the target currency to converted to.",
                "Display",
                "   In the lower section of the app the current exchange rate is ",
                "   being displayed.",
                "",
                "Please Note that this app only provide conversion between rather popular currencies same as shown on the European Central Bank website"
                + "https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html"
        };
        TextView textView = findViewById(R.id.information);
        for (String a: info) {
            textView.append(a + "\n");
        }
    }
}
