import java.io.*;
import java.net.*;

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

                System.out.println("New client connected " + connected.getRemoteSocketAddress().toString().replace((connected.getLocalAddress().toString()) + ":", "user-") + ".");

                //CLIENT HANDLER
                ClientHandler handler = new ClientHandler(connected);
                //THREAD OLUŞTURMA
                new Thread(handler).start();

                if (connected.isClosed()) {
                    System.out.println("Client " + connected.getRemoteSocketAddress().toString().replace(connected.getLocalAddress().toString(), "KULLANICI") + " disconnected.");
                }
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
        PrintWriter sent = null;
        BufferedReader received = null;
        try {
            // OUT STREAM
            sent = new PrintWriter(clientSocket.getOutputStream(), true);
            // INPUT STREAM
            received = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            while ((message = received.readLine()) != null) {
                //CLIENT MESAJINI SERVER TARAFINDA GÖRÜNTÜLEME
                System.out.println(clientSocket.getRemoteSocketAddress().toString().replace((clientSocket.getLocalAddress().toString() + ":"), "user-") + ":\n\t" + message);
                sent.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
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