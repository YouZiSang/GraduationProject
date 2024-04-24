import './assets/main.css'

import {createApp} from "vue";
import router from "@/router";
import App from './App.vue';
import {createPinia} from 'pinia'
import { createPersistedState} from "pinia-persistedstate-plugin";
import echarts from "./utils/echarts.js";
const app = createApp(App)
const pinia = createPinia()
const persist = createPersistedState()

pinia.use(persist)

app.use(pinia)
app.use(router)
app.provide('$echarts', echarts);
app.mount('#app')
