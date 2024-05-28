package com.example.metoww;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PollenForecastActivity extends AppCompatActivity {

    private TextView tvPollenInfo;
    private static final String API_URL = "https://air-quality-api.open-meteo.com/v1/air-quality?latitude=52.52&longitude=13.41&hourly=pm10,pm2_5,alder_pollen,birch_pollen,grass_pollen,mugwort_pollen,olive_pollen,ragweed_pollen";
    private static final String TAG = "PollenForecastActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pollen_forecast);

        tvPollenInfo = findViewById(R.id.tvPollenInfo);

        fetchPollenData();
    }

    private void fetchPollenData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, API_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "API Response: " + response.toString());
                        try {
                            JSONObject hourly = response.getJSONObject("hourly");
                            JSONArray timeArray = hourly.getJSONArray("time");
                            JSONArray alderPollenArray = hourly.getJSONArray("alder_pollen");
                            JSONArray birchPollenArray = hourly.getJSONArray("birch_pollen");
                            JSONArray grassPollenArray = hourly.getJSONArray("grass_pollen");
                            JSONArray mugwortPollenArray = hourly.getJSONArray("mugwort_pollen");
                            JSONArray olivePollenArray = hourly.getJSONArray("olive_pollen");
                            JSONArray ragweedPollenArray = hourly.getJSONArray("ragweed_pollen");

                            StringBuilder pollenInfoBuilder = new StringBuilder();

                            for (int i = 0; i < timeArray.length(); i++) {
                                String time = timeArray.getString(i);
                                Double alderPollen = alderPollenArray.isNull(i) ? null : alderPollenArray.getDouble(i);
                                Double birchPollen = birchPollenArray.isNull(i) ? null : birchPollenArray.getDouble(i);
                                Double grassPollen = grassPollenArray.isNull(i) ? null : grassPollenArray.getDouble(i);
                                Double mugwortPollen = mugwortPollenArray.isNull(i) ? null : mugwortPollenArray.getDouble(i);
                                Double olivePollen = olivePollenArray.isNull(i) ? null : olivePollenArray.getDouble(i);
                                Double ragweedPollen = ragweedPollenArray.isNull(i) ? null : ragweedPollenArray.getDouble(i);

                                pollenInfoBuilder.append("시간: ").append(time)
                                        .append("\n오리나무 꽃가루: ").append(alderPollen != null ? alderPollen : "N/A")
                                        .append("\n자작나무 꽃가루: ").append(birchPollen != null ? birchPollen : "N/A")
                                        .append("\n잔디 꽃가루: ").append(grassPollen != null ? grassPollen : "N/A")
                                        .append("\n쑥 꽃가루: ").append(mugwortPollen != null ? mugwortPollen : "N/A")
                                        .append("\n올리브 꽃가루: ").append(olivePollen != null ? olivePollen : "N/A")
                                        .append("\n돼지풀 꽃가루: ").append(ragweedPollen != null ? ragweedPollen : "N/A")
                                        .append("\n\n");
                            }

                            if (pollenInfoBuilder.length() > 0) {
                                tvPollenInfo.setText(pollenInfoBuilder.toString());
                            } else {
                                tvPollenInfo.setText("No pollen data available.");
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Parsing error: ", e);
                            Toast.makeText(PollenForecastActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "API Request error: ", error);
                        Toast.makeText(PollenForecastActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
