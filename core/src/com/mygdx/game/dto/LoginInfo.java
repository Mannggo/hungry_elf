package com.mygdx.game.dto;

import java.util.UUID;

public class LoginInfo {
	private String token;
	private String name;
	private String type;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LoginInfo(String name, String type) {
		this.type = type;
		this.name = name;
		this.token = UUID.randomUUID().toString().replaceAll("-", "");
	}
}
