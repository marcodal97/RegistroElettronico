import java.util.*;
import java.net.*;
import java.io.*;

public class ClientManager implements Runnable {
    Socket assigned_client;
    final char end = '§';

    public ClientManager(Socket assigned_client) {
        this.assigned_client = assigned_client;
    }

    @Override
    public void run() {

        Scanner from_client = null;
        PrintWriter to_client = null;
        int utente;


        try {
            from_client = new Scanner(assigned_client.getInputStream());
            to_client = new PrintWriter(assigned_client.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        String welcomeMessage = "***** BENVENUTO *****\n";
        String menu = "1 Login\n2 Registrazione";
        to_client.print(welcomeMessage+menu+end);
        to_client.flush();

        int message_from_client = from_client.nextInt();

        if (message_from_client == 1) {
            utente = login(assigned_client, from_client, to_client);

            if(utente == 0) direttore(assigned_client, from_client, to_client);
            if(utente == 1) docente(assigned_client, from_client, to_client);
        }

        }

    private int login(Socket client, Scanner from_client, PrintWriter to_client){
        String username;
        String password;
        Utente utente;
        int tipo = -1;

        while (tipo != 0 && tipo != 1) {
            to_client.print("\nInserisci username" + end);
            to_client.flush();
            username = from_client.next();
            to_client.print("\nInserisci password"+ end);
            to_client.flush();
            password = from_client.next();

            //utente = checklogin(username, password);
            tipo = checklogin(username, password);
            if (tipo == 2) to_client.print("\nErrati");
        }
        return tipo;
    }

    private int checklogin(String username, String password){

        if(username.equals("admin") && password.equals("adminp")) return 0;
        else if (username.equals("user") && password.equals("userp")) return 1;
        else return 2;

    }

    private void direttore(Socket client, Scanner from_client, PrintWriter to_client){
        to_client.print("\nDIRETTORE LOGGATO"+"§");
        to_client.flush();
    }

    private void docente(Socket client, Scanner from_client, PrintWriter to_client){
        to_client.print("\nDOCENTE LOGGATO"+"§");
        to_client.flush();
    }



}
