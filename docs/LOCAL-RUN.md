# Run locally from source

This is an isolated classroom demo, not a public deployment. Compose builds the four WARs from source, starts Tomcat 9 on Java 11, three MySQL databases, and the open-source [KubeMQ Community broker](https://github.com/kubemq-io/kubemq-community). No Google Cloud project or NetBeans installation is required. The historical Kubernetes files remain a separate deployment example.

## Windows

1. Install [Docker Desktop for Windows](https://docs.docker.com/desktop/setup/install/windows-install/), using its WSL 2 backend. Complete any installation/restart prompts and start Docker Desktop. Use Linux containers.
2. Download this repository using **Code > Download ZIP** and extract it, or clone it using Git.
3. Open the extracted project folder in File Explorer. Type `powershell` in its address bar and press Enter. The folder should contain `compose.yml` and the root `pom.xml`.
4. Run:

```powershell
docker compose version
docker compose up --build --detach --wait --wait-timeout 240
```

5. Open [the frontend](http://localhost:8080/FrontEnd/). Search for **Psychology**. Demo login: **AndrewBadie** / **1234** (case-sensitive). After login, search again and select **Book**. An already-booked appointment displays **Booked**.

The first build downloads dependencies and images and can take several minutes. Allow roughly 6 GB RAM for the full Docker environment and several GB of free disk space. The original 2 GB RAM / nearly full 10 GB VM is not a suitable default for this full stack.

If port 8080 is already occupied, run `$env:APP_PORT="8081"` before the Compose command, then use `http://localhost:8081/FrontEnd/`. Alternatively copy `.env.example` to `.env` and set `APP_PORT` there.

## Linux or the existing VM

Use Docker Engine with the Compose v2 plugin. From a fresh clone/export of this revision, run the same `docker compose up` command. Add `sudo` if your account requires it. Avoid starting both the old manually created preview and this stack on port 8080. This setup creates its own volumes; it does not migrate old VM/cloud database contents.

## Stop, restart, and update

```bash
docker compose stop
docker compose start
docker compose up --build --detach --wait --wait-timeout 240
```

Use the last command after downloading/pulling source updates. `docker compose down` removes the containers and network but retains named database volumes. **Adding `--volumes` deletes this Compose project's stored data**, so use it only when intentionally resetting a disposable demo.

Seed scripts run only on first database initialization. An existing volume does not automatically receive schema changes. This setup fixes the booking username column and the case-sensitive confirmation table name for newly initialized databases; existing databases need a deliberate migration.

## Check behavior

The **Local stack integration** GitHub Actions workflow builds fresh images and exercises anonymous search, valid/invalid/missing login credentials, logged-in search, booking, duplicate rejection, delivery through KubeMQ to the confirmation database, and persistence after a database restart.

`python3 scripts/smoke.py` is intended for a **new disposable stack**, because it creates the seeded Psychology booking (code 1234). Do not run it against a demo with bookings you want to retain.

For errors:

```bash
docker compose ps --all
docker compose logs --tail 100 frontend searchappointment bookappointment confirmappointment kubemq
```

## Remaining limits

- Only the frontend is published, on the host loopback interface. Backends and databases use the internal Compose network.
- Demo credentials are public and fixed. Backend APIs trust the internal caller; this is not production authorization.
- The Community broker and MySQL version match the legacy application's recovery needs. The Community broker is pinned to its downloaded image digest. Maven and MySQL use version tags; the Tomcat 9 / Java 11 tag can receive updates.
- The database write and message publication are not one transaction. A broker outage can leave a booking without confirmation; an outbox/retry design remains future work.
- The confirmation database is updated asynchronously. The frontend displays booking status, not a separate confirmation screen.
- SQL/resource-handling improvements and broader failure/restart coverage remain work beyond this demo.

## Verified revision

[The integration run](https://github.com/Andrew-Badie/appointment-booking-system/actions/runs/34874717740) passed all listed checks on Ubuntu with fresh images and volumes. Windows instructions have not been executed on the user's laptop. This confirms the demo path, not production reliability or recovery from every service outage.
