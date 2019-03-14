package engine.gameTypes;

/**
 * A general game type
 */
public abstract class GameType {

    private Type type;

    /**
     * Constructor
     *
     * @param type the type of gametype object
     */
    GameType(Type type) {
        this.type = type;
    }

    public enum Type {
        FirstToXKills(1), Timed(2), Hill(3),Regicide(4);

        private byte id;

        Type(int id) {
            this.id = (byte) id;
        }

        public byte getId() {
            return this.id;
        }

        public static Type getById(byte id) {
            for (Type t : Type.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            return null;
        }
    }

    public Type getType() {
        return this.type;
    }
}
