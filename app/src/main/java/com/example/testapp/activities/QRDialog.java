package com.example.testapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.testapp.R;

public class QRDialog extends Dialog implements View.OnClickListener {

    public Activity c;

    public QRDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_layout);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
