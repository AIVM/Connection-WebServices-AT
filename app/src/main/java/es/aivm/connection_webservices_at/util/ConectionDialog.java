package es.aivm.connection_webservices_at.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ConectionDialog extends ProgressDialog {

    public ConectionDialog(Context context) {
        super(context);
        setMessage("Realizando petición al servidor");
        setIndeterminate(true);
        setCancelable(false);
    }

    public ConectionDialog(Context context, OnCancelListener listener) {
        super(context);
        setMessage("Realizando petición al servidor");
        setIndeterminate(true);
        setCancelable(true);
        setOnCancelListener(listener);
    }

    public void showDialog() {
        if (!isShowing())
            show();
    }

    public void hideDialog() {
        if (isShowing())
            dismiss();
    }
}
