package ex1;

import ex1.multiindex.*;
import java.util.Iterator;

public class PetCollection {

    private final HashedUniqueIndex<Integer, PetData> idIndex = new HashedUniqueIndex<>(o -> o.id);
    private final HashedNonUniqueIndex<String, PetData> nameIndex = new HashedNonUniqueIndex<>(o -> o.name);
    private final OrderedNonUniqueIndex<Object[], PetData> sortedIndex = new OrderedNonUniqueIndex<>(
            o -> new Object[]{o.owner.name, o.name, o.weight},
            (o1, o2) -> {
                int comp = ((String)o1[0]).compareTo((String)o2[0]);
                if (comp != 0) {
                    return comp;
                }
                comp = ((String)o1[1]).compareTo((String)o2[1]);
                if (comp != 0) {
                    return comp;
                }
                return (Integer)o2[2] - (Integer)o1[2];
            });

    private final MultiIndexCollection<PetData> multiIndexCollection = new MultiIndexCollection<>(
            idIndex,
            nameIndex,
            sortedIndex
    );

    public void add(PetData newData) throws AlreadyExistsException {
        if (!multiIndexCollection.add(newData)) {
            throw new AlreadyExistsException("id " + newData.id + " already exists");
        }
    }

    public Iterator<PetData> searchByName(String name) {
        return nameIndex.find(name);
    }

    public void replaceById(PetData newData) {
        Iterator<PetData> itr = idIndex.find(newData.id);
        if (itr.hasNext()) {
            multiIndexCollection.remove(itr.next());
        }
        multiIndexCollection.add(newData);
    }

    public Iterator<PetData> sortedIterator() {
        return sortedIndex.iterator();
    }
}
