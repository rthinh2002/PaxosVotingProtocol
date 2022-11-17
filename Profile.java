import java.util.HashMap;
import java.util.Map;

// This class contains the profile of participants and settings of the programs

public class Profile {
    public static int acceptors = 6; // M4 -> 9
    public static int proposers = 3; // M1, M2, M3
    public static int responseTime = 5; // Maximum of 5 seconds delay in response for M4 -> 9
    private static int id = 0;
    // declare status for the participants

    // List of participants
    public static String[] participants = { "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9" };

    // Initialize the hashmap for port number and name of the participants
    // Start port numbers from 4001 to 4009
    public static Map<String, Integer> map = new HashMap<String, Integer>() {
        {
            put("M1", 4001);
            put("M2", 4002);
            put("M3", 4003);
            put("M4", 4004);
            put("M5", 4005);
            put("M6", 4006);
            put("M7", 4007);
            put("M8", 4008);
            put("M9", 4009);
        }
    };

    // This method accept a name of participants and return it's port number
    public static int getPortNumber(String name) {
        return map.containsKey(name) ? map.get(name) : -1;
    }

    // This method will return the incremental of the id to make it unique
    public static synchronized int getId() {
        id++;
        return id;
    }

    // This method return the hostname
    public static String getHostName() {
        return "localhost";
    }
}
