#!/bin/bash

args=()

if [ "$TACHILOAD_WEBHOOK" != "" ]; then
args+=("--webhook" "$TACHILOAD_WEBHOOK")
fi

if [ "$TACHILOAD_CRON" != "" ]; then
args+=("--cron" "$TACHILOAD_CRON")
fi

if [ "$TACHILOAD_CONFIG" != "" ]; then
args+=("--configFile" "$TACHILOAD_CONFIG")
fi

if [ "$TACHILOAD_DOWNLOAD" != "" ]; then
args+=("--downloadPath" "$TACHILOAD_DOWNLOAD")
fi

cd /app && java -jar app.jar "${args[@]}"