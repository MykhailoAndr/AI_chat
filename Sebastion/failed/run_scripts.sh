#!/bin/bash
# Initialize the pygame mixer

# Create a named pipe for communication

echo -en "\007"


FIFO_FILE2="/tmp/sebastion_fifo2"
mkfifo $FIFO_FILE2
FIFO_FILE3="/tmp/sebastion_fifo3"
mkfifo $FIFO_FILE3

python3 Text_to_Speach.py < $FIFO_FILE2 &
sleep 2




# Run App.java in the background and redirect input/output to the FIFO
java App < $FIFO_FILE2 &

    # Get the process ID of the Java process
JAVA_PID=$!


    # Run Text_to_Speach.py in the background and redirect input/output to the FIFO


    # Wait for the Java process to finish




FIFO_FILE="/tmp/sebastion_fifo"
mkfifo $FIFO_FILE
input=""
while true; do
    read -p "" input
    
    if [ "$input" == "By" ]; then
        break
    fi


    echo "$input" > $FIFO_FILE
done



unset FIFO_FILE
echo "Program shut down."

wait $JAVA_PID


# Remove the FIFO file
