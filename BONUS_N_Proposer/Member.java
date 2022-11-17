import java.util.HashMap;

// This class will represent an acceptor M4 - 9

public class Member {
    private boolean accepted;
    private boolean finalDecision;
    private int currentAcceptedProposalID;
    private int id;
    private String responseTime;
    private String acceptedValue;
    private String proposedValue;
    private String name;
    private String status;
    private boolean isOffline;
    private HashMap<String, Integer> statusMap;
    private HashMap<String, String> messageMap = new HashMap<>();

    public Member(boolean accepted, int acceptedProposalID, String acceptedValue, String name, String responseTime) {
        this.accepted = accepted;
        this.currentAcceptedProposalID = acceptedProposalID;
        this.acceptedValue = acceptedValue;
        this.name = name;
        this.responseTime = responseTime;
    }

    public Member(String name) {
        this.isOffline = false;
        this.status = "PREPARE";
        this.id = Profile.getId();
        this.name = name;
        this.statusMap = new HashMap<>();
        // Assign proposer value
        this.proposedValue = getName();
    }

    public String getName() {
        return this.name;
    }

    public String getProposedValue() {
        return this.acceptedValue;
    }

    // This method responsible for handling the message from proposer
    public void receivedMessageFromProposer(String message) {
        this.messageMap = ParsingCommunication.parseMessages(message);
        String responseMessage = "";
        String proposerName = this.messageMap.get("PROPOSER");
        String status = this.messageMap.get("STATUS");
        int portNumber = Profile.getPortNumber(proposerName);

        if (status.equals("PREPARE")) {
            responseMessage = handlingPrepare();
        } else if (status.equals("ACCEPT")) {
            responseMessage = handlingAccept();
        } else if (status.equals("VOTING")) {
            responseMessage = handlingVoting();
        }

        // Check if delay of response is necessary for the scenario
        if (getResponseTime(this.responseTime) != 0) {
            if (getResponseTime(this.responseTime) == -999) {
                System.out.println(this.name + " is too busy, never response");
                return;
            } else {
                try {
                    Thread.sleep(getResponseTime(this.responseTime));
                } catch (InterruptedException e) {
                    System.out.println("UNABLE TO PROCESS ACCEPTOR " + e.getMessage());
                }
            }
        }

        if (responseMessage.isEmpty())
            return;
        Server.sendMessages(Profile.getHostName(), portNumber, responseMessage);
    }

    // This will be the method handling PREPARE status - Phase 1b
    // Check if the id value is greater than any previous accepted value
    // If yes, save the max value and the accept value
    // If no, just response with not accepted status
    private String handlingPrepare() {
        if (Integer.parseInt(this.messageMap.get("ID")) <= this.currentAcceptedProposalID) {
            logMessage("NO_PROMISE", this.accepted);
            return composeStringRespond("NO_PROMISE");
        } else {
            this.currentAcceptedProposalID = Integer.parseInt(this.messageMap.get("ID"));
            logMessage("PROMISE", this.accepted);
            return composeStringRespond("PROMISE");
        }
    }

    // This method will be the method handling the ACCEPT status
    // If it is the max id, then accept the proposal
    // save the accepted proposal id
    // save the accepted proposal value
    // Respond to all proposers and learners
    private String handlingAccept() {
        if (Integer.parseInt(this.messageMap.get("ID")) >= this.currentAcceptedProposalID) {
            this.accepted = true;
            this.currentAcceptedProposalID = Integer.parseInt(this.messageMap.get("ID"));
            this.acceptedValue = this.messageMap.get("VALUE");
            logMessage("ACCEPTED", this.accepted);
            return composeStringRespond("ACCEPTED");
        } else {
            logMessage("REJECTED", this.accepted);
            return composeStringRespond("REJECTED");
        }
    }

    // Method to handleVoting signal to set the final decision when the majority of
    // the participants decided for president
    private String handlingVoting() {
        if (!this.finalDecision) {
            this.finalDecision = true;
        }
        logMessage("DECIDED", this.accepted);
        return composeStringRespond("DECIDED");
    }

    // Get the response time for a given string time
    private int getResponseTime(String message) {
        switch (message) {
            case "immediately":
                return 0;
            case "medium":
                return 1000;
            case "late":
                return 3000;
            case "never":
                return -999;
            default:
                return -1;
        }
    }

    // Compose the respond for acceptor
    private String composeStringRespond(String responseStatus) {
        return "ACCEPTOR:" + this.name + "\nSTATUS:" + responseStatus + "\nACCEPTED:" + this.accepted
                + "\nACCEPTED PROPOSAL ID:" + this.currentAcceptedProposalID + "\nACCEPTED_VAL:" + this.acceptedValue
                + "\n";
    }

    // Compose message for terminal
    private void logMessage(String status, Boolean accepted) {
        System.out.println(this.name + " information: Current Accept Proposal ID: "
                + this.currentAcceptedProposalID + " - Accepted Value: " + this.acceptedValue
                + " - Status of Acceptor: " + status + " - Previous Acception: " + accepted + "\n");
    }

    // Set the proposer offline if if receiving the signal
    public void shutDown(boolean offline) {
        this.isOffline = offline;
        // Send a "OFFLINE" message to the Server
        Server.sendMessages(Profile.getHostName(), Profile.map.get(this.name), "OFFLINE");
    }

    // Proposing, sending message to all proposers
    public void propose() {
        this.statusMap.clear(); // Refresh the status map to check for majority of messages
        for (int i = 0; i < Profile.acceptors; i++) { // Propose to all the acceptors
            Server.sendMessages(Profile.getHostName(), Profile.map.get(Profile.participants[i + Profile.proposers]),
                    composeStringMessage());
            System.out.println(this.name + " send proposal to " + Profile.participants[i + Profile.proposers] + "\n");
        }
    }

    // This method will responsible for handling messages
    public void receivedMessageFromAcceptor(String message) {
        if (this.isOffline)
            return;
        HashMap<String, String> messMap = ParsingCommunication.parseMessages(message);
        String status = messMap.get("STATUS");

        if (status.equals("PROMISE")) {
            handlePromise(messMap);
        } else if (status.equals("NO_PROMISE")) {
            handleNoPromise();
        } else if (status.equals("ACCEPTED")) {
            handleAccepted();
        } else if (status.equals("REJECTED")) {
            handleRejected();
        } else if (status.equals("DECIDED")) {
            handleDecided(messMap.get("ACCEPTED_VAL"), messMap.get("ACCEPTOR"));
        }
    }

    // This method will handle message from acceptor with status RECEIVED_PREPARE
    // Set status to ACCEPT and resend the proposal if received the status from half
    // + 1
    private void handlePromise(HashMap<String, String> map) {
        int count = this.statusMap.getOrDefault("PROMISE", 0) + 1;
        String acceptorAcceptedValue = map.get("ACCEPTED_VAL");
        if (count > Profile.acceptors / 2) {
            if (!acceptorAcceptedValue.isEmpty()) {
                this.proposedValue = acceptorAcceptedValue;
            }
            this.status = "ACCEPT";
            propose();
        } else {
            this.statusMap.put("PROMISE", count);
        }
    }

    // Method to handle status RECEIVED_PREPARED_NOT_ACCEPTED
    // Update the ID and propose
    private void handleNoPromise() {
        int count = this.statusMap.getOrDefault("NO_PROMISE", 0) + 1;
        if (count > Profile.acceptors / 2) {
            this.status = "PREPARE";
            this.id = Profile.getId();
            propose();
        } else {
            this.statusMap.put("NO_PROMISE", count);
        }
    }

    // Method to handle accepted
    // If get accepted from half + 1, then propose a voting request to finalize the
    // voting phase
    private void handleAccepted() {
        int count = this.statusMap.getOrDefault("ACCEPTED", 0) + 1;
        if (count > Profile.acceptors / 2) {
            this.status = "VOTING";
            propose();
        } else {
            this.statusMap.put("ACCEPTED", count);
        }
    }

    // Method to handle rejected
    // If get rejected from half + 1, update id and propose again
    private void handleRejected() {
        int count = this.statusMap.getOrDefault("REJECTED", 0) + 1;
        if (count > Profile.acceptors / 2) {
            this.status = "PREPARE";
            this.id = Profile.getId();
            propose();
        } else {
            this.statusMap.put("REJECTED", count);
        }
    }

    // Method to handle voting decision
    // Print out the message
    private void handleDecided(String val, String name) {
        System.out.println(name + " announced that it voted " + val + " for President");
    }

    // Send request
    public String composeStringMessage() {
        return "PROPOSER:" + this.name + "\nSTATUS:" + this.status + "\nID:" + this.id + "\nVALUE:"
                + this.proposedValue
                + "\n";
    }
}
