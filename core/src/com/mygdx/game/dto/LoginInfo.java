package com.mygdx.game.dto;

import java.util.UUID;

public class LoginInfo {
	private String token;
	private String name;
	public LoginInfo(String name) {
		this.name = name;
		this.token = UUID.randomUUID().toString().replaceAll("-", "");
	}
}
