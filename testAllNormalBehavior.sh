#!/bin/bash

javac *.java
pkill -f java
echo "TESTING FOR GENERAL SCENARIO <M1 - M9 have immediate responses to voting queries, 1 proposer - 0 offline>"
java Election normal_scenario.txt > "testing_result/1_proposer_0_offline.txt" & 
sleep 5
echo "TEST DONE! SEND RESULT TO testing_result/1_proposer_0_offline.txt"

pkill -f java
sleep 1
echo "TESTING FOR GENERAL SCENARIO <2 proposers - 0 offline>"
java Election 2_proposer_online.txt > "testing_result/2_proposer_0_offline.txt" &
sleep 5
echo "TEST DONE! SEND RESULT TO testing_result/2_proposer_0_offline.txt"

pkill -f java
sleep 1
echo "TESTING FOR GENERAL SCENARIO <3 proposers - 2 offline>"
java Election 2_proposer_off.txt > "testing_result/3_proposer_2_offline.txt" &
sleep 5
echo "TEST DONE! SEND RESULT TO testing_result/3_proposer_2_offline.txt"

pkill -f java
sleep 1
echo "TESTING FOR GENERAL SCENARIO <3 proposers - 0 offline>"
java Election 3_proposers_no_offline.txt > "testing_result/3_proposer_0_offline.txt" &
sleep 5
echo "TEST DONE! SEND RESULT TO testing_result/3_proposer_0_offline.txt"

pkill -f java
sleep 1
echo "TESTING FOR GENERAL SCENARIO <2 proposers - 1 offline>"
java Election 2_proposer_1_offline.txt > "testing_result/2_proposer_1_offline.txt" &
sleep 5
echo "TEST DONE! SEND RESULT TO testing_result/2_proposer_1_offline.txt"

pkill -f java
sleep 1
echo "TESTING FOR GENERAL SCENARIO <3 proposers - 1 offline>"
java Election 3_proposer_1_offline.txt > "testing_result/3_proposer_1_offline.txt" &
sleep 5
echo "TEST DONE! SEND RESULT TO testing_result/3_proposer_1_offline.txt"

pkill -f java
sleep 1
echo "TESTING FOR GENERAL SCENARIO <2 proposers - 0 offline - never response>"
java Election 2_proposer_never_response.txt > "testing_result/2_proposer_never_response.txt" &
sleep 5
echo "TEST DONE! SEND RESULT TO testing_result/2_proposer_never_response.txt"

pkill -f java
sleep 1
echo "TESTING FOR GENERAL SCENARIO <1 proposers - 0 offline - medium delay>"
java Election 1_proposer_0_offline_delay_medium.txt > "testing_result/1_proposer_0_offline_delay_medium.txt" &
sleep 5
echo "TEST DONE! SEND RESULT TO testing_result/1_proposer_0_offline_delay_medium.txt"

pkill -f java
sleep 1
echo "TESTING FOR GENERAL SCENARIO <3 proposers - 1 offline - late delay>"
java Election 3_proposer_1_offline_delay_late.txt > "testing_result/3_proposer_1_offline_delay_late.txt" &
sleep 35
echo "TEST DONE! SEND RESULT TO testing_result/3_proposer_1_offline_delay_late.txt"

pkill -f java