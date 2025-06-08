const { app, BrowserWindow, screen } = require('electron');
const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');
const { downloadAndExtractNode } = require(app.isPackaged ? path.join(process.resourcesPath, 'app', 'download-node') : './download-node'); // Import the async function

if (app.isPackaged) {
  // You're in a packaged, built app
  fs.writeFileSync('log.txt', `Running in production build.\n`, { flag: 'a' });
} else {
  // You're in development (e.g. `electron .`)
  fs.writeFileSync('log.txt', `Running in development.\n`, { flag: 'a' });
}

fs.writeFileSync('log.txt', `__dirname: ${__dirname}\n`, { flag: 'a' });
fs.writeFileSync('log.txt', `process.resourcesPath: ${process.resourcesPath}\n`, { flag: 'a' });

const nodePath = path.join(app.isPackaged ? process.resourcesPath : __dirname, 'node.exe');
fs.writeFileSync('log.txt', `nodePath: ${nodePath}\n`, { flag: 'a' });
const rendererPath = app.isPackaged ? path.join(process.resourcesPath, 'app', 'renderer') : 'renderer';
fs.writeFileSync('log.txt', `rendererPath: ${rendererPath}\n`, { flag: 'a' });

let serverProcess;

function createWindow() {
  const { width, height } = screen.getPrimaryDisplay().workAreaSize;

  const win = new BrowserWindow({
    width: Math.floor(width * 0.7),
    height: Math.floor(height * 0.9),
    webPreferences: {
      nodeIntegration: false,
    },
  });

  win.setMenu(null); // remove menu
  win.center(); // center the window
  win.loadURL('http://localhost:3000'); // Connect to Next.js server
}

app.whenReady().then(async () => {
  if (!fs.existsSync(nodePath)) {
    fs.writeFileSync('log.txt', `⬇️ node.exe not found, downloading...\n`, { flag: 'a' });

    // Load and run your downloader inline
    await downloadAndExtractNode()
  }
  const serverPath = path.join('server.js');
  fs.writeFileSync('log.txt', `Server path: ${serverPath}\n`, { flag: 'a' });

  serverProcess = spawn(app.isPackaged ? nodePath : 'node', [serverPath], {
    cwd: rendererPath,
    stdio: 'inherit',
  });

  serverProcess.on('error', (err) => {
    fs.writeFileSync('log.txt', `Spawn error: ${err.message}\n`, { flag: 'a' });
  });

  serverProcess.on('exit', (code, signal) => {
    fs.writeFileSync('log.txt', `Server exited with code ${code}, signal ${signal}\n`, { flag: 'a' });
  });

  setTimeout(() => {
    createWindow();
  }, 2000); // Give server a moment to start
});

app.on('window-all-closed', () => {
  if (serverProcess) {
    serverProcess.kill();
  }
  app.quit();
});
