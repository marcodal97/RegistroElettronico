import java.util.Date;

public class Lezione {
    private  int idLezione;
    private Date data;
    private int ore;
    private String argomento;

    public Lezione(int idLezione, Date data, int ore, String argomento) {
        this.idLezione = idLezione;
        this.data = data;
        this.ore = ore;
        this.argomento = argomento;
    }
}
