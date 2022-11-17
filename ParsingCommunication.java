import java.util.HashMap;

// This class will responsible for parsing the message, convert them into hashmap and return it to the 
// proposer, acceptors

public class ParsingCommunication {

    // This method will receive a string of message, and convert them into hashmap
    public static HashMap<String, String> parseMessages(String message) {
        HashMap<String, String> resultMap = new HashMap<>();
        String[] messageArr = message.split("\n");
        for (int i = 0; i < messageArr.length; i++) {
            int delim = messageArr[i].indexOf(":");
            resultMap.put(messageArr[i].substring(0, delim), messageArr[i].substring(delim + 1));
        }
        return resultMap;
    }
}
