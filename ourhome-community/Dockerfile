FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

# docker build -t 계정명/레포지토리명:이미지명(선택) .
# docker push 계정명/레포지토리명:이미지명(선택)
# docker run --name spring_con -p 8081:8081 -e "SPRING_PROFILES_ACTIVE=local" 계정명/레포지토리명:이미지명(선택)