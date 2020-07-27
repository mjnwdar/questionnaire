<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card>
        <v-card-title>
            <span class="title">客户 {{pagination? '('+pagination.totalItems+')': ''}}
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
        </v-card-title>
        <Table :headers="headers" :items="items" :pagination="pagination" :showOp="false" v-if="loading===false"/>
      </v-card>
    </v-flex>
    <search-panel :rightDrawer="rightDrawer" @cancelSearch="cancelSearch" @searchData="searchProducts">
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <v-text-field label="客户手机号" light name="productName" v-model="searchVm.contains.productName"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <label class="heading text-sm-central" light>客户姓名</label>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex offset-xs2 xs10>
          <v-text-field label="客户姓氏" light name="productName" v-model="searchVm.contains.productName"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex offset-xs2 xs10>
          <v-text-field label="客户名字" light name="productName" v-model="searchVm.contains.productName"/>
        </v-flex>
      </v-layout>
    </search-panel>
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
			rightDrawer: false,
			right: true,
			search: '',
			headers: [
				{ text: '客户姓氏', left: true, value: 'productName' },
				{ text: '客户名字', value: 'category.categoryName' },
				{ text: '客户姓名全拼', value: 'unitPrice' },
				{ text: '客户手机号', value: 'unitInStock' },
				{ text: '客户工作电话', value: 'unitInStock' },
				{ text: '客户邮箱', value: 'unitInStock' },
				{ text: '备注', value: 'unitInStock' }
			],
			searchVm: {
				contains: {
					productName: '',
					category: ''
				},
				between: {
					unitPrice: {
						former: 0,
						latter: 0
					}
				}
			},
			productId: '',
			query: '',
			snackbarStatus: false,
			timeout: 2000,
			color: '',
			quickSearchFilter: ''
		}
	},
	methods: {
		edit(item) {
			this.$router.push({
				name: 'Product',
				params: { id: item.id }
			})
		},
		add() {
			this.$router.push('NewProduct')
		},
		remove(item) {
			this.productId = item.id
			this.dialog = true
		},
		searchProducts() {
			this.rightDrawer = !this.rightDrawer
			this.appUtil.buildSearchFilters(this.searchVm)
			this.query = this.appUtil.buildJsonServerQuery(this.searchVm)
			this.quickSearch = ''
			Store.dispatch(
				'products/searchProducts',
				this.query,
				this.pagination
			)
		},
		reloadData() {
			this['test']()
			this.query = ''
			this.quickSearch = ''
			Store.dispatch('products/getAllProducts')
		},
		cancelSearch() {
			this.rightDrawer = false
		},
		closeSnackbar() {
			Store.commit('products/setSnackbar', { snackbar: false })
			Store.commit('products/setNotice', { notice: '' })
		},
		quickSearchProducts: debounce(function() {
			this.quickSearchFilter &&
				Store.dispatch('products/quickSearch', {
					headers: this.headers,
					qsFilter: this.quickSearchFilter.toLowerCase(),
					pagination: this.pagination
				})
		}, 300)
	},
	computed: {
		...mapState('products', {
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
				this.quickSearchFilter && this.quickSearchProducts()
			}
		}
	},
	created() {
		Store.dispatch('products/getAllProducts')
	}
}
</script>
