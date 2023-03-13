

public class Person {
    private String name;
    public int ID;


    public Person(String name, int ID) {
        this.name = name;
        this.ID=ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void validData(Person person) throws Exception{
        if(String.valueOf(ID).length()!=4){
            throw new InvalidDataException("ID not correct");
        }
        if(person.getName().length()<6){
            throw new InvalidDataException("Not sufficient characters for a name");
        }
    }
}
