package com.samyx.models.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "authorities", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "authority" }) })
public class Role implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String authority;

	private static final long serialVersionUID = 1L;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getDetalleRole() {
		String response = "";
		switch (getAuthority()) {
		case "ROLE_SUPER_ADMIN":
			response = "Super admin";
			break;
		case "ROLE_ADMIN":
			response = "Full permisos";
			break;
		case "ROLE_USER":
			response = "Administrador";
			break;
		case "ROLE_USER_COBRADOR":
			response = "Solo factura";
			break;
		case "ROLE_CAJAS_FULL":
			response = "Control cajas detallado";
			break;
		}
		return response;
	}
}
