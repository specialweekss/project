const { defineConfig } = require('@vue/cli-service')
// 获取本机电脑IP
const os = require('os');


module.exports = defineConfig({

        publicPath: "./",
        assetsDir: "static",
        outputDir: 'dist',
    devServer: {
        port: 8081,

    },
  transpileDependencies: true
})