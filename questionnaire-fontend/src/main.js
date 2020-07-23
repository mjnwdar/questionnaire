// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
'use strict'
import Vue from 'vue'
import Vuetify from 'vuetify'
import App from './App'
import router from '@/router'
import store from '@/store'
import api from '@/utils/backend-api'
import appUtil from '@/utils/app-util'

import VueProgressBar from 'vue-progressbar'

Vue.config.productionTip = false
Vue.config.devtools = true
Vue.prototype.api = api
Vue.prototype.appUtil = appUtil

window.Store = store
/* eslint-disable no-new */

Vue.use(VueProgressBar, {
	color: '#2196f3',
	failedColor: '#874b4b',
	thickness: '5px',
	transition: {
		speed: '0.1s',
		opacity: '0.5s',
		termination: 400
	},
	autoRevert: true,
	location: 'top',
	inverse: false
})

Vue.use(Vuetify)
new Vue({
	strict: true,
	el: '#app',
	router,
	store,
	render: (h) => h(App)
})
