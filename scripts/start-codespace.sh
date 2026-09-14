#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")/.."

echo "Waiting for Docker inside the development container..."
ready=false
for attempt in {1..30}; do
  if docker info >/dev/null 2>&1; then
    ready=true
    break
  fi
  sleep 2
done
if [ "$ready" != true ]; then
  echo "Docker did not become ready. In Codespaces, rebuild the dev container and retry." >&2
  exit 1
fi

# Match the port forwarded by devcontainer.json even if a local .env exists.
export APP_PORT=8080
docker compose up --build --detach --wait --wait-timeout 240
echo
echo "App ready: http://localhost:8080/FrontEnd/"
if [ -n "${CODESPACE_NAME:-}" ] && [ -n "${GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN:-}" ]; then
  printf 'Browser URL: https://%s-8080.%s/FrontEnd/\n' "$CODESPACE_NAME" "$GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN"
fi
echo "Demo login: AndrewBadie / 1234. Try searching Psychology."
