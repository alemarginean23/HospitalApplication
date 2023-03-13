import java.util.List;

public class Doctor extends Person{

    int vindecari=0;

    public Doctor(String name, int ID) {
        super(name, ID);
    }

    public int getVindecari() {
        return vindecari;
    }

    public void setVindecari(int vindecari) {
        this.vindecari = vindecari;
    }

}
