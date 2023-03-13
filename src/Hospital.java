
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hospital {
    Person[] persons=new Person[40];
    List<Patient> patients = new ArrayList<>();
    List<Doctor> doctors;
    int indexPatient=0;
    int indexDoctor=0;

    public void addPatient(Patient patient){
        boolean added=false;
        for(int i=0;i< indexPatient;i++){
            if(this.persons[i].equals(persons)) {
                added = true;
            }
        }
        if(!added){
            this.persons[indexPatient]=patient;
            indexPatient++;
            System.out.println("Patient " + patient.getName()+ " with ID " + patient.ID + " was added");
        }
        else{
            System.out.println("Patient "+patient.getName()+" already added");
        }
    }

    public void addDoctor(Doctor doctor){
        boolean added=false;
        for(int i=0;i< indexDoctor;i++){
            if(this.persons[i].equals(persons)) {
                added = true;
            }
        }
        if(!added){
            this.persons[indexDoctor]=doctor;
            indexDoctor++;
            System.out.println("Doctor " + doctor.getName()+ " with ID " + doctor.ID + " was added");
        }
        else{
            System.out.println("Doctor "+doctor.getName()+" already added");
        }
    }

    void printPatients(Patient patient) {
        for (int i=0;i<patient.getDiseases().size();i++) {
                System.out.println("The diseases for patient " +patient.getName()+  " are: "+patient.getDiseases().get(i).getName());
            }
    }

    void printDoctors() {
        if (indexDoctor==0){
            System.out.println("No doctors");
        }else {
            System.out.println("The doctors are: ");
            for (Person d : persons) {
                if (d instanceof Doctor) System.out.println(d.getName()+" "+((Doctor) d).vindecari);
            }
        }
    }

    public void treatment(Patient patient, List<Disease> diseases, List<Doctor> doctors){
        try{
            patient.validData(patient);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        for(int i=0;i<diseases.size();i++){
            for(int j=0;j<doctors.size();j++){
                if(patient.cure(patient.getDiseases().get(i))){
                    patient.cured(patient,diseases.get(i),doctors.get(j));
                    doctors.get(j).setVindecari(doctors.get(j).getVindecari()+1);
                    break;
                }
            }
        }
        //patient.cured(patient,diseases,doctor);
    }
    //sorted list of patients by the number of diseaases they have
    public void patients(List<Patient> patients){
    //>1
    // =0
    // <-1
        System.out.println("Pacientii in functie de nr de boli:");
        Collections.sort(patients, new CustomComparator<Patient>());
        for(int i=0;i<patients.size();i++){
            System.out.println(patients.get(i).getName());
        }
    }

    //sorted list of doctors by the number of diseased they have cured
    public void doctors(List<Doctor> doctors){
        System.out.println("Doctorii in funuctie de nr de vindecari");
        Collections.sort(doctors, new ComparatorMedici<Doctor>());
        for(int i=0;i<doctors.size();i++){
            System.out.println(doctors.get(i).getName());
        }
    }

    public void statistics(List<Patient> patients){
        int cured=0;
        int notCured=0;

        for(int i=0;i<patients.size();i++){
            cured=0;
            notCured=0;
            for(int j=0;j<patients.get(i).getDiseases().size();j++){
                if(patients.get(i).cure(patients.get(i).getDiseases().get(j))){
                    cured++;
                }else{
                    notCured++;
                }
            }
            System.out.println("Patient "+patients.get(i).getName()+" cured of " + cured + " diseases and not cured of " + notCured);

        }

    }

}

