package com.mproject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mproject.DTO.ResponseDto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getCanonicalName();
    private Spinner requestMethodSpinner;
    private EditText requestUrl;
    private Spinner contentTypeSpinner;
    private EditText request;
    private Button btnSubmit;
    private EditText responseBody;
    ArrayAdapter<String> requestMethodAdapter;
    ArrayAdapter<String> contentTypeAdapter;
    List<String> requestMethodList;
    List<String> contentTypeList;
    private TextView status;
    ProgressDialog dialog;

    String requestType,contentType,requestUrlString,requestString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        requestMethodSpinner = (Spinner) findViewById(R.id.requestMethodSpinner);
        requestUrl = (EditText) findViewById(R.id.requestUrl);
        contentTypeSpinner = (Spinner) findViewById(R.id.contentTypeSpinner);
        request = (EditText) findViewById(R.id.request);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        responseBody = (EditText) findViewById(R.id.responseBody);
        btnSubmit.setOnClickListener(this);
        status = (TextView)findViewById(R.id.status);

        requestMethodList = new ArrayList<>();
        requestMethodList.add("GET");
        requestMethodList.add("POST");

        requestMethodAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, requestMethodList);
        requestMethodAdapter.setDropDownViewResource(R.layout.simple_selectable_list_item);
        requestMethodSpinner.setAdapter(requestMethodAdapter);

        requestMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                requestType = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        contentTypeList = new ArrayList<>();
        contentTypeList.add("application/json");
        contentTypeList.add("application/xml");

        contentTypeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, contentTypeList);
        contentTypeAdapter.setDropDownViewResource(R.layout.simple_selectable_list_item);
        contentTypeSpinner.setAdapter(contentTypeAdapter);

        contentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contentType = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        requestUrl.setText(" http://52.66.76.172:9201/versionUpgrade/view");
        request.setText("{\"id\": 0,\"releaseDate\": 0,\"statusCode\": 2000,\"version\": 0,\"appType\": \"FPS\"}");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                submit();

                break;
        }
    }

    private void submit() {
        requestUrlString = requestUrl.getText().toString().trim();
        if (TextUtils.isEmpty(requestUrlString)) {
            Toast.makeText(MainActivity.this, "Please Enter Request URL", Toast.LENGTH_SHORT).show();
            return;
        }

         requestString = request.getText().toString().trim();
        if (TextUtils.isEmpty(requestString)) {
            Toast.makeText(MainActivity.this, "Please Enter Request Body", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e(TAG,"Request Type"+requestType);
        Log.e(TAG,"Content Type"+contentType);
        Log.e(TAG,"Request URL"+requestUrlString);
        Log.e(TAG,"Request"+requestString);

        new HttpCall().execute();

    }
    class HttpCall extends AsyncTask<String,String,ResponseDto>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dialog = new ProgressDialog(MainActivity.this); // this = YourActivity
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


        }

        @Override
        protected ResponseDto doInBackground(String... params) {
            ResponseDto responseDto=serviceUtil.postRequest(MainActivity.this,requestUrlString,requestType,requestString);
            return responseDto;
        }

        @Override
        protected void onPostExecute(ResponseDto responseDto) {
            super.onPostExecute(responseDto);
            dialog.dismiss();
            if(responseDto.getStatusCode() == 200){
                responseBody.setText(toPrettyFormat(""+responseDto.getResponse()));
                status.setText("Status: "+responseDto.getStatusCode() +" "+responseDto.getErrorMessage());
            }else{
                status.setText("Status: "+responseDto.getStatusCode() +" "+responseDto.getErrorMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public static String toPrettyFormat(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }
}
