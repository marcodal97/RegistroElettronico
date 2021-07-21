import java.io.Serializable;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Docente extends Utente implements Serializable {

    private LinkedList<Corso> listaCorsi;
    private int totCorsi;
    private int totLezioni;
    private static final long serialVersionUID = 437850178713925464L;

    public Docente(String username, String password, String nome, String cognome) {
        super(username, password, nome, cognome);
        listaCorsi = new LinkedList<>();
        this.totCorsi = 0;
        this.totLezioni = 0;
    }

    public LinkedList<Corso> getListaCorsi() {
        return listaCorsi;
    }

    public int getTotCorsi() {
        return totCorsi;
    }

    public int getTotLezioni() {
        return totLezioni;
    }

    public void addCorso(String nome){
        int id;

        try{
            Corso c = listaCorsi.getLast();
            id = c.getIdCorso()+1;
        }catch(NoSuchElementException ex){
            id = 1;
        }
        Corso corso = new Corso(id, nome);
        totCorsi = totCorsi+1;
        listaCorsi.add(corso);
    }

    public void addNumLezioni(){
        totLezioni = totLezioni+1;
    }

    public void remNumLezioni(){
        totLezioni = totLezioni - 1;
    }

    public boolean checkIdCorso(int id){
        for(Corso c : listaCorsi){
            if(c.getIdCorso() == id) return true;
        }
        return false;
    }

    public Corso getCorso(int id){
        for(Corso c : listaCorsi){
            if(c.getIdCorso()==id) return c;
        }
        return null;
    }

    public boolean checkCorso(int id){
        for(Corso c : listaCorsi){
            if(c.getIdCorso() == id) return true;
        }
        return false;
    }

    public void delCorso(int id) {

        for (Corso c : listaCorsi) {
            if (c.getIdCorso() == id) {
                totCorsi = totCorsi-1;
                totLezioni = totLezioni - c.getTotLezioni();
                listaCorsi.remove(c);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return "Docente{" +
                super.toString()+
                "listaCorsi=" + listaCorsi +
                ", totCorsi=" + totCorsi +
                ", totLezioni=" + totLezioni +
                '}';
    }


}
