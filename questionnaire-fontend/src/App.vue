<template>
  <v-app>
    <vue-progress-bar></vue-progress-bar>
    <template v-if="!loggedIn">
      <router-view></router-view>
    </template>
    <template v-if="loggedIn">
      <v-navigation-drawer class="blue lighten-5" width="250" light :mini-variant.sync="mini" v-model="drawer" app>
        <v-list class="pa-0">
          <v-list-tile avatar tag="div">
            <v-list-tile-avatar>
              <img src="/assets/img/avatar0.png">
            </v-list-tile-avatar>
            <v-list-tile-content>
              <v-list-tile-title>{{user.lastName}}.{{user.firstName}}</v-list-tile-title>
            </v-list-tile-content>
            <v-spacer/>
            <v-list-tile-action style="min-width:30px;">
              <v-menu bottom right offset-y origin="bottom right" transition="v-slide-y-transition">
                <v-btn icon light slot="activator">
                  <v-icon>more_vert</v-icon>
                </v-btn>
                <v-list>
                  <v-list-tile v-for="item in userMenus" :key="item.title" value="true" :to="item.link" router>
                    <v-list-tile-title v-text="item.title"/>
                  </v-list-tile>
                </v-list>
              </v-menu>
            </v-list-tile-action>
            <v-list-tile-action style="min-width:30px;">
              <v-btn icon @click.native.stop="mini = !mini">
                <v-icon>chevron_left</v-icon>
              </v-btn>
            </v-list-tile-action>
          </v-list-tile>
        </v-list>
        <v-list>
          <template v-for="item in menuItems">
            <v-list-group :key="item.vertical" :prepend-icon="item.icon" no-action="" style="line-height:38px;padding-top:15px;padding-bottom: 15px;" v-if="item.children">
              <template slot="activator">
                <v-list-tile-content>
                  <v-list-tile-title :key="item.title" style="padding-left: 12px;" v-text="item.title"/>
                </v-list-tile-content>
              </template>
              <template v-for="child in item.children">
                <v-list-tile :key="`${item.vertical}-${child.vertical}`" @click="clickMenu(child)" router>
                  <v-list-item-action>
                    <v-icon :class="activeMenuItem.includes(child.link)?'blue--text': ''" :title="child.title" light v-html="child.icon"/>
                  </v-list-item-action>
                  <v-list-item-content>
                    <v-list-item-title style="padding-left: 18px;" v-text="child.title"/>
                  </v-list-item-content>
                </v-list-tile>
              </template>
            </v-list-group>
            <v-list-tile v-else :key="item.vertical" @click="clickMenu(item)" router>
              <v-list-tile-action>
                <v-icon :class="activeMenuItem.includes(item.link)?'blue--text': ''" :title="item.title" light
                        v-html="item.icon"/>
              </v-list-tile-action>
              <v-list-tile-content :class="activeMenuItem.includes(item.link)?'blue--text':''">
                <v-list-tile-title v-text="item.title"/>
              </v-list-tile-content>
            </v-list-tile>
          </template>
        </v-list>
      </v-navigation-drawer>
      <v-content>
        <v-container fluid>
          <router-view/>
        </v-container>
      </v-content>
    </template>
  </v-app>

</template>
<script>
import auth from './utils/auth'
import { mapState } from 'vuex'

export default {
	data() {
		return {
			dialog: false,
			mini: false,
			dialogText: '',
			dialogTitle: '',
			loggedIn: auth.loggedIn(),
			isRootComponent: true,
			drawer: window.innerWidth > 960,
			fixed: false,
			miniVariant: false,
			right: true,
			rightDrawer: false,
			menuItem: '',
			userMenus: [
				{
					icon: 'bubble_chart',
					title: '注销',
					link: '/logout'
				}
			]
		}
	},
	created() {
		auth.onChange = (loggedIn) => {
			this.loggedIn = loggedIn
		}
		this.$Progress.start()
		this.$router.beforeEach((to, from, next) => {
			if (to.meta.progress !== undefined) {
				let meta = to.meta.progress
				this.$Progress.parseMeta(meta)
			}
			this.$Progress.start()
			next()
		})
		this.$router.afterEach((to, from) => {
			if (to.name !== 'ErrorPage') {
				this.menuItem = to.name
			}
			this.$Progress.finish()
		})
	},
	computed: {
		...mapState('user', {
			user: 'user'
		}),
		menuItems() {
			// eslint-disable-next-line no-undef
			return Store.state.user.menuItems
		},
		auth() {
			return auth
		},
		activeMenuItem() {
			return this.menuItem
		}
	},
	methods: {
		clickMenu(item) {
			if (!item.children) {
				this.menuItem = item.title
				this.$router.push({
					name: item.link
				})
			}
		}
	},
	mounted() {
		this.$Progress.finish()
	}
}
</script>
<style lang="stylus">
  @import './stylus/main';
</style>
