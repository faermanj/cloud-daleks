#!/bin/bash

# Maven cleanup script
echo "Cleaning Maven project..."
./mvnw clean

if [ $? -eq 0 ]; then
    echo "✅ Maven clean completed successfully"
else
    echo "❌ Maven clean failed"
    exit 1
fi