import java.util.Comparator;

public class ComparatorMedici<D extends Person> implements Comparator<Doctor> {

    @Override
    public int compare(Doctor o1, Doctor o2) {
        if(o1.getVindecari() > o2.getVindecari()){
            return 1;
        }else if(o1.getVindecari() == o2.getVindecari()){
            return 0;
        }else return -1;
    }
}
