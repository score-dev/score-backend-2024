# 멀티 스테이지 빌드를 통해 최종 이미지 크기 최적화
# 빌드 스테이지
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Gradle 래퍼와 빌드 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Gradle 래퍼에 실행 권한 부여
RUN chmod +x gradlew

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew build -x test

# 실행 스테이지
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 컨테이너 실행 시 애플리케이션 시작
ENTRYPOINT ["java", "-jar", "app.jar"]

# 애플리케이션 포트 노출
EXPOSE 8080