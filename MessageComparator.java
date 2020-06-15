import java.util.*;
import java.io.*;

public class MessageComparator implements Comparator<Message>{
    public int compare(Message m1, Message m2){
        if(m1.priority < m2.priority){
            return 1;
        }else if(m1.priority > m2.priority){
            return -1;
        }
        return 0;
    }
}