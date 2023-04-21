import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import 'element-plus/dist/index.css'
import axios from "axios";

const app = createApp(App)

// 跨域的域名不同会刷新session
axios.defaults.baseURL='http://127.0.0.1:8080';
axios.defaults.withCredentials=true;

app.use(createPinia())
app.use(router)

app.mount('#app')
