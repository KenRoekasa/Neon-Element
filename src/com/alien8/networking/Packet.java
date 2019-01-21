package com.alien8.networking;

public abstract class Packet {

    public static enum PacketType {
        HELLO(0);
        
        private int id;
        
        PacketType(int id) {
            this.id = id;
        }
    }
}
