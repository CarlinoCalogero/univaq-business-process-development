// download-node.js
const { app } = require('electron');
const https = require('https');
const fs = require('fs');
const path = require('path');
const unzip = require('unzipper'); // Make sure this package is installed: npm install unzipper

const version = 'v18.20.2'; // change as needed
const arch = 'win-x64';
const nodeDistUrl = `https://nodejs.org/dist/${version}/node-${version}-${arch}.zip`;

// Function to determine the target path for node.exe
// It's generally safer to pass `app` or resolve paths when `app` is ready.
// For simplicity, we'll keep it defined here, but be mindful of `app.whenReady()`
let targetNodeExe;

/**
 * Downloads and extracts the Node.js executable for the specified version and architecture.
 * The extracted node.exe will be placed in the app's resources path (if packaged)
 * or the current directory (if unpacked/during development).
 *
 * @returns {Promise<string>} A promise that resolves with the path to the downloaded node.exe
 * or rejects if the download/extraction fails.
 */
function downloadAndExtractNode() {
    const targetNodeExe = path.join(app.isPackaged ? process.resourcesPath : __dirname, 'node.exe');

    return new Promise((resolve, reject) => {
        fs.writeFileSync('log.txt', `⬇️  Downloading Node.js ${version} for ${arch}...\n`, { flag: 'a' });

        https.get(nodeDistUrl, (res) => {
            if (res.statusCode !== 200) {
                const errorMessage = `❌ Failed to download Node.js: HTTP Status ${res.statusCode}`;
                fs.writeFileSync('log.txt', `${errorMessage}\n`, { flag: 'a' });
                reject(new Error(errorMessage));
                return;
            }

            let resolved = false;

            res.pipe(unzip.Parse())
                .on('entry', (entry) => {
                    const fileName = entry.path;
                    if (fileName.endsWith('node.exe')) {
                        fs.writeFileSync('log.txt', '✅ Extracting node.exe...\n', { flag: 'a' });

                        const writeStream = fs.createWriteStream(targetNodeExe);
                        entry.pipe(writeStream)
                            .on('finish', () => {
                                if (!resolved) {
                                    resolved = true;
                                    fs.writeFileSync('log.txt', `🎉 Node.js downloaded to: ${targetNodeExe}\n`, { flag: 'a' });
                                    resolve(targetNodeExe);
                                }
                            })
                            .on('error', (err) => {
                                if (!resolved) {
                                    resolved = true;
                                    fs.writeFileSync('log.txt', `❌ Error writing node.exe: ${err.message}\n`, { flag: 'a' });
                                    reject(err);
                                }
                            });
                    } else {
                        entry.autodrain();
                    }
                })
                .on('error', (err) => {
                    if (!resolved) {
                        resolved = true;
                        fs.writeFileSync('log.txt', `❌ Unzip stream error: ${err.message}\n`, { flag: 'a' });
                        reject(err);
                    }
                })
                .on('close', () => {
                    // Do nothing here — resolution happens in writeStream.finish
                    if (!resolved) {
                        fs.writeFileSync('log.txt', '❌ node.exe not found in the zip (close event)\n', { flag: 'a' });
                        reject(new Error('node.exe not found in the zip'));
                    }
                });
        }).on('error', (err) => {
            fs.writeFileSync('log.txt', `❌ Download request error: ${err.message}\n`, { flag: 'a' });
            reject(err);
        });
    });
}


// Export the function
module.exports = {
    downloadAndExtractNode
};