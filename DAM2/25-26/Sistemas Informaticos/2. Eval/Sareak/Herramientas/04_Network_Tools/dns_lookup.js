const dns = require('dns').promises;

/**
 * Herramienta para consultar registros DNS de un dominio
 */

async function lookupDNS(domain) {
    console.log(`Consultando registros DNS de: ${domain}\n`);
    
    try {
        // Dirección IP (A)
        console.log('--- Registros A (IPv4) ---');
        const addresses = await dns.resolve4(domain);
        addresses.forEach(addr => console.log(`  ${addr}`));
        
        // IPv6
        try {
            console.log('\n--- Registros AAAA (IPv6) ---');
            const addresses6 = await dns.resolve6(domain);
            addresses6.forEach(addr => console.log(`  ${addr}`));
        } catch (e) {
            console.log('  No disponible');
        }
        
        // MX (Mail Exchange)
        try {
            console.log('\n--- Registros MX (Mail) ---');
            const mx = await dns.resolveMx(domain);
            mx.forEach(record => console.log(`  ${record.priority} ${record.exchange}`));
        } catch (e) {
            console.log('  No disponible');
        }
        
        // NS (Name Servers)
        try {
            console.log('\n--- Registros NS (Name Servers) ---');
            const ns = await dns.resolveNs(domain);
            ns.forEach(server => console.log(`  ${server}`));
        } catch (e) {
            console.log('  No disponible');
        }
        
        // TXT
        try {
            console.log('\n--- Registros TXT ---');
            const txt = await dns.resolveTxt(domain);
            txt.forEach(record => console.log(`  ${record.join('')}`));
        } catch (e) {
            console.log('  No disponible');
        }
        
    } catch (error) {
        console.error(`Error: ${error.message}`);
    }
}

if (process.argv.length < 3) {
    console.log('Uso: node dns_lookup.js dominio.com');
    process.exit(1);
}

const domain = process.argv[2];
lookupDNS(domain);
