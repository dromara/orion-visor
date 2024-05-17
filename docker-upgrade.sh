#/bin/bash
docker compose down
sh ./pull.sh
docker compose up -d
