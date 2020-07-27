<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card class="grey lighten-4 elevation-0">
        <v-card-title class="title">
          {{title}}
          <v-spacer></v-spacer>
          <v-btn fab small class="grey" @click.native="cancel()">
            <v-icon>cancel</v-icon>
          </v-btn>
          &nbsp;
          <v-btn fab small class="purple" @click.native="save()">
            <v-icon>save</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text v-if="loading !== true">
          <v-container fluid grid-list-sm>
            <v-layout row wrap>
              <v-flex md3 sm12>
                <img v-if="customer.avatar" class="profile" v-bind:src="customer.avatar"/>
              </v-flex>
              <v-flex md9 sm12>
                <v-container fluid grid-list-sm>
                  <v-layout row wrap>
                    <v-flex md4 sm12 xs12 class="mx-1 px-0">
                      <v-text-field hint="Last name is required" label="名字" name="firstName" value="Input text"
                                    v-model="customer.firstName"
                                    class="input-group--focused" required/>
                    </v-flex>
                    <v-flex md4 sm12 class="mx-1 px-0">
                      <v-text-field hint="Last name is required" label="姓氏" maxlength="10" name="lastName"
                                    value="Input text" v-model="customer.lastName"
                                    class="input-group--focused" required/>
                    </v-flex>
                    <v-flex md4 sm12 xs12 class="mx-1 px-0">
                      <v-text-field label="邮件" name="email" type="email" v-model="customer.email" value="Input text"
                                    v-bind:rules="rules.email"
                                    class="input-group--focused" required/>
                    </v-flex>
                    <v-flex md4 sm12 xs12 class="mx-1 px-0">
                      <v-text-field label="手机" name="mobile" type="text" v-model="customer.mobile"
                                    class="input-group--focused" required/>
                    </v-flex>
                    <v-flex md4 sm12 class="mx-1 px-0">
                      <v-text-field label="工作电话" name="workphone" type="text" v-model="customer.workphone"
                                    class="input-group--focused" required/>
                    </v-flex>
                    <v-flex md4 sm12 xs12 class="mx-1 px-0">
                      <v-text-field hint="Number between 0 and 9999" label="酬劳" name="rewards" type="number"
                                    v-bind:rules="rules.rewards"
                                    class="input-group--focused" required v-model="customer.rewards"/>
                    </v-flex>
                    <v-flex md6 sm12 class="mx-1 px-0">
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
							(this.customer.rewards < 0 ||
								this.customer.rewards > 9999)
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
						if (this.customer && !re.test(this.customer.email)) {
							return 'Email is invalid.'
						}
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
