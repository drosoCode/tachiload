FROM openjdk:8-jdk-buster AS builder

WORKDIR /app

RUN apt-get update && apt-get install -y python3 git
ADD . /app

RUN git clone --depth 1 https://github.com/tachiyomiorg/tachiyomi-extensions /tmp/tachiyomi-extensions \
    && mkdir -p /app/app/src/main/kotlin/tachiload/extension \
    && mv /tmp/tachiyomi-extensions/src/* /app/app/src/main/kotlin/tachiload/extension \
    && cd /app/scripts && python3 prepare.py \
    && python3 build.py

FROM openjdk:8-jre-slim

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends python3 python3-pip whiptail \
    libwoff1 libopus0 libwebp6 libwebpdemux2 libenchant1c2a libgudev-1.0-0 libsecret-1-0 libhyphen0 libgdk-pixbuf2.0-0 libegl1 libnotify4 libxslt1.1 libevent-2.1-6 libgles2 libvpx5 libxcomposite1 libatk1.0-0 libatk-bridge2.0-0 libepoxy0 libgtk-3-0 libharfbuzz-icu0 libnss3 libxss1 libasound2 fonts-noto-color-emoji libxtst6 libdbus-glib-1-2 libxt6 xvfb \
    && pip3 install whiptail-dialogs
COPY --from=builder /app/entrypoint.sh /app/entrypoint.sh
COPY --from=builder /app/scripts/configure.py /app/configure.py
COPY --from=builder /app/app/build/libs/app-all.jar /app/app.jar
ENTRYPOINT ["/app/entrypoint.sh"]
