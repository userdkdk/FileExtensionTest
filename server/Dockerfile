FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
# jar 파일 복제
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# 실행 명령어 test
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]