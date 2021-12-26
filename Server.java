import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.*;

import java.text.SimpleDateFormat;
import java.util.Date;

class Server {

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            //SUNUCU OLUŞTURMA
            server = new ServerSocket(3000);

            //SUNUCUYA ÇOKLU BAĞLANTI KABUL ETMEK İÇİN
            server.setReuseAddress(true);

            while (true) {
                //GELEN BAĞLANTIYI KABUL ETME
                Socket connected = server.accept();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();

                System.out.println("["+formatter.format(date)+"] "+connected.getRemoteSocketAddress().toString().replace((connected.getLocalAddress().toString()) + ":", "user-") + " connected.");

                //CLIENT HANDLER
                ClientHandler handler = new ClientHandler(connected);
                //THREAD OLUŞTURMA
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// THREAD İŞLEMLERİ
class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        String user = "Kullanıcı";
        PrintWriter sent = null;
        BufferedReader received = null;
        try {
            // OUT STREAM
            sent = new PrintWriter(clientSocket.getOutputStream(), true);
            // INPUT STREAM
            received = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            user = clientSocket.getRemoteSocketAddress().toString().replace((clientSocket.getLocalAddress().toString() + ":"), "user-");
            String message;
            while ((message = received.readLine()) != null) {
                //CLIENT MESAJINI SERVER TARAFINDA GÖRÜNTÜLEME
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                System.out.println("["+formatter.format(date)+"] " + user + ":\t" + message);
                sent.println(message);
            }
        } catch (Exception e) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            System.out.println("["+formatter.format(date)+"] "+user + " disconnected.");
        } finally {
            try {
                if (sent != null) {
                    sent.close();
                }
                if (received != null) {
                    received.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}