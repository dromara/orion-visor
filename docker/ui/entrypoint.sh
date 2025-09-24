#!/bin/sh

# 设置环境变量
NGINX_SERVICE_HOST="${NGINX_SERVICE_HOST:-service}"
NGINX_SERVICE_PORT="${NGINX_SERVICE_PORT:-9200}"

# 替换环境变量
sed -i "s|\${NGINX_SERVICE_HOST}|${NGINX_SERVICE_HOST}|g" /etc/nginx/conf.d/service.conf
sed -i "s|\${NGINX_SERVICE_PORT}|${NGINX_SERVICE_PORT}|g" /etc/nginx/conf.d/service.conf

exec "$@"