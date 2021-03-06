<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card>
        <v-card-title>
            <span class="title">问卷列表 {{pagination? '('+pagination.totalItems+')': ''}}
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
          <v-btn @click.native="add" class="deep-orange darken-3" dark fab small v-if="userInfo.role === 4">
            <v-icon>add</v-icon>
          </v-btn>
        </v-card-title>
        <Table :headers="headers" :items="items" :pagination="pagination" :showRemove="userInfo.role === 4" @edit="edit" @remove="remove"/>
      </v-card>
    </v-flex>
    <search-panel :rightDrawer="rightDrawer" @cancelSearch="cancelSearch" @searchData="searchProducts">
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <v-text-field label="产品名称" light name="productName" v-model="searchVm.contains.productName"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs11 offset-xs1>
          <label class="heading text-sm-central" light>价格区间</label>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs8 offset-xs1>
          <v-slider class="text-xs-central" label="低价" light v-bind:max="100"
                    v-model="searchVm.between.unitPrice.former"/>
        </v-flex>
        <v-flex xs3>
          <v-text-field light type="number" v-model="searchVm.between.unitPrice.former"/>
        </v-flex>
      </v-layout>
      <v-layout row>
        <v-flex xs8 offset-xs1>
          <v-slider class="text-xs-central" label="高价" light v-bind:max="999"
                    v-model="searchVm.between.unitPrice.latter"/>
        </v-flex>
        <v-flex xs3>
          <v-text-field light type="number" v-model="searchVm.between.unitPrice.latter"/>
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
			dialogTitle: '删除提示',
			dialogText: '是否要删除这个问卷?',
			rightDrawer: false,
			right: true,
			search: '',
			headers: [
				{ text: '问卷标题', left: true, value: 'productName' },
				{ text: '截止时间', value: 'category.categoryName' },
				{ text: '创建时间', value: 'unitInStock' },
				{ text: '状态', value: 'unitPrice' }
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
			if (this.userInfo.role === 3) {
				this.$router.push({
					name: 'EditQuestionnaireScore',
					params: { id: item.id }
				})
			} else {
				this.$router.push({
					name: 'EditQuestionnaire',
					params: { id: item.id }
				})
			}
		},
		add() {
			if (this.userInfo.role === 3) {
				this.$router.push('/new-score-questionnaire')
			} else {
				this.$router.push('/new-questionnaire')
			}
		},
		remove(item) {
			this.productId = item.id
			this.dialog = true
		},
		onConfirm() {
			Store.dispatch('products/deleteProduct', this.productId).then(
				() => {
					Store.dispatch(
						'products/searchProducts',
						this.query,
						this.pagination
					)
					Store.dispatch('products/closeSnackBar', 2000)
				}
			)
			this.dialog = false
		},
		onCancel() {
			this.productId = ''
			this.dialog = false
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
		...mapState('user', {
			userInfo: 'user'
		}),
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
