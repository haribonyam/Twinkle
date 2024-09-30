# 프론트엔드 Dockerfile (Thymeleaf)
FROM openjdk:17-jdk-slim as build-stage

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 복사
COPY . .

# 애플리케이션 빌드
RUN gradle clean build --no-daemon

# 최종 이미지
FROM openjdk:17-jdk-slim
COPY --from=build-stage /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 3000