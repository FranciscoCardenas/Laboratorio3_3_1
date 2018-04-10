package com.example.kid_d_000.lab331;

/**
 * Created by kid_d_000 on 12/03/2018.
 */

public class Item {
    private String idAlumno,nombre,direccion;

    public Item(String idAlumno, String nombre, String direccion) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
