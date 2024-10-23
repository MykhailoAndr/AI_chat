#!/bin/bash



FIFO_FILE2="/tmp/sebastion_fifo2"
mkfifo $FIFO_FILE2
FIFO_FILE3="/tmp/sebastion_fifo3"
mkfifo $FIFO_FILE3


python3 Text_to_Speach.py &
sleep 2

java App &
JAVA_PID=$!


FIFO_FILE="/tmp/sebastion_fifo"
mkfifo $FIFO_FILE
input=""
while true; do
    sleep 1.5
    tput cud1  
    read -p "" input

    
    if [ "$input" == "By" ]; then
        break
    fi

    echo "$input" > $FIFO_FILE
done


unset FIFO_FILE
echo "Program shut down."

wait $JAVA_PID