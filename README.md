# Appointment Booking System

COE692 course project recovered from the original NetBeans/Linux VM. The system separates appointment search, booking, and confirmation into Java web applications, with a servlet/JSP frontend, MySQL databases, Docker images, and a Kubernetes deployment.

**Status: recovered course/demo source.** Search was exercised locally using the original Docker images during recovery. Booking-status search worked after correcting the booking servlet configuration. All four services compiled and packaged successfully in [GitHub Actions](https://github.com/Andrew-Badie/appointment-booking-system/actions/runs/34873318370) using Java 11 and Maven 3.9.16. The local booking-to-confirmation path is now verified by the [Compose integration run](https://github.com/Andrew-Badie/appointment-booking-system/actions/runs/34874717740), including login, search, booking, messaging, duplicate rejection and database restart persistence. This is not production-ready authentication or deployment.

## Run the application

See [the local setup guide](docs/LOCAL-RUN.md) for Windows/Docker Desktop and Linux steps. From the repository root with Docker running:

```bash
docker compose up --build --detach --wait --wait-timeout 240
```

Open [http://localhost:8080/FrontEnd/](http://localhost:8080/FrontEnd/). This builds from source and starts the applications, databases, and KubeMQ Community. The new **Local stack integration** workflow exercises the running services; see its actual run result for verification.

## Repository contents

| Path | Purpose |
| --- | --- |
| `services/Frontend/` | Servlet/JSP frontend, search orchestration, and JWT demo |
| `services/SearchAppointments/` | Appointment search REST API |
| `services/BookAppointment/` | Booking status, booking writes, and KubeMQ publisher |
| `services/ConfirmAppointment/` | Confirmation REST API and KubeMQ subscriber |
| `deployment/docker/` | Seven original Dockerfiles and three database seed scripts |
| `deployment/kubernetes/lab5deployment.yaml` | Original deployments, services, and volume claims |

Each application has its own Maven POM and inherits shared Java 11 build settings from the root Maven parent/aggregator. Folder names and capitalization are retained from the VM.

## Architecture

The frontend calls the search API. For logged-in searches it also calls the booking API to determine whether each result is already booked. Search, booking, and confirmation have separate databases: `LBS`, `book_LBS`, and `confirm_LBS`.

After inserting a booking, the booking service publishes a `BOOK:<code>:<username>:<date>` message on `book_appointment_channel`. The confirmation service subscribes to stored events and copies booking information into its database. The Compose integration check validates a booking event reaching the confirmation database. Broker outage recovery and transactional delivery remain unverified.

Historical runtime: Tomcat 8.5 with JDK 11, MySQL 8.0.32, and Google Kubernetes Engine. The old Dockerfiles use `tomcat:8.5-jdk11-openjdk` and `mysql:8.0.32`. These are preserved historical dependencies, not recommendations for a new public deployment.

## Build and deployment notes

NetBeans and the original VM are not required to compile this project. Use JDK 11 and Maven 3.9.x. From the repository root:

```bash
mvn --version
mvn --batch-mode --no-transfer-progress clean verify
```

The root POM builds all four services. It pins the compiler and WAR plugins, targets Java 11 using `maven.compiler.release`, and removes the obsolete endorsed-directory setup. Individual builds still work, for example `mvn -f services/Frontend/pom.xml clean verify`. The existing Java EE/Jersey application dependencies are retained; this is not a migration to Jakarta EE.

### Build from a browser

Open this repository's **Actions** tab and select **Build Java services**. Pushes to main, build branches, and pull requests trigger the workflow. Once the workflow is on the default branch, you can also select **Run workflow**. Open the run to inspect compilation results. Successful runs provide an **appointment-war-files** artifact containing all four WARs.

The workflow checks compilation and WAR packaging. It does not start Tomcat, MySQL, or KubeMQ, and does not validate login, JSP rendering, or the booking-to-confirmation flow. The recovered project does not yet contain automated application tests. Consult the actual Actions run before describing a revision as build-verified.

After successful builds, the original application Dockerfiles expect these WAR files in the Docker build context:

| Maven output | Dockerfile | Deployed context |
| --- | --- | --- |
| `services/Frontend/target/FrontEnd-1.0-SNAPSHOT.war` | `Dockerfile-frontendservice` | `/FrontEnd/` |
| `services/SearchAppointments/target/SearchAppointments-1.0-SNAPSHOT.war` | `Dockerfile-searchservice` | `/SearchAppointments/` |
| `services/BookAppointment/target/BookAppointment-1.0-SNAPSHOT.war` | `Dockerfile-bookservice` | `/BookAppointment/` |
| `services/ConfirmAppointment/target/ConfirmAppointment-1.0-SNAPSHOT.war` | `Dockerfile-confirmservice` | `/ConfirmAppointment/` |

Copy newly built WARs into `deployment/docker/` before using those Dockerfiles with that directory as context. WARs and `target/` are ignored and are not included in this source import. The MySQL connector is declared through Maven rather than a local `systemPath` JAR.

For local Docker networking, use the following container names and environment values. The frontend is the only component that needs a published browser port, such as `127.0.0.1:8080:8080`.

| Container | Environment |
| --- | --- |
| `frontend` | `searchService=searchappointment:8080`, `bookService=bookappointment:8080` |
| `searchappointment` | `DB_URL=searchdb:3306` |
| `bookappointment` | `DB_URL=bookdb:3306`, `kubeMQAddress=<reachable-broker>:50000` |
| `confirmappointment` | `DB_URL=confirmdb:3306`, `kubeMQAddress=<reachable-broker>:50000` |

Attach the containers to the same user-defined Docker network. Mount each database's own persistent volume at `/var/lib/mysql`. Wait for each database's final server to report ready on port 3306 before starting its client application. The seed search data includes the service **Psychology**.

The original Kubernetes YAML uses service port 80 for the application services, translated to container port 8080. It references historical Docker Hub image tags and a hardcoded KubeMQ IP. It does not deploy a KubeMQ broker. Its database volume mounts use `/data/...`, which must be reconciled with MySQL's actual data directory before relying on persistence. Do not treat this file as a ready-to-apply deployment.

## Known limitations and next edits

- **Login now checks a single hardcoded demo account.** `services/Frontend/src/main/java/ryerson/ca/business/Business.java` accepts the case-sensitive username `AndrewBadie` and password `1234`. For non-null inputs, other combinations return `false`; the previous unconditional `true` has been removed. This is a classroom demo check, not a user-account system or production authentication.
- Demo credential comparisons are null-safe. Login forms send the expected fields, invalid login returns HTTP 401 with an error page, and the session cookie is HttpOnly. The demo session lasts 30 minutes; restarting the frontend invalidates its runtime signing key.
- Database code and Dockerfiles retain the original `root` / `student` classroom credentials. Use only disposable local demo databases; externalize credentials before broader deployment.
- The JWT key is generated at runtime. Token values are no longer printed by the JWT helper; session and authorization design still need review before real use.
- SQL is assembled using string concatenation in several places; use parameterized queries and explicit error handling.
- New booking database volumes use a string `userid` and a unique appointment code. Existing databases require migration; changing seed SQL does not alter initialized volumes.
- Confirmation endpoints contain hardcoded/demo values and need end-to-end validation with KubeMQ.
- The frontend's booking-status HTTP call can propagate backend failures as HTTP 500.
- Runtime dependency upgrades and broader failure-path/restart tests remain follow-up work; the local happy-path integration check is included.

## Recovery provenance

Imported from Andrew Badie's original VM export on 2026-09-14. The source, SQL, Dockerfiles, Kubernetes YAML, and shared NetBeans configuration are retained. The working booking `web.xml` correction to `ryerson.ca.endpoint.ApplicationConfig` was already included in the export.

The migration added this README and ignore rules. Andrew subsequently replaced the unconditional authentication result with a single demo-account credential check; this README reflects that source change. The Maven build has since been verified in GitHub Actions; login has also been exercised in the local Compose integration workflow. Compiled artifacts, personal cloud configuration, and the Windows shortcut are excluded. Editing GitHub files alone does not update existing containers; rebuild and redeploy the affected application.

Subsequent recovery work adds the source-built Compose environment, integration checks, login/form corrections, frontend booking handler, and database insert/schema fixes. These are documented follow-up improvements to the recovered course implementation, not claims that the original submission contained this tooling.
