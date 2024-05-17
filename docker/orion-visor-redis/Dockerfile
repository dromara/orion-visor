FROM redis:6.0.16-alpine
WORKDIR /data
COPY ./redis.conf /tmp
RUN cat /tmp/redis.conf > /usr/local/redis.conf
