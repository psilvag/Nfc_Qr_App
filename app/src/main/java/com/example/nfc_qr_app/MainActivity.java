package com.example.nfc_qr_app;


import com.example.nfc_qr_app.nfc.NfcActivity;
import com.example.nfc_qr_app.nfc.NfcActivity_tps900;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;




public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> nfcActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar el ActivityResultLauncher
        nfcActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String nfcData = result.getData().getStringExtra("nfcData");
                        if (nfcData != null) {
                            Toast.makeText(this, "Datos NFC recibidos: " + nfcData, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        // Botón NFC
        Button nfcButton = findViewById(R.id.nfcbtn);
        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NfcActivity_tps900.class);
                nfcActivityLauncher.launch(intent);
            }
        });





        //----------------- Botón QR----------------------------------
        /*
    Button qrButton = findViewById(R.id.BnQRCode);
    qrButton.setOnClickListener(v -> {
        if (checkPackage("com.telpo.tps550.api")) {
              Intent intent = new Intent();
              intent.setClassName("com.telpo.tps550.api", "com.telpo.tps550.api.barcode.Capture");
              try {
                  qrResultLauncher.launch(intent);
              }catch (ActivityNotFoundException e) {
                  Toast.makeText(MainActivity.this, getResources().getString(R.string.identify_fail), Toast.LENGTH_LONG).show();
              }
            } else {
              Toast.makeText(MainActivity.this, getResources().getString(R.string.identify_fail), Toast.LENGTH_LONG).show();
            }
        });
    };

    // Metodo para verificar si un paquete está instalado
    private boolean checkPackage(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // Configurar ActivityResultLauncher para manejar resultados de la actividad QR
    private final ActivityResultLauncher<Intent> qrResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(MainActivity.this, "QR Escaneado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Falló el escaneo QR", Toast.LENGTH_SHORT).show();
                }
            });

    */


    }
}





