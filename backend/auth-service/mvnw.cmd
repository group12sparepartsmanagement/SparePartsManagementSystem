@echo off
rem ===================================================
rem  ASPMS Auth Service — Maven Wrapper
rem  Uses locally downloaded Maven 3.9.6
rem ===================================================
setlocal

set JAVA_HOME=C:\Program Files\Java\jdk-21.0.10
set MVN_CMD=C:\Users\ANZAR\apache-maven-3.9.6\bin\mvn.cmd

if not exist "%MVN_CMD%" (
    echo ERROR: Maven not found at %MVN_CMD%
    echo Please download Maven 3.9.6 to C:\Users\ANZAR\apache-maven-3.9.6
    exit /b 1
)

"%MVN_CMD%" %*
