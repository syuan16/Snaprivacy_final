package com.notbytes.barcodereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 1208;
    private TextView mTvResult;
    private TextView mTvResultHeader;
    private TextView mTvResult_name;
    private TextView mTvResult_brand;
    private Button openAR;
//    private boolean Envryption;
    private TextView mTVPrivacy;
    public RequestQueue queue;
    private final static String projectID = "acn-careful-granite-240620";
    private final static String sessionID = "123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        findViewById(R.id.btn_activity).setOnClickListener(this);
//        findViewById(R.id.btn_fragment).setOnClickListener(this);
        mTvResult = findViewById(R.id.tv_result);
        mTvResultHeader = findViewById(R.id.tv_result_head);
        mTvResult_name = findViewById(R.id.tv_result_name);
        openAR = findViewById(R.id.openAR);
        mTvResult_brand = findViewById(R.id.tv_result_brand);
        mTVPrivacy = findViewById(R.id.tv_privacy_info);
        addBarcodeReaderFragment();
    }

    public void openAR(View view) {
        Intent intent = new Intent(this, SolarActivity.class);
        startActivity(intent);

    }

    private void addBarcodeReaderFragment() {
        BarcodeReaderFragment readerFragment = BarcodeReaderFragment.newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_fragment:
//                addBarcodeReaderFragment();
//                break;
//            case R.id.btn_activity:
//                FragmentManager supportFragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//                Fragment fragmentById = supportFragmentManager.findFragmentById(R.id.fm_container);
//                if (fragmentById != null) {
//                    fragmentTransaction.remove(fragmentById);
//                }
//                fragmentTransaction.commitAllowingStateLoss();
//                launchBarCodeActivity();
//                break;
//        }
//    }


//    private void launchBarCodeActivity() {
//        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false);
//        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != Activity.RESULT_OK) {
//            Toast.makeText(this, "error in  scanning", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
//            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
//            Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show();
//            mTvResultHeader.setText("On Activity Result");
//            mTvResult.setText(barcode.rawValue);
//        }
//
//    }

    @Override
    public void onScanned(Barcode barcode) {
        Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show();
        mTvResultHeader.setText("Barcode value from fragment");
        mTvResult.setText(barcode.rawValue);
        final String TAG = "UPC scanner";
        final String barcode_value= barcode.rawValue;
        final JSONObject jsonBody = new JSONObject();
        String UPC_url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + barcode.rawValue;
        final StringBuilder title = new StringBuilder();
        final StringBuilder brand = new StringBuilder();
        Log.d(TAG, "url: "+UPC_url);

        JsonObjectRequest jsonObjectRequest_deleteserver = new JsonObjectRequest
                (Request.Method.GET, UPC_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            Log.d(TAG, "inside try");
                            JSONArray items = (JSONArray) response.get("items");
                            JSONObject item = (JSONObject) items.get(0);
                            title.append ((String) item.get("title"));
//                            Log.d(TAG, "Title"+title);
                            brand.append ((String) item.get("brand"));
//                            Log.d(TAG, response.);
                            mTvResult_name.setText(title.toString());
                            mTvResult_brand.setText(brand.toString() + " modified");
                            if(barcode_value.equals("045496590086")){
                                mTVPrivacy.setText("Encryption:Yes, Security updates:Yes, Strong password:Yes, Manages vulnerabilities:Yes, Privacy policy:Yes");
                            }
                            else if(barcode_value.equals("888462858410")){
                                mTVPrivacy.setText("Encryption:Yes, Security updates:Yes, Strong password:N/A, Manages vulnerabilities:Yes, Privacy policy:Yes");

                            }
                            else if(barcode_value.equals("848719083774")){
                                mTVPrivacy.setText("Encryption:Yes, Security updates:Yes, Strong password:N/A, Manages vulnerabilities:N/A, Privacy policy:Yes");
                            }
                            else{
                                mTVPrivacy.setText("Encryption:N/A, Security updates:N/A, Strong password:N/A, Manages vulnerabilities:N/A, Privacy policy:N/A");
                            }

                        }
                        catch (JSONException exception) {
                            Log.d(TAG, "onErrorResponse: "+"request error");

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                    }
                });
        //readDB.close();
        queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest_deleteserver);
        openAR.setVisibility(View.VISIBLE);
//        JSONObject privacy = fuzzyMatching(title.toString(), brand.toString());
//        String feature1 = "";
//        try {
//            feature1 = (String) privacy.get("fulfillmentText");
//        } catch (JSONException e) {
//
//        }
//        mTVPrivacy.setText(feature1);
    }

    private JSONObject fuzzyMatching(String title, String brand) {
        final ArrayList<JSONObject> res = new ArrayList<>();
        final JSONObject jsonBody = new JSONObject();
        JSONObject text = new JSONObject();
        JSONObject text1 = new JSONObject();
        try {
            text.put("text", title + " " + brand);
            text.put("language_code", "en-US");
            text1.put("text", text);
            jsonBody.put("query_input", text1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://dialogflow.googleapis.com/v2/projects/" + projectID +
                "/agent/sessions/" + sessionID + ":detectIntent";
        JsonObjectRequest dialogFlowRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            res.add( (JSONObject) response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //readDB.close();
        queue = Volley.newRequestQueue(this);
        queue.add(dialogFlowRequest);
        JSONObject result = null;
        try {
            result = (JSONObject) res.get(0).get("queryResult");
        } catch (JSONException e) {

        }
        return result;
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}
