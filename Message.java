public class Message {
    public int uniqueId;
    public String message;
    public int priority;
    public boolean command;

    Message(int uniqueId, String message, int priority, boolean command) {
        this.uniqueId = uniqueId;
        this.message = message;
        this.priority = priority;
        this.command = command;
    }

    public String toString() {
        return message + " priority:" + priority + " uniqueId:" + uniqueId;
    }
}