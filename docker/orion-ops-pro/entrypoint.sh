#!/bin/bash
nginx
cd /app
java -jar app.jar --spring.profiles.active=prod
