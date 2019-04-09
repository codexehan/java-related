FROM java:8-jdk

ENV JAVA_HOME              /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH                   $PATH:$JAVA_HOME/bin
ENV JAVA_OPTIONS    -Xmx2048m

ENV TIME_ZONE              Asia/Singapore
ENV SPRING_PROFILES_ACTIVE dt

RUN echo "$TIME_ZONE" > /etc/timezone
RUN dpkg-reconfigure -f noninteractive tzdata

WORKDIR /app

EXPOSE 15000

ADD build/libs/data-pipeline-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT java $JAVA_OPTIONS -Djava.security.egd=file:/dev/./urandom -jar app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE