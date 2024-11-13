# Java Multithreaded Network Scanner

A robust and efficient multithreaded network scanner built in Java. This tool scans a specified network range to identify active IP addresses and open ports, providing valuable insights into your network's security and availability.

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Code Overview](#code-overview)
  - [IP.java](#ipjava)
  - [MAC.java](#macjava)
  - [Primary.java (Main)](#primaryjava-main)
  - [Utilities.java](#utilitiesjava)
- [Contributing](#contributing)
- [License](#license)

## Features

- **IP Address Validation:** Ensures that only correctly formatted IP addresses are processed.
- **MAC Address Retrieval:** Fetches MAC addresses associated with active IPs.
- **Port Scanning:** Scans a range of ports on active IPs to identify open ports.
- **Multithreading:** Utilizes Java's `ExecutorService` for efficient parallel processing, significantly reducing scan times.
- **User-Friendly Interface:** Interactive prompts guide users through selecting network interfaces, setting network masks, and choosing port ranges.
- **Comprehensive Output:** Displays reachable IPs along with their MAC addresses and lists of open ports.

## Requirements

- **Java Development Kit (JDK) 8 or higher**
- **Operating System:** Compatible with Windows, macOS, and Linux (commands like `ping` and `arp` are used; ensure they are available in your environment)

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/BetV3/Network_Scanner.git
   ```

2. **Navigate to the Project Directory:**

   ```bash
   cd Network_Scanner
   ```

3. **Compile the Java Files:**

   Ensure you have JDK installed and `javac` is available in your PATH.

   ```bash
   javac network/*.java
   ```

## Usage

1. **Run the Scanner:**

   ```bash
   java network.Primary
   ```

2. **Follow the Prompts:**

   - **Select Network Interface:** The scanner will list available network interfaces with their IP addresses. Choose the correct one by entering `y` for the desired interface.
   - **Enter Network Mask:** Specify the network mask (e.g., `24` for a `/24` subnet). Press Enter to use the default mask of `24`.
   - **Port Scanning Prompt:** After identifying active IPs, choose whether to perform a port scan by entering `y` or `n`.
   - **Specify Port Range:** If opting for port scanning, define the start and end ports (default range is `1-1024`).

3. **View Results:**

   The scanner will display reachable IP addresses with their corresponding MAC addresses and any open ports within the specified range. It will also display the total time taken for the scan.

## Code Overview

### IP.java

**Package:** `network`

The `IP` class represents an IP address and its associated MAC address. It includes methods for validating IP formats, retrieving and setting IP and MAC addresses, and converting IP addresses to byte arrays.

**Key Features:**

- **Constructors:** Initialize IP objects with default or specified IP and MAC addresses.
- **Validation:** Ensures IP addresses follow the standard `0.0.0.0` format.
- **Getters and Setters:** Access and modify IP and MAC addresses.
- **Utility Methods:** Convert IP addresses to byte arrays and provide string representations.

### MAC.java

**Package:** `network`

The `MAC` class handles MAC addresses associated with IPs. It validates MAC address formats and provides methods to retrieve and set MAC addresses.

**Key Features:**

- **Constructors:** Initialize MAC objects with default or specified MAC addresses.
- **Validation:** Ensures MAC addresses follow the standard `00:00:00:00:00:00` format.
- **Getters and Setters:** Access and modify MAC addresses.
- **Utility Methods:** Provide string representations of MAC addresses.

### Primary.java (Main)

**Package:** `network`

The `Primary` class contains the `main` method and orchestrates the scanning process. It handles user interactions, manages multithreading for IP and port scanning, and displays results.

**Key Features:**

- **User Interaction:** Prompts users to select network interfaces, set network masks, and choose port scanning options.
- **Multithreading:** Utilizes `ExecutorService` with a fixed thread pool to perform concurrent IP and port scans.
- **IP Scanning:** Identifies reachable IPs within the specified network range using the `ping` command.
- **MAC Retrieval:** Fetches MAC addresses for reachable IPs using the `arp` command.
- **Port Scanning:** Scans specified port ranges on active IPs to identify open ports.
- **Performance Tracking:** Measures and displays the total time taken for the scanning process.

### Utilities.java

**Package:** `network`

The `Utilities` class provides helper methods to support various functionalities of the scanner, including IP address retrieval, conversion between IP formats, and port checking.

**Key Features:**

- **getAddress():** Retrieves the user's IP address by listing available network interfaces and allowing user selection.
- **ipToLong(IP address):** Converts an `IP` object to its long representation for network calculations.
- **longToIp(long ip):** Converts a long value back to its standard IP address string format.
- **isPortOpen(String ip, int port):** Checks if a specific port on an IP address is open by attempting to establish a socket connection.

## Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the Repository**

2. **Create a Feature Branch:**

   ```bash
   git checkout -b feature/YourFeature
   ```

3. **Commit Your Changes:**

   ```bash
   git commit -m "Add Your Feature"
   ```

4. **Push to the Branch:**

   ```bash
   git push origin feature/YourFeature
   ```

5. **Open a Pull Request**

Please ensure your code adheres to the project's coding standards and includes appropriate comments and documentation.

## License

This project is licensed under the [MIT License](LICENSE).

---

*Disclaimer: Use this network scanner responsibly and ensure you have permission to scan the networks and devices you target. Unauthorized scanning may be illegal and unethical.*
