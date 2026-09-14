# Run in your browser with GitHub Codespaces

Docker Desktop, Windows WSL and the old VirtualBox VM are not required. GitHub hosts the development environment; the repository's dev container supplies its own Docker engine and Compose.

## Create and start

1. Open [this repository in Codespaces](https://codespaces.new/Andrew-Badie/appointment-booking-system). Select the main branch and create a codespace. The configuration requests at least 2 CPUs, 8 GB RAM and 32 GB storage.
2. Wait for the browser editor and development container setup to finish. If you already created a codespace before this configuration existed, open the command palette and run **Codespaces: Rebuild Container**.
3. In the editor, choose **Terminal → New Terminal**. From the repository root run:

   ```bash
   bash scripts/start-codespace.sh
   ```

   The first build downloads dependencies and may take several minutes. The script waits for Docker, builds all four Java applications, and starts the applications, three databases and KubeMQ.
4. When it says **App ready**, open the printed **Browser URL**. Alternatively, select **Ports**, open port **8080** in the browser, and append **/FrontEnd/** to its address. A Tomcat 404 at the bare root is expected; use the complete path.
5. Keep port visibility **Private**, and stay signed into the same GitHub account. The backend uses HTTP internally; GitHub supplies the external HTTPS address. [GitHub port-forwarding documentation](https://docs.github.com/en/codespaces/developing-in-a-codespace/forwarding-ports-in-your-codespace).

Demo login: **AndrewBadie / 1234** (case-sensitive). Search for **Psychology**, then book an available result. A booked result remains booked in the database; this is expected.

## Stop and resume

When finished, go to [your codespaces](https://github.com/codespaces), open the codespace's **…** menu and select **Stop codespace**. Closing a browser tab does not immediately stop it. Reopen the codespace and run the same start command to resume the application.

Docker uses named volumes for the demo databases. Stopping containers preserves those volumes. Deleting the codespace discards its local data; commit any source edits you want to keep first.

Codespaces uses your GitHub account's compute and storage allowance. Stopped codespaces still use storage. Check your remaining allowance and budget before creating one; usage beyond the included allowance can be billed if enabled. [GitHub billing documentation](https://docs.github.com/en/billing/concepts/product-billing/github-codespaces).

## If startup fails

Run:

```bash
docker compose ps
docker compose logs --tail 80
df -h
free -h
```

If the health-check wait times out, inspect the logs before changing anything; rerunning the start command is safe and does not reset the databases. Do not run `docker compose down --volumes` unless you intend to erase the demo data.

This environment reuses the Compose stack already covered by the Local stack integration workflow. The Codespaces-specific container provisioning and GitHub's authenticated port forwarding must also be checked in a real codespace; a passing ordinary Compose workflow alone does not prove those work.
