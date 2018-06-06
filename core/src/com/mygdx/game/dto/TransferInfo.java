package com.mygdx.game.dto;

import java.util.List;

public class TransferInfo {
	private PlayerInfo playerInfo;
	private LoginInfo loginInfo;
	private List<StarInfo> starInfos;
	private List<BillInfo> billInfos;
	
	public List<StarInfo> getStarInfos() {
		return starInfos;
	}
	public void setStarInfos(List<StarInfo> starInfos) {
		this.starInfos = starInfos;
	}
	public List<BillInfo> getBillInfos() {
		return billInfos;
	}
	public void setBillInfos(List<BillInfo> billInfos) {
		this.billInfos = billInfos;
	}
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
