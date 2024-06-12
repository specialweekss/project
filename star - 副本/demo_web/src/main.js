import  { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import axios from 'axios'
const app=createApp(App);
app.mount('#app');
app.use(ElementPlus);
app.config.globalProperties.$http = axios


