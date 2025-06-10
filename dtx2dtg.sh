#!/usr/bin/env bash

set -e
mvn exec:java -Dexec.args="$*"
