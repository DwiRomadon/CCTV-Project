package com.example.dwi.cctv_view.cctvpolda;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dwi.cctv_view.Apps;
import com.example.dwi.cctv_view.R;
import com.example.dwi.cctv_view.pojo.AdapterCctv;
import com.example.dwi.cctv_view.pojo.DataCctv;
import com.example.dwi.cctv_view.server.AppController;
import com.example.dwi.cctv_view.server.Config_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListCctvPolda extends AppCompatActivity {

    ProgressDialog pDialog;

    AdapterCctv adapter;
    ListView list;

    ArrayList<DataCctv> newsList = new ArrayList<DataCctv>();

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cctv_polda);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("List Cctv Polda");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        list = (ListView) findViewById(R.id.list_news);
        newsList.clear();


        adapter = new AdapterCctv(ListCctvPolda.this, newsList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ListCctvPolda.this, PlayCctvPolda.class);
                intent.putExtra("id", newsList.get(position).getId());
                intent.putExtra("judul", newsList.get(position).getJudul());
                intent.putExtra("ip", newsList.get(position).getIp());
                startActivity(intent);
                finish();
            }
        });
        Intent intent = getIntent();
        String kategori   = intent.getStringExtra("kategori");
        getListCCTV(kategori);
    }

    //Fungsi Kembali
    @Override
    public void onBackPressed() {
        Intent a = new Intent(ListCctvPolda.this, Apps.class);
        a.putExtra("kategori","polda");
        startActivity(a);
        finish();
    }


    // fungsi kembali
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Fungsi get JSON Mahasiswa
    private void getListCCTV(final String kategori) {

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

                            DataCctv news = new DataCctv();

                            news.setId(jsonObject.getString("id"));
                            news.setJudul(jsonObject.getString("judul"));
                            news.setAlamat(jsonObject.getString("alamat"));
                            news.setIp(jsonObject.getString("IP"));

                            newsList.add(news);
                        }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListCctvPolda.this);
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
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(String.valueOf(getApplication()), "Error : " + error.getMessage());
                error.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(ListCctvPolda.this);
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
                params.put("tag","cctv_kategori");
                params.put("kategori",kategori);
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
