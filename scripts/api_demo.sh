#!/bin/bash
# Run after docker compose up -d --build. This script performs all CRUD operations.
set -euo pipefail
base_url="${1:-http://localhost:8080}/api/students"

echo 'CREATE: HTTP 201'
created="$(curl --fail-with-body -sS -X POST "$base_url" \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Mambetali Aruzhan Imanaliyevna","email":"aruzhan@example.com","program":"Software Engineering","year":4}')"
echo "$created"
id="$(printf '%s' "$created" | sed -n 's/.*"id":\([0-9]*\).*/\1/p')"
if [[ -z "$id" ]]; then
  echo 'Could not extract student ID' >&2
  exit 1
fi

echo 'GET ALL: HTTP 200'
curl --fail-with-body -sS "$base_url"
echo
echo "GET ONE: HTTP 200 (ID=$id)"
curl --fail-with-body -sS "$base_url/$id"
echo
echo 'UPDATE: HTTP 200'
curl --fail-with-body -sS -X PUT "$base_url/$id" \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Mambetali Aruzhan Imanaliyevna","email":"updated@example.com","program":"Java Backend","year":4}'
echo
echo 'DELETE: HTTP 204'
curl -sS -o /dev/null -w 'HTTP %{http_code}\n' -X DELETE "$base_url/$id"
echo 'NOT FOUND: HTTP 404'
curl -sS -o /dev/null -w 'HTTP %{http_code}\n' "$base_url/$id"
