# Tachiload

## Manga downloader based on [tachiyomi-extensions](https://github.com/tachiyomiorg/tachiyomi-extensions)

This software is compatible with most of the extensions of [Tachiyomi](https://github.com/tachiyomiorg/tachiyomi), see the compatibility list [here](COMPATIBILITY.md)

## Usage
 - set your config file path and your download path in the `docker-compose.yml` file
 - (optionnal) edit the environment variables (TACHILOAD_CRON, TACHILOAD_WEBHOOK, TACHILOAD_CONFIG, TACHILOAD_DOWNLOAD) in the `docker-compose.yml` file (they correspond to the cli args)
 - run `docker-compose build` to build the container and `docker-compose up -d` to run it
 - to use the configuration utility, run `docker-compose exec tachiload /app/configure.py` and restart the container to apply changes

## CLI
|Name | Short Name | Description | Example|
|--------|------|------------|-------|
| --cron [pattern] | -c | Cron pattern to run the download, if not specified, the download will run once and the program will exit | --cron "0 */6 * * *" |
| --webhook [url] | -wh | Webhook url to receive notifications (discord) | --webhook "https://discord.com/api/webhooks/ID/TOKEN" |
| --configPath [path] | -cfg | Path to the configuration file (default: ./config.json) | --configPath "/app/config.json" |
| --downloadPath [path] | -dl | Path to the download directory (default: ./downloads) | --downloadPath "/app/downloads" |
| --extensions | |  (DEV) return a json object of all available extensions | --extensions |
| --search [name] [ext1_lang] [ext1_name] [ext2_lang] [ext2_name] | | (DEV) return a json list of results for a search | --search "solo leveling" "en" "mangasee" |

Thanks to [ClementD64](https://github.com/ClementD64) for his help on this project
