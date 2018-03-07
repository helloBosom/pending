package com.logic.netty;

public enum MessageType {

    LOGIN_REQ((byte) 3), LOGIN_RESP((byte) 4), HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP((byte) 6);
    public byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }
}
