
# Selenium Grid Setup with Firefox Node (Export/Import via Docker)

## Overview
This guide explains how to export your local Selenium Grid and Firefox Node Docker containers, transfer them to another machine, and run them using Docker Desktop.

You will have:

- Selenium Hub container
- Firefox Node container registered to the hub

> **Note:** You must have **Java installed** on the machine where you will run Selenium tests. Verify with:
```bash
java -version
```

---

## 1️⃣ Prerequisites

- Docker Desktop installed on both source and target machines.
- Java installed on the target machine for Selenium tests.
- Access to the terminal/command prompt.
- Maven 

---

## 2️⃣ Export Containers from Source Machine

### Step 1: List your running containers
```bash
docker ps
```
Identify your hub and node containers. For example:

- `selenium-hub`
- `firefox-node`

### Step 2: Export each container(Don't do this step)
Export the Hub container:
```bash
docker export selenium-hub -o selenium_hub.tar
```

Export the Firefox Node container:
```bash
docker export firefox-node -o firefox_node.tar
```

> You now have two `.tar` files: `selenium_hub.tar` and `firefox_node.tar`.

---

## 3️⃣ Transfer the Containers

Copy the `.tar` files to the target machine using your preferred method:

- USB drive
- Network share
- SCP/SFTP

---

## 4️⃣ Load the Containers on the Target Machine (Follow from here)

### Step 1: Load the Hub
```bash
docker import selenium_hub.tar selenium/hub:local
```

### Step 2: Load the Firefox Node
```bash
docker import firefox_node.tar selenium/node-firefox:local
```

### Step 3: Verify images
```bash
docker images
```
You should see:

```
REPOSITORY                TAG       IMAGE ID
selenium/hub              local     <ID>
selenium/node-firefox     local     <ID>
```

---

## 5️⃣ Start the Selenium Hub
```bash
docker run -d -p 4444:4444 --name selenium-hub selenium/hub:local
```

- Hub will be accessible at `http://localhost:4444`.

---

## 6️⃣ Start the Firefox Node
```bash
docker run -d --name firefox-node     --link selenium-hub:hub     -e SE_EVENT_BUS_HOST=selenium-hub     -e SE_EVENT_BUS_PUBLISH_PORT=4442     -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443     selenium/node-firefox:local
```

- Node will register automatically with the hub.

---

## 7️⃣ Verify Setup

Open a browser on the target machine and go to:

```
http://localhost:4444/ui
```

You should see the Firefox Node listed as ready.

---

