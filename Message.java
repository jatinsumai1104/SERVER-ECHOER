import java.io.*;
import java.util.*;
import java.net.Socket;
public class Message{
    public int uniqueId;
    public String message;
    public int priority;
    public boolean command;

    Message(int uniqueId, String message, int priority,boolean command){
        this.uniqueId = uniqueId;
        this.message = message; 
        this.priority = priority;
        this.command = command;
    }
    public String toString(){
        return message+" priorty:" +priority+" uniqueId:"+uniqueId;
    }
}