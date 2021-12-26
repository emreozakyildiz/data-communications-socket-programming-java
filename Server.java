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
                Socket client = server.accept();

                System.out.println("New client connected " + client.getRemoteSocketAddress().toString().replace((client.getLocalAddress().toString())+":","user-")+".");

                //CLIENT HANDLER
                ClientHandler clientSock = new ClientHandler(client);
                //THREAD OLUŞTURMA
                new Thread(clientSock).start();

                if(client.isClosed()){
                    System.out.println("Client "+client.getRemoteSocketAddress().toString().replace(client.getLocalAddress().toString(),"KULLANICI")+" disconnected.");
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

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                // get the outputstream of client
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                // get the inputstream of client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    //CLIENT MESAJINI SERVER TARAFINDA GÖRÜNTÜLEME
                    System.out.printf(clientSocket.getRemoteSocketAddress().toString().replace((clientSocket.getLocalAddress().toString()+":"),"user-") + ":\n\t" + line + "\n");
                    out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
