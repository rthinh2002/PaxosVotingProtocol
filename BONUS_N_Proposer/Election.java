import java.util.HashMap;
import java.util.stream.Collectors;
import java.io.*;

// Main class to start

public class Election {
    public static void main(String[] args) throws IOException {
        int proposers = 1;
        String responseTime = "immediately";
        int offline = 0;
        int member = 9;

        if (args.length > 1 || args.length < 1) {
            System.out.println("Error: use this format: java Election <filename>");
        } else {
            HashMap<String, String> map = parseFile(args[0]);
            proposers = Integer.parseInt(map.getOrDefault("proposers", "1"));
            responseTime = map.getOrDefault("response_time", "immediately");
            offline = Integer.parseInt(map.getOrDefault("offline", "0"));
            member = Integer.parseInt(map.getOrDefault("member", "9"));

            // Validating error for response time
            if (!responseTime.equals("immediately") && !responseTime.equals("medium")
                    && !responseTime.equals("late") && !responseTime.equals("never")) {
                System.out.println(
                        "Invalid response_time value, response_time should be either immediately, medium, late, or never");
                return;
            }

            // Validating error for offline members
            if (offline >= proposers) {
                System.out.println(
                        "Invalid number of offline members, for this scenario, a maximum of 2 offline proposers will be accepted");
                return;
            }

            if (member < proposers || member < offline) {
                System.out.println("Invalid number members, number of members need to be larger than proposers");
                return;
            }
        }

        startElection(proposers, responseTime, offline, member);
    }

    // This method will receive number of proposers, response_time, and offline
    public static void startElection(int proposerNumber, String responseTime, int offline, int member) {
        System.out.println("====== Election is Beginning ======");
        System.out.println("====== There are " + proposerNumber + " proposers and " + (member - proposerNumber)
                + " acceptors ======");
        System.out.println("====== Delay in response time: " + responseTime + " ======");
        System.out.println("Members that disconnect: " + offline);
        new Profile(proposerNumber, member);
        Member[] proposers = new Member[proposerNumber];
        MemberRunnable[] threadP = new MemberRunnable[proposerNumber];
        for (int i = 0; i < proposerNumber; i++) {
            proposers[i] = new Member(Profile.participants[i]);
            threadP[i] = new MemberRunnable(proposers[i]);
        }

        // Check for offline proposers, propose and then go off
        if (offline > 0) {
            for (int i = 0; i < offline; i++) {
                proposers[i + 1].shutDown(true);
            }
        }

        // Start run the acceptors thread
        for (int i = 0; i < Profile.acceptors; i++) {
            Member acceptor = new Member(false, -1, "", Profile.participants[i + Profile.proposers], responseTime);
            new MemberRunnable(acceptor);
        }

        // Sending propose messages
        for (int i = 0; i < proposerNumber; i++) {
            threadP[i].propose();
        }

    }

    // This method will read the file and parse it with the given file name
    public static HashMap<String, String> parseFile(String fileName) {
        BufferedReader file = null;

        // Read the file
        try {
            InputStream fStream = ClassLoader.getSystemClassLoader().getResourceAsStream("scenario/" + fileName);
            if (fStream == null)
                throw new FileNotFoundException();
            file = new BufferedReader(new InputStreamReader(fStream));
        } catch (FileNotFoundException e) {
            System.out.println("Error when reading file: " + e.getMessage());
        }

        // Parsing
        HashMap<String, String> inputMap = new HashMap<String, String>();

        String fileContent = file.lines().collect(Collectors.joining("\n"));
        String[] categories = fileContent.split("\n");

        for (int i = 0; i < categories.length; i++) {
            int delim = categories[i].indexOf(":");
            if (delim != -1) {
                inputMap.put(categories[i].substring(0, delim), categories[i].substring(delim + 1));
            }
        }
        return inputMap;
    }
}
