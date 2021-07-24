import java.io.Serializable;

public abstract class Utente implements Serializable, Comparable<Utente>{
    private static final long serialVersionUID = -5354313318998779641L;
    private String username;
    private String password;
    private String nome;
    private String cognome;

    public Utente(String username, String password, String nome, String cognome) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized String getNome() {
        return nome;
    }

    public synchronized String getCognome() {
        return cognome;
    }

    @Override
    public synchronized int compareTo(Utente utente)
    {
        return cognome.compareTo(utente.getCognome());
    }

    @Override
    public synchronized String toString() {
        return
                "username='" + username + '\'' +
                        ", password='" + password + '\'' +
                        ", nome='" + nome + '\'' +
                        ", cognome='" + cognome + '\'' +
                        '}';
    }
}
