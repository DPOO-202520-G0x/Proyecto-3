#!/usr/bin/env bash
set -euo pipefail

VERSION="1.10.2"
JAR="junit-platform-console-standalone-${VERSION}.jar"
JAR_PATH="lib/${JAR}"

if [[ ! -f "${JAR_PATH}" ]]; then
  echo "[INFO] No se encontró ${JAR_PATH}. Ejecutando scripts/descargar_junit.sh..."
  ./scripts/descargar_junit.sh
fi

mkdir -p bin
find src -name "*.java" > all-sources.txt
javac -cp "${JAR_PATH}" -d bin @all-sources.txt

java -jar "${JAR_PATH}" --class-path bin --scan-class-path --include-package Tests
