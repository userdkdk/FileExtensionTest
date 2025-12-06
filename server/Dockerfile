FROM openjdk:17-jdk
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
# jar 파일 복제
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# 실행 명령어 test
ENTRYPOINT ["java", "-Dspring.profiles.active=prod,secret", "-jar", "app.jar"]