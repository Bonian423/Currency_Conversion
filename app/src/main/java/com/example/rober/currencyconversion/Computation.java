package com.example.rober.currencyconversion;

import android.util.Log;

import java.util.HashMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import java.lang.reflect.Type;
import java.util.Map;

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
    private static String basecase;
    /** the target currency the user chose.
     */
    private static String target;
    /** a HashMap storing all the currency and their corresponding rate of exchange according to the
     * chosen base case.
     */
    public HashMap<String, Double> bcrate;
    /** a HashMap storing all the currency and their corresponding rate of exchange.
     */
    private HashMap<String, Double> rate;
    /** The HashMap storing the onverted value of all currencies based on the user input.
     */
    private HashMap<String, Double> result;
    /** The type of rate stored as a type object.
     */
    private Type type = new TypeToken<HashMap<String, Object>>() { }.getType();

    /**initializing a instance of computation, with all data from the json object processed.
     * @param resp the json object to be processed.
     */
    public Computation(final JSONObject resp) {
        try {
            String timestamp = Integer.toString(resp.getInt("timestamp")).substring(0, 3);
            time =  timestamp.substring(0, 1) + " : " + timestamp.substring(2, 3);
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Double>>(){}.getType();
            ECBobj ecbObj = gson.fromJson(resp.toString(), ECBobj.class);
            Log.d("s", ecbObj.rates.toString());
            rate.putAll(ecbObj.rates);
            setBaseCase("EUR");
        } catch (JSONException e) {
            Log.e("APP->src", "Computation: unexpected json error", e);
        }
    }

    /** retrieve the conversion rate of each currency.
     * @param abbrev currency abbreviation as in 3 letters.
     * @return the exchange rate.
     */
    public double getRate(final String abbrev) {
        return bcrate.get(abbrev);
    }
    /** retrieve the stored target.
     * @return the exchange rate.
     */
    public String getTarget() {
        return target;
    }
    /** retrieve the stored target.
     * @return the exchange rate.
     */
    public String getBasecase() {
        return basecase;
    }
    /** Setting the base case of the currency conversion rate(default euro(EUR)).
     * @param abbrev  desired base case currency abbreviation as in 3 letters.
     */
    public void setBaseCase(final String abbrev) {
        basecase = abbrev;
        if (abbrev == "EUR") {
            bcrate = rate;
        } else {
            double val = rate.get(abbrev);
            for (String cur : rate.keySet()) {
                bcrate.replace(cur, rate.get(cur) / val);
            }
        }
    }
    public void setTarget(final String abbrev) throws IllegalArgumentException {
        if (abbrev != null && rate.keySet().contains(abbrev)) {
            target = abbrev;
        } else {
            throw new IllegalArgumentException("INVALID Target");
        }
    }
    /** The calculation functionality of the app, based a given number of a specific currency,
     * this function will update the HashMap of result.
     * @param abbrev specify the type of the entered value.
     * @param value the number input from the user.
     */
    public void calc(final String abbrev, final double value) {
        double val = rate.get(abbrev);
        for (String cur : rate.keySet()) {
            bcrate.replace(cur, value * rate.get(cur) / val);
        }
    }
    public void calc(final double value) {
        double val = rate.get(target);
        for (String cur : rate.keySet()) {
            bcrate.replace(cur, value * rate.get(cur) / val);
        }
    }
}
