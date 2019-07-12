package ru.geekbrains.filecloud;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Packet implements Serializable {
    private byte[] payload;
    private PacketType type;
}
