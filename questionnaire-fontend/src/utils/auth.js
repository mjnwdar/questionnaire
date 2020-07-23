/* globals Store */
// import api from '@/utils/backend-api'
import api from '@/utils/demo-api'

export default {
	login(email, pass, cb) {
		api.login('token', email).then(
			(res) => {
				const token = res.access_token || res.data.access_token
				const user = res.user || res.data.user
				Store.dispatch('user/updateUser', { user, token })
				if (cb) {
					cb(true, null)
				}
				this.onChange(true)
			},
			(err) => {
				console.log(err)
				if (cb) {
					cb(false, err)
				}
				this.onChange(false)
			}
		)
	},
	getToken() {
		return Store.state.user.token
	},
	logout(cb) {
		// delete localStorage.token
		// Store.commit('setToken', null)
		Store.dispatch('user/logout')
		if (cb) {
			cb(false)
		}
		this.onChange(false)
	},
	loggedIn() {
		return !!Store.state.user.token
	},
	onChange() {}
}
