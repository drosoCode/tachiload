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
    && pip3 install whiptail-dialogs

COPY --from=builder /app/entrypoint.sh /app/entrypoint.sh
COPY --from=builder /app/scripts/configure.py /app/configure.py
COPY --from=builder /app/app/build/libs/app-all.jar /app/app.jar
ENTRYPOINT ["/app/entrypoint.sh"]
