# Middleman: Man-In-The-Middle Logger
### For debugging purposes only. Use responsibly.

Middleman is a simple man-in-the-middle (MITM) logger—a lightweight server for capturing, logging, and forwarding HTTP(S) requests. It is particularly useful for debugging IoT devices and integrating them with home automation systems.

## Why Use Middleman?
Middleman is designed for debugging. For example, it can be used to:
- Intercept and analyze communication between desktop applications and IoT devices or backend services.
- Automate home network devices by understanding their communication protocols.

This makes Middleman ideal for developers and enthusiasts working on home automation.

---

## Build Middleman
### Requirements
- **JDK 21+**

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/isaiah-v/middleman.git
   ```
2. Navigate to the project directory:
   ```bash
   cd middleman
   ```
3. Build the project:
   ```bash
   .\gradlew build
   ```
4. Navigate to the build directory:
   ```bash
   cd build\bin
   ```
5. Set the `PATH` environment variable:
    - **Note**: Unless you set this in your system's environment variables, you will need to run this command every time you open a new terminal.
   ```bash
   set PATH=%cd%;%PATH%
   ```

---

## Run Middleman

### Start Middleman
Run the `start` command. This will:
1. Create a self-signed certificate and keystore for the provided hostnames.
2. Import the certificate into the system's trusted store.
3. Update the system's hosts file to redirect traffic (**if the `hostfile` flag is provided**; requires admin privileges).
4. Start the server on port **443** (HTTPS).

#### Example:
```bash
middleman start --hostfile google.com www.google.com
```

### Test the Setup
Test by making a request to `https://google.com` using a browser or `curl`.

### Stop Middleman
Press `Ctrl+C` to stop the server.

### Clean Up
Run the `clean` command. This will:
1. Remove the certificate from the system's trusted store.
2. Remove entries added to the hosts file (**if the `hostfile` flag is provided**; requires admin privileges).
3. Delete the keystore.

#### Example:
```bash
middleman clean --hostfile
```

---

## Notes
1. Middleman is currently **only tested on Windows**. The server itself should work on other platforms, but processes like updating the hosts file or adding certificates to the system's trusted store are specific to Windows.
2. Updates to Middleman will likely be infrequent unless there is expressed interest.
3. There are no plans for an official release.

---

**Use this tool responsibly. It is intended solely for debugging and local network testing.**

