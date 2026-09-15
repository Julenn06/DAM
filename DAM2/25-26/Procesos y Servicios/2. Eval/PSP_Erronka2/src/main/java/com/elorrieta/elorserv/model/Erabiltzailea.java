package com.elorrieta.elorserv.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

// Erabiltzaileak (users taula) - god, admin, irakasle, ikasle
@Entity
@Table(name = "users")
public class Erabiltzailea implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_id")
	private Mota mota;

	private String email;
	private String username;
	private String password;
	private String nombre;
	private String apellidos;
	private String dni;
	private String direccion;
	private String telefono1;
	private String telefono2;

	@Column(name = "argazkia_url")
	private String argazkiaUrl;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@JsonIgnore
	@OneToMany(mappedBy = "erabiltzailea")
	private Set<Matrikulazioa> matrikulazioak = new HashSet<>(0);

	@JsonIgnore
	@OneToMany(mappedBy = "ikaslea")
	private Set<Bilera> bilerakIkasle = new HashSet<>(0);

	@JsonIgnore
	@OneToMany(mappedBy = "erabiltzailea")
	private Set<Ordutegia> ordutegia = new HashSet<>(0);

	@JsonIgnore
	@OneToMany(mappedBy = "irakaslea")
	private Set<Bilera> bilerakIrakasle = new HashSet<>(0);

	public Erabiltzailea() {
	}

	public Erabiltzailea(Mota mota, String email, String username, String password) {
		this.mota = mota;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public Erabiltzailea(Mota mota, String email, String username, String password, String nombre, String apellidos,
			String dni, String direccion, String telefono1, String telefono2, String argazkiaUrl, Timestamp createdAt,
			Timestamp updatedAt, Set<Matrikulazioa> matrikulazioak, Set<Bilera> bilerakIkasle, Set<Ordutegia> ordutegia,
			Set<Bilera> bilerakIrakasle) {
		this.mota = mota;
		this.email = email;
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.direccion = direccion;
		this.telefono1 = telefono1;
		this.telefono2 = telefono2;
		this.argazkiaUrl = argazkiaUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.matrikulazioak = matrikulazioak;
		this.bilerakIkasle = bilerakIkasle;
		this.ordutegia = ordutegia;
		this.bilerakIrakasle = bilerakIrakasle;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Mota getMota() {
		return this.mota;
	}

	public void setMota(Mota mota) {
		this.mota = mota;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono1() {
		return this.telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getArgazkiaUrl() {
		return this.argazkiaUrl;
	}

	public void setArgazkiaUrl(String argazkiaUrl) {
		this.argazkiaUrl = argazkiaUrl;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Set<Matrikulazioa> getMatrikulazioak() {
		return this.matrikulazioak;
	}

	public void setMatrikulazioak(Set<Matrikulazioa> matrikulazioak) {
		this.matrikulazioak = matrikulazioak;
	}

	public Set<Bilera> getBilerakIkasle() {
		return this.bilerakIkasle;
	}

	public void setBilerakIkasle(Set<Bilera> bilerakIkasle) {
		this.bilerakIkasle = bilerakIkasle;
	}

	@JsonIgnore
	public Set<Ordutegia> getOrdutegia() {
		return this.ordutegia;
	}

	public void setOrdutegia(Set<Ordutegia> ordutegia) {
		this.ordutegia = ordutegia;
	}

	public Set<Bilera> getBilerakIrakasle() {
		return this.bilerakIrakasle;
	}

	public void setBilerakIrakasle(Set<Bilera> bilerakIrakasle) {
		this.bilerakIrakasle = bilerakIrakasle;
	}

	@Override
	public String toString() {
		return "Erabiltzailea{" + "id=" + id + ", email='" + email + '\'' + ", username='" + username + '\''
				+ ", nombre='" + nombre + '\'' + ", apellidos='" + apellidos + '\'' + '}';
	}
}
