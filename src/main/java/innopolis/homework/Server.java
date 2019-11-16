package innopolis.homework;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Server {
    static int activeConnections = 0;
    private static int count = 0;

    public static void doWithClient(String word, PrintWriter output) {
        File folder = new File(".\\");
        File[] listOfFiles = folder.listFiles();
        List<File> filesDirs = Arrays.asList(listOfFiles);
        Iterator<File> filesIter = filesDirs.iterator();
        if ((word.length() > 3) && word.substring(0, 3).equals("GET")) {
            output.println("\nPaths and files in current dir\n<br>");
            while (filesIter.hasNext()) {
                String dir1 = filesIter.next().toString();
                output.println(dir1 + "<br>\n");
                output.println();
            }
        } else {
            output.println("HTTP/1.1 404 ERROR");
        }
    }

    public static void main(String[] args) throws IOException {
        Runnable runServ = new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(8081);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        NewClient client = new NewClient(clientSocket, "client" + count);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread serv = new Thread(runServ);
        serv.start();
    }

    public static class NewClient implements Runnable {
        Socket clientSocket = null;
        Thread thread;

        public NewClient(Socket socket, String name) {
            this.clientSocket = socket;
            thread = new Thread(this, name);
            thread.start();
        }

        @Override
        public void run() {
            try {
                count++;
                System.out.println("Client" + count + " has connected!");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();

                if (in.ready()) {
                    activeConnections++;
                    String word = in.readLine();
                    doWithClient(word, output);
                    output.println("There is " + activeConnections + " active connections");
                    output.println("You will be disconnect in 50 seconds");
                    thread.sleep(15000);
                    System.out.println("Client" + count + " has disconnected!");
                    System.out.println("Thank you for connecting to " + clientSocket.getLocalSocketAddress() + "\nGoodbye!");
                    activeConnections--;
                } else {
                    System.out.println("*************\nThere is no request from the client\n*************");
                }
            } catch (IOException | InterruptedException e) {
                activeConnections--;
                e.printStackTrace();
            }
        }
    }
}
