<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card class="grey lighten-4 elevation-0">
        <v-card-title class="title">
          {{title}}
          <v-spacer/>
          <v-btn @click.native="cancel()" class="grey" fab small>
            <v-icon>cancel</v-icon>
          </v-btn>
          &nbsp;
          <v-btn @click.native="save()" class="purple" fab small>
            <v-icon>save</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text v-if="loading !== true">
          <v-container fluid grid-list-sm>
            <v-layout row wrap>
              <v-flex md3 sm12>
                <img class="profile" v-bind:src="customer.avatar" v-if="customer.avatar"/>
              </v-flex>
              <v-flex md9 sm12>
                <v-container fluid grid-list-sm>
                  <v-layout row wrap>
                    <v-flex class="mx-1 px-0" md4 sm12 xs12>
                      <v-text-field class="input-group--focused" hint="Last name is required" label="名字" name="firstName"
                                    required
                                    v-model="customer.firstName" value="Input text"/>
                    </v-flex>
                    <v-flex class="mx-1 px-0" md4 sm12>
                      <v-text-field class="input-group--focused" hint="Last name is required" label="姓氏" maxlength="10"
                                    name="lastName" required
                                    v-model="customer.lastName" value="Input text"/>
                    </v-flex>
                    <v-flex class="mx-1 px-0" md4 sm12 xs12>
                      <v-text-field class="input-group--focused" label="邮件" name="email" required type="email"
                                    v-bind:rules="rules.email"
                                    v-model="customer.email" value="Input text"/>
                    </v-flex>
                    <v-flex class="mx-1 px-0" md4 sm12 xs12>
                      <v-text-field class="input-group--focused" label="手机" name="mobile" required
                                    type="text" v-model="customer.mobile"/>
                    </v-flex>
                    <v-flex class="mx-1 px-0" md4 sm12>
                      <v-text-field class="input-group--focused" label="工作电话" name="workphone" required
                                    type="text" v-model="customer.workphone"/>
                    </v-flex>
                    <v-flex class="mx-1 px-0" md4 sm12 xs12>
                      <v-text-field class="input-group--focused" hint="Number between 0 and 9999" label="酬劳" name="rewards"
                                    required
                                    type="number" v-bind:rules="rules.rewards" v-model="customer.rewards"/>
                    </v-flex>
                    <v-flex class="mx-1 px-0" md6 sm12>
                      <v-switch label="是否是会员" light v-model="customer.membership"/>
                    </v-flex>
                  </v-layout>
                </v-container>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>
      </v-card>
    </v-flex>
  </v-container>
</template>
<script>
/* global Store */
import { mapState } from 'vuex'

export default {
	data() {
		return {
			title: '',
			rules: {
				rewards: [
					() => {
						if (
							this.customer &&
							(this.customer.rewards < 0 || this.customer.rewards > 9999)
						) {
							return 'Reward is required. It must be bewteen 0 and 9999'
						}
						return true
					}
				],
				email: [
					() => {
						/* eslint-disable no-useless-escape */
						const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
						if (this.customer && !re.test(this.customer.email))
							return 'Email is invalid.'
						return true
					}
				]
			}
		}
	},
	computed: {
		...mapState('customers', {
			orders: 'orders',
			customer: 'customer',
			loading: 'loading',
			mode: 'mode',
			snackbar: 'snackbar',
			notice: 'notice'
		})
	},
	methods: {
		save() {
			const customer = { ...this.customer }
			Store.dispatch('customers/saveCustomer', customer).then(() => {
				Store.dispatch('customers/closeSnackBar', 2000)
			})
		},
		cancel() {
			this.$router.push({ name: 'Customers' })
		},
		closeSnackbar() {
			Store.commit('customers/setSnackbar', { snackbar: false })
			Store.commit('customers/setNotice', { notice: '' })
		}
	},
	created() {
		Store.dispatch('customers/getCustomerById', this.$route.params.id)
	},
	mounted() {
		if (this.$route.params.id) {
			this.title = '编辑用户'
		} else this.title = '新建用户'
	}
}
</script>
<style>
.profile {
	max-width: 160px;
}
</style>
