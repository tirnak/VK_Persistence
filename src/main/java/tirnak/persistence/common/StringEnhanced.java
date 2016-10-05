package tirnak.persistence.common;

import java.util.Optional;

public class StringEnhanced {

    private String s;

    public StringEnhanced (String s) {
        this.s = s;
    }

    public static StringEnhanced wrapString(String nullable) {
        return new StringEnhanced(Optional.ofNullable(nullable).orElse(NullObjects.getEmptyString()));
    }

    public boolean contains(String string) {
        return s.contains(string);
    }

    public boolean containsOneOf(String[] strings) {
        for (String string : strings) {
            if (s.contains(string)) {
                return true;
            }
        }
        return false;
    }

    public boolean equalsOneOf(String[] strings) {
        for (String string : strings) {
            if (s.equals(string)) {
                return true;
            }
        }
        return false;
    }

    public boolean matches(String regexp) {
        return s.matches(regexp);
    }

}
