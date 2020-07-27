<template>
  <v-container fill-height justify-center align-center>
    <!-- <v-layout row > -->
    <v-flex xs12 sm5 md4 lg3>
      <v-card class="mt-0 pt-0">
        <v-card-title class="blue darken-1">
          <h4 style="color:white">张智期CRM</h4>
        </v-card-title>
        <v-card-text>
          <form @submit.prevent="login">
            <v-layout row wrap>
              <v-flex xs12 md4>
                <v-subheader>登录名</v-subheader>
              </v-flex>
              <v-flex xs12 md8>
                <v-text-field class="input-group--focused" name="email" v-model="email"></v-text-field>
              </v-flex>
            </v-layout>
            <v-layout row wrap>
              <v-flex xs12 md4>
                <v-subheader>密码</v-subheader>
              </v-flex>
              <v-flex xs12 md8>
                <v-text-field class="input-group--focused" name="password" type="password"
                              v-model="pass"></v-text-field>
              </v-flex>
            </v-layout>
            <v-btn type="submit">登录</v-btn>
            <v-snackbar v-if="error" :timeout="timeout" :top="true" :multi-line="mode === 'multi-line'"
                        :vertical="mode === 'vertical'" v-model="error">
              {{ text }}
              <v-btn @click.native="error = false" class="pink--text" flat>关闭</v-btn>
            </v-snackbar>
          </form>
        </v-card-text>
      </v-card>
    </v-flex>
    <!-- </v-layout> -->
  </v-container>
</template>
<script>
import auth from '../utils/auth'

export default {
	data() {
		return {
			email: 'admin',
			pass: '123456',
			error: false,
			text: ''
		}
	},
	methods: {
		login() {
			auth.login(this.email, this.pass, (loggedIn, err) => {
				if (err) {
					console.log('login', err)
					this.error = true
					this.text = err
				} else if (loggedIn === false) {
					console.log('login', loggedIn)
					this.error = true
					this.text = '登录信息错误'
				} else {
					// eslint-disable-next-line no-undef
					this.$router.push(
						this.$route.query.redirect ||
							Store.state.user.activeMenu
					)
				}
			})
		}
	}
}
</script>
<style lang="stylus">
  @import '../stylus/main';
</style>
