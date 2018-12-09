package com.example.rober.currencyconversion;

import android.util.Log;

import java.util.HashMap;
import java.util.TreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;
import com.google.gson.Gson;

/**
 * Where the actual calculation takes place.
 */
public class Computation {
    /** time of retrieval of the data.
     */
    private String time;
    /** the base case currency the user chose.
     */
    private static String basecase = "EUR";
    /** the target currency the user chose.
     */
    private static String target = "EUR";
    /** a TreeMap storing all the currency and their corresponding rate of exchange according to the
     * chosen base case.
     */
    public TreeMap<String, Double> bcrate = new TreeMap<>();
    /** a TreeMap storing all the currency and their corresponding rate of exchange.
     */
    private TreeMap<String, Double> rate = new TreeMap<>();
    /** The type of rate stored as a type object.
     */
    private Type type = new TypeToken<TreeMap<String, Object>>() { }.getType();

    /**initializing a instance of computation, with all data from the json object processed.
     * @param resp the json object to be processed.
     */
    public Computation(final JSONObject resp) throws IllegalArgumentException{
        if (resp != null) {
            try {
                String timestamp = Integer.toString(resp.getInt("timestamp")).substring(0, 3);
                time = timestamp.substring(0, 1) + " : " + timestamp.substring(2, 3);
                Gson gson = new Gson();
                ECBobj ecbObj = gson.fromJson(resp.toString(), ECBobj.class);
                Log.d("s", ecbObj.rates.toString());
                rate = new TreeMap<>(ecbObj.rates);
                setBaseCase("EUR");
                setTarget("EUR");
            } catch (JSONException e) {
                Log.e("APP->src", "Computation: unexpected json error", e);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
    /** Setting the base case of the currency conversion rate(default euro(EUR)).
     * @param abbrev  desired base case currency abbreviation as in 3 letters.
     */
    public String setBaseCase(String abbrev) {
        basecase = abbrev;
        if (abbrev == "EUR" || abbrev == null) {
            bcrate = new TreeMap<String, Double>(rate);
            return "Please select a currency";
        } else {
            double val = rate.get(abbrev);
            for (String cur : rate.keySet()) {
                bcrate.replace(cur, rate.get(cur) / val);
            }
            return abbrev + "selected";
        }
    }
    public String setTarget(String abbrev) {
        if (abbrev != null && rate.keySet().contains(abbrev)) {
            target = abbrev;
            return "Please select a currency";
        } else {
            return abbrev + "selected";
        }
    }
    public TreeMap getRate() {
        return rate;
    }

    /** The calculation functionality of the app, based a given number of a specific currency,
     * this function will update the TreeMap of result.
     * @param value the number input from the user.
     */
    public void calc(final double value) {
        double val = rate.get(target);
        for (String cur : rate.keySet()) {
            bcrate.replace(cur, value / rate.get(cur) * val);
            Log.d("d", value + "    " + rate.get(cur) + "  " + val + "\n");
        }

    }
}
