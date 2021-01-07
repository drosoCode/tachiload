FROM openjdk:8-jdk-buster

RUN apt-get update && apt-get install -y python3 git

WORKDIR /app
ADD . /app
RUN git clone --depth 1 https://github.com/tachiyomiorg/tachiyomi-extensions /tmp/tachiyomi-extensions
RUN mkdir -p /app/app/src/main/kotlin/tachiload/extension
RUN mv /tmp/tachiyomi-extensions/src/* /app/app/src/main/kotlin/tachiload/extension
RUN cp /app/scripts/prepare.py /app/app/src/main/kotlin/tachiload/extension
RUN cp /app/scripts/build.py /app/
RUN cd /app/app/src/main/kotlin/tachiload/extension && python3 prepare.py
RUN chmod +x /app/gradlew
RUN python3 build.py

CMD "bash"