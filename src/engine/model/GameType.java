package engine.model;

import utils.InvalidEnumId;
import utils.LookupableById;

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
    public GameType(Type type) {
        this.type = type;
    }

    public static enum Type implements LookupableById {
        FirstToXKills(1), Timed(2), Hill(3), Regicide(4);

        private byte id;

        Type(int id) {
            this.id = (byte) id;
        }

        public byte getId() {
            return this.id;
        }

        public static Type getById(byte id) throws InvalidEnumId {
            return LookupableById.lookup(Type.class, id);
        }
    }

    public Type getType() {
        return this.type;
    }
}
