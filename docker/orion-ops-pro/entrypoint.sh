#!/bin/bash
cd /app
nohup java -jar app.jar --spring.profiles.active=prod 2>&1 &
nginx -g 'daemon off;'