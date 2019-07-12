package ru.geekbrains.filecloud;

public class Main {

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connect("localhost", 8080);
        Packet auth = client.prepareAuthPacket("w8", "testPassw0rd");
        client.send(auth);
        //System.out.println("hello");
        client.disconnect();
    }
}
