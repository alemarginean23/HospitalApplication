
import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        /*
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient("Marin Sorescu",1111,"headache"));
        patients.add(new Patient("Adrian Dobrescu", 2222,"otitis"));

        Hospital hospitall=new Hospital("Hospital");
        hospitall.setPatients(patients);

        HospitalView view = new HospitalView();
        HospitalController controller = new HospitalController(view, hospitall);
        */
        Disease disease1 = new Disease("headache",Severity.LOW,1);
        Disease disease2 = new Disease("appendicitis",Severity.MEDIUM,2);
        Disease disease3 = new Disease("myasthenia gravis",Severity.HIGH,3);
        Disease disease4 = new Disease("tonsillitis",Severity.LOW,4);
        Disease disease5 = new Disease("otitis",Severity.MEDIUM,5);
        Disease disease6 = new Disease("flu",Severity.LOW,6);
        Disease disease7 = new Disease("hand fracture",Severity.MEDIUM,7);
        Disease disease8 = new Disease("anemia",Severity.LOW,8);
        Disease disease9 = new Disease("hernia",Severity.MEDIUM,9);
        Disease disease10 = new Disease("pancreatitis",Severity.HIGH,10);

        List<Disease> diseasesPatient1 = new ArrayList<Disease>();
        Patient patient1 = new Patient("Marin Sorescu",1234,diseasesPatient1);
        diseasesPatient1.add(disease1);
        diseasesPatient1.add(disease2);
        patient1.setDiseases((List<Disease>) diseasesPatient1);


        List<Disease> diseasesPatient2 = new ArrayList<Disease>();
        Patient patient2 = new Patient("Adrian Dobrescu",2345,diseasesPatient2);
        diseasesPatient2.add(disease3);
        diseasesPatient2.add(disease4);
        patient2.setDiseases((List<Disease>) diseasesPatient2);

        List<Disease> diseasesPatient3 = new ArrayList<Disease>();
        Patient patient3 = new Patient("Silvia Eremițiu",1245,diseasesPatient3);
        diseasesPatient3.add(disease5);
        diseasesPatient3.add(disease6);
        patient3.setDiseases((List<Disease>) diseasesPatient3);

        List<Disease> diseasesPatient4 = new ArrayList<Disease>();
        Patient patient4 = new Patient("Nadia Birău",1546,diseasesPatient4);
        diseasesPatient4.add(disease7);
        diseasesPatient4.add(disease8);
        diseasesPatient4.add(disease9);
        patient4.setDiseases((List<Disease>) diseasesPatient4);

        List<Disease> diseasesPatient5 = new ArrayList<Disease>();
        Patient patient5 = new Patient("Sabina Chirilă",7194,diseasesPatient5);
        diseasesPatient5.add(disease10);
        patient5.setDiseases((List<Disease>) diseasesPatient5);

        List<Disease> diseasesPatient6 = new ArrayList<Disease>();
        Patient patient6 = new Patient("Robert Dumitrescu",3290,diseasesPatient6);
        diseasesPatient6.add(disease1);
        diseasesPatient6.add(disease3);
        diseasesPatient6.add(disease5);
        patient6.setDiseases((List<Disease>) diseasesPatient6);

        List<Disease> diseasesPatient7 = new ArrayList<Disease>();
        Patient patient7 = new Patient("Valentin Dragos",9413,diseasesPatient7);
        diseasesPatient7.add(disease7);
        diseasesPatient7.add(disease9);
        diseasesPatient7.add(disease2);
        patient7.setDiseases((List<Disease>) diseasesPatient7);

        List<Disease> diseasesPatient8 = new ArrayList<Disease>();
        Patient patient8 = new Patient("Mădălina Găbureanu",5251,diseasesPatient8);
        diseasesPatient8.add(disease4);
        patient8.setDiseases((List<Disease>) diseasesPatient8);

        List<Disease> diseasesPatient9 = new ArrayList<Disease>();
        Patient patient9 = new Patient("Emanuel Mihăilescu",4078,diseasesPatient9);
        diseasesPatient9.add(disease6);
        patient9.setDiseases((List<Disease>) diseasesPatient9);

        List<Disease> diseasesPatient10 = new ArrayList<Disease>();
        Patient patient10 = new Patient("Sergiu Dobre",6452,diseasesPatient10);
        diseasesPatient10.add(disease8);
        patient10.setDiseases((List<Disease>) diseasesPatient10);

        Doctor doctor1 = new Doctor("Elvira Drăgan",1097);
        Doctor doctor2 = new Doctor("Robert Negoiță",9189);
        Doctor doctor3 = new Doctor("Sofia Stăruială",7862);
        Doctor doctor4 = new Doctor("Ionuț Negoiu",7052);
        Doctor doctor5 = new Doctor("Corina Lămboiu",5600);



        Hospital hospital = new Hospital();

        hospital.addPatient(patient1);
        hospital.addPatient(patient2);
        hospital.addPatient(patient3);
        hospital.addPatient(patient4);
        hospital.addPatient(patient5);
        hospital.addPatient(patient6);
        hospital.addPatient(patient7);
        hospital.addPatient(patient8);
        hospital.addPatient(patient9);
        hospital.addPatient(patient10);

        System.out.println("\n");

        hospital.addDoctor(doctor1);
        hospital.addDoctor(doctor2);
        hospital.addDoctor(doctor3);
        hospital.addDoctor(doctor4);
        hospital.addDoctor(doctor5);

        List<Doctor> doctorsList = new ArrayList<Doctor>();
        doctorsList.add(doctor1);
        doctorsList.add(doctor2);
        doctorsList.add(doctor3);
        doctorsList.add(doctor4);
        doctorsList.add(doctor5);
        List<Patient> patientList = new ArrayList<Patient>();
        patientList.add(patient1);
        patientList.add(patient2);
        patientList.add(patient3);
        patientList.add(patient4);
        patientList.add(patient5);
        patientList.add(patient6);
        patientList.add(patient7);
        patientList.add(patient8);
        patientList.add(patient9);
        patientList.add(patient10);

        System.out.println("\n");

        hospital.treatment(patient1,diseasesPatient1,doctorsList);
        System.out.println("\n");
        hospital.treatment(patient2,diseasesPatient2,doctorsList);
        System.out.println("\n");
        hospital.treatment(patient3,diseasesPatient3,doctorsList);
        System.out.println("\n");
        hospital.treatment(patient4,diseasesPatient4,doctorsList);
        System.out.println("\n");
        hospital.treatment(patient5,diseasesPatient5,doctorsList);
        System.out.println("\n");
        hospital.treatment(patient6,diseasesPatient6,doctorsList);
        System.out.println("\n");
        hospital.treatment(patient7,diseasesPatient7,doctorsList);
        System.out.println("\n");
        hospital.treatment(patient8,diseasesPatient8,doctorsList);
        System.out.println("\n");
        hospital.treatment(patient9,diseasesPatient9,doctorsList);
        System.out.println("\n");
        hospital.treatment(patient10,diseasesPatient10,doctorsList);
        System.out.println("\n");
        hospital.statistics(patientList);
        System.out.println("\n");

        System.out.println("Doctorul "+ doctor1.getName()+" are "+ doctor1.getVindecari()+" vindecari");
        System.out.println("Doctorul "+ doctor2.getName()+" are "+doctor2.getVindecari()+" vindecari");
        System.out.println("Doctorul "+ doctor3.getName()+" are "+doctor3.getVindecari()+" vindecari");
        System.out.println("Doctorul "+ doctor4.getName()+" are "+doctor4.getVindecari()+" vindecari");
        System.out.println("Doctorul "+ doctor5.getName()+" are "+doctor5.getVindecari()+" vindecari");

        System.out.println("\n");
        hospital.patients(patientList);
        System.out.println("\n");
        hospital.doctors(doctorsList);

    }

}