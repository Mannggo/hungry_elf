package com.mygdx.game.udp.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import com.google.gson.Gson;
import com.mygdx.game.dto.PlayerInfo;

public class UDPClient implements Runnable{
    private DatagramSocket receivesocket;
    private DatagramSocket sendSocket;
    private Integer randomPort;
    public PlayerInfo playerInfo;
    private Gson gson;
    public UDPClient() {
        try {
        	gson = new Gson();
            Random random = new Random();
            this.randomPort = random.nextInt(1000) + 8000;
            sendSocket = new DatagramSocket(randomPort);
            receivesocket = new DatagramSocket(randomPort + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            DatagramPacket dp = null;
            dp = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName("localhost"), 9000);
            // 发出数据报
            sendSocket.send(dp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, 1024);
                receivesocket.receive(dp);
                // 从数据报中取出数据
                String info = new String(dp.getData(), 0, dp.getLength());
                playerInfo = gson.fromJson(info, PlayerInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
