package com.mygdx.game.dto;

/**
 * @author xiezd
 *
 */
public class PlayerInfo{
	private Float positionX;
	private Float positionY;
	private String elfAvatar;
	private Integer score;
	private Integer roomNumber;
	public Integer getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}
	public Float getPositionX() {
		return positionX;
	}
	public void setPositionX(Float positionX) {
		this.positionX = positionX;
	}
	public Float getPositionY() {
		return positionY;
	}
	public void setPositionY(Float positionY) {
		this.positionY = positionY;
	}
	public String getElfAvatar() {
		return elfAvatar;
	}
	public void setElfAvatar(String elfAvatar) {
		this.elfAvatar = elfAvatar;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	
 }
