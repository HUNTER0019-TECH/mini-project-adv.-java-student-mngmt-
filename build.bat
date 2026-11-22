@echo off
setlocal enabledelayedexpansion
title Building Student Management System

echo ========================================================
echo  Compiling Student Management System (Advanced Java)
echo ========================================================

:: Detect Java Compiler
set JAVAC_CMD=javac

where javac >nul 2>nul
if %ERRORLEVEL% equ 0 (
    goto :COMPILE
)

if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\javac.exe" (
        set JAVAC_CMD="%JAVA_HOME%\bin\javac.exe"
        goto :COMPILE
    )
)

if exist "C:\Program Files\Android\Android Studio\jbr\bin\javac.exe" (
    set JAVAC_CMD="C:\Program Files\Android\Android Studio\jbr\bin\javac.exe"
    goto :COMPILE
)

if exist "C:\Program Files\Java" (
    for /d %%D in ("C:\Program Files\Java\*") do (
        if exist "%%D\bin\javac.exe" (
            set JAVAC_CMD="%%D\bin\javac.exe"
            goto :COMPILE
        )
    )
)

echo ERROR: javac compiler not found in PATH or standard directories.
echo Please install JDK 17+ or set JAVA_HOME.
pause
exit /b 1

:COMPILE
echo Using compiler: %JAVAC_CMD%
if not exist "bin" mkdir "bin"

echo Gathering source files...
dir /s /b src\main\java\*.java > sources.txt

echo Compiling Java sources...
%JAVAC_CMD% -encoding UTF-8 -cp "lib/*" -d bin @sources.txt
set BUILD_STATUS=%ERRORLEVEL%
del sources.txt

if %BUILD_STATUS% equ 0 (
    echo.
    echo ========================================================
    echo  BUILD SUCCESSFUL! Class files generated in 'bin' directory.
    echo ========================================================
) else (
    echo.
    echo ========================================================
    echo  BUILD FAILED! Please review errors above.
    echo ========================================================
)

pause
exit /b %BUILD_STATUS%
