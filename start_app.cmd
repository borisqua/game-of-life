@REM @echo off
@chcp 65001
./gradlew.bat clean build
echo Start app...
java -jar app/build/libs/app.jar
