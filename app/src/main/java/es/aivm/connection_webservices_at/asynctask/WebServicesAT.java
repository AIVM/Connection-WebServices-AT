package es.aivm.connection_webservices_at.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import es.aivm.connection_webservices_at.util.ConectionDialog;

/**
 *  Created by AIVM on 12/08/2017.
 */
public class WebServicesAT extends AsyncTask<String, Void, String> {

    private Context contexto;
    private Fragment fragment;
    private URL url;
    private int error;
    private ConectionDialog cDialog;

    public WebServicesAT(Context context, String url, Fragment fragment) {
        if(context != null){//es activity
            this.contexto = context;
        }else{//es fragment
            this.contexto = fragment.getContext();
            this.fragment = fragment;
        }
        this.error = 0; // No hay error
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            error = 1; // Error en URL
            cancel(true);
        }
        cDialog = new ConectionDialog(contexto, null);//null = NO HAY CANCELACION
    }

    @Override
    protected void onPreExecute() {
        cDialog.showDialog();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg) {
        HttpsURLConnection connection = null;
        String response = "[{\"ErrorNum\":-1}]";

        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            if(connection.getResponseCode()==HttpsURLConnection.HTTP_OK) {
                response = parseJSONP(getResponse(connection.getInputStream()));
            }
        } catch (IOException e){
            error = 3; // Error de conexión
            cancel(true);
        } finally {
            connection.disconnect();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        cDialog.hideDialog();

        if(fragment != null){
            ((CallbackResultsAT)fragment).getResults(response);
        }else{
            ((CallbackResultsAT)contexto).getResults(response);
        }
        super.onPostExecute(response);
    }

    @Override
    protected void onCancelled() {
        if(fragment != null){
            ((CallbackResultsAT)fragment).getErrors(error);
        }else{
            ((CallbackResultsAT)contexto).getErrors(error);
        }
        super.onCancelled();
    }

    private String getResponse(InputStream io) throws IOException {
        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(io));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line + "\n");
        }
        reader.close();
        return response.toString();
    }

    private String parseJSONP(String jsonp){
        return jsonp.substring(jsonp.indexOf("(") + 1, jsonp.lastIndexOf(")"));
    }
}

