package com.example.kid_d_000.lab331;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> implements ServerResponse{
    ArrayList<Item> listDatos;
    Context context;
    PostAlumnos conexionservidor;

    public AdapterDatos(ArrayList<Item> listDatos, Context context) {
        this.listDatos = listDatos;
        this.context = context;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderDatos holder, final int position) {
        holder.idAlumno.setText(listDatos.get(position).getIdAlumno());
        holder.nombre.setText(listDatos.get(position).getNombre());
        holder.direccion.setText(listDatos.get(position).getDireccion());

        holder.opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, holder.opciones);
                menu.inflate(R.menu.option_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_actualizar:
                                Intent intent = new Intent(context, UpdateActivity.class);
                                intent.putExtra("idAlumno", holder.idAlumno.getText().toString());
                                intent.putExtra("nombre", holder.nombre.getText().toString());
                                intent.putExtra("direccion", holder.direccion.getText().toString());
                                context.startActivity(intent);
                                break;
                            case R.id.item_eliminar:
                                JSONObject datosAlumnos = new JSONObject();
                                try {
                                    conexionservidor = new PostAlumnos(AdapterDatos.this);
                                    datosAlumnos.put("idalumno", holder.idAlumno.getText().toString());
                                    try {
                                        conexionservidor.getJSON(String.valueOf(datosAlumnos));
                                        URL url = new URL("http://grenas.x10.mx//datos1/borrar_alumno.php");
                                        conexionservidor.execute(url);
                                        listDatos.remove(position);
                                        notifyDataSetChanged();
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                menu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    @Override
    public void procesarRespuesta(String r) {
        if (r == null) {

        } else {
            try {
                JSONObject respuesta = new JSONObject(r);
                if (respuesta.getInt("estado")==1) {
                    if (respuesta.getString("mensaje").equals("Eliminacion exitosa")) {
                        Toast.makeText(context, "Datos eliminados con éxito", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView idAlumno, nombre, direccion, opciones;

        public ViewHolderDatos(View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.idNombre);
            direccion = itemView.findViewById(R.id.idDireccion);
            opciones = itemView.findViewById(R.id.idOpciones);
            idAlumno = itemView.findViewById(R.id.idAlumno);
        }
    }
}