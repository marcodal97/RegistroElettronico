import java.io.*;
import java.util.LinkedList;

public class Archivio implements Serializable {
    private LinkedList<Utente> listaUtenti;

    public Archivio() {
        this.listaUtenti = new LinkedList<Utente>();
    }

    public LinkedList<Utente> getListaUtenti() {
        return listaUtenti;
    }

    public Utente getUtente(String username){
        Utente utente = null;

        for(Utente u : listaUtenti){
            if(u.getUsername().equals(username)) return u;
        }
        return utente;
    }

    public void addUtente(Utente utente){
        listaUtenti.add(utente);
        onFile();
        System.out.println(this.toString());
    }


    public void onFile(){
        try {
            FileOutputStream f = new FileOutputStream("archivio.ser");
            ObjectOutputStream os = new ObjectOutputStream(f);
            os.writeObject(this);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Archivio{" +
                "listaUtenti=" + listaUtenti +
                '}';
    }
}
