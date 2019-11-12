package innopolis.homework;

import java.io.*;
import java.net.Socket;

public class Client {
    public static final int PORT = 8081;
    public static void main(String[] args) throws IOException {

        while (true) {
            Socket clientSocket = new Socket("localhost", PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            System.out.println("Вы что-то хотели сказать? Введите это здесь:");
            String word = "qq";//reader.readLine();
//            out.write(word + "\n");
            out.write("POST qq");
            out.flush();
            String serverWord = in.readLine();
            System.out.println(serverWord);;
            in.close();
            out.close();
            clientSocket.close();
            System.out.println("Клиент был закрыт...\nПродолжаем?(Для завершения введите n)");
            if (reader.readLine().equals("n")){
                break;

            }
        }
    }
}
