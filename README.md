# Tachiload

## Manga downloader based on [tachiyomi-extensions](https://github.com/tachiyomiorg/tachiyomi-extensions)

This software is compatible with most of the extensions of [Tachiyomi](https://github.com/tachiyomiorg/tachiyomi), see the compatibility list [here](COMPATIBILITY.md)

## Usage
 - set your config file path and your download path in the `docker-compose.yml` file
 - (optionnal) edit the crontab in the last arg of CMD in the `Dockerfile`
 - run `docker-compose build` to build the container and `docker-compose up -d` to run it
 - to use the configuration utility, run `docker-compose exec tachiload /app/configure.py` and restart the container to apply changes

Thanks to [ClementD64](https://github.com/ClementD64) for his help on this project
