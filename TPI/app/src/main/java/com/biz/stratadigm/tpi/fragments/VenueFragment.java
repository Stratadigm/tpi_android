package com.biz.stratadigm.tpi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.biz.stratadigm.tpi.activity.MainActivity;
import com.biz.stratadigm.tpi.components.CustomEditText;
import com.biz.stratadigm.tpi.components.CustomTextView;
import com.biz.stratadigm.tpi.tools.Constant;
import com.biz.stratadigm.tpi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tamara on 12/11/16.
 * VenueFragment class
 */

public class VenueFragment extends Fragment {
    private CustomEditText mEtName;
    private CustomTextView mPossiton;
    private Button mSend;
    private HashMap<String,Float> mLocation = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venue_framgent, container, false);

        mEtName = (CustomEditText) view.findViewById(R.id.editTextName);
        mSend = (Button) view.findViewById(R.id.buttonSend);
        mPossiton = (CustomTextView) view.findViewById(R.id.possition);
        mPossiton.setText("Longitude " + MainActivity.longitude + " Latitude " + MainActivity.latitude);
        mLocation.put("Lat",Float.parseFloat(""+MainActivity.latitude));
        mLocation.put("Lng",Float.parseFloat(""+MainActivity.longitude));

        mSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    sendVenue();
                } catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(),"Wrong input",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
    /**
     * Add new venue on server
     */
    private void sendVenue() {


        Map<String, Object> params = new HashMap<String, Object>();
       // JSONObject ob = new JSONObject(mLocation);
        params.put("name", mEtName.getText().toString());
        params.put("location", mLocation);
        JsonObjectRequest stringReguest = new JsonObjectRequest(Request.Method.POST, Constant.VENUES,

                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject o = new JSONObject(String.valueOf(response));
                            Log.e("tamara", response.toString());
                            Toast.makeText(getActivity().getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        // inspect type of error
                        if (networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                        }
                        if (error instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        } else if (error instanceof NoConnectionError) {
                            Log.e("Volley", "NoConnectionError");
                        } else if (error instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError");
                        } else if (error instanceof ServerError) {
                            Log.e("Volley", "ServerError");
                        } else if (error instanceof NetworkError) {
                            Log.e("Volley", "NetworkError");
                        } else if (error instanceof ParseError) {
                            Log.e("Volley", "ParseError");
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("USerFragment-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringReguest);
    }
}
