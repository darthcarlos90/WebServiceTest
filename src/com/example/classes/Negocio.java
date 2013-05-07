package com.example.classes;

public class Negocio {

	String nombre;
	String direccion;

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

	public Negocio() {

	}

	public Negocio(String n, String d) {
		this.nombre = n;
		this.direccion = d;
	}
}
