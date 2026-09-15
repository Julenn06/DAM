const { exec } = require('child_process');
const util = require('util');
const execPromise = util.promisify(exec);

/**
 * Herramienta para verificar dependencias de Node.js con vulnerabilidades
 */

async function checkVulnerabilities() {
    console.log('Verificando vulnerabilidades en dependencias...\n');
    
    try {
        // Ejecutar npm audit
        const { stdout, stderr } = await execPromise('npm audit --json');
        const audit = JSON.parse(stdout);
        
        console.log('=== Resumen de Auditoría ===');
        console.log(`Vulnerabilidades encontradas: ${audit.metadata.vulnerabilities.total}`);
        console.log(`  • Críticas: ${audit.metadata.vulnerabilities.critical}`);
        console.log(`  • Altas: ${audit.metadata.vulnerabilities.high}`);
        console.log(`  • Medias: ${audit.metadata.vulnerabilities.moderate}`);
        console.log(`  • Bajas: ${audit.metadata.vulnerabilities.low}`);
        console.log(`  • Info: ${audit.metadata.vulnerabilities.info}`);
        
        if (audit.metadata.vulnerabilities.total > 0) {
            console.log('\n=== Detalles ===');
            
            for (const [name, vuln] of Object.entries(audit.vulnerabilities || {})) {
                console.log(`\nPaquete: ${name}`);
                console.log(`  Severidad: ${vuln.severity.toUpperCase()}`);
                console.log(`  Rango afectado: ${vuln.range}`);
                if (vuln.fixAvailable) {
                    console.log(`  Solución: Actualizar a ${vuln.fixAvailable.version}`);
                } else {
                    console.log(`  Solución: No disponible automáticamente`);
                }
            }
            
            console.log('\n💡 Ejecuta "npm audit fix" para intentar reparar automáticamente');
        } else {
            console.log('\n✓ No se encontraron vulnerabilidades');
        }
        
    } catch (error) {
        if (error.stdout) {
            try {
                const audit = JSON.parse(error.stdout);
                console.log('Se encontraron problemas. Ejecuta "npm audit" para más detalles.');
            } catch {
                console.log('Error al ejecutar npm audit. ¿Estás en un proyecto Node.js?');
            }
        } else {
            console.log('Error:', error.message);
        }
    }
}

checkVulnerabilities();
