#!/bin/bash

declare -A args

if [ ! -z "$TACHILOAD_WEBHOOK" ]; then
  args+=("--webhook" "$TACHILOAD_WEBHOOK")
fi

if [ ! -z "$TACHILOAD_CRON" ]; then
  args+=("--cron" "$TACHILOAD_CRON")
fi

if [ ! -z "$TACHILOAD_CONFIG" ]; then
  args+=("--configFile" "$TACHILOAD_CONFIG")
fi

if [ ! -z "$TACHILOAD_DOWNLOAD" ]; then
  args+=("--downloadPath" "$TACHILOAD_DOWNLOAD")
fi

exec java -jar app.jar "${args[@]}" "$@"