package com.mproject;

import android.content.Context;
import android.util.Log;

import com.mproject.DTO.ResponseDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by root on 28/10/17.
 */
public class serviceUtil {

    private static final String TAG=serviceUtil.class.getCanonicalName();

    public static ResponseDto postRequest(Context context, String urlString, String requestMethod, String requestData) {
        ResponseDto responseDto = new ResponseDto();
        try {

            URL url = new URL(urlString);
            HttpURLConnection conn;

            if (requestMethod.equalsIgnoreCase("POST")) {
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();
                os.write(requestData.getBytes("UTF-8"));
                os.close();


            } else {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
            }


            int responseCode = conn.getResponseCode();
            Log.e(TAG," <==== Response Code ====> " + responseCode);
            responseDto.setStatusCode(responseCode);

            switch (responseCode) {
                case 200:

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.e(TAG," <==== Response ====> " + response.toString());
                    responseDto.setResponse(response.toString());
                    responseDto.setErrorMessage("Ok");

                    break;
                case 400:
                    responseDto.setErrorMessage("Bad Request");
                    break;
                case 401:
                    responseDto.setErrorMessage("Unauthorized");
                    break;
                case 402:
                    responseDto.setErrorMessage("Payment Required");
                    break;
                case 403:
                    responseDto.setErrorMessage("Forbidden");
                    break;
                case 404:
                    responseDto.setErrorMessage("Http Not Found");
                    break;
                case 405:
                    responseDto.setErrorMessage("Method Not Allowed");
                    break;
                case 406:
                    responseDto.setErrorMessage("Not Acceptable");
                    break;
                case 407:
                    responseDto.setErrorMessage("Proxy Authentication Required");
                    break;
                case 408:
                    responseDto.setErrorMessage("Request Timeout");
                    break;
                case 409:
                    responseDto.setErrorMessage("Conflict");
                    break;
                case 410:
                    responseDto.setErrorMessage("Gone");
                    break;
                case 411:
                    responseDto.setErrorMessage("Length Required");
                    break;
                case 412:
                    responseDto.setErrorMessage("Precondition Failed");
                    break;
                case 413:
                    responseDto.setErrorMessage("Request Entity Too Large");
                    break;
                case 414:
                    responseDto.setErrorMessage("Request-url Too Long");
                    break;
                case 415:
                    responseDto.setErrorMessage("Unsupported Media Type");
                    break;
                case 417:
                    responseDto.setErrorMessage("Expectation Failed");
                    break;
                case 500:
                    responseDto.setErrorMessage("Internal Server Error");
                    break;
                case 501:
                    responseDto.setErrorMessage("Not Implemented");
                    break;
                case 502:
                    responseDto.setErrorMessage("Bad Gateway");
                    break;
                case 503:
                    responseDto.setErrorMessage("Service Unavailable");
                    break;
                case 504:
                    responseDto.setErrorMessage("Gateway Timeout");
                    break;
                case 505:
                    responseDto.setErrorMessage("HTTP Version Not Supported");
                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
            responseDto.setStatusCode(2000);
            responseDto.setErrorMessage("Internal Error");
        }
        return responseDto;
    }
}
