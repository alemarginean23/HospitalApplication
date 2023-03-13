import java.util.Comparator;

public class CustomComparator<P extends Person> implements Comparator<Patient> {


    @Override
    public int compare(Patient o1, Patient o2) {
        if(o1.getDiseases().size() > o2.getDiseases().size()){
            return 1;
        }else if(o1.getDiseases().size()==o2.getDiseases().size()){
            return 0;
        }else return -1;
    }
}
