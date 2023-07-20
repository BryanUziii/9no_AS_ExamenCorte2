package com.example.examencorte2java;

public class Producto {

    private int id;
    private String codigo;
    private String nombre;
    private String marca;
    private String precio;
    private String estado;

    public Producto(int id, String codigo, String nombre, String marca, String precio, String estado){
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }

    public String getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }
}
