package utils;

public interface LookupableById {
    public byte getId();

    public static <E extends Enum<E> & LookupableById> E lookup(Class<E> e, byte id) {
        for (E v : e.getEnumConstants()) {
            if (v.getId() == id) {
                return v;
            }
        }

        return null;
    }
}