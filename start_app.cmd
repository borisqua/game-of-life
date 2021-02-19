@echo off
@chcp 65001
./gradlew.bat clean build
java -jar app/build/libs/app.jar
