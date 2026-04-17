@echo off
REM Build all Java sources using the jars in lib\
cd /d "%~dp0"
setlocal
if exist files.txt del files.txt
for /r src %%f in (*.java) do echo %%f>>files.txt
if not defined JAVA_HOME (
    if exist "%APPDATA%\Code\User\globalStorage\pleiades.java-extension-pack-jdk\java\latest" (
        set "JAVA_HOME=%APPDATA%\Code\User\globalStorage\pleiades.java-extension-pack-jdk\java\latest"
    )
)
if defined JAVA_HOME (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)
where javac >nul 2>nul
if errorlevel 1 (
    echo javac was not found. Please install a JDK or set JAVA_HOME before running build.bat.
    pause
    exit /b 1
)
set CP=lib\mysql-connector-j-8.2.0.jar;lib\javax.mail.jar;lib\javax.activation.jar;lib\slf4j-api-1.7.36.jar;lib\slf4j-simple-2.0.9.jar;lib\forms-7.0.8.jar;lib\kernel-7.0.8.jar;lib\layout-7.0.8.jar;lib\io-7.0.8.jar;lib\barcodes-7.0.8.jar;lib\font-asian-7.0.8.jar;lib\hyph-7.0.8.jar;lib\pdfa-7.0.8.jar;lib\sign-7.0.8.jar;lib\pdftest-7.0.8.jar;lib\killbill-platform-osgi-bundles-lib-slf4j-osgi-0.41.9.jar;lib\forms-7.0.8-sources.jar
javac -cp "%CP%;." @files.txt
if errorlevel 1 pause
endlocal
