FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/nginx.conf

RUN apk update \
 && apk upgrade \
 && apk add bash

# Copy the .sh files  to the /scripts folder
RUN mkdir /scripts/
COPY *.sh /scripts/
RUN chmod +x /scripts/*.sh

EXPOSE 4200

ENTRYPOINT ["/bin/bash", "/scripts/startCmd.sh"]

WORKDIR /usr/share/nginx/html
COPY dist/fe-bestellingen .
