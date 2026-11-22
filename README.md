# Student Management System (SMS) - Advanced Java Mini Project

A modern, clean, and complete desktop application built using **Advanced Java**, **Java Swing**, and **SQLite (JDBC)** following the **MVC (Model-View-Controller)** and **DAO (Data Access Object)** design patterns. Designed specifically for BCA, MCA, and B.Tech computer science college mini-project submissions.

---

## 📌 Project Overview
- **Project Name:** Student Management System
- **Language:** Java (JDK 17 or higher recommended)
- **GUI Framework:** Java Swing & AWT (Custom modern dark navy & white theme)
- **Database:** SQLite 3 (Serverless, zero configuration, permanent local storage)
- **Database Connectivity:** JDBC with `PreparedStatement` (SQL injection proof)
- **IDE Compatibility:** IntelliJ IDEA, Eclipse, Apache NetBeans, VS Code
- **Target Audience:** College Mini Project / BCA / MCA / B.Tech / Diploma

---

## 🌟 Key Features

### 1. Modern & Responsive UI
- Professional **White and Dark Navy** theme (`#0F172A` / `#2563EB`) with subtle blue accents.
- Anti-aliased rounded panels and modern flat buttons with hover states.
- Clean JTable styling with custom headers, alternating row colors, and cell padding.
- Responsive dashboard layout with no unnecessary animations.

### 2. Secure Login Authentication
- Administrator login authentication against SQLite database.
- Toggleable **Show/Hide password** option.
- Validation for empty inputs with user-friendly alert banners.
- **Default Credentials:**
  - **Username:** `admin`
  - **Password:** `admin123`

### 3. Comprehensive Dashboard
- **Metric Stat Cards:** Live counts for Total Students, Total Courses, Academic Departments, and Database Engine status.
- **Quick Action Bar:** One-click shortcuts to register students, add courses, and refresh data.
- **Recent Registrations:** Real-time preview of the latest student enrollments.
- **Course Distribution Summary:** Instant visibility into enrollment numbers per course.

### 4. Student Management (Full CRUD)
- **Add Student:** Captures Student ID, Full Name, Email, Phone Number, Course (dynamic dropdown), Gender, Date of Birth (YYYY-MM-DD), and Residential Address.
- **Update Student:** Instant form population upon clicking any table row for seamless editing.
- **Delete Student:** Permanent record removal with a safety confirmation prompt.
- **Search System:** Real-time live filtering by Student ID, Full Name, or Course keyword.
- **Data Validation:**
  - Mandatory fields check
  - Regex email verification (`name@domain.com`)
  - 10-digit mobile number format
  - Past date validation for Date of Birth
  - Duplicate Student ID prevention

### 5. Academic Course Management
- Add new academic courses with Course Code (e.g. `BCA-101`), Course Name, Duration, and Department.
- Display all courses in an interactive table.
- Delete courses with confirmation.
- Changes in courses immediately synchronize with the student registration course dropdown!
- **Pre-seeded with 6 sample courses** (BCA, B.Tech CSE, MCA, BBA, B.Sc IT, MBA).

### 6. Zero-Setup SQLite Database
- Database file (`student_management.db`) is **automatically generated** on first launch.
- Database tables (`users`, `courses`, `students`) are automatically created.
- Pre-seeds sample data (admin user, 6 courses, 5 sample students) so the project is immediately ready for demonstration.
- Permanent persistence across restarts without any database server installation (no MySQL or XAMPP required).

---

## 📁 Project Directory Structure

```
student-management-system/
├── pom.xml                     # Maven build configuration
├── run.bat                     # 1-click Windows runner script
├── build.bat                   # 1-click Windows compile script
├── run.sh                      # 1-click Linux / macOS runner script
├── README.md                   # Quickstart and overview documentation
├── SYNOPSIS.md                 # Academic project synopsis
├── PROJECT_REPORT.md           # Full formal college project report
├── VIVA_QUESTIONS.md           # 20 high-yield viva questions with detailed answers
├── IDE_SETUP_GUIDE.md          # Setup guide for IntelliJ, Eclipse & NetBeans
├── student_management.db       # SQLite local database file (auto-generated)
├── lib/                        # Offline standalone JAR dependencies
│   ├── sqlite-jdbc-3.45.2.0.jar
│   ├── slf4j-api-2.0.12.jar
│   └── slf4j-simple-2.0.12.jar
└── src/
    └── main/
        └── java/
            └── com/
                └── student/
                    ├── Main.java               # Application entry point
                    ├── TestSystem.java         # Headless verification test suite
                    ├── database/
                    │   └── DatabaseConnection.java # SQLite JDBC setup & schema seeder
                    ├── models/
                    │   ├── Person.java         # Abstract base class (OOP inheritance)
                    │   ├── Student.java        # Student model extending Person
                    │   ├── Course.java         # Course model
                    │   └── User.java           # User credentials model
                    ├── dao/
                    │   ├── BaseDAO.java        # Generic DAO interface (<T>)
                    │   ├── UserDAO.java        # User authentication queries
                    │   ├── CourseDAO.java      # Course CRUD queries
                    │   └── StudentDAO.java     # Student CRUD & search queries
                    ├── ui/
                    │   ├── LoginFrame.java     # Admin sign-in screen
                    │   ├── DashboardFrame.java # Main application frame & sidebar
                    │   ├── OverviewPanel.java  # Dashboard cards & metrics panel
                    │   ├── StudentPanel.java   # Complete student CRUD panel
                    │   ├── CoursePanel.java    # Course management panel
                    │   ├── AboutPanel.java     # Architecture & system info panel
                    │   └── theme/
                    │       ├── UITheme.java           # Central colors, fonts & table stylers
                    │       ├── RoundedPanel.java      # Anti-aliased rounded container
                    │       ├── ModernButton.java      # Modern hover-animated button
                    │       ├── ModernTextField.java   # Input field with focus effect
                    │       └── ModernPasswordField.java # Password field with focus effect
                    └── utils/
                        └── ValidationUtils.java# Regex and date format validation rules
```

---

## 🚀 How to Run the Project

### Option A: Using 1-Click Batch File (Windows - Easiest)
Simply double click `run.bat` in the project root folder.
The script automatically detects your installed JDK, compiles any modified source files, and starts the application!

### Option B: Using Command Line (Terminal / PowerShell)

1. Open PowerShell or Command Prompt in the project folder:
   ```cmd
   cd C:\Users\black\.gemini\antigravity\scratch\student-management-system
   ```

2. Compile the Java files:
   ```cmd
   javac -encoding UTF-8 -cp "lib/*" -d bin src/main/java/com/student/*.java src/main/java/com/student/*/*.java src/main/java/com/student/*/*/*.java
   ```

3. Run the application:
   ```cmd
   java --enable-native-access=ALL-UNNAMED -cp "bin;lib/*" com.student.Main
   ```

### Option C: Using Maven
If Maven is installed on your machine:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.student.Main"
```

### Option D: In Your Favorite IDE (IntelliJ IDEA / Eclipse / NetBeans)
See [IDE_SETUP_GUIDE.md](IDE_SETUP_GUIDE.md) for step-by-step instructions with screenshots and project import steps.

---

## 🔑 Default Login Credentials
- **Username:** `admin`
- **Password:** `admin123`

---

## 🧪 Running Automated Headless Verification Tests
To verify all database connections, DAO queries, and CRUD functionality without opening the GUI, run:
```cmd
java -cp "bin;lib/*" com.student.TestSystem
```
This runs 9 automated unit tests verifying the database, auth, course DAO, student DAO, search, add, update, delete, and aggregation metrics.

---

## 📚 College Submission Documentation Included
1. **[SYNOPSIS.md](SYNOPSIS.md)**: Formal Project Synopsis ready for submission.
2. **[PROJECT_REPORT.md](PROJECT_REPORT.md)**: Full academic project report with UML diagrams, ER diagrams, data dictionaries, test cases, and conclusions.
3. **[VIVA_QUESTIONS.md](VIVA_QUESTIONS.md)**: Top 20 viva voce questions with detailed answers to help score top marks in practical exams.
4. **[IDE_SETUP_GUIDE.md](IDE_SETUP_GUIDE.md)**: Detailed instructions for IntelliJ IDEA, Eclipse, and NetBeans.
