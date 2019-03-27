package utils;

public interface LookupableById {
    public byte getId();

    public static <E extends Enum<E> & LookupableById> E lookup(Class<E> e, byte id) throws InvalidEnumId {
        for (E v : e.getEnumConstants()) {
            if (v.getId() == id) {
                return v;
            }
        }

        throw new InvalidEnumId(e.getSimpleName() + " does not have a value with id " + (new Integer((int) id)).toString());
    }
}