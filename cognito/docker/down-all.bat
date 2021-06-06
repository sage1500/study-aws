@echo off
set base=%~d0%~p0

docker-compose -f %base%db\docker-compose.yml down
REM docker-compose -f %base%keycloak\docker-compose.yml down
