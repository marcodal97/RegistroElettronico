import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

public class Archivio implements Serializable {
    private static final long serialVersionUID = -4140436349008090061L;
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

    public Utente loginCheck(String username, String password){
        Utente utente =  null;

        for(Utente u : listaUtenti){
            if(u.getUsername().equals((username)) && u.getPassword().equals(password)) return u;
        }
        return utente;
    }

    public void addUtente(Utente utente){
        listaUtenti.add(utente);
        onFile();
    }

    public void addCorso(Docente docente, String nome){
        docente.addCorso(nome);
        onFile();
    }

    public void addLezione(Docente docente, Corso corso, Date data, int ore, String argomento){
        corso.addLezione(data, ore, argomento);
        docente.addNumLezioni();
        onFile();
    }

    public void delLezione(Docente docente, Corso corso, int id){
        corso.delLezione(id);
        docente.remNumLezioni();
        onFile();
    }

    public void delCorso(Docente docente, int id){
        docente.delCorso(id);
        onFile();
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

    public boolean checkDoc(String username){
         for(Utente u : listaUtenti){
             if(u instanceof Docente)
                 if(u.getUsername().equals(username)) return true;
         }
         return false;
    }

    public void delDocente(String username){
        for(Utente u : listaUtenti){
            if(u.getUsername().equals(username)) listaUtenti.remove(u);
        }
        onFile();
    }

    public Docente getDocente(String username){
        for(Utente u : listaUtenti){
            if(u.getUsername().equals(username)) return (Docente)u;
        }
        return null;
    }

    public LinkedList<Utente> ordinaUtenti(){
        LinkedList<Utente> listaord = new LinkedList<>(listaUtenti);
        Collections.sort(listaord);
        return listaord;
    }

    @Override
    public String toString() {
        return "Archivio{" +
                "listaUtenti=" + listaUtenti +
                '}';
    }
}
