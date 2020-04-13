FROM alpine:3.10

COPY ./config /config

ENV KUBECONFIG=/config

RUN apk add --no-cache curl ca-certificates gettext

RUN curl -L https://storage.googleapis.com/kubernetes-release/release/v1.14.9/bin/linux/amd64/kubectl -o /usr/local/bin/kubectl

RUN chmod u+x /usr/local/bin/kubectl