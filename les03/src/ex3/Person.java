package ex3;

import java.util.Objects;

public class Person {
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
        return name + ": " + sex.sexStr.toLowerCase() + ", age " + age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(sex, person.sex) &&
                Objects.equals(name, person.name);
    }
}
