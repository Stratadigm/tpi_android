package com.biz.stratadigm.tpi.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.biz.stratadigm.tpi.R;
import com.biz.stratadigm.tpi.activity.MainActivity;
import com.biz.stratadigm.tpi.activity.StartActivity;
import com.biz.stratadigm.tpi.components.CustomEditText;
import com.biz.stratadigm.tpi.tools.Constant;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tamara on 12/22/16.
 */

public class LoginFragment extends Fragment {
    private CustomEditText mEtPass, mEtEmail;
    private Button mConfirm;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        mEtEmail = (CustomEditText) view.findViewById(R.id.editTextEmail);
        mEtPass = (CustomEditText) view.findViewById(R.id.editTextPass);
        mEtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mConfirm = (Button) view.findViewById(R.id.buttonLogin);
        sharedPreferences = getActivity().getSharedPreferences(Constant.TAG, Context.MODE_PRIVATE);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerUser();
                } catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(),"Wrong input",Toast.LENGTH_SHORT).show();
                }

              //  startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
            }
        });
        return view;
    }

    /**
     * Method for reistration user on server. Using volley request
     */
    private void registerUser() {


        Map<String, String> params = new HashMap<String, String>();

        params.put("email", mEtEmail.getText().toString());//param email of user
        params.put("password", mEtPass.getText().toString());

        //making json object of params
        JsonObjectRequest stringReguest = new JsonObjectRequest(Request.Method.POST, "https://thalipriceindex.appspot.com/token_auth",

                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tamara", response.toString());
                        try {
                            String token = response.getString("token");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", token);
                            editor.apply();
                            loginToken(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//
                        Toast.makeText(getActivity().getApplicationContext(), "No that user", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("USerFragment-agent", System.getProperty("http.agent"));
                return headers; // headers of request (needs for https)
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringReguest);//post request on queue
    }

    public void loginToken(final String token) {
        final Map<String, String> params = new HashMap<String, String>();

        //making json object of params
        StringRequest stringReguest = new StringRequest(Request.Method.GET, Constant.TEST,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tamara", response.toString());
                        Toast.makeText(getActivity().getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
                        getActivity().finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("tamara", error.toString());
                        Toast.makeText(getActivity().getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("USerFragment-agent", System.getProperty("http.agent"));
                headers.put("authorization", token);
                return headers; // headers of request (needs for https)
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringReguest);//post request on queue
    }
}
