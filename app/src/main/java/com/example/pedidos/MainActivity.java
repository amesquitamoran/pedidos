package com.example.pedidos;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class MainActivity extends AppCompatActivity {
    private EditText txtdni;
    private Button btningresar;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtdni = findViewById(R.id.txtdni);
        btningresar = findViewById(R.id.btningresar);

        SQLiteOpenHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = txtdni.getText().toString().trim();
                if (validarDNI(dni)) {
                    saveDniToSQLite(dni);
                    navigateToPedidosActivity();
                } else {
                    txtdni.setError("DNI inválido");
                }
            }
        });
    }

    private void navigateToPedidosActivity() {
        Intent intent = new Intent(MainActivity.this, pedidos.class);
        startActivity(intent);
    }

    private void saveDniToSQLite(String dni) {
        try {
            String query = "INSERT INTO cliente (dni) VALUES (?)";
            Object[] values = {dni};
            database.execSQL(query, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
    private boolean validarDNI(String dni) {
        // Verificar que tenga una longitud de 8 caracteres
        if (dni.length() != 8) {
            return false;
        }

        // Verificar que todos los caracteres sean dígitos numéricos
        for (char c : dni.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}