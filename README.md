<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>

<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <h2 align="center">Public Billposting Service</h2>
  <h4 align="center">University of L'Aquila &middot; Business Process Development</h4>

  <p align="center">
    An automated, end-to-end business process orchestration system for public billposting management.
    <br />
    Designed and developed as an academic project for the <strong>Business Process Development</strong> course at the <strong>University of L'Aquila (Univaq)</strong>.
    <br />
    <br />
    <a href="https://github.com/CarlinoCalogero/univaq-business-process-development"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="#about-the-project">About The Project</a>
    &middot;
    <a href="#project-architecture--subsystems">Subsystems</a>
    &middot;
    <a href="#getting-started">Getting Started</a>
    &middot;
    <a href="#contributing">Contributors</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#project-architecture--subsystems">Project Architecture &amp; Subsystems</a></li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation--running">Installation &amp; Running</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

> [!NOTE]
> **University Project Notice:**
> This repository contains the source code for an academic project developed for the **Business Process Development** course within the Master's Degree program in Computer Science at the **University of L'Aquila (Univaq)** by [Calogero Carlino](https://github.com/CarlinoCalogero).

The **Public Billposting Service** automates the end-to-end business process required when customers request billboard and poster reservations across multiple municipalities. The system handles customer verification, geographic zone querying, multi-criteria budget optimization, SOAP-based provider availability validation, and asynchronous order confirmation or cancellation with automated file generation.

### Key Capabilities
* **BPMN 2.0 Process Orchestration:** Fully automated workflow powered by Camunda BPM embedded within Spring Boot.
* **Parallel Service Execution:** Fetches user profiles and municipal zone tariffs concurrently via HTTP REST connectors.
* **Multi-Strategy Zone Allocation:** Algorithmic zone assignment supporting `GREEDY`, `CHEAPEST_FIRST`, `BEST_VALUE`, `EQUAL_BUDGET`, and `RANDOM` budget policies.
* **Heterogeneous Service Integration:** Interoperates seamlessly with external REST endpoints and SOAP web services formatted using FreeMarker XML envelopes.
* **Modern Cross-Platform Desktop UI:** Next.js 15 client packaged with Electron 36 providing an intuitive, multi-step booking wizard.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* [![Camunda][Camunda.com]][Camunda-url]
* [![Spring Boot][Spring.io]][Spring-url]
* [![Java][Java.com]][Java-url]
* [![Next.js][Next.js]][Next-url]
* [![React][React.js]][React-url]
* [![Electron][Electron.js]][Electron-url]
* [![TypeScript][TypeScript.org]][TypeScript-url]
* [![Apache Maven][Maven.apache.org]][Maven-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- PROJECT ARCHITECTURE & SUBSYSTEMS -->
## Project Architecture &amp; Subsystems

The repository is structured into two primary modules:

```text
univaq-business-process-development/
├── public_billposting_service/          # Backend BPMN engine & REST API (Spring Boot + Camunda)
│   ├── src/main/resources/processes/    # BPMN 2.0 executable diagram
│   ├── src/main/resources/templates/    # FreeMarker envelopes for SOAP communication
│   └── src/main/java/it/univaq/disim/   # Java Delegates, Controllers, and Strategy services
└── public_billposting_service_webapp/   # Frontend desktop & web application (Next.js + Electron)
    ├── main.ts                          # Electron main lifecycle process
    └── renderer/                        # Next.js 15 app router application
```

1. **[`public_billposting_service/`](file:///c:/Users/calog/Documents/univaq-business-process-development/public_billposting_service/README.md):**
   * Hosts the Camunda Platform 7 engine and Spring Boot REST controllers.
   * Coordinates the `PublicBillpostingService` process definition.
   * Evaluates zone choices based on user budget constraints and communicates with external posting services via SOAP connectors.
   * Generates formatted bill receipts into an `output/` directory upon order confirmation.

2. **[`public_billposting_service_webapp/`](file:///c:/Users/calog/Documents/univaq-business-process-development/public_billposting_service_webapp/README.md):**
   * Serves as the interactive user interface.
   * Allows clients to specify username, poster format, target cities with individualized spending caps, and allocation strategies.
   * Visualizes calculated quotes and zone selections, enabling the customer to confirm or cancel the transaction.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

Follow the instructions below to get a local copy of both the backend and frontend up and running.

### Prerequisites

* **Java Development Kit (JDK):** Version 8 or 17
  ```sh
  java -version
  ```
* **Apache Maven:** Version 3.8+
  ```sh
  mvn -version
  ```
* **Node.js:** Version 20+ and npm
  ```sh
  node -v
  npm -v
  ```

### Installation &amp; Running

1. **Clone the repository:**
   ```sh
   git clone https://github.com/CarlinoCalogero/univaq-business-process-development.git
   cd univaq-business-process-development
   ```

2. **Start the Backend Service:**
   ```sh
   cd public_billposting_service
   mvn clean spring-boot:run
   ```
   * The backend starts at `http://localhost:8080`.
   * Access the Camunda Cockpit / Tasklist at `http://localhost:8080/camunda` (Credentials: `demo` / `demo`).

3. **Start the Frontend Desktop / Web Application:**
   Open a new terminal window:
   ```sh
   cd public_billposting_service_webapp
   npm install
   cd renderer && npm install && cd ..
   ```
   * **Run as Electron desktop application:**
     ```sh
     npm run dev
     ```
   * **Run as web application in browser:**
     ```sh
     npm run dev:next
     ```
     Navigate to [http://localhost:3000](http://localhost:3000).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Usage

1. **Submit Poster Availability Request:**
   * Enter client username (e.g. registered user in the user service).
   * Specify the desired poster format and add one or more target cities with their respective maximum budget.
   * Pick an allocation strategy (`GREEDY`, `CHEAPEST_FIRST`, `BEST_VALUE`, `EQUAL_BUDGET`, or `RANDOM`).
   * Submit to initiate the BPMN process.

2. **Review Availability &amp; Quotation:**
   * The system aggregates available poster spots, computes optimal distribution per city, validates availability via SOAP, and renders the result on `/[request]`.
   * Inspect the selected zones, per-zone pricing, and total invoice estimation.

3. **Confirm or Cancel Order:**
   * **Confirm:** Correlates the message event to finalize the booking. The backend issues a SOAP confirmation, returns invoice credentials, and saves a summary receipt to `output/bill_<timestamp>.txt`.
   * **Cancel:** Aborts the reservation via SOAP cancellation and notifies the client.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

> [!IMPORTANT]
> **No External Contributions Accepted:**
> This project was created exclusively for academic evaluation as part of a university course at the **University of L'Aquila (Univaq)**. It is currently archived and maintained for educational and portfolio demonstration purposes only. As such, **pull requests, new feature additions, and external code contributions are not accepted**.
>
> We nonetheless gladly highlight the contributors who developed this project!

### Top contributors:

<a href="https://github.com/CarlinoCalogero/univaq-business-process-development/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=CarlinoCalogero/univaq-business-process-development" alt="contrib.rocks image" />
</a>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

**Calogero Carlino**  
Master's Graduate in Computer Science (110/110 *cum laude*) &middot; University of L'Aquila  

* **Email:** [carlinocalogero.dev@gmail.com](mailto:carlinocalogero.dev@gmail.com)
* **LinkedIn:** [linkedin.com/in/calogero-carlino-2a94a33b5](https://www.linkedin.com/in/calogero-carlino-2a94a33b5/)
* **GitHub:** [@CarlinoCalogero](https://github.com/CarlinoCalogero)
* **Portfolio:** [carlinocalogero.github.io](https://carlinocalogero.github.io)

**Project Link:** [https://github.com/CarlinoCalogero/univaq-business-process-development](https://github.com/CarlinoCalogero/univaq-business-process-development)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* **University of L'Aquila (Univaq):** Department of Information Engineering, Computer Science and Mathematics (DISIM).
* **Business Process Development Course:** Course instructors and teaching assistants for project guidelines and evaluation.
* **Camunda Platform:** Open-source workflow and decision automation engine.
* **Spring Boot:** Framework for building production-ready Spring applications.
* **Next.js &amp; Vercel:** Full-stack React framework.
* **Electron:** Cross-platform desktop application framework.
* **Best-README-Template:** Template inspiration by [othneildrew](https://github.com/othneildrew/Best-README-Template).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/CarlinoCalogero/univaq-business-process-development.svg?style=for-the-badge
[contributors-url]: https://github.com/CarlinoCalogero/univaq-business-process-development/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/CarlinoCalogero/univaq-business-process-development.svg?style=for-the-badge
[forks-url]: https://github.com/CarlinoCalogero/univaq-business-process-development/network/members
[stars-shield]: https://img.shields.io/github/stars/CarlinoCalogero/univaq-business-process-development.svg?style=for-the-badge
[stars-url]: https://github.com/CarlinoCalogero/univaq-business-process-development/stargazers
[issues-shield]: https://img.shields.io/github/issues/CarlinoCalogero/univaq-business-process-development.svg?style=for-the-badge
[issues-url]: https://github.com/CarlinoCalogero/univaq-business-process-development/issues
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/calogero-carlino-2a94a33b5/

[Camunda.com]: https://img.shields.io/badge/Camunda_BPM_7.19-EA6724?style=for-the-badge&logo=camunda&logoColor=white
[Camunda-url]: https://camunda.com/
[Spring.io]: https://img.shields.io/badge/Spring_Boot_2.7-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white
[Spring-url]: https://spring.io/projects/spring-boot
[Java.com]: https://img.shields.io/badge/Java_8%2F17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.oracle.com/java/
[Next.js]: https://img.shields.io/badge/Next.js_15-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React_19-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Electron.js]: https://img.shields.io/badge/Electron_36-47848F?style=for-the-badge&logo=electron&logoColor=white
[Electron-url]: https://www.electronjs.org/
[TypeScript.org]: https://img.shields.io/badge/TypeScript_5-3178C6?style=for-the-badge&logo=typescript&logoColor=white
[TypeScript-url]: https://www.typescriptlang.org/
[Maven.apache.org]: https://img.shields.io/badge/Apache_Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white
[Maven-url]: https://maven.apache.org/
