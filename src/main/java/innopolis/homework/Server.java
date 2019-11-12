package innopolis.homework;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public class NewClient implements Runnable{
        @Override
        public void run() {

        }
    }
    public static void doWithClient(String word, PrintWriter output){
        File folder = new File(".\\");
        File[] listOfFiles = folder.listFiles();
        List<File> filesDirs = Arrays.asList(listOfFiles);
        Iterator<File> filesIter = filesDirs.iterator();
        if ((word.length() > 3) && word.substring(0, 3).equals("GET")) {
            output.println("\nPaths and files in current dir\n<br>");
            while (filesIter.hasNext()) {
                output.println(filesIter.next() + "<br>");
                output.println();
            }
        } else {
            output.println("HTTP/1.1 404 ERROR");
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        int count = 0;
        while (true) {
            
            try( Socket clientSocket = serverSocket.accept() ) {
                count++;
                System.out.println("Client" + count + " has connected!");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();

                if (in.ready()) {
                    String word = in.readLine();
                    doWithClient(word, output);
                    System.out.println("Client" + count + " has disconnected!");
                } else {
                    System.out.println("*************\nThere is no request from the client\n*************");
                    continue;
                }
                System.out.println("Thank you for connecting to " + serverSocket.getLocalSocketAddress() + "\nGoodbye!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
