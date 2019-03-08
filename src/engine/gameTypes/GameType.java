package engine.gameTypes;

import utils.InvalidEnumId;
import utils.LookupableById;

public abstract class GameType {

    private Type type;

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

    GameType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }
}
