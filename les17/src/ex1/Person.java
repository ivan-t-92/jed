package ex1;

public class Person {
    public enum Sex {
        MALE,
        FEMALE
    }
    public final int age;
    public final Sex sex;
    public final String name;

    public Person(int age, Sex sex, String name) {
        this.age = age;
        this.sex = sex;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + ": " + (sex == Sex.MALE ? "male" : "female") + ", age " + age;
    }
}
