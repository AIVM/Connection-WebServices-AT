package es.aivm.connection_webservices_at;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import es.aivm.connection_webservices_at.asynctask.CallbackResultsAT;
import es.aivm.connection_webservices_at.asynctask.WebServicesAT;

public class MainActivity extends AppCompatActivity implements CallbackResultsAT{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //request data
        new WebServicesAT(this,"URL",null).execute();
    }

    @Override
    public void getResults(String result) {
        String errorNum="-1";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            errorNum = jsonObject.getString("ErrorNum");
        } catch (JSONException e) {
            getErrors(4); // Error de conversion
        }

        switch (errorNum){
            case "0":
                //Process correct
                break;
            case "1":
                //Error API
                break;
            default:getErrors(-1);
        }
    }

    @Override
    public void getErrors(int error) {
        String messageError = "";
        switch (error){
            case -1: messageError = getResources().getString(R.string.WS_toast_error_API_general);
                break;
            case 1: messageError = getResources().getString(R.string.WS_toast_error_API);
                break;
            case 2: messageError = getResources().getString(R.string.WS_toast_error_usuario);
                break;
            case 3: messageError = getResources().getString(R.string.WS_toast_error_IOexception);
                break;
            case 4: messageError = getResources().getString(R.string.WS_toast_error_JSONexception);
                break;
        }
        Toast.makeText(getApplicationContext(), messageError, Toast.LENGTH_LONG).show();
    }
}
