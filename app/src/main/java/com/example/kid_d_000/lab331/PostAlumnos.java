package com.example.kid_d_000.lab331;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class PostAlumnos extends AsyncTask<URL, String, String> {
    ServerResponse server;
    String jsonPost;

    public PostAlumnos(ServerResponse server) {
        this.server = server;
    }

    public void getJSON(String r) {
        jsonPost = r;
    }

    @Override
    protected String doInBackground(URL... urls) {
        String respuesta_servidor = "";
        HttpURLConnection conexion = null;
        try {
            conexion = (HttpURLConnection) urls[0].openConnection();
            conexion.setDoOutput(true);
            conexion.setDoInput(true);
            conexion.setFixedLengthStreamingMode(jsonPost.length());
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            OutputStream flujoSalida = new BufferedOutputStream(conexion.getOutputStream());
            flujoSalida.write(jsonPost.getBytes());
            flujoSalida.flush();
            flujoSalida.close();
            if (conexion.getResponseCode() == 200) {
                InputStreamReader entrada = new InputStreamReader(conexion.getInputStream(), "UTF-8");
                BufferedReader flujoEntrada = new BufferedReader(entrada);
                String temp = "";
                do {
                    temp = flujoEntrada.readLine();
                    if (temp != null) {
                        respuesta_servidor += temp;
                    }
                } while (temp != null);
                flujoEntrada.close();
            } else {
                respuesta_servidor = "No hubo respuesta";
            }
        } catch (UnknownHostException e) {
            respuesta_servidor = "Imposible tener acceso al host";
        } catch (IOException e) {
            respuesta_servidor = "Error de lectura o escritura";
        } finally {
            if (conexion != null) {
                conexion.disconnect();
            }
        }
        return respuesta_servidor;
    }

    @Override
    protected void onPostExecute(String s) {
        server.procesarRespuesta(s);
    }
}
