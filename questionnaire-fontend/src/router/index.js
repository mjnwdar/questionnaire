import Vue from 'vue'
import Router from 'vue-router'
import ErrorPage from '@/components/404'
import Dashboard from '@/pages/Dashboard'
import OrderList from '@/pages/OrderList'
import OrderForm from '@/pages/OrderForm'
import About from '@/pages/About'
import ContactList from '@/pages/ContactList'
import ContactForm from '@/pages/ContactForm'
import CustomerList from '@/pages/CustomerList'
import CustomerForm from '@/pages/CustomerForm'
import QuestionnaireList from '@/pages/QuestionnaireList'
import QuestionnaireForm from '@/pages/QuestionnaireForm'
import QuestionnaireScore from '@/pages/QuestionnaireScore'
import Login from '@/components/Login'
import AnswerPage from '@/pages/AnswerPage'
import PersonalInfo from '@/pages/PersonalInfo'

import auth from '@/utils/auth'

Vue.use(Router)

function requireAuth(to, from, next) {
	if (!auth.loggedIn()) {
		next({
			path: '/login',
			query: { redirect: to.fullPath }
		})
	} else {
		next()
	}
}

// const debug = process.env.NODE_ENV !== 'production'

export default new Router({
	base: __dirname,
	mode: 'history',
	scrollBehavior: () => ({ y: 0 }),
	routes: [
		{
			path: '/dashboard',
			component: Dashboard,
			name: 'Dashboard',
			beforeEnter: requireAuth
		},
		{
			path: '/order/:id',
			component: OrderForm,
			name: 'EditOrder',
			beforeEnter: requireAuth
		},
		{
			path: '/new-order',
			component: OrderForm,
			name: 'NewOrder',
			beforeEnter: requireAuth
		},
		{
			path: '/orders',
			component: OrderList,
			name: 'OrderList',
			beforeEnter: requireAuth
		},

		{
			path: '/contact/:id',
			component: ContactForm,
			name: 'EditContact',
			beforeEnter: requireAuth
		},
		{
			path: '/new-contact',
			component: ContactForm,
			name: 'NewContact',
			beforeEnter: requireAuth
		},
		{
			path: '/contacts',
			component: ContactList,
			name: 'ContactList',
			beforeEnter: requireAuth
		},
		{
			path: '/customer/:id',
			component: CustomerForm,
			name: 'EditCustomer',
			beforeEnter: requireAuth
		},
		{
			path: '/new-customer',
			component: CustomerForm,
			name: 'NewCustomer',
			beforeEnter: requireAuth
		},
		{
			path: '/customers',
			component: CustomerList,
			name: 'CustomerList',
			beforeEnter: requireAuth
		},
		{
			path: '/questionnaire/:id',
			component: QuestionnaireForm,
			name: 'EditQuestionnaire',
			beforeEnter: requireAuth
		},
		{
			path: '/new-questionnaire',
			component: QuestionnaireForm,
			name: 'NewQuestionnaire',
			beforeEnter: requireAuth
		},
		{
			path: '/questionnaire',
			component: QuestionnaireList,
			name: 'QuestionnaireList',
			beforeEnter: requireAuth
		},
		{
			path: '/score-questionnaire/:id',
			component: QuestionnaireScore,
			name: 'EditQuestionnaireScore',
			beforeEnter: requireAuth
		},
		{
			path: '/new-score-questionnaire',
			component: QuestionnaireScore,
			name: 'NewQuestionnaireScore',
			beforeEnter: requireAuth
		},
		{
			path: '/about',
			component: About,
			name: 'About',
			beforeEnter: requireAuth
		},
		{
			path: '/login',
			component: Login,
			name: 'Login'
		},
		{
			path: '/personal-info',
			component: PersonalInfo,
			name: 'PersonalInfo'
		},
		{
			path: '/answer',
			component: AnswerPage,
			name: 'AnswerPage'
		},
		{
			path: '/404',
			component: ErrorPage,
			name: 'ErrorPage'
		},
		{
			path: '/logout',
			beforeEnter(to, from, next) {
				auth.logout()
				next('/login')
			}
		},
		{ path: '/', redirect: '/personal-info' },
		{ path: '*', redirect: '/404' }
	],
	meta: {
		progress: {
			func: [
				{ call: 'color', modifier: 'temp', argument: '#ffb000' },
				{ call: 'fail', modifier: 'temp', argument: '#6e0000' },
				{ call: 'location', modifier: 'temp', argument: 'top' },
				{
					call: 'transition',
					modifier: 'temp',
					argument: { speed: '1.5s', opacity: '0.6s', termination: 400 }
				}
			]
		}
	}
})
