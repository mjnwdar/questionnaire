<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card>
        <v-card-title>
            <span class="title">用户 {{pagination? '('+pagination.totalItems+')': ''}}
              <v-text-field append-icon="search" hide-details label="快速搜索" single-line
                            v-model="quickSearch"/>
            </span>
          <v-spacer/>
          <v-btn class="blue-grey" fab small dark @click.native.stop="rightDrawer = !rightDrawer">
            <v-icon>search</v-icon>
          </v-btn>
          <v-btn class="brown lighten-1" fab small dark @click.native="reloadData()">
            <v-icon>refresh</v-icon>
          </v-btn>
          <v-btn class="deep-orange darken-3" fab small dark @click.native="add">
            <v-icon>add</v-icon>
          </v-btn>
        </v-card-title>
        <Table v-if="loading===false" :headers="headers" :items="items" :pagination="pagination" @edit="edit"
               :showRemove="true" @remove="remove"/>

      </v-card>
    </v-flex>
    <search-panel :rightDrawer="rightDrawer" @cancelSearch="cancelSearch" @searchData="searchCustomers">
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <v-text-field label="名字" light name="input-1-3" v-model="searchVm.contains.firstName"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <v-text-field label="姓氏" light name="input-1-3" v-model="searchVm.contains.lastName"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <v-subheader class="text-sm-central" light>酬劳区间</v-subheader>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs8 offset-xs1>
          <v-slider label="下区间" light v-bind:max="50" v-model="searchVm.between.rewards.former"/>
        </v-flex>
        <v-flex xs3>
          <v-text-field light type="number" v-model="searchVm.between.rewards.former"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs8 offset-xs1>
          <v-slider label="上区间" light v-bind:max="100" v-model="searchVm.between.rewards.latter"/>
        </v-flex>
        <v-flex xs3>
          <v-text-field light type="number" v-model="searchVm.between.rewards.latter"/>
        </v-flex>
      </v-layout>
    </search-panel>

    <confirm-dialog :dialog="dialog" :dialogTitle="dialogTitle" :dialogText="dialogText" @onConfirm="onConfirm"
                    @onCancel="onCancel"/>
    <v-snackbar v-if="loading===false" :right="true" :timeout="timeout" :color="mode" v-model="snackbar">
      {{ notice }}
      <v-btn dark flat @click.native="closeSnackbar">Close</v-btn>
    </v-snackbar>
  </v-container>
</template>
<script>
/* globals Store */
import Table from '@/components/Table.vue'
import SearchPanel from '@/components/SearchPanel.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'
import { mapState } from 'vuex'
import { debounce } from 'lodash'

export default {
	components: {
		Table,
		SearchPanel,
		ConfirmDialog
	},
	data() {
		return {
			dialog: false,
			dialogTitle: '删除客户',
			dialogText: '你想删除这个客户吗?',
			rightDrawer: false,
			right: true,
			search: '',
			headers: [
				{ text: '名字', left: true, value: 'firstName' },
				{ text: '姓氏', value: 'lastName' },
				{ text: '邮件', value: 'email' },
				{ text: '电话', value: 'mobile' },
				{ text: '角色', value: 'rewards' },
				{ text: '备注', value: 'orderAmount' }
			],
			searchVm: {
				contains: {
					firstName: '',
					lastName: ''
				},
				between: {
					rewards: { former: 0, latter: 0 }
				}
			},
			customerId: '',
			query: '',
			snackbarStatus: false,
			timeout: 2000,
			color: '',
			quickSearchFilter: ''
		}
	},
	methods: {
		print() {
			window.print()
		},
		edit(item) {
			this.$router.push({ name: 'EditCustomer', params: { id: item.id } })
		},
		add() {
			this.$router.push({ name: 'NewCustomer' })
		},
		remove(item) {
			this.orderId = item.id
			this.dialog = true
		},
		onConfirm() {
			Store.dispatch('customers/deleteCustomer', this.orderId).then(() => {
				Store.dispatch('customers/searchCustomers', this.query, this.pagination)
				Store.dispatch('customers/closeSnackBar', 2000)
			})
			this.dialog = false
		},
		onCancel() {
			this.orderId = ''
			this.dialog = false
		},
		searchCustomers() {
			this.rightDrawer = !this.rightDrawer
			this.appUtil.buildSearchFilters(this.searchVm)
			this.query = this.appUtil.buildJsonServerQuery(this.searchVm)
			this.quickSearch = ''
			Store.dispatch('customers/searchCustomers', this.query, this.pagination)
		},
		clearSearchFilters() {
			this.rightDrawer = !this.rightDrawer
			this.appUtil.clearSearchFilters(this.searchVm)
			this.api.getData('customers/').then(
				(res) => {
					this.items = res.data
					this.items.forEach((item) => {
						if (item.orders && item.orders.length) {
							item.orderAmount = item.orders.length
						} else {
							item.orderAmount = 0
						}
					})
					this.pagination.totalItems = this.items.length
				},
				(err) => {
					console.log(err)
				}
			)
		},
		reloadData() {
			this.query = ''
			Store.dispatch('customers/getAllCustomers')
		},
		cancelSearch() {
			this.rightDrawer = false
		},
		closeSnackbar() {
			Store.commit('customers/setSnackbar', { snackbar: false })
			Store.commit('customers/setNotice', { notice: '' })
		},
		quickSearchCustomers: debounce(function() {
			this.quickSearchFilter &&
				Store.dispatch('customers/quickSearch', {
					headers: this.headers,
					qsFilter: this.quickSearchFilter.toLowerCase(),
					pagination: this.pagination
				})
		}, 300)
	},
	computed: {
		...mapState('customers', {
			items: 'items',
			pagination: 'pagination',
			loading: 'loading',
			mode: 'mode',
			snackbar: 'snackbar',
			notice: 'notice'
		}),
		quickSearch: {
			get: function() {
				return this.quickSearchFilter
			},
			set: function(val) {
				this.quickSearchFilter = val
				this.quickSearchFilter && this.quickSearchCustomers()
			}
		}
	},
	created() {
		Store.dispatch('customers/getAllCustomers')
	}
}
</script>
