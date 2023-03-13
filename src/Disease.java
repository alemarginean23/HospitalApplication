import java.util.Objects;
import java.util.Random;

public class Disease {
    String name;
    Severity severity;
    public int idDisease;

    public Disease(String name, Severity severity,int idDisease) {
        this.name = name;
        this.severity=severity;
        this.idDisease=idDisease;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public int getIdDisease() {
        return idDisease;
    }

    public void setIdDisease(int idDisease) {
        this.idDisease = idDisease;
    }

}
