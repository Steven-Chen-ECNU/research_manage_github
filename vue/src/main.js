import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import DataVVue3 from '@kjgl77/datav-vue3'
import '@/bigscreen/style.css'

const app = createApp(App)

app.use(router)
app.use(ElementPlus, { locale: zhCn })
app.use(DataVVue3)
app.mount('#app')

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
