const { defineConfig } = require('@vue/cli-service')
// 获取本机电脑IP
const os = require('os');

function getLocalIP() {
    const interfaces = os.networkInterfaces();
    for (const name of Object.keys(interfaces)) {
        for (const interf of interfaces[name]) {
            const {address, family, internal} = interf;
            if (family === 'IPv4' && !internal) {
                return address;
            }
        }
    }

    return '0.0.0.0';
}
module.exports = defineConfig({

        publicPath: "./",
        assetsDir: "static",
        outputDir: 'dist',
    devServer: {
        port: 8081,
        host:getLocalIP(),
    },
  transpileDependencies: true
})