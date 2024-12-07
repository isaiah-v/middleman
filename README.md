# middleman
Man-In-The-Middle Logger

This is a simple man-in-the-middle logger—a basic server that captures, logs, and forwards HTTPS requests. I wrote this to debug some IoT devices and integrate them better into my home automation system.

**For debugging only. Drink responsibly.**

## Basic Walkthrough
**Problem:** An IoT device on your Wi-Fi can be controlled using a desktop application. While the application fulfills your needs, it lacks functionality for programmatic control.

**Task:** Understand how the desktop application communicates with the IoT device and use that information to enable programmatic control.

### 01. Network Level Packet Analyzer
[Provide details on how the network-level packet analysis works, tools or libraries involved, and what users should look for during this step.]

### 02. DNS Setup
[Explain how to configure DNS for intercepting traffic, such as setting up local DNS servers or modifying /etc/hosts.]

### 03. Self-Signed Certificates
[Describe the process of generating self-signed certificates, such as using keytool or OpenSSL, and how to set them up for HTTPS interception.]

### 04. Local Trusted Certification Authority
[Guide users on how to set up a local trusted certificate authority (CA), explaining how to install and trust the CA certificate on their devices for interception without warnings.]
