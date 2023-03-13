

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Patient extends Person {
    // private Disease[] diseases;
    private List<Disease> diseases;
    public Disease disease;

    public Patient(String name, int ID,List<Disease> diseases)
    {
        super(name, ID);
        this.diseases=new ArrayList<>();
    }




    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }


    Random r = new Random();
    int low = 1;
    int high = 100;

    public boolean cure(Disease disease){
        boolean cured=false;
        int chance = r.nextInt(high-low) + low;
        //System.out.println("sansa "+chance);
        for(int i=0;i<diseases.size();i++) {
            //System.out.println(diseases.size());
            if (diseases.get(i).getSeverity() == Severity.LOW) {
                if (chance <= 75) {
                    cured = true;
                }
            } else if (diseases.get(i).getSeverity() == Severity.MEDIUM) {
                if (chance <= 50) {
                    cured = true;
                }
            } else {
                if (chance <= 25) {
                    cured = true;
                }
            }
        }
        return cured;
    }

    public boolean cured(Patient patient, Disease disease,Doctor doctor){
        boolean patientcured=false;
        //System.out.println("aa");
        //System.out.println("boli "+diseases.size());
        if(patient.getDiseases() != null){
                //System.out.println("bolnav");
                //means he/she is sick
                if(patient.cure(disease)){
                    //System.out.println("cured");
                        System.out.println("Patient " + patient.getName() + " cured from " + disease.getName() + " by doctor "+doctor.getName());

                    //patientcured=true;
                }
                else{
                        System.out.println("Patient " + patient.getName() + " not cured from " + disease.getName());
                }
            }


        return patientcured;
    }



}
