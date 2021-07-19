import java.io.Serializable;
import java.util.LinkedList;

public class Docente extends Utente implements Serializable {

    private LinkedList<Corso> listaCorsi;
    private int totCorsi;
    private int totLezioni;

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
