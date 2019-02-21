package engine.gameTypes;

public abstract class GameType {
    
    private Type type;

    public static enum Type {
        FirstToXKills(1), TimedGame(2);

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
    
    public GameType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }
}
