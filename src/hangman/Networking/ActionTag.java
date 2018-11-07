package hangman.Networking;

public enum ActionTag {
    EXIT("EXIT"),
    RESTART("RESTART"),
    SEND("SEND");

    private String actionTag;

    ActionTag(String actionTag){
        this.actionTag = actionTag;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return actionTag.equals(otherName);
    }

    public String toString() {
        return this.actionTag;
    }
}
