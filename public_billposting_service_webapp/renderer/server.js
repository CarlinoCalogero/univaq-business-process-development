const { createServer } = require('http');
const { parse } = require('url');
const fs = require('fs');
const next = require('next');
const path = require('path');

const port = process.env.PORT || 3000;
const app = next({ dev: false, dir: '.' });
const handle = app.getRequestHandler();

const logFilePath = path.join(path.dirname(__dirname), 'log.txt');
fs.writeFileSync(logFilePath, `__dirname: ${__dirname}\n`, { flag: 'a' });

fs.writeFileSync(logFilePath, '📡 Starting Next.js server...\n', { flag: 'a' });

app.prepare().then(() => {
  fs.writeFileSync(logFilePath, '✅ Next.js server prepared\n', { flag: 'a' });
  createServer((req, res) => {
    const parsedUrl = parse(req.url, true);
    handle(req, res, parsedUrl);
  }).listen(port, () => {
    fs.writeFileSync(logFilePath, `> Ready on http://localhost:${port}\n`, { flag: 'a' });
  });
}).catch((err) => {
  console.error();
  fs.writeFileSync(logFilePath, `❌ Failed to start server: ${err}\n`, { flag: 'a' });
});
