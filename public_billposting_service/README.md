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
  <h2 align="center">Public Billposting Service - Backend Engine</h2>
  <h4 align="center">University of L'Aquila &middot; Business Process Development</h4>

  <p align="center">
    Spring Boot and Camunda BPMN 2.0 process engine orchestrating billboard and poster reservation workflows.
    <br />
    Developed as an academic backend component for the <strong>Business Process Development</strong> course at the <strong>University of L'Aquila (Univaq)</strong>.
    <br />
    <br />
    <a href="https://github.com/CarlinoCalogero/univaq-business-process-development/tree/main/public_billposting_service"><strong>Explore the module docs »</strong></a>
    <br />
    <br />
    <a href="#about-the-project">About The Project</a>
    &middot;
    <a href="#process-architecture--workflow">BPMN Workflow</a>
    &middot;
    <a href="#rest-api-reference">REST API</a>
    &middot;
    <a href="#getting-started">Getting Started</a>
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
    <li>
      <a href="#process-architecture--workflow">Process Architecture &amp; Workflow</a>
      <ul>
        <li><a href="#external-service-connectors">External Service Connectors</a></li>
        <li><a href="#zone-selection-strategies">Zone Selection Strategies</a></li>
      </ul>
    </li>
    <li><a href="#rest-api-reference">REST API Reference</a></li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation--running">Installation &amp; Running</a></li>
      </ul>
    </li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

> [!NOTE]
> **University Project Notice:**
> This service forms the backend orchestration core of an academic university project developed by [Calogero Carlino](https://github.com/CarlinoCalogero) for the **Business Process Development** course at the **University of L'Aquila (Univaq)**.

The **`public_billposting_service`** coordinates all business interactions required to handle municipal billboard requests. It embeds the **Camunda Platform 7** workflow engine within a **Spring Boot** application, combining automated service tasks, parallel gateways, asynchronous message events, and custom Java delegates.

### Core Responsibilities
* **Process Orchestration:** Executes the executable BPMN 2.0 workflow `PublicBillpostingService`.
* **Parallel Asynchronous Lookups:** Concurrently queries separate REST endpoints for client account details and geographic zone availability.
* **Intelligent Zone Optimization:** Implements algorithmic allocation strategies to maximize advertising impact within customer-defined city budget constraints.
* **SOAP Protocol Translation:** Bridges modern JSON REST requests into SOAP 1.1 envelopes via FreeMarker templates.
* **Document Generation:** Writes formatted text summary invoices upon booking confirmation into an `output/` folder.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* [![Spring Boot][Spring.io]][Spring-url]
* [![Camunda][Camunda.com]][Camunda-url]
* [![Java][Java.com]][Java-url]
* [![Apache Maven][Maven.apache.org]][Maven-url]
* [![FreeMarker][FreeMarker.apache.org]][FreeMarker-url]
* [![H2 Database][H2.com]][H2-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- PROCESS ARCHITECTURE & WORKFLOW -->
## Process Architecture &amp; Workflow

The core business process is defined in [`src/main/resources/processes/camunda_exam_business_process.bpmn`](file:///c:/Users/calog/Documents/univaq-business-process-development/public_billposting_service/src/main/resources/processes/camunda_exam_business_process.bpmn):

1. **Start Event:** Triggered via the `/poster-availability/request` REST endpoint with an incoming `PosterAvailabilityRequest`.
2. **Parallel Gateway Fork:**
   * **`GetUserData` (HTTP REST):** Queries `http://localhost:9080/user/{username}` using `http-connector` and unmarshals XML/JSON with `userParserService`.
   * **`GetZoneData` (HTTP REST):** Queries `http://localhost:9090/zones/{posterFormat}` using `http-connector` and unmarshals zone data via `zoneParserService`.
3. **Parallel Gateway Join:** Synchronizes both data streams before proceeding.
4. **`SelectZones` (Java Delegate - `ZoneSelector`):** Evaluates candidate zones in the requested cities and applies the selected budget strategy.
5. **`CheckAvailability` (SOAP HTTP Connector):** Formats an XML payload using `availabilityRequestEnvelope.ftl` and calls `http://localhost:8888/postingservice`.
6. **`PosterRequestDecision` (Intermediate Message Catch Event):** Waits for an external confirmation or cancellation signal correlated on `correlationVariable = requestId`.
7. **Exclusive Gateway Branching:**
   * **Decision == "confirm":**
     * **`ConfirmPosterRequest` (SOAP):** Dispatches confirmation envelope (`posterRequestConfirmationEnvelope.ftl`) to finalize booking and retrieves invoice information (`Bill`).
     * **`WriteRequestInformationFile` (`RequestFileWriter`):** Compiles an invoice summary file into `output/bill_<timestamp>.txt`.
   * **Decision == "cancel":**
     * **`CancelPosterRequest` (SOAP):** Dispatches cancellation envelope (`posterRequestCancelEnvelope.ftl`).
     * **`SendRequestCancelNotification` (`RequestCancelNotifier`):** Logs and dispatches cancel alerts.
8. **End Event:** Terminates process execution.

### External Service Connectors
* **User Service (REST):** `http://localhost:9080/user/{username}`
* **Zone Service (REST):** `http://localhost:9090/zones/{posterFormat}`
* **Billposting SOAP Service:** `http://localhost:8888/postingservice`

### Zone Selection Strategies
Implemented in [`ZoneSelector.java`](file:///c:/Users/calog/Documents/univaq-business-process-development/public_billposting_service/src/main/java/it/univaq/disim/bpd/publicbillpostingservice/delegate/ZoneSelector.java):

* **`GREEDY`:** Sorts available zones by highest price first to allocate premium spaces within each city's maximum budget.
* **`CHEAPEST_FIRST`:** Sorts zones ascending by price, maximizing the number of allocated billboards.
* **`BEST_VALUE`:** Prioritizes billboards based on an efficiency ratio (`zone.id / zone.price`).
* **`EQUAL_BUDGET`:** Computes an equal partition of the aggregate budget across all designated cities before greedy packing.
* **`RANDOM`:** Stochastically picks valid billboard configurations within budget thresholds.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- REST API REFERENCE -->
## REST API Reference

The backend exposes two REST endpoints under the `/poster-availability` base route (configured with CORS support for `http://localhost:3000`):

### 1. Request Availability &amp; Initiate Process
* **URL:** `POST /poster-availability/request`
* **Content-Type:** `application/json`
* **Sample Request Body:**
  ```json
  {
    "username": "mario_rossi",
    "posterFormat": "6x3",
    "selectionStrategy": "GREEDY",
    "citiesAndMaximumPrice": [
      {
        "city": "L'Aquila",
        "maxPrice": 250.0
      },
      {
        "city": "Rome",
        "maxPrice": 500.0
      }
    ]
  }
  ```
* **Sample Response (HTTP 200):**
  ```json
  {
    "requestId": "REQ-20250612-4821",
    "totalPrice": 720.0,
    "selectedZones": {
      "L'Aquila": [
        { "id": 101, "city": "L'Aquila", "name": "Piazza Duomo", "price": 120.0 }
      ],
      "Rome": [
        { "id": 204, "city": "Rome", "name": "Termini Station", "price": 400.0 }
      ]
    }
  }
  ```

### 2. Submit Booking Decision
* **URL:** `POST /poster-availability/decide`
* **Content-Type:** `application/json`
* **Sample Request Body (Confirm):**
  ```json
  {
    "requestId": "REQ-20250612-4821",
    "decision": "confirm"
  }
  ```
* **Sample Response (Confirm - HTTP 200):**
  ```json
  {
    "accountHolder": "Mario Rossi",
    "invoiceNumber": "INV-893241",
    "amountDue": 720.0
  }
  ```
* **Sample Request Body (Cancel):**
  ```json
  {
    "requestId": "REQ-20250612-4821",
    "decision": "cancel"
  }
  ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

* **Java Development Kit (JDK):** Version 8 or 17
* **Apache Maven:** Version 3.8 or higher

Verify your installation:
```sh
java -version
mvn -version
```

### Installation &amp; Running

1. **Navigate to the backend directory:**
   ```sh
   cd public_billposting_service
   ```

2. **Build the project with Maven:**
   ```sh
   mvn clean install
   ```

3. **Run the Spring Boot application:**
   ```sh
   mvn spring-boot:run
   ```

4. **Verify Application and Camunda Cockpit:**
   * Backend REST API: `http://localhost:8080/poster-availability`
   * Camunda Webapp: [http://localhost:8080/camunda](http://localhost:8080/camunda)
   * Default credentials:
     * **Username:** `demo`
     * **Password:** `demo`

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

> [!IMPORTANT]
> **No External Contributions Accepted:**
> This module was built as part of an academic examination for the **Business Process Development** course at the **University of L'Aquila (Univaq)**. Because the course requirements have been completed, **external contributions and pull requests are not accepted**.
>
> We proudly showcase the original authors and project contributors below!

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
* **Camunda Platform Community:** For documentation on Spring Boot Starter, Connectors, and Spin.
* **Spring Boot:** For modular microservice development and autoconfiguration.
* **Apache FreeMarker:** For template-driven XML/text generation.

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

[Spring.io]: https://img.shields.io/badge/Spring_Boot_2.7.9-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white
[Spring-url]: https://spring.io/projects/spring-boot
[Camunda.com]: https://img.shields.io/badge/Camunda_BPM_7.19-EA6724?style=for-the-badge&logo=camunda&logoColor=white
[Camunda-url]: https://camunda.com/
[Java.com]: https://img.shields.io/badge/Java_8%2F17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.oracle.com/java/
[Maven.apache.org]: https://img.shields.io/badge/Apache_Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white
[Maven-url]: https://maven.apache.org/
[FreeMarker.apache.org]: https://img.shields.io/badge/Apache_FreeMarker-00599C?style=for-the-badge&logo=apache&logoColor=white
[FreeMarker-url]: https://freemarker.apache.org/
[H2.com]: https://img.shields.io/badge/H2_Database-003545?style=for-the-badge&logo=sqlite&logoColor=white
[H2-url]: https://www.h2database.com/
