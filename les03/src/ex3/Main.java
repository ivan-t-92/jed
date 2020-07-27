package ex3;

import java.util.Arrays;
import java.util.Random;

public class Main {
    private final static String[] MALE_NAMES = {"Ellsworth","Marc","Lamar","Reyes","Ike","Conrad","Ezekiel","Wilson","Aldo","Harland","Issac","Derick","Darryl","Seymour","Tod","Otis","Gregorio","Agustin","Seth","Milford","Chong","Filiberto","Loyd","Warner","Antwan","Lavern","Ambrose","Van","Jesus","Kelly","Jerald","Jerome","Hans","Norman","Jake","Emmanuel","Ted","Isaiah","Rod","Jeremiah","Del","Bud","Rick","Alfred","Bart","Raul","Bradly","Norris","John","Antione"};
    private final static String[] FEMALE_NAMES = {"Erna","Aleta","Zenia","Terri","Elizabet","Effie","Queen","Winnifred","Amal","Linn","Jaimee","Shari","Nelda","Dia","Stephine","Irene","Onie","Ailene","Jamika","Audra","Neta","Jonell","Margarite","Iliana","Xiao","Shakira","Catina","Crysta","Lael","Nancie","Geri","Loria","Aura","Cammie","Kandis","Cathryn","Kylie","Amberly","Bernardine","Tawanda","Idell","Deena","Muoi","Caroyln","Rosalia","Mika","Jesusa","Latesha","Shizue","Ela"};

    public static void main(String[] args) throws SameNameAgeException {
        Person[] persons = generateRandomPersons(10000);

        Person[] personsBubbleSort = persons.clone();
        {
            long time = sortPersons(personsBubbleSort, new BubbleSortAlgorithm<Person>());
            for (Person p : personsBubbleSort) {
                System.out.println(p);
            }
            System.out.println("Bubble sort: " + time + " ms");
        }

        Person[] personsInsertionSort = persons.clone();
        {
            long time = sortPersons(personsInsertionSort, new InsertionSortAlgorithm<Person>());
            System.out.println("Insertion sort: " + time + " ms");
        }

        if (!Arrays.equals(personsBubbleSort, personsInsertionSort)) {
            throw new RuntimeException("different sorting results");
        }

        for (int i = 1; i < persons.length; i++) {
            if (persons[i-1].name == persons[i].name &&
                persons[i-1].age  == persons[i].age) {

                throw new SameNameAgeException(persons[i].toString());
            }
        }
    }

    private static long sortPersons(Person[] persons, SortAlgorithm<Person> sortAlgorithm) {
        long startTime = System.currentTimeMillis();
        sortAlgorithm.sort(persons, new Compare<Person>() {
            @Override
            public boolean less(Person o1, Person o2) {
                return (o1.sex.sexStr == Sex.MAN && o2.sex.sexStr == Sex.WOMAN) ||
                        (o1.sex.sexStr == o2.sex.sexStr && o1.age > o2.age) ||
                        (o1.sex.sexStr == o2.sex.sexStr && o1.age == o2.age && o1.name.compareTo(o2.name) < 0);
            }
        });
        return System.currentTimeMillis() - startTime;
    }

    private static Person[] generateRandomPersons(int n) {
        Person[] persons = new Person[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int age = random.nextInt(100);
            boolean man = random.nextBoolean();
            Sex sex = new Sex(man ? Sex.MAN : Sex.WOMAN);
            String name = man ? MALE_NAMES[random.nextInt(MALE_NAMES.length)] :
                                FEMALE_NAMES[random.nextInt(FEMALE_NAMES.length)];
            persons[i] = new Person(age, sex, name);
        }
        return persons;
    }
}
