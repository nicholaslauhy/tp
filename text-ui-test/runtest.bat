@echo off
setlocal enableextensions
pushd %~dp0

cd ..
call gradlew clean shadowJar

cd build\libs
for /f "tokens=*" %%a in (
    'dir /b *.jar'
) do (
    set jarloc=%%a
)

java -jar %jarloc% < ..\..\text-ui-test\input.txt > ..\..\text-ui-test\ACTUAL.TXT

cd ..\..\text-ui-test

findstr /v "remaining" ACTUAL.TXT > ACTUAL_FILTERED.TXT
findstr /v "remaining" EXPECTED.TXT > EXPECTED_FILTERED.TXT

cmd /c "FC ACTUAL_FILTERED.TXT EXPECTED_FILTERED.TXT >NUL"
if %errorlevel% == 0 (
    echo Test passed!
    del ACTUAL_FILTERED.TXT EXPECTED_FILTERED.TXT
) else (
    echo Test failed!
    del ACTUAL_FILTERED.TXT EXPECTED_FILTERED.TXT
    exit /b 1
)