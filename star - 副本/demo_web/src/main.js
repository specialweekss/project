import  { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus';
import VueDatepicker from 'vue-datepicker/vue-datepicker-es6.vue'
import '@vuepic/vue-datepicker/dist/main.css'
import 'element-plus/theme-chalk/index.css';
import axios from 'axios'
// 获取本机电脑IP

const app=createApp(App);
window.Ip='http://'+process.env.VUE_APP_LOCAL_IP+':8090'
app.component('VueDatePicker', VueDatepicker);
app.mount('#app');
app.use(ElementPlus);
app.config.globalProperties.$http = axios


