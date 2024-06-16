import  { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus';
import VueDatepicker from 'vue-datepicker/vue-datepicker-es6.vue'
import '@vuepic/vue-datepicker/dist/main.css'
import 'element-plus/theme-chalk/index.css';
import axios from 'axios'
const app=createApp(App);
app.component('VueDatePicker', VueDatepicker);
app.mount('#app');
app.use(ElementPlus);
app.config.globalProperties.$http = axios