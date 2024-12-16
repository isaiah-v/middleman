# Middleman: Man-In-The-Middle Logger
**For debugging purposes only. Use responsibly.**

Middleman is a simple man-in-the-middle (MITM) logger—a lightweight server for capturing, logging, and forwarding
HTTP(S) requests. It is particularly useful for debugging IoT devices and integrating them with home automation systems.

## Overview
Many IoT devices are controlled by desktop applications, but these apps often lack programmatic interfaces. Middleman
helps you intercept and understand the communication between such applications and devices, enabling custom control over
your devices.

**Disclaimer:** Ensure you have permission to analyze any network traffic or devices before using this tool.

## Setting Up Middleman

### System Requirements
 - Java 21+

### Prerequisites / Preparation

#### 1. Analyze Network Traffic
   Start by capturing network traffic between the desktop application and the IoT device using tools like *Wireshark* or
   *tcpdump*.

 - This reveals endpoints, communication protocols, and the type of data being exchanged.
 - In some cases, data may be encrypted, but DNS requests and server endpoints can still provide valuable insights.

#### 2. Redirect Traffic
   Identify the servers used by the application and redirect their traffic to the middleman server:

 - Use an internal DNS server or modify the `hosts` file on the machine running the desktop application.
 - For example, map `iot-server.example.com` to the IP of the middleman server.

#### 3. Handle HTTPS Encryption
   For HTTPS traffic, you’ll need a self-signed certificate:

 - Use the middleman-tool, keytool, or OpenSSL to generate a self-signed certificate.
 - Configure the middleman server to use this certificate for encrypted traffic.

#### 4. Add Trusted Certificates
   Most applications require trusted certificates to establish secure connections. To prevent connection errors, add the
   self-signed certificate to the system’s trusted certification authorities:

 - Use the middleman-tool or manually add the certificate to the system’s trusted store.

Note: This should work for most applications, but some include additional security measures (e.g., certificate pinning),
which may block this approach.

## Building Middleman
To build Middleman, you need to have Java 21+ installed on your system. Run the following command to build the project:

Build Middleman:
```shell
git clone https://github.com/isaiah-v/middleman.git
cd middleman
.\gradlew build
```

Run Middleman:
```shell
# Navigate to the build directory
cd build\bin

# Self-signed certificate for HTTPS traffic
middleman keystore sign google.com www.google.com

# Export the certificate
middleman keystore export

# Install the certificate in the system's trusted store
middleman ca install

# Start the server
middleman server start
```

Notes:
- Currently, Middleman is only tested on Windows. The server probably works on other platforms, but the scripts to
  generate certificates and configure the server are Windows-specific.
