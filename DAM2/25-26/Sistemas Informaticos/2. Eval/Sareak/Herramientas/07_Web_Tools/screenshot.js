const puppeteer = require('puppeteer');

/**
 * Herramienta para capturar screenshots de páginas web
 */

async function takeScreenshot(url, outputFile = 'screenshot.png', fullPage = false) {
    console.log(`Capturando screenshot de: ${url}`);
    
    const browser = await puppeteer.launch();
    const page = await browser.newPage();
    
    await page.setViewport({ width: 1920, height: 1080 });
    await page.goto(url, { waitUntil: 'networkidle2' });
    
    await page.screenshot({ 
        path: outputFile,
        fullPage: fullPage
    });
    
    await browser.close();
    
    console.log(`✓ Screenshot guardado: ${outputFile}`);
}

if (process.argv.length < 3) {
    console.log('Uso: node screenshot.js URL [salida.png] [--full-page]');
    process.exit(1);
}

const url = process.argv[2];
const output = process.argv[3] && !process.argv[3].startsWith('--') ? process.argv[3] : 'screenshot.png';
const fullPage = process.argv.includes('--full-page');

takeScreenshot(url, output, fullPage)
    .catch(error => console.error('Error:', error));
