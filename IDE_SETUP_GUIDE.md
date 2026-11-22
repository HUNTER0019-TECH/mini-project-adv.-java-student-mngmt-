# 💻 Complete IDE Setup & Execution Guide
### Student Management System (Advanced Java)

This guide provides clear, step-by-step instructions to open, configure, and execute the **Student Management System** in:
1. **IntelliJ IDEA**
2. **Eclipse IDE**
3. **Apache NetBeans**
4. **Command Prompt / Terminal**

---

## 📂 Project Location
The complete project folder is located at:
```text
C:\Users\black\.gemini\antigravity\scratch\student-management-system
```

---

## 1. 🚀 Running in IntelliJ IDEA (Recommended)

### Step 1: Open the Project
1. Launch **IntelliJ IDEA**.
2. On the Welcome screen, click **Open** (or go to `File` → `Open...`).
3. Navigate to and select the directory:
   `C:\Users\black\.gemini\antigravity\scratch\student-management-system`
4. Click **OK**. Choose **Trust Project** if prompted.

### Step 2: Configure Project SDK
1. Open `File` → `Project Structure...` (or press `Ctrl + Alt + Shift + S`).
2. Under **Project Settings** → **Project**, set **SDK** to **Java 17** (or 21/25).
3. If no SDK is selected, click **Add SDK** → **Download JDK** (or point to an installed JDK).

### Step 3: Add Library JARs (If Not Using Maven)
IntelliJ will automatically recognize the `pom.xml` file. If you prefer to run it as a standard Java project:
1. In `Project Structure...`, navigate to **Libraries**.
2. Click the **+** (Add) icon → **Java**.
3. Select the `lib` folder inside the project (containing `sqlite-jdbc-3.45.2.0.jar`, `slf4j-api-2.0.12.jar`, `slf4j-simple-2.0.12.jar`).
4. Click **Apply** and **OK**.

### Step 4: Run the Application
1. In the Project tool window, expand:
   `src` → `main` → `java` → `com` → `student` → `Main.java`
2. Right-click on `Main.java` and select **Run 'Main.main()'** (or press `Shift + F10`).
3. The Login Screen will appear! Use:
   - **Username:** `admin`
   - **Password:** `admin123`

---

## 2. 🚀 Running in Eclipse IDE

### Step 1: Import the Project
1. Open **Eclipse IDE for Java Developers**.
2. Go to `File` → `Import...`.
3. Select **Maven** → **Existing Maven Projects**, and click **Next**.
   *(Alternative: Select **General** → **Existing Projects into Workspace**).*
4. Under **Root Directory**, click **Browse...** and select:
   `C:\Users\black\.gemini\antigravity\scratch\student-management-system`
5. Click **Finish**. Eclipse will build the project workspace.

### Step 2: Configure Java Build Path & Libraries
If imported as a standard Java project:
1. Right-click on the imported project in Package Explorer → **Properties**.
2. Select **Java Build Path** from the left panel.
3. Switch to the **Libraries** tab.
4. Click **Add External JARs...**.
5. Navigate to the `lib` folder inside the project and select all `.jar` files.
6. Click **Apply and Close**.

### Step 3: Execute `Main.java`
1. In the Package Explorer, locate:
   `src/main/java` → `com.student` → `Main.java`
2. Right-click on `Main.java` → **Run As** → **Java Application**.

---

## 3. 🚀 Running in Apache NetBeans

### Step 1: Open the Project
1. Launch **Apache NetBeans**.
2. Click `File` → `Open Project...` (or press `Ctrl + Shift + O`).
3. NetBeans will detect the project with a Maven or Java icon. Select:
   `student-management-system`
4. Click **Open Project**.

### Step 2: Verify Dependencies
1. Expand the project tree in NetBeans.
2. If dependencies need resolving, right-click the project → **Build with Dependencies**.
3. (For non-Maven): Right-click **Libraries** node → **Add JAR/Folder...** → select all JAR files from the `lib` folder.

### Step 3: Set Main Class & Run
1. Right-click the project → **Properties**.
2. Select **Run** on the left menu.
3. Ensure **Main Class** is set to:
   `com.student.Main`
4. Click **OK**.
5. Press `F6` (or click the green **Run** arrow in the top toolbar).

---

## 4. 🚀 Running via Command Line / Terminal

### On Windows:
Double click `run.bat` in the project root folder.

Or run manually from PowerShell:
```powershell
cd C:\Users\black\.gemini\antigravity\scratch\student-management-system
java -cp "bin;lib/*" com.student.Main
```

### On macOS / Linux:
```bash
cd student-management-system
chmod +x run.sh
./run.sh
```

---

## 📦 How to Export a Standalone Runnable JAR

To distribute or present this application as a standalone `.jar` file that runs on any machine with double-click:

### Using Maven:
Open command prompt inside the project folder and run:
```bash
mvn clean package
```
This produces a complete executable JAR inside the `target/` directory:
`target/student-management-system-1.0.0-jar-with-dependencies.jar`

You can run it on any computer using:
```bash
java -jar target/student-management-system-1.0.0-jar-with-dependencies.jar
```

### In Eclipse:
1. Right-click the project → **Export...** → **Java** → **Runnable JAR file**.
2. Choose Launch Configuration: `Main - student-management-system`.
3. Choose Export destination (e.g. `StudentManagementSystem.jar`).
4. Select `Package required libraries into generated JAR`.
5. Click **Finish**.

### In IntelliJ IDEA:
1. `File` → `Project Structure...` → `Artifacts` → **+** → **JAR** → **From modules with dependencies...**.
2. Select `Main.java` as the Main Class.
3. Click `Build` menu → **Build Artifacts...** → **Build**.
