<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card>
        <v-card-title>
            <span class="title">订单 {{pagination? '('+pagination.totalItems+')': ''}}
              <v-text-field append-icon="search" hide-details label="快速查询" single-line
                            v-model="quickSearch"/>
            </span>
          <v-spacer/>
          <v-btn class="blue-grey" fab small dark @click.native.stop="rightDrawer = !rightDrawer">
            <v-icon>search</v-icon>
          </v-btn>
          <v-btn class="brown lighten-1" fab small dark @click.native="reloadData()">
            <v-icon>refresh</v-icon>
          </v-btn>
          <!--v-btn class="teal darken-2" fab small dark @click.native="print()">
            <v-icon>print</v-icon>
          </v-btn-->
          <v-btn class="deep-orange darken-3" fab small dark @click.native="add">
            <v-icon>add</v-icon>
          </v-btn>
        </v-card-title>
        <Table v-if="loading===false" :headers="headers" :items="items" :pagination="pagination" @edit="edit"
               @remove="remove"/>
      </v-card>
    </v-flex>
    <search-panel :rightDrawer="rightDrawer" @cancelSearch="cancelSearch" @searchData="searchOrders">
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <v-text-field label="订单号" light name="reference" v-model="searchVm.contains.reference"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <v-text-field label="客户" light name="customer" v-model="searchVm.contains.customer"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <label class="heading text-sm-central" light>金额区间</label>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs8 offset-xs1>
          <v-slider class="text-xs-central" label="低价" light v-bind:max="100"
                    v-model="searchVm.between.amount.former"/>
        </v-flex>
        <v-flex xs3>
          <v-text-field light type="number" v-model="searchVm.between.amount.former"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs8 offset-xs1>
          <v-slider class="text-xs-central" label="高价" light v-bind:max="9999"
                    v-model="searchVm.between.amount.latter"/>
        </v-flex>
        <v-flex xs3>
          <v-text-field light type="number" v-model="searchVm.between.amount.latter"/>
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
			dialogTitle: 'Order Delete Dialog',
			dialogText: 'Do you want to delete this order?',
			rightDrawer: false,
			right: true,
			search: '',
			headers: [
				{
					text: '订单号',
					left: true,
					value: 'reference'
				},
				{ text: '订单项', value: 'quantity' },
				{ text: '金额', value: 'amount' },
				{ text: '客户', value: 'customer' },
				{ text: '订单日期', value: 'orderDate' },
				{ text: '购买日期', value: 'shippedDate' }
			],
			searchVm: {
				contains: {
					reference: '',
					customer: ''
				},
				between: {
					amount: { former: 0, latter: 0 }
				}
			},
			orderId: '',
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
			this.$router.push({ name: 'Order', params: { id: item.id } })
		},
		add() {
			this.$router.push('NewOrder')
		},
		remove(item) {
			this.orderId = item.id
			this.dialog = true
		},
		onConfirm() {
			Store.dispatch('orders/deleteOrder', this.orderId).then(() => {
				Store.dispatch('orders/searchOrders', this.query, this.pagination)
				Store.dispatch('orders/closeSnackBar', 2000)
			})
			this.dialog = false
		},
		onCancel() {
			this.orderId = ''
			this.dialog = false
		},
		searchOrders() {
			this.rightDrawer = !this.rightDrawer
			this.appUtil.buildSearchFilters(this.searchVm)
			this.query = this.appUtil.buildJsonServerQuery(this.searchVm)
			this.quickSearch = ''
			Store.dispatch('orders/searchOrders', this.query, this.pagination)
		},
		reloadData() {
			this.query = ''
			Store.dispatch('orders/getAllOrders')
		},
		cancelSearch() {
			this.rightDrawer = false
		},
		closeSnackbar() {
			Store.commit('orders/setSnackbar', { snackbar: false })
			Store.commit('orders/setNotice', { notice: '' })
		},
		quickSearchOrders: debounce(function() {
			this.quickSearchFilter &&
				Store.dispatch('orders/quickSearch', {
					headers: this.headers,
					qsFilter: this.quickSearchFilter.toLowerCase(),
					pagination: this.pagination
				})
		}, 300)
	},
	computed: {
		...mapState('orders', {
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
				this.quickSearchFilter && this.quickSearchOrders()
			}
		}
	},
	created() {
		Store.dispatch('orders/getAllOrders')
	},
	mounted() {
		this.$nextTick(() => {
			console.log(this.headers)
		})
	}
}
</script>
