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
  <h2 align="center">Public Billposting Service - Web &amp; Desktop App</h2>
  <h4 align="center">University of L'Aquila &middot; Business Process Development</h4>

  <p align="center">
    Cross-platform desktop and web user interface built with Next.js 15, React 19, and Electron.
    <br />
    Developed as an academic client application for the <strong>Business Process Development</strong> course at the <strong>University of L'Aquila (Univaq)</strong>.
    <br />
    <br />
    <a href="https://github.com/CarlinoCalogero/univaq-business-process-development/tree/main/public_billposting_service_webapp"><strong>Explore the module docs »</strong></a>
    <br />
    <br />
    <a href="#about-the-project">About The Project</a>
    &middot;
    <a href="#ui-workflow--pages">UI Workflow</a>
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
    <li>
      <a href="#ui-workflow--pages">UI Workflow &amp; Pages</a>
      <ul>
        <li><a href="#1-reservation-request-form-">1. Reservation Request Form</a></li>
        <li><a href="#2-quotation-and-zone-review-request">2. Quotation &amp; Zone Review</a></li>
        <li><a href="#3-decision-outcome-requestdecide">3. Decision Outcome</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
        <li><a href="#running-in-development">Running in Development</a></li>
        <li><a href="#building-and-distribution">Building &amp; Distribution</a></li>
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
> This application constitutes the frontend client component of an academic project developed by [Calogero Carlino](https://github.com/CarlinoCalogero) for the **Business Process Development** course at the **University of L'Aquila (Univaq)**.

The **`public_billposting_service_webapp`** offers a client interface for reserving public advertising spaces across cities. It bridges user requests to the Camunda BPMN backend, presenting complex multi-city budget calculations, interactive zone selections, and asynchronous order resolutions.

### Key Highlights
* **Dual Execution Modes:** Runs as a standalone browser-based web application or as a native desktop application powered by **Electron 36**.
* **Modern React 19 &amp; Next.js 15:** Utilizes the Next.js App Router with React Server/Client Components and Turbopack support.
* **Interactive Multi-City Budgeting:** Custom dynamic input fields allowing users to select several target cities and allocate specific maximum prices for each.
* **Algorithmic Strategy Selection:** Allows users to choose between `GREEDY`, `CHEAPEST_FIRST`, `BEST_VALUE`, `EQUAL_BUDGET`, and `RANDOM` allocation algorithms.
* **Two-Way IPC Communication:** Secure preload script (`preload.ts`) and context isolation for desktop features and logging.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* [![Next.js][Next.js]][Next-url]
* [![React][React.js]][React-url]
* [![Electron][Electron.js]][Electron-url]
* [![TypeScript][TypeScript.org]][TypeScript-url]
* [![Node.js][Node.js]][Node-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- UI WORKFLOW & PAGES -->
## UI Workflow &amp; Pages

The application guides users through three progressive stages:

```text
  ┌─────────────────────────┐
  │ 1. Request Form ( / )   │
  │    Username, Format,    │
  │  City Budgets, Strategy │
  └────────────┬────────────┘
               │ Submit
               ▼
  ┌─────────────────────────┐
  │ 2. Review ( /[request] )│
  │   Selected Zones List,  │
  │   Total Price, Actions  │
  └────────────┬────────────┘
               │ Confirm / Cancel
               ▼
  ┌─────────────────────────┐
  │ 3. Outcome ( /[decide] )│
  │  Invoice (Account, ID)  │
  │    or Cancellation info │
  └─────────────────────────┘
```

### 1. Reservation Request Form (`/`)
* Entry point collecting customer details.
* Accepts client username and billboard format.
* Dynamically adds multiple target cities, each paired with an individual spending limit.
* Allows selecting the zone allocation algorithm:
  * `GREEDY`
  * `CHEAPEST_FIRST`
  * `BEST_VALUE`
  * `EQUAL_BUDGET`
  * `RANDOM`
* Sends payload to the backend REST endpoint (`POST /poster-availability/request`).

### 2. Quotation and Zone Review (`/[request]`)
* Renders the calculated billposting quote returned by the Camunda process engine.
* Displays allocated zones grouped by city, with individual pricing and aggregate cost.
* Shows unique `requestId` tracked by the running BPMN process instance.
* Provides **Confirm** and **Cancel** decision buttons.

### 3. Decision Outcome (`/[request]/[decide]`)
* Sends decision to `POST /poster-availability/decide`.
* If **confirmed**: renders the final bill with invoice number, account holder name, and total amount due.
* If **cancelled**: displays cancellation confirmation and acknowledgement.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

* **Node.js:** Version 20.x or later
* **npm:** Version 9.x or later
* **public_billposting_service Backend:** Running on `http://localhost:8080`

Verify your Node.js environment:
```sh
node -v
npm -v
```

### Installation

1. **Navigate to the webapp directory:**
   ```sh
   cd public_billposting_service_webapp
   ```

2. **Install root dependencies (Electron &amp; tooling):**
   ```sh
   npm install
   ```

3. **Install renderer dependencies (Next.js &amp; React):**
   ```sh
   cd renderer
   npm install
   cd ..
   ```

### Running in Development

* **Desktop Mode (Electron + Next.js concurrent):**
  ```sh
  npm run dev
  ```
  This command compiles TypeScript for Electron and launches the desktop app window pointing to the local Next.js dev server.

* **Browser Mode (Next.js only):**
  ```sh
  npm run dev:next
  ```
  Open [http://localhost:3000](http://localhost:3000) in your browser.

### Building and Distribution

1. **Build Next.js Standalone and Electron:**
   ```sh
   npm run build
   ```

2. **Package desktop executable with electron-builder:**
   ```sh
   npm run dist
   ```
   The installer executable will be generated inside the `dist-electron/` folder.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

> [!IMPORTANT]
> **No External Contributions Accepted:**
> This repository was created as an academic project for the **Business Process Development** course at the **University of L'Aquila (Univaq)**. Because the academic examination and curriculum have concluded, **external contributions and pull requests are not accepted**.
>
> We nonetheless gladly highlight the contributors who built and designed this project!

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
* **Next.js &amp; Vercel:** For the frontend framework and Turbopack compiler.
* **Electron Toolkit:** For development utilities and cross-platform desktop integration.
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

[Next.js]: https://img.shields.io/badge/Next.js_15-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React_19-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Electron.js]: https://img.shields.io/badge/Electron_36-47848F?style=for-the-badge&logo=electron&logoColor=white
[Electron-url]: https://www.electronjs.org/
[TypeScript.org]: https://img.shields.io/badge/TypeScript_5-3178C6?style=for-the-badge&logo=typescript&logoColor=white
[TypeScript-url]: https://www.typescriptlang.org/
[Node.js]: https://img.shields.io/badge/Node.js_20-43853D?style=for-the-badge&logo=node.js&logoColor=white
[Node-url]: https://nodejs.org/
