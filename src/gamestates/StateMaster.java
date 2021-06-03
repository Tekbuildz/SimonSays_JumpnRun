package gamestates;

public class StateMaster {

    private static State state = null;

    public static State getState() {
        return state;
    }

    public static void setState(State newState) {
        state = newState;
    }
}
