FROM alpine:3.11

RUN apk --no-cache update \
    && apk --no-cache add bash python3 py3-pip \
    && pip3 --no-cache-dir install awscli s3cmd \
    && rm -rf /var/cache/apk/*

COPY s3push.sh .
RUN chmod +x ./s3push.sh

ENTRYPOINT [ "/bin/sh" ]
CMD [ "./s3push.sh" ]
