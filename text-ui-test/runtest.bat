@echo off
setlocal enableextensions
pushd %~dp0

cd ..
call gradlew clean shadowJar

cd build\libs
for /f "tokens=*" %%a in ('dir /b *.jar') do (
    set jarloc=%%a
)

java -jar %jarloc% < ..\..\text-ui-test\input.txt > ..\..\text-ui-test\ACTUAL.TXT

cd ..\..\text-ui-test

cmd /c "FC /W ACTUAL.TXT EXPECTED.TXT >NUL"
if %errorlevel% == 0 (
    echo Test passed!
) else (
    echo Test failed!
    exit /b 1
)