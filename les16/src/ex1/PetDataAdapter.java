package ex1;

public class PetDataAdapter implements PetData {

    private final PetDataPublicFields adaptee;

    public PetDataAdapter(int id, String name, Person owner, int weight) {
        adaptee = new PetDataPublicFields(id, name, owner, weight);
    }

    @Override
    public int id() {
        return adaptee.id;
    }

    @Override
    public String name() {
        return adaptee.name;
    }

    @Override
    public Person owner() {
        return adaptee.owner;
    }

    @Override
    public int weight() {
        return adaptee.weight;
    }

    @Override
    public String toString() {
        return adaptee.toString();
    }
}
