package ru.geekbrains.filecloud;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        new FileServer(port).run();
    }
}
