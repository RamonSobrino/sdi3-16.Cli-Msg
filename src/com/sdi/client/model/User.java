package com.sdi.client.model;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.mindrot.jbcrypt.BCrypt;

import com.sdi.client.model.util.BusinessException;
import com.sdi.client.model.util.UserStatus;

/**
 * An implementation of the DTO pattern
 * 
 * @author alb
 */
@XmlRootElement(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = -4317542179579794228L;

	private Long id;

	private String login;
	private String email;
	private String password;
	private Boolean isAdmin = false;
	private UserStatus status = UserStatus.ENABLED;

	public User setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
		return this;
	}

	@XmlElement
	public Long getId() {
		return id;
	}

	public User setId(Long id) {
		this.id = id;
		return this;
	}

	@XmlElement
	public String getLogin() {
		return login;
	}

	public User setLogin(String login) {
		this.login = login;
		return this;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	@XmlElement
	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Asigna la contraseña al usuario y la hashea para guardarla
	 * 
	 * @param newPass
	 * @throws BusinessException
	 *             si no cumple las condiciones de contraseña valida
	 */
	public void setAndHashPassword(String newPass) throws BusinessException {
		//UserCheck.minPasswordLength(newPass);
		if(newPass.length()<=6)
		{
			throw new BusinessException(
					"La longitud del passwor ha de ser mayor de 6");
		}
		setPassword(BCrypt.hashpw(newPass, BCrypt.gensalt()));
	}

	@XmlElement
	public Boolean getIsAdmin() {
		return isAdmin;
	}

	/**
	 * Comprueba que la contraseña pertenece al usuario
	 * 
	 * @param toCheck
	 *            contraseña a comprobar
	 * @return si la contraseña es correcta para el usuario
	 */
	public boolean checkPassword(String toCheck) {
		return BCrypt.checkpw(toCheck, this.password);
	}

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", login=" + login + ", email=" + email
				+ ", password=" + password + ", isAdmin=" + isAdmin + "]";
	}

	@XmlElement
	public UserStatus getStatus() {
		return status;
	}

	public User setStatus(UserStatus status) {
		this.status = status;
		return this;
	}

	public boolean isEnabled() {
		return status.equals(UserStatus.ENABLED);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isAdmin == null) ? 0 : isAdmin.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isAdmin == null) {
			if (other.isAdmin != null)
				return false;
		} else if (!isAdmin.equals(other.isAdmin))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

}
