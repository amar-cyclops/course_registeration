# Course Registration System

A Java web app (Servlets + JSP + JSTL + Maven + H2) implementing the Course
Registration System SRS: student/admin authentication, admin course CRUD,
and student course registration with prerequisite and seat-availability
checks (user story US-0014).

## Tech Stack
- Java 17
- Maven
- Servlets 4.0 / JSP / JSTL (`javax.servlet` — targets Tomcat 8.5.x/9.x)
- H2 embedded database (file-based, auto-created on first run)
- Session-based authentication with Servlet Filters for access control

## How This Maps to the SRS

| SRS Requirement | Implementation |
|---|---|
| User registration (name, email, password, student ID) | `SignupServlet` + `signup.jsp` |
| Login by email/password | `LoginServlet` + `login.jsp` |
| Two user types (student/admin) | `users.role` column; `AdminFilter` restricts `/admin/*` |
| Admin CRUD on courses | `CourseServlet` + `admin/courses.jsp`, `admin/edit-course.jsp` |
| Course attributes (code, title, description, credits, instructor, availability) | `courses` table / `Course` model |
| Student views & registers for courses | `RegisterCourseServlet` + `student/available-courses.jsp` |
| Prerequisite + seat checks before registering | `RegisterCourseServlet.attemptRegistration()` |
| Profile management (name, email, password) | `ProfileServlet` + `profile.jsp` |

## US-0014 "Register for Course" — How It Works

1. Student clicks **Register for New Courses** → `GET /register-courses`
   lists only courses that are Active, have open seats, and the student
   isn't already registered for.
2. Student checks one or more courses and clicks **Submit** → `POST /register-courses`.
3. For each selected course, the server:
   - Checks prerequisites (student must have an existing `REGISTERED` row
     for every course code listed in that course's `prerequisites` field).
   - Atomically claims a seat (`UPDATE courses SET available_seats = available_seats - 1
     WHERE id = ? AND available_seats > 0`) — this prevents overbooking even
     under concurrent requests.
   - Inserts a `registrations` row on success.
4. A results table is shown listing each selected course as **Registered** or
   **Failed**, with a specific reason for any failure (missing prerequisite,
   no seats left) — covering the use case's extension flows 5a and 5b.
5. If no courses are available at all, an explanatory message is shown
   (extension 2a).

## Project Structure

```
course-registration/
├── pom.xml
└── src/main/
    ├── java/com/university/registration/
    │   ├── model/          # User, Course, Registration, RegistrationResult
    │   ├── dao/             # DBConnection (schema + seed data), UserDAO, CourseDAO, RegistrationDAO
    │   ├── filter/          # AuthFilter (login required), AdminFilter (/admin/* only)
    │   └── servlet/         # Login, Signup, Logout, Dashboard, Course (admin CRUD),
    │                        # RegisterCourse (US-0014), MyCourses, Profile
    └── webapp/
        ├── login.jsp, signup.jsp, dashboard.jsp, profile.jsp
        ├── admin/courses.jsp, admin/edit-course.jsp
        ├── student/available-courses.jsp, student/my-courses.jsp
        ├── css/style.css
        └── WEB-INF/web.xml, navbar.jsp
```

## Database Schema (auto-created in H2 on first run)

- **users**: id, name, email (unique), password (SHA-256 hash), role
  (`STUDENT`/`ADMIN`), student_id (unique, null for admins)
- **courses**: id, course_code (unique), title, description, credit_hours,
  instructor, total_seats, available_seats, prerequisites (comma-separated
  course codes), status (`Active`/`Inactive`)
- **registrations**: id, student_user_id (FK), course_id (FK), status
  (`REGISTERED`/`FAILED`), reason, registered_at

A seeded admin account and five sample courses (including one with a
prerequisite and one with zero seats, to exercise the failure paths) are
created automatically the first time the app runs.

**Seeded admin login:** `admin@university.edu` / `admin123`

## How to Run

### Option A — no standalone Tomcat needed (recommended)
```
cd course-registration
mvn jetty:run
```
Then open `http://localhost:8080/course-registration/`. `Ctrl+C` stops it.

### Option B — deploy to a standalone Tomcat
```
mvn clean package
```
Copy `target/course-registration.war` into Tomcat's `webapps/` folder, start
Tomcat, then open `http://localhost:8080/course-registration/`.

## Suggested Test Flow

1. Log in as admin (`admin@university.edu` / `admin123`) → **Manage Courses**
   to see/add/edit courses.
2. Log out, click **Sign up**, create a student account.
3. Go to **Register for New Courses**, select a couple of courses (try
   `CS201`, which requires `CS101` as a prerequisite — it should fail unless
   you've registered for `CS101` first) and submit to see the confirmation
   report.
4. Check **My Courses** to see what's registered.
5. Try registering for `ENG105`, which is seeded with 0 available seats, to
   see the "no seats available" failure path.

## Ideas for Extending This Project

- Add a "drop course" action for students
- Add unit tests (JUnit + an in-memory H2 test DB) for the DAO layer
- Add an admin view of all student registrations
- Add pagination/search on the course list
- Enforce max credit-hour limits per registration
