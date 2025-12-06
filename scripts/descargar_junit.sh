#!/usr/bin/env bash
set -euo pipefail

VERSION="1.10.2"
JAR="junit-platform-console-standalone-${VERSION}.jar"
URL="https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${VERSION}/${JAR}"
DEST="lib/${JAR}"

mkdir -p lib

if [[ -f "${DEST}" ]]; then
  echo "[INFO] Ya existe ${DEST}; no se descarga de nuevo."
  exit 0
fi

echo "[INFO] Descargando ${JAR}..."
if command -v curl >/dev/null 2>&1; then
  curl -L -o "${DEST}" "${URL}"
elif command -v wget >/dev/null 2>&1; then
  wget -O "${DEST}" "${URL}"
else
  echo "[ERROR] Necesitas curl o wget para descargar ${JAR}." >&2
  exit 1
fi

echo "[OK] Guardado en ${DEST}"
