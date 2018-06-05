package com.mygdx.game.dto;

public class TransferInfo {
	private PlayerInfo playerInfo;
	private LoginInfo loginInfo;
	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}
	public void setPlayerInfo(PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}
	public LoginInfo getLoginInfo() {
		return loginInfo;
	}
	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	public TransferInfo(PlayerInfo playerInfo, LoginInfo loginInfo) {
		super();
		this.playerInfo = playerInfo;
		this.loginInfo = loginInfo;
	}
	
}
