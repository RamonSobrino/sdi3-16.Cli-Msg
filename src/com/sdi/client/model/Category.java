package com.sdi.client.model;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "category")
public class Category implements Serializable {
	
	private static final long serialVersionUID = 5568866959974234572L;
	private Long id;
	private String name;
	private Long userId; 
	
	@XmlElement
	public String getName() {
		return name;
	}

	@XmlElement
	public Long getId() {
		return id;
	}

	public Category setId(Long id) {
		this.id = id;
		return this;
	}

	public Category setName(String name) {
		this.name = name;
		return this;
	}

	@XmlElement
	public Long getUserId() {
		return userId;
	}

	public Category setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public String toString() {
		return "CategoryDto [id=" + id 
				+ ", name=" + name 
				+ ", userId=" + userId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
