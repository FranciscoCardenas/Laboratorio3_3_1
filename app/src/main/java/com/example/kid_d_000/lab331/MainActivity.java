package com.example.kid_d_000.lab331;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements ServerResponse {
    static RecyclerView recycler;
    PostAlumnos conexionservidor;
    EditText nombre, direccion, id;
    Button insertar, listar, buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.txtId);
        nombre = findViewById(R.id.txtNombre);
        direccion = findViewById(R.id.txtDireccion);
        insertar = findViewById(R.id.btnInsertar);
        listar = findViewById(R.id.btnListar);
        buscar = findViewById(R.id.btnBuscar);
        recycler = findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setHasFixedSize(true);

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject datosAlumnos = new JSONObject();
                try {
                    conexionservidor = new PostAlumnos(MainActivity.this);
                    datosAlumnos.put("nombre", URLEncoder.encode(nombre.getText().toString().toUpperCase(), "utf-8"));
                    datosAlumnos.put("direccion", URLEncoder.encode(direccion.getText().toString().toUpperCase(), "utf-8"));
                    try {
                        conexionservidor.getJSON(String.valueOf(datosAlumnos));
                        URL url = new URL("http://grenas.x10.mx//datos1/insertar_alumno.php");
                        conexionservidor.execute(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetAlumnos process = new GetAlumnos(MainActivity.this);
                process.execute();
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(id.getText().toString());
                GetAlumnosId process = new GetAlumnosId(n, MainActivity.this);
                process.execute();
            }
        });
    }

    @Override
    public void procesarRespuesta(String r) {
        if (r == null) {

        } else {
            try {
                JSONObject respuesta = new JSONObject(r);
                if (respuesta.getInt("estado")==1) {
                    if (respuesta.getString("mensaje").equals("Creacion correcta")) {
                        Toast.makeText(MainActivity.this, "Datos insertados con éxito", Toast.LENGTH_SHORT).show();
                        nombre.setText("");
                        direccion.setText("");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
