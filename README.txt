Xuan Thinh Le - a1807507

This is my implementation of Paxos algorithm in the goal of reaching concensus in a voting protocol between processes, which
can be understand/represent as participants with name from M1 -> M9
The implementation of Paxos Algorithm is based on follow link:
https://people.cs.rutgers.edu/~pxk/417/notes/paxos.html
https://www.microsoft.com/en-us/research/uploads/prod/2016/12/paxos-simple-Copy.pdf

For testing of delay in response time, please do it manually since testing result will be log out into files, can't really see the delay.

For compile and running manually:
1. pkill -f java
1. Run command: javac *.java
2. Run command: java Election <filename> (filename will be the name of file in scenario folder - Example: java Election normal_scenario)
Please use the following files for testing delay: 1_proposer_0_offline.txt, 2_proposer_never_response.txt, and 3_proposer_1_offline_delay_late.txt


AUTOMATED TESTING INSTRUCTION:
Navigate to the assignment3 folder
Run this command: chmod +x testAllNormalBehavior.sh
Run this command: ./testAllNormalBehavior.sh
This test will run the total of 4 scenarios for testing:
1. 3 proposers sending proposals, M2, M3 go off after send a proposals
2. 3 proposers sending proposals, no offline
3. 2 proposers, no offline
4. 1 proposer, no offline 
5. 1 proposer, with delay medium
6. 2 proposers, 1 offline
7. 2 proposers, no response
8. 3 proposers, 1 offline
9. 3 proposers, 1 offline, delay late

Please view testing_result folder to the output of each scenarios
Run this command after you done with automated testing to clear java task running in the background: pkill -f java


BONUS:
Navigate to the BONUS_N_Proposer folder
Run the command: chmod +x testAllNormalBehavior.sh
Run the command ./testAllNormalBehavior.sh
This program will run inside the terminal
Specify number proposers and number of members inside the /scenarios/normal_scenario.txt for testing more cases, please use a reasonable number
Run this command after you done with automated testing to clear java task running in the background: pkill -f java


This assignment has been tested with University Red Hat machine
Please contact me with any issues or questions
