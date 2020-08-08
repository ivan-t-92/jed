package ex1;

import java.util.*;

public class PetCollection {

    // Keep this array sorted by name field
    private final ArrayList<PetData> pets = new ArrayList<>();

    public void add(PetData newData) throws AlreadyExistsException {
        if (findById(newData.id) >= 0) {
            throw new AlreadyExistsException("id " + newData.id + " already exists");
        }
        addNoIdCheck(newData);
    }

    private void addNoIdCheck(PetData newData) {
        int index = Collections.binarySearch(pets, newData, new Comparator<>() {
            @Override
            public int compare(PetData o1, PetData o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        if (index >= 0) {
            pets.add(index, newData);
        } else {
            pets.add(-index - 1, newData);
        }
    }

    private int findById(int id) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).id == id) {
                return i;
            }
        }
        return -1;
    }

    public Iterator<PetData> searchByName(String name) {
        return new Iterator<PetData>() {
            int index = Util.lowerBound(pets, null, new Comparator<>() {
                @Override
                public int compare(PetData o1, PetData o2) {
                    return o1.name.compareTo(name);
                }
            });

            @Override
            public boolean hasNext() {
                return index < pets.size() && pets.get(index).name.equals(name);
            }

            @Override
            public PetData next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return pets.get(index++);
            }
        };
    }

    public void replaceById(PetData newData) {
        int i = findById(newData.id);
        if (i >= 0) {
            if (pets.get(i).name.equals(newData.name)) {
                pets.set(i, newData);
            } else {
                pets.remove(i);
                addNoIdCheck(newData);
            }
        }
    }

    public Iterator<PetData> sortedIterator(Comparator<PetData> c) {
        ArrayList<PetData> list = new ArrayList<>(pets);
        list.sort(c);
        return list.iterator();
    }
}
