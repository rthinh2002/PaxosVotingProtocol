#!/bin/bash

javac *.java
pkill -f java

echo "TESTING IS RUNNING"
pkill -f java
sleep 1
java Election 10_members_5_p.txt > "testing_result/10_members_5_p.txt" &
sleep 5

pkill -f java
sleep 1
java Election 60_members_5_p.txt > "testing_result/60_members_5_p.txt" &
sleep 10

pkill -f java
sleep 1
java Election 50_members_2_p.txt > "testing_result/50_members_2_p.txt" &

echo "Testing is Done, see testing_result folder for result"
