#!/usr/bin/env bash
# Student Management System - Unix/Linux/macOS Runner

echo "========================================================"
echo " Starting Student Management System (Advanced Java)"
echo "========================================================"

mkdir -p bin

# Compile if classes do not exist
if [ ! -f "bin/com/student/Main.class" ]; then
    echo "Compiling Java source files..."
    find src/main/java -name "*.java" > sources.txt
    javac -encoding UTF-8 -cp "lib/*" -d bin @sources.txt
    rm sources.txt
fi

echo "Launching Application..."
echo "Default Credentials: admin / admin123"
echo "Database: student_management.db"
echo ""

java -cp "bin:lib/*" com.student.Main
