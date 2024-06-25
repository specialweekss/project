import  { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import axios from 'axios'
const app=createApp(App);
window.Ip='http://'+process.env.VUE_APP_LOCAL_IP+':8090'
app.mount('#app');
app.use(ElementPlus);
app.config.globalProperties.$http = axios