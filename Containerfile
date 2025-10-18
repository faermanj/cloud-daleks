# docker build --no-cache --progress=plain -f Containerfile -t exterminate:latest .
# docker push exterminate:latest

ARG UBI="public.ecr.aws/amazoncorretto/amazoncorretto:25-jdk"
FROM ${UBI} AS build-stage

# System packages and updates
USER root
ENV PATH="/usr/bin:${PATH}"
RUN bash -c "yum update -y \
    && yum -y groupinstall 'Development Tools'"

## Create User
ARG USERNAME=container-user
ARG USER_UID=1001
RUN echo "$USERNAME:x:$USER_UID:$USER_UID:$USERNAME User:/home/$USERNAME:/bin/sh" >> /etc/passwd && \
    echo "$USERNAME:x:$USER_UID:" >> /etc/group && \
    mkdir -p /home/$USERNAME && \
    chown -R $USER_UID:$USER_UID /home/$USERNAME
    
# User level
USER $USERNAME

## Copy source code
WORKDIR "/home/$USERNAME/src"
COPY --chown=$USERNAME  . .


# Build
RUN make build

# RUNTIME STAGE
FROM ${UBI}

COPY --from=build-stage "/usr/src/bin/entrypoint" "/entrypoint"
ENTRYPOINT ["/entrypoint"]
