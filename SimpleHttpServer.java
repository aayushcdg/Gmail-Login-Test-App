package selenium;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleHttpServer {
    public static void startServer() {
        Thread thread = new Thread(() -> {
            try {
                int port = 8080;
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server running on port " + port + "...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String request = in.readLine();
                    System.out.println("Request: " + request);

                    if (request != null) {
                        if (request.startsWith("GET / ")) {
                            String html = new String(Files.readAllBytes(Paths.get("/Users/ajain03/Desktop/Seneca/Seneca Winter 2024/TPJ/Test/GmailLoginTest/src/selenium/LoginAttempt.html")));
                            out.println("HTTP/1.1 200 OK");
                            out.println("Content-Type: text/html");
                            out.println("Content-Length: " + html.length());
                            out.println();
                            out.println(html);
                        } else if (request.startsWith("POST /save_login")) {
                            String line;
                            StringBuilder requestBody = new StringBuilder();
                            while ((line = in.readLine()) != null && line.length() > 0) {
                                requestBody.append(line).append("\n");
                            }

                            String[] formData = requestBody.toString().split("&");
                            String email = formData[0].split("=")[1];
                            String password = formData[1].split("=")[1];

                            // Process login attempt here
                            System.out.println("Received login attempt with email: " + email + ", password: " + password);

                            // Send response back to the client
                            out.println("HTTP/1.1 200 OK");
                            out.println("Content-Type: text/plain");
                            out.println("Content-Length: " + "Login attempt received".length());
                            out.println();
                            out.println("Login attempt received");
                        } else {
                            out.println("HTTP/1.1 404 Not Found");
                            out.println();
                        }
                    }

                    in.close();
                    out.close();
                    clientSocket.close();
                    System.out.println("Client disconnected.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
