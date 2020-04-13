FROM openjdk:12-alpine

# Argument to pass version via build-arg parameter
ARG APP_VERSION

WORKDIR /app

# Set 'noninteractive' seting. 'noninteractive' is needed for all the build steps, while 'teletype' is needed for interactive configuration steps the user might want to do inside a running container.
ENV DEBIAN_FRONTEND noninteractive

# Set default timezone
ENV TZ "Europe/Amsterdam"

# Copy build jar to image
COPY target/ds-medewerkerbeheer-$APP_VERSION.jar ./application.jar

# Set 'teletype' setting. 'noninteractive' is needed for all the build steps, while 'teletype' is needed for interactive configuration steps the user might want to do inside a running container.
ENV DEBIAN_FRONTEND teletype

EXPOSE 8080

# Start command
CMD [ "java", "-jar", "application.jar" ]
