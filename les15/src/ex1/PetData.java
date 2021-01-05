package ex1;

public class PetData {
    public final int id;
    public final String name;
    public final Person owner;
    public final int weight;

    public PetData(int id, String name, Person owner, int weight) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", owner=[" + owner.toString() + "], weight=" + weight;
    }
}
