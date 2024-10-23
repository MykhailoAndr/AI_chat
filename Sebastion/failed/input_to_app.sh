#!/bin/bash

# Check if the FIFO file exists
FIFO_FILE="/tmp/sebastion_fifo"
if [ ! -p $FIFO_FILE ]; then
    echo "Error: FIFO file does not exist. Make sure the Java application is running."
    exit 1
fi

# Read input from the user
read -p "Enter text for App.java: " input

# Send the input to the FIFO file
echo "$input" > $FIFO_FILE