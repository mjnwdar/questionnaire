const state = {
	callingAPI: false,
	searching: '',
	user: null,
	token: null,
	mode: '',
	userInfo: {
		messages: [],
		notifications: [],
		tasks: []
	},
	menuItems: [],
	activeMenu: ''
}

const actions = {
	updateUser({ commit }, { user, token }) {
		commit('setToken', token)
		commit('setUser', user)
		commit('setMenuItems', user.role)
	},
	logout({ commit }) {
		commit('setToken', null)
		commit('setUser', {})
	}
}

const mutations = {
	loginLoading(state) {
		state.callingAPI = !state.callingAPI
	},
	globalSearching(state) {
		state.searching = state.searching === '' ? 'loading' : ''
	},
	setUser(state, user) {
		state.user = user
	},
	setToken(state, token) {
		state.token = token
	},
	setMenuItems(state, role) {
		if (role === 1) {
			state.menuItems = [
				{
					icon: 'dashboard',
					title: '面板',
					vertical: 'Dashboard',
					link: 'Dashboard'
				},
				{
					icon: 'forum',
					title: '我的问卷',
					vertical: 'QuestionnaireList',
					link: 'QuestionnaireList'
				},
				{
					icon: 'forum',
					title: '系统管理',
					children: [
						{
							icon: 'shopping_cart',
							title: '角色管理',
							vertical: 'Order',
							link: 'Orders'
						},
						{
							icon: 'bubble_chart',
							title: '菜单管理',
							vertical: 'Product',
							link: 'Products'
						},
						{
							icon: 'perm_identity',
							title: '用户',
							vertical: 'Customer',
							link: 'CustomerList'
						},
						{
							icon: 'bubble_chart',
							title: '客户',
							vertical: 'Contact',
							link: 'ContactList'
						}
					]
				}
			]
			state.activeMenu = 'Dashboard'
		} else if (role === 2) {
			state.menuItems = [
				{
					icon: 'dashboard',
					title: '面板',
					vertical: 'Dashboard',
					link: 'Dashboard'
				},
				{
					icon: 'perm_identity',
					title: '用户',
					vertical: 'Customer',
					link: 'CustomerList'
				},
				{
					icon: 'bubble_chart',
					title: '客户',
					vertical: 'Contact',
					link: 'ContactList'
				}
			]
			state.activeMenu = 'Dashboard'
		} else if (role === 3) {
			state.menuItems = [
				{
					icon: 'forum',
					title: '我的问卷',
					vertical: 'Questionnaire',
					link: 'QuestionnaireList'
				}
			]
			state.activeMenu = 'Dashboard'
		} else {
			state.menuItems = [
				{
					icon: 'forum',
					title: '我的问卷',
					vertical: 'Questionnaire',
					link: 'QuestionnaireList'
				}
			]
			state.activeMenu = 'Questionnaire'
		}
		state.menuItems.push({
			icon: 'thumbs_up_down',
			title: '关于',
			vertical: 'About',
			link: 'About'
		})
	},
	setUserInfo(state, userInfo) {
		state.userInfo = userInfo
	}
}

export default {
	namespaced: true,
	state,
	actions,
	mutations
}
