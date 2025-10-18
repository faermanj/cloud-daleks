#!/bin/bash
set -euo pipefail

BIN_NAME="exterminate"

java -version
./mvnw -f "${BIN_NAME}" clean install -Pnative
mkdir -p bin
cp "${BIN_NAME}/target/${BIN_NAME}" bin/entrypoint

ls -liah bin/entrypoint

echo "make completed."