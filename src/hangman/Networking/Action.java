package hangman.Networking;

public class Action {

    private String user;
    private ActionTag actionTag;
    private String extra = "NULL";

    public Action(String user, ActionTag actionTag, String extra){
        this.user = user;
        this.actionTag = actionTag;
        this.extra = extra;
    }

    public Action(String user, ActionTag actionTag){
        this.user = user;
        this.actionTag = actionTag;
    }

    public Action(String action){
        String[] actionIn = action.split(";");
        String[] actionParts = actionIn[1].split(",");
        ActionTag actionTag;
        switch (actionParts[1]){
            case "SEND":
                actionTag = ActionTag.SEND;
                break;
            case "RESTART":
                actionTag = ActionTag.RESTART;
                break;
            case "EXIT":
                actionTag = ActionTag.EXIT;
                break;
            default:
                actionTag = ActionTag.RESTART;
                break;
        }
        this.user = actionParts[0];
        this.actionTag = actionTag;
        this.extra = actionParts[2];
    }

    public String getUser(){
        return user;
    }

    public ActionTag getActionTag(){
        return actionTag;
    }

    public String getExtra() {
        return extra;
    }
}
