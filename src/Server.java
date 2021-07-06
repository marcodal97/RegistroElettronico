import java.util.*;
import java.net.*;
import java.io.*;

public class Server {

    ServerSocket server_socket;
    Socket client_socket;
    private int port;

    public Server(int port){
        this.port = port;
        System.out.println("Creazione server sulla porta" + port);
    }

    public void start(){

        int n_connection = 0;

        try {
            server_socket = new ServerSocket(port);

            while(true){
                System.out.println("Listening on port" + port);
                client_socket = server_socket.accept();
                n_connection++;
                System.out.println("Accepted connection from client" + client_socket.getRemoteSocketAddress());

                ClientManager cm = new ClientManager(client_socket);
                Thread t = new Thread(cm);
                t.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
	Server server = new Server(5000);
	server.start();
    }
}
