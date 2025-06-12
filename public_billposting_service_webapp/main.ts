import { app, BrowserWindow, ipcMain } from 'electron';
import * as path from 'path';
import { spawn } from 'child_process';
import { is } from "@electron-toolkit/utils";
import { appendFileSync } from 'fs';

let mainWindow: BrowserWindow | null = null;
let serverProcess: any = null;

// Log message
function logMessage(message: string) {
  const logPath = path.join(__dirname, 'log.txt');
  const timestamp = new Date().toISOString();
  const line = `[${timestamp}] ${message}\n`;

  // Append the message (or create the file if it doesn't exist)
  appendFileSync(logPath, line, 'utf8');
};

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1200,
    height: 800,
    webPreferences: {
      contextIsolation: true,
      preload: path.join(__dirname, 'preload.js'),
    },
    autoHideMenuBar: true  // hides the menu bar
  });

  // Open DevTools automatically
  mainWindow.webContents.openDevTools();

  const devURL = 'http://localhost:3000';
  const prodURL = 'http://localhost:3000'; // same, since standalone runs a web server

  mainWindow.loadURL(is.dev ? devURL : prodURL);
}

app.whenReady().then(() => {
  if (is.dev) {
    // Run `next dev` from renderer/
    const rendererPath = path.join(__dirname, '../renderer');
    serverProcess = spawn('npm', ['run', 'dev'], {
      cwd: rendererPath,
      env: process.env,
      stdio: 'inherit',
      shell: true,
    });
  } else {
    // In production, assume standalone was built and installed as a separate server binary
    const standalonePath = path.join(__dirname, '../renderer/.next/standalone');
    serverProcess = spawn('node', ['server'], {
      cwd: standalonePath,
      env: { ...process.env, PORT: '3000' },
      stdio: 'inherit',
      windowsHide: true,
      shell: false // set to false or omit unless necessary
    });
  }

  createWindow();

  ipcMain.on('ping', (event, arg) => {
    console.log('[main] Received ping:', arg);
    event.reply('pong', 'Pong from main process!');
  });
});

app.on('window-all-closed', () => {
  if (serverProcess) serverProcess.kill();
  if (process.platform !== 'darwin') app.quit();
});
