public class PetData {
    public final int id;
    public final String name;
    public final int ownerId;
    public final int weight;

    public PetData(int id, String name, int ownerId, int weight) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "PetData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", weight=" + weight +
                '}';
    }
}
