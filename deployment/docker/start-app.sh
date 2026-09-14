#!/usr/bin/env bash
set -euo pipefail
if [[ -n "${kubeMQAddress:-}" ]]; then
  host="${kubeMQAddress%:*}"
  port="${kubeMQAddress##*:}"
  ready=false
  for attempt in {1..60}; do
    if timeout 2 bash -c 'echo > /dev/tcp/"$1"/"$2"' _ "$host" "$port" 2>/dev/null; then
      ready=true
      break
    fi
    sleep 2
  done
  if [[ "$ready" != true ]]; then
    echo "Message broker did not become reachable" >&2
    exit 1
  fi
fi
exec catalina.sh run
