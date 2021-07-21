import java.io.Serializable;
import java.util.LinkedList;

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

    @Override
    public String toString() {
        return
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    @Override
    public int compareTo(Utente utente)
    {
        return cognome.compareTo(utente.getCognome());
    }

}
