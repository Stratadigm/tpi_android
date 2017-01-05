package com.biz.stratadigm.tpi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
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
import com.biz.stratadigm.tpi.tools.Constant;
import com.biz.stratadigm.tpi.R;
import com.biz.stratadigm.tpi.components.CustomEditText;

import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tamara on 12/11/16.
 * ThaliFragment class
 */

public class ThaliFragment extends Fragment {

    // components
    private static final int PICK_IMAGE_REQUEST = 1;
    private CustomEditText target, price;
    public static CustomEditText venue;
    private Button mButSend;
    private Switch switcher;
    private CheckBox north, west, east, south;
    String region;
    private String filePath;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thail_framgent, container, false);

        // Init components and listneres
        sharedPreferences = getActivity().getSharedPreferences(Constant.TAG, Context.MODE_PRIVATE);
        mButSend = (Button) view.findViewById(R.id.btsend);
        target = (CustomEditText) view.findViewById(R.id.taget);
        price = (CustomEditText) view.findViewById(R.id.price);
      //  price.setInputType(InputType.NU);
        venue = (CustomEditText) view.findViewById(R.id.venue);
        venue.setInputType(InputType.TYPE_NULL);
        venue.setText(sharedPreferences.getString("venue",""));

        switcher = (Switch) view.findViewById(R.id.limited);
        north = (CheckBox) view.findViewById(R.id.north);
        west = (CheckBox) view.findViewById(R.id.west);
        south = (CheckBox) view.findViewById(R.id.south);
        east = (CheckBox) view.findViewById(R.id.east);

        // Chack region
        north.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    region = "north";
                    west.setChecked(false);
                    east.setChecked(false);
                    south.setChecked(false);
                }
            }
        });
        west.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    region = "west";
                    north.setChecked(false);
                    east.setChecked(false);
                    south.setChecked(false);
                }
            }
        });
        east.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    region = "east";
                    west.setChecked(false);
                    north.setChecked(false);
                    south.setChecked(false);
                }
            }
        });
        south.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    region = "south";
                    west.setChecked(false);
                    east.setChecked(false);
                    north.setChecked(false);
                }
            }
        });


        mButSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendThali();
                } catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(),"Wrong input",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    // Upload picture on server

    /**
     * Sending thali on server
     */
    public void sendThali() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("target", target.getText().toString());
        params.put("venue", Integer.parseInt(venue.getText().toString()));
        params.put("limited", switcher.isChecked());
        params.put("region", region);
        if(price.getText().toString().equalsIgnoreCase("")) {
            params.put("price", Integer.parseInt("0"));
        } else {
            params.put("price", Integer.parseInt(price.getText().toString()));
        }
        params.put("photo", "");
        //  params.put("action",null);
        JsonObjectRequest stringReguest = new JsonObjectRequest(Request.Method.POST, Constant.THALIS,

                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity().getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                        Log.e("tamara", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
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

    public void selected() {
        venue.setText(sharedPreferences.getString("venue",""));
    }
}