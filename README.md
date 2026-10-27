# Mambetali Aruzhan Student API

**Student:** Mambetali Aruzhan Imanaliyevna  
**University:** International Information Technology University (IITU)  
**Subject:** DevOps Midterm  
**Stack:** Java 17, Spring Boot 3.5, Gradle, Spring Data JPA, PostgreSQL, Docker Compose, Jenkins

Project focus: Clear setup instructions and reproducible evidence for an individual Spring Boot CRUD API.

## Architecture

- `app`: Spring Boot REST API on port 8080.
- `db`: PostgreSQL 16 on internal port 5432, reachable from the app by the Compose hostname `db`.
- `postgres_data`: Docker named volume for persistent PostgreSQL records.
- `student_network`: isolated Compose bridge network.
- Jenkins: checkout → build → tests → image build → image verification.

## Student API

| Method | Endpoint | Expected response |
|---|---|---|
| POST | `/api/students` | `201 Created` and student JSON |
| GET | `/api/students` | `200 OK` and array of students |
| GET | `/api/students/{id}` | `200 OK`, or `404 Not Found` |
| PUT | `/api/students/{id}` | `200 OK`, or `404 Not Found` |
| DELETE | `/api/students/{id}` | `204 No Content`, or `404 Not Found` |
| GET | `/actuator/health` | `200 OK` with status `UP` |

Sample record:

```json
{
  "fullName": "Mambetali Aruzhan Imanaliyevna",
  "email": "aruzhan@example.com",
  "program": "Software Engineering",
  "year": 4
}
```

The personalized CRUD feature normalizes student email addresses to lowercase
and removes surrounding spaces during both POST and PUT operations. An
integration test checks uppercase input and the returned normalized value.

## Part 1 — Linux administration

Run these commands in the project root on **Ubuntu/Linux or WSL**:

```bash
pwd
find . -maxdepth 3 -print | sort
chmod 744 scripts/Mambetali_Aruzhan_system_report.sh
ls -l scripts/Mambetali_Aruzhan_system_report.sh
bash scripts/Mambetali_Aruzhan_system_report.sh
tail -n 5 logs/Mambetali_Aruzhan_system_report.log
grep -n 'Mambetali' logs/Mambetali_Aruzhan_system_report.log > logs/Mambetali_Aruzhan_log_analysis.txt
cat logs/Mambetali_Aruzhan_log_analysis.txt
cp README.md backups/README_Mambetali_Aruzhan_backup.md
diff README.md backups/README_Mambetali_Aruzhan_backup.md
ps aux | head -n 12
ps aux | grep '[j]ava' || true
```

`chmod 744`: owner may **read/write/execute** (7); group and others may **read only** (4, 4). `>` replaces a file; `>>` appends to an existing file. Use `bash scripts/Mambetali_Aruzhan_log_analysis.sh` to print and save the surname matches again.

## Part 2 — Git and GitHub

The project is designed for the repository `mambetali-aruzhan-student-api`, branches `feature/mambetali-aruzhan-crud` and `feature/mambetali-aruzhan-documentation`, and tag `v1.0-mambetali-aruzhan`.

Check local history:

```bash
git status
git log --all --graph --oneline --decorate
git branch -a
git tag -n
git remote -v
```

To publish, create a **new empty** GitHub repository named `mambetali-aruzhan-student-api` under your own account (do not initialize it with a README). Then run:

```bash
git remote add origin https://github.com/AruzhanMambetali/mambetali-aruzhan-student-api.git
git push -u origin main
git push origin feature/mambetali-aruzhan-crud feature/mambetali-aruzhan-documentation
git push origin v1.0-mambetali-aruzhan
```

**Never commit** `.env`, real passwords or credentials. The tracked `.env.example` contains an obvious sample, not a usable secret. The `.gitignore` excludes local `.env` and Gradle build output.

## Part 3 — Docker and PostgreSQL

Requirements: Docker Engine/Desktop with Docker Compose, and a port 8080 that is free. On Windows use Docker Desktop with Linux containers and run commands in WSL or Ubuntu.

```bash
cp .env.example .env
# Edit .env and set a strong local POSTGRES_PASSWORD.
docker compose config
docker compose up -d --build
docker compose ps
docker images mambetali-aruzhan-student-api
docker image inspect mambetali-aruzhan-student-api:v1.0-mambetali-aruzhan
docker compose logs --tail 50 app
docker compose exec app java -version
docker compose stop app
docker compose start app
curl -sS http://localhost:8080/actuator/health
```

Use **`bash scripts/api_demo.sh`** to exercise POST, GET list, GET by ID, PUT, DELETE and a missing-ID response in sequence. Use **`bash scripts/persistence_demo.sh`** to insert a student and confirm it survives `docker compose down` followed by `docker compose up -d`. PostgreSQL stores its records in the `postgres_data` named volume, and `down` without `-v` preserves that volume.

To demonstrate a rebuild after a version change: update `version = '1.0.1'` in `build.gradle`, run `docker compose build --no-cache app`, then show `docker image inspect` and `docker compose up -d app`. Note that the image *tag* stays as specified unless you intentionally update the tag as well. Restore `version = '1.0.0'` afterward if you need the same source revision as the tagged release.

Important: do **not** run `docker compose down -v` when demonstrating persistence because that would delete the named volume.

## Part 4 — Jenkins CI/CD

1. Install/configure Jenkins on your **authorized** machine. Make sure a build agent has JDK 17 or newer, Git, a Docker CLI/daemon and permission to build images. Add the agent label `docker`.
2. Create a Pipeline job named `Mambetali_Aruzhan_Student_API`.
3. Set **Pipeline → Definition: Pipeline script from SCM**; **SCM: Git**; repository URL: your published repository; branch specifier: `*/main`; Script Path: `Jenkinsfile`.
4. For a private repository, select an appropriate GitHub credential stored in **Jenkins Credentials**. Do not paste a token into the Jenkinsfile.
5. Run **Build Now**. Inspect **Console Output**, test reports and the five stages: `Checkout`, `Build`, `Test`, `Docker Build`, `Verify`.
6. For a **controlled failed build**, create your own branch from main, e.g. `feature/mambetali-aruzhan-ci-failure`. On this branch change one assertion in `StudentApiIntegrationTest` to expect a wrong HTTP status (`status().isOk()` instead of `status().isNotFound()` in `missingStudentReturns404`); commit and run Jenkins against that branch. Capture the failed Test stage. Restore the assertion in a new commit and rebuild. Do not merge the intentionally broken commit into main.
7. The Jenkinsfile uses `pollSCM('H/5 * * * *')`. Once published and successfully built, push another commit to the monitored branch and wait for polling to discover it; show the automatic run in Jenkins Build History.

The pipeline does **not** deploy. Jenkins will stop if Gradle or tests fail; Docker image creation only happens after both succeed.

## Build without Docker

Install JDK 17 or newer and use the included Gradle Wrapper:

```bash
java -version
./gradlew --version
./gradlew clean test bootJar
```

The tests run against an in-memory H2 database in PostgreSQL compatibility mode, so they do not require a running PostgreSQL service. Runtime containers use PostgreSQL.

## Evidence checklist

- Linux: directory structure; `ls -l` showing `-rwxr--r--`; full system report; last 5 log lines; surname search; matching backup content; process list.
- Git: `git log --graph --all`, two feature branches, visible conflict resolution commit, `git tag`, and remote URL plus GitHub page.
- Docker: `docker compose ps`, image ID/size, logs, `exec`, stop/start, successful REST responses, persistence before/after restart.
- Jenkins: job settings, five green stages, console output containing student name, failed test stage on isolated branch and subsequent successful run, automatic run after commit.

Only screenshots from *actual* runs can serve as execution evidence. The repository itself supplies code, tests, configs, scripts and documentation.
