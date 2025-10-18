#!/bin/bash

if ! command -v devbox &> /dev/null
then
    echo "Devbox could not be found, installing..."
    curl -fsSL https://get.jetify.com/devbox | bash -s -- --force    
    export PATH="$HOME/.devbox/bin:$PATH"
    echo "Installing devbox packages..."
    mkdir -p /nix
    sudo chown -R $(whoami) /nix
    yes | devbox install 
    sudo chown -R $(whoami) /nix
fi  

# if quarkus cli is not installed, install it using sdkman
if ! command -v quarkus &> /dev/null
then
    echo "Quarkus CLI could not be found, installing..."
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"
    sdk install quarkus
fi
echo "Quarkus CLI version: $(quarkus --version)"

# Check musl
if ! command -v x86_64-linux-musl-gcc &> /dev/null
then
    echo "Musl toolchain could not be found, installing..."
    sudo apt install  -y musl-dev upx


    
    export PATH
fi
echo "Musl toolchain gcc version: $(x86_64-linux-musl-gcc --version | head -n 1)"
echo "UPX version: $(upx --version | head -n 1)"


# run mvn go offline to download dependencies
if [ -f "./pom.xml" ]; then
    echo "Downloading Maven dependencies for main project..."
    ./mvnw dependency:go-offline
    ./mvnw dependency:tree
fi
