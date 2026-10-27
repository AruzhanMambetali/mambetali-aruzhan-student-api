#!/bin/bash
# Stops and restarts both services without deleting the PostgreSQL volume.
set -euo pipefail
cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.."

created="$(curl --fail-with-body -sS -X POST http://localhost:8080/api/students \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Mambetali Aruzhan Imanaliyevna","email":"persist@example.com","program":"Software Engineering","year":4}')"
echo "Before restart: $created"
id="$(printf '%s' "$created" | sed -n 's/.*"id":\([0-9]*\).*/\1/p')"
test -n "$id"

docker compose down # Important: no --volumes / -v
docker compose up -d

for attempt in $(seq 1 40); do
  if curl --fail -sS "http://localhost:8080/api/students/$id" >/dev/null 2>&1; then
    echo "After restart, student $id remains in PostgreSQL:"
    curl --fail-with-body -sS "http://localhost:8080/api/students/$id"
    echo
    exit 0
  fi
  sleep 2
done
echo "Student $id was not reachable after restart" >&2
exit 1
