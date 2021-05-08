import java.util.Objects;

public class Person {
    public enum Sex {
        MALE,
        FEMALE
    }
    public final int id;
    public final int age;
    public final Sex sex;
    public final String name;

    public Person(int id, int age, Sex sex, String name) {
        this.id = id;
        this.age = age;
        this.sex = sex;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", age=" + age +
                ", sex=" + sex +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && age == person.age && sex == person.sex && name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, sex, name);
    }
}
