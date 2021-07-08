import java.util.LinkedList;

public class Docente extends Utente{

    private LinkedList<Corso> listaCorsi;
    private int totCorsi;
    private int totLezioni;

    public Docente(String username, String password, String nome, String cognome) {
        super(username, password, nome, cognome);
        listaCorsi = new LinkedList<>();
        this.totCorsi = 0;
        this.totLezioni = 0;
    }


}
