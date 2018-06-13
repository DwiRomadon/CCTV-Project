package com.example.dwi.cctv_view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dwi.cctv_view.cctvpolda.ListCctvPolda;
import com.example.dwi.cctv_view.cctvpolresta.ListCctvPolresta;
import com.example.dwi.cctv_view.server.AppController;
import com.example.dwi.cctv_view.server.Config_URL;
import com.example.dwi.cctv_view.webview.WebViewCctv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Apps extends AppCompatActivity {

    ProgressDialog pDialog;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    private Button btnPolda;
    private Button btnPolresta;
    private Button btnCctvDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnPolda = findViewById(R.id.btnCctPolda);
        btnPolresta = findViewById(R.id.btncctvPolres);
        btnCctvDelay = findViewById(R.id.btncctvDelay);

        getJudul();

        btnPolda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Apps.this, ListCctvPolda.class);
                intent.putExtra("kategori", "polda");
                startActivity(intent);
                finish();
            }
        });

        btnPolresta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Apps.this, ListCctvPolresta.class);
                intent.putExtra("kategori", "polresta");
                startActivity(intent);
                finish();
            }
        });

        btnCctvDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Apps.this, WebViewCctv.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Fungsi get JSON Mahasiswa
    private void getJudul() {

        String tag_string_req = "req";

        pDialog.setMessage("Loading.....");
        showDialog();

        String tag_json_obj = "json_obj_req";
        StringRequest strReq = new StringRequest(Request.Method.POST, Config_URL.base_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){

                        String getObject = jObj.getString("result");
                        JSONArray jsonArray = new JSONArray(getObject);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String judul = jsonObject.getString("alamat");
                            setTitle(judul);
                        }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Apps.this);
                        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
                        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>Gagal mengambil data</b></font>"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(String.valueOf(getApplication()), "Error : " + error.getMessage());
                error.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(Apps.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>"+error.getMessage()+"</b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag","judulapk");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
