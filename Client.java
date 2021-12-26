import java.io.*;
import java.net.*;
import java.util.*;

class Client {

    public static void main(String[] args)
    {
        //SUNUCUYA BAĞLANMA
        try (Socket socket = new Socket("localhost", 3000)) {
            //SUNUCUYA GÖNDERİLEN : LINE22
            PrintWriter sent = new PrintWriter(socket.getOutputStream(), true);
            //SUNUCUDAN ALINAN : LINE25
            BufferedReader received = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //MESAJ YAZMA
            Scanner input = new Scanner(System.in);
            String line = null;

            while (!"exit".equalsIgnoreCase(line)) {
                line = input.nextLine();
                sent.println(line);
                sent.flush();

                System.out.println("Server replied " + received.readLine());
            }
            input.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
