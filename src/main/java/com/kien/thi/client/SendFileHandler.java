package com.kien.thi.client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

public class SendFileHandler {
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

    public SendFileHandler() {
        try {
            socket = new Socket("localhost", 5000);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            dos.writeInt((int) file.length());
            byte[] buffer = new byte[4096];
            while (fis.read(buffer) > 0) {
                dos.write(buffer);
            }
            fis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void downloadFile(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            byte[] buffer = new byte[4096];

            int filesize = dis.readInt();
            int read = 0;
            int totalRead = 0;
            int remaining = filesize;
            while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                fos.write(buffer, 0, read);
            }
            fos.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
