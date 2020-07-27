package ex3;

import java.util.Objects;

public class Sex {
    public static final String MAN = "MAN";
    public static final String WOMAN = "WOMAN";

    public final String sexStr;

    public Sex(String sexStr) {
        switch (sexStr) {
            case MAN:
            case WOMAN:
                this.sexStr = sexStr;
                break;
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sex sex = (Sex) o;
        return Objects.equals(sexStr, sex.sexStr);
    }
}
