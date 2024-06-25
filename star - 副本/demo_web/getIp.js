const os = require('os');
const fs = require('fs');

function getNetworkIp() {
    const interfaces = os.networkInterfaces();
    for (const name of Object.keys(interfaces)) {
        for (const interface of interfaces[name]) {
            const {address, family, internal} = interface;

            // 排除 loopback 和 external 地址
            if (!internal && family === 'IPv4') {
                return address;
            }
        }
    }
    return null;
}

const ip = getNetworkIp();

if (ip) {
    console.log(`Found local IP: ${ip}`);
    fs.writeFileSync('.env.local', `VUE_APP_LOCAL_IP=${ip}`);
} else {
    console.log('Could not find local IP.');
}