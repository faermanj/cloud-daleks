
#!/bin/bash

export IMAGE_NAME=${IMAGE_NAME:-"exterminate"}
export IMAGE_VERSION=${IMAGE_VERSION:-"latest"}

docker build \
    -f Containerfile \
    --no-cache \
    --progress=plain \
    -t $IMAGE_NAME:$IMAGE_VERSION \
    .

docker push $IMAGE_NAME:$IMAGE_VERSION
