package ex1;

import ex1.multiindex.HashedNonUniqueIndex;
import ex1.multiindex.HashedUniqueIndex;
import ex1.multiindex.MultiIndexCollection;
import ex1.multiindex.OrderedNonUniqueIndex;

import java.util.List;
import java.util.stream.Stream;

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

    private final MultiIndexCollection<PetData> multiIndexCollection = new MultiIndexCollection.Builder<PetData>()
            .withIndex(idIndex)
            .withIndex(nameIndex)
            .withIndex(sortedIndex)
            .build();

    public void add(PetData newData) throws AlreadyExistsException {
        if (!multiIndexCollection.add(newData)) {
            throw new AlreadyExistsException("id " + newData.id + " already exists");
        }
    }

    public List<PetData> searchByName(String name) {
        return nameIndex.find(name);
    }

    public void replaceById(PetData newData) {
        List<PetData> l = idIndex.find(newData.id);
        if (!l.isEmpty()) {
            multiIndexCollection.remove(l.get(0));
        }
        multiIndexCollection.add(newData);
    }

    public Stream<PetData> sortedStream() {
        return sortedIndex.stream();
    }
}
