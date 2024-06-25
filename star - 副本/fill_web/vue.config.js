const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  publicPath: "./",
  assetsDir: "static",
  outputDir: 'dist',
  devServer: {
    port: 8080
  },// 指定端口号
  transpileDependencies: true
})
