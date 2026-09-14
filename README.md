# Appointment Booking System

COE692 course project recovered from the original NetBeans/Linux VM. The system separates appointment search, booking, and confirmation into Java web applications, with a servlet/JSP frontend, MySQL databases, Docker images, and a Kubernetes deployment.

**Status: recovered course/demo source.** Search was exercised locally using the original Docker images during recovery. Booking-status search worked after correcting the booking servlet configuration. All four services compiled and packaged successfully in [GitHub Actions](https://github.com/Andrew-Badie/appointment-booking-system/actions/runs/34873318370) using Java 11 and Maven 3.9.16. The complete booking-to-confirmation flow has not yet been verified. This is not production-ready authentication or deployment.

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

After inserting a booking, the booking service publishes a `BOOK:<code>:<username>:<date>` message on `book_appointment_channel`. The confirmation service subscribes to stored events and copies booking information into its database. The source includes this messaging implementation; local recovery has not yet validated it end to end.

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
- The credential check calls `.equals(...)` on request inputs, so missing parameters can cause a `NullPointerException`. Use null-safe comparisons or validate inputs first. The frontend login controller still needs an explicit failed-login response; it currently does not forward or display an error when validation returns `false`. Some HTML forms use different field names; align those with the controller's `username` and `password` parameters.
- The login change has been reviewed in source only. Rebuild and redeploy the frontend, then verify valid, invalid, empty, and missing credentials before claiming the deployed login flow works.
- Database code and Dockerfiles retain the original `root` / `student` classroom credentials. Use only disposable local demo databases; externalize credentials before broader deployment.
- The JWT key is generated at runtime, tokens are short-lived, and token values are logged. Remove token logging and review session handling before real use.
- SQL is assembled using string concatenation in several places; use parameterized queries and explicit error handling.
- The booking seed schema defines `userid` as an integer, while Java booking code supplies a username string. Reconcile that contract before claiming successful booking.
- Confirmation endpoints contain hardcoded/demo values and need end-to-end validation with KubeMQ.
- The frontend's booking-status HTTP call can propagate backend failures as HTTP 500.
- Runtime dependency upgrades and automated end-to-end checks remain follow-up work.

## Recovery provenance

Imported from Andrew Badie's original VM export on 2026-09-14. The source, SQL, Dockerfiles, Kubernetes YAML, and shared NetBeans configuration are retained. The working booking `web.xml` correction to `ryerson.ca.endpoint.ApplicationConfig` was already included in the export.

The migration added this README and ignore rules. Andrew subsequently replaced the unconditional authentication result with a single demo-account credential check; this README reflects that source change. The Maven build has since been verified in GitHub Actions; the updated deployed login flow has not yet been verified. Compiled artifacts, personal cloud configuration, and the Windows shortcut are excluded. Editing GitHub files alone does not update existing containers; rebuild and redeploy the affected application.
