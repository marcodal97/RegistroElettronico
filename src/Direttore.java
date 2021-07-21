public class Direttore extends Utente{

    public Direttore(String username, String password, String nome, String cognome) {
        super(username, password, nome, cognome);
    }

    @Override
    public String toString() {
        return "Direttore{" +
                super.toString()+
                "}";
    }


}
