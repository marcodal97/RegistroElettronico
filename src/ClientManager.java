import java.util.*;
import java.net.*;
import java.io.*;

public class ClientManager implements Runnable{
    Socket assigned_client;
    Archivio archivio;
    final char end = '§';
    private volatile boolean running = true;

    public ClientManager(Socket assigned_client, Archivio archivio) {
        this.assigned_client = assigned_client;
        this.archivio = archivio;
    }

    @Override
    public void run(){
        while(running){
            try {
                Scanner from_client = null;
                PrintWriter to_client = null;
                int message_from_client;
                int utente = -1;

                try {
                    from_client = new Scanner(assigned_client.getInputStream());
                    to_client = new PrintWriter(assigned_client.getOutputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (utente != 0 && utente != 1) {
                    String welcomeMessage = "***** BENVENUTO *****\n";
                    String menu = "1 Login\n2 Registrazione\n3 Esci";
                    to_client.print(welcomeMessage + menu + "\nscelta: "+end);
                    to_client.flush();

                    while(true) {
                        if(from_client.hasNextInt() == true){
                            message_from_client = from_client.nextInt();
                            break;
                        }
                        from_client.next();
                        to_client.print("Inserisci un numero: "+end);
                        to_client.flush();
                    }

                    if (message_from_client == 1) {
                        utente = login(assigned_client, from_client, to_client);
                        if (utente == 0) direttore(assigned_client, from_client, to_client);
                        if (utente == 1) docente(assigned_client, from_client, to_client);
                    }
                    if (message_from_client == 2) {
                        registrazione(assigned_client, from_client, to_client);
                    }
                    if(message_from_client == 3){
                        running = false;
                        to_client.print("Chiusura Connessione..."+end);
                        to_client.flush();
                        break;
                    }
                }
            }catch (Exception ex){
                running = false;
            }
        }
        System.out.println("Connessione chiusa col Client: "+assigned_client.getRemoteSocketAddress());
    }

    private void registrazione(Socket client, Scanner from_client, PrintWriter to_client){
        int scelta = 0;
        String pass = "";
        Utente utente;
        HashMap<String, String> dati;

        while(scelta != 1 && scelta != 2) {
            to_client.print("\n-----REGISTRAZIONE-----");
            to_client.print("\n1) Registrazione Admin");
            to_client.print("\n2) Registrazione Docente");
            to_client.print("\nscelta: "+end);
            to_client.flush();
            while(true) {
                if(from_client.hasNextInt() == true){
                    scelta = from_client.nextInt();
                    break;
                }
                from_client.next();
                to_client.print("Inserisci un numero: "+end);
                to_client.flush();
            }
        }

        if (scelta == 1) {
            while(true) {
                to_client.print("\nDigita password per registrazione: " + end);
                to_client.flush();
                pass = from_client.next();
                if(pass.equals("passadmin") == false) {
                    to_client.print("\nPassword errata, ritenta");
                }else break;
            }
            dati = registrazioneUtente(client, from_client, to_client);
            Direttore direttore = new Direttore(dati.get("username"), dati.get("password"), dati.get("nome"), dati.get("cognome"));
            archivio.addUtente(direttore);
            to_client.print("\nDirettore registrato!\n\n");
        }

        if (scelta == 2) {
            while(true) {
                to_client.print("\nDigita password per registrazione: " + end);
                to_client.flush();
                pass = from_client.next();
                if(pass.equals("passdoc") == false) {
                    to_client.print("\nPassword errata, ritenta");
                }else break;
            }
            dati = registrazioneUtente(client, from_client, to_client);
            Docente docente = new Docente(dati.get("username"), dati.get("password"), dati.get("nome"), dati.get("cognome"));
            archivio.addUtente(docente);
            to_client.print("\nDocente registrato!\n\n");
        }
    }

    private HashMap<String, String> registrazioneUtente(Socket client, Scanner from_client, PrintWriter to_client){
        HashMap<String, String> dati = new HashMap<>();
        String username;
        String nome;
        String cognome;
        String pass;

        to_client.print("\n-----------------------");
        to_client.print("\nInserisci username: "+end);
        to_client.flush();
        dati.put("username", from_client.next());
        to_client.print("\nInserisci password: "+end);
        to_client.flush();
        dati.put("password", from_client.next());
        to_client.print("\nInserisci Nome: "+end);
        to_client.flush();
        dati.put("nome", from_client.next());
        to_client.print("\nInserisci Cognome: "+end);
        to_client.flush();
        dati.put("cognome", from_client.next());

        return dati;
    }

    private int login(Socket client, Scanner from_client, PrintWriter to_client){
        String username;
        String password;
        Utente utente;
        int tipo = -1;

        while (tipo != 0 && tipo != 1) {
            to_client.print("\nInserisci username: " + end);
            to_client.flush();
            username = from_client.next();
            to_client.print("\nInserisci password: "+ end);
            to_client.flush();
            password = from_client.next();

            //utente = checklogin(username, password);
            tipo = checklogin(username, password);
            if (tipo == 2) to_client.print("Errati\n");
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
