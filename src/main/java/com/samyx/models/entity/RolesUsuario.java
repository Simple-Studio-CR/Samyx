package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "roles_usuarios", uniqueConstraints = { @UniqueConstraint(columnNames = { "rol_usuario" }) })
public class RolesUsuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "rol_usuario")
	private String rolUsuario;

	@Column(name = "details_authority")
	private String detailsAuthority;

	public String getRolUsuario() {
		return this.rolUsuario;
	}

	public void setRolUsuario(String rolUsuario) {
		this.rolUsuario = rolUsuario;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDetailsAuthority() {
		return this.detailsAuthority;
	}

	public void setDetailsAuthority(String detailsAuthority) {
		this.detailsAuthority = detailsAuthority;
	}
}