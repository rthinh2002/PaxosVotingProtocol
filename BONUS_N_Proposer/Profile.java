import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// This class contains the profile of participants and settings of the programs

public class Profile {
    public static int acceptors; // M4 -> 9
    public static int proposers; // M1, M2, M3
    public static int members;
    private static int id = 0;
    public static Map<String, Integer> map = new HashMap<>();
    public static String[] participants;
    // declare status for the participants

    public Profile(int proposers, int members) {
        this.proposers = proposers;
        this.members = members;
        this.acceptors = members - proposers;
        this.participants = new String[members];
        createMembers();
        populateMap();
    }

    // Create members
    private static void createMembers() {
        for (int i = 0; i < members; i++) {
            String name = "M" + String.valueOf(i + 1);
            participants[i] = name;
        }
    }

    // populate map
    private static void populateMap() {
        int port = new Random().nextInt(9000) + 1000;
        for (int i = 0; i < participants.length; i++) {
            map.put(participants[i], port++);
        }
    }

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
