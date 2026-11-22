@echo off
setlocal enabledelayedexpansion
title Student Management System - College Edition

echo ========================================================
echo   Launching Student Management System (Advanced Java)
echo ========================================================

:: Detect Java Runtime
set JAVA_CMD=java

where java >nul 2>nul
if %ERRORLEVEL% equ 0 (
    goto :CHECK_BUILD
)

if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\java.exe" (
        set JAVA_CMD="%JAVA_HOME%\bin\java.exe"
        goto :CHECK_BUILD
    )
)

if exist "C:\Program Files\Android\Android Studio\jbr\bin\java.exe" (
    set JAVA_CMD="C:\Program Files\Android\Android Studio\jbr\bin\java.exe"
    goto :CHECK_BUILD
)

if exist "C:\Program Files\Java" (
    for /d %%D in ("C:\Program Files\Java\*") do (
        if exist "%%D\bin\java.exe" (
            set JAVA_CMD="%%D\bin\java.exe"
            goto :CHECK_BUILD
        )
    )
)

echo ERROR: Java Runtime (java.exe) not found!
echo Please install JDK or JRE 17+ or set JAVA_HOME.
pause
exit /b 1

:CHECK_BUILD
if not exist "bin\com\student\Main.class" (
    echo Binaries not found. Running build first...
    call build.bat
    if %ERRORLEVEL% neq 0 (
        echo Compilation failed. Cannot start application.
        pause
        exit /b 1
    )
)

echo Running Application with %JAVA_CMD%...
echo Default Credentials: admin / admin123
echo Database: student_management.db (SQLite)
echo.

%JAVA_CMD% --enable-native-access=ALL-UNNAMED -cp "bin;lib/*" com.student.Main

if %ERRORLEVEL% neq 0 (
    echo Application exited with code %ERRORLEVEL%
    pause
)
