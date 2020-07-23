<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card class="grey lighten-4 elevation-0">
        <v-card-title class="title">
          {{title}}
          <v-spacer/>
          <!--<v-text-field append-icon="search" label="Search" single-line hide-details v-model="search"></v-text-field>-->
          <v-btn fab small class="grey" @click.native="cancel()">
            <v-icon>cancel</v-icon>
          </v-btn>
          &nbsp;
          <v-btn fab small class="purple" @click.native="save()">
            <v-icon>save</v-icon>
          </v-btn>
          &nbsp;
          <v-btn fab small class="blue" @click.native="addProduct()">
            <v-icon>add</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <v-container fluid grid-list-md>
            <v-layout row wrap>
              <v-flex md4 xs12>
                <v-text-field hint="订单编号不能为空" label="订单编号" name="reference" value="Input text"
                              v-model="order.reference"
                              class="input-group--focused" required/>
              </v-flex>
              <v-flex md4 xs12>
                <v-text-field hint="价格不能为空" label="价格" name="Amount" prefix="RMB ￥" value="Input text"
                              v-model="order.amount"
                              class="input-group--focused" required/>
              </v-flex>
              <v-flex md4 xs12>
                <v-text-field hint="Number between 1 to 100" label="产品数量" name="quantity"
                              v-model="order.products.length" class="input-group--focused"
                              required/>
              </v-flex>
              <v-flex md4 xs12>
                <v-select label="订购客户" required v-bind:items="customers" v-model="order.customerId"/>
              </v-flex>
              <v-flex md4 xs12>
                <v-menu lazy :close-on-content-click="false" v-model="orderDateMenu" transition="v-scale-transition"
                        offset-y full-width
                        :nudge-left="40" max-width="290px">
                  <v-text-field label="订购日期" prepend-icon="event" slot="activator" v-model="order.orderDate"
                                readonly/>
                  <v-date-picker v-model="order.orderDate" no-title scrollable>
                  </v-date-picker>
                </v-menu>
              </v-flex>
              <v-flex md4 xs12>
                <v-menu lazy :close-on-content-click="false" v-model="shippedDateMenu" transition="v-scale-transition"
                        offset-y full-width
                        :nudge-left="40" max-width="290px">
                  <v-text-field label="购买日期" prepend-icon="event" slot="activator" v-model="order.shippedDate"
                                readonly/>
                  <v-date-picker v-model="order.shippedDate" no-title scrollable>
                  </v-date-picker>
                </v-menu>
              </v-flex>
              <v-flex md4 xs12>
                <v-text-field hint="Address is required" label="地址" name="address" value="Input text"
                              v-model="order.shipAddress.address"
                              class="input-group--focused" required/>
              </v-flex>
              <v-flex md4 xs12>
                <v-text-field hint="City is required" label="城市" name="city" value="Input text"
                              v-model="order.shipAddress.city" class="input-group--focused"
                              required/>
              </v-flex>
              <v-flex md4 xs12>
                <v-text-field hint="邮编不能为空" label="邮编" name="zipcode" value="Input text"
                              v-model="order.shipAddress.zipcode"
                              class="input-group--focused" required/>
              </v-flex>
              <v-flex md4 xs12>
                <v-text-field hint="Country is required" label="国家" name="country" value="Input text"
                              v-model="order.shipAddress.country"
                              class="input-group--focused" required/>
              </v-flex>
              <v-flex xs12 v-if="order.products && order.products.length>0">

                <v-list class="transparent elevation-0" two-line>
                  <v-list-tile avatar ripple v-for="(item, index) in order.products"
                               v-if="item !== null && item !== undefined" v-bind:key="index"
                               class="grey lighten-2 mt-2 mb-2 ">
                    <v-list-tile-content dark>
                      <v-list-tile-title class="heading blue--text">{{ item.productName }}

                      </v-list-tile-title>
                      <v-list-tile-sub-title class="grey--text text--darken-4">RMB ￥{{ item.unitPrice }}
                      </v-list-tile-sub-title>
                      <!--<v-list-tile-sub-title>{{ item.unitInStock }}
                        </v-list-tile-sub-title>-->
                    </v-list-tile-content>
                    <v-list-tile-action>
                      <v-btn fab small class="navy" @click.native="remove(item)">
                        <v-icon v-bind:class="[item.active ? 'teal--text': 'grey--text']">delete</v-icon>
                      </v-btn>
                    </v-list-tile-action>
                  </v-list-tile>
                </v-list>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>
      </v-card>
    </v-flex>

    <v-layout row justify-center>
      <v-dialog v-model="addProductModal" width="700" persistent>
        <v-card>
          <v-card-title>{{modalTitle}}</v-card-title>
          <v-card-text>{{modalText}}</v-card-text>
          <v-card-text>
            <v-container fluid grid-list-md>
              <v-layout row wrap>
                <v-flex md6 xs12>
                  <v-select label="产品分类" required v-bind:items="categories" v-model="categoryId"
                            v-on:change="getProductsByCategory"/>
                </v-flex>
                <v-flex md6 xs12>
                  <v-select label="产品列表" required v-bind:items="products" v-model="productId"/>
                </v-flex>
              </v-layout>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-btn @click.native="saveProduct" class="green--text darken-1" flat="flat">确定</v-btn>
            <v-btn @click.native="cancelAddProduct" class="orange--text darken-1" flat="flat">取消</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-layout>
    <confirm-dialog :dialog="dialog" :dialogTitle="dialogTitle" :dialogText="dialogText" @onConfirm="onConfirm"
                    @onCancel="onCancel"/>
    <v-snackbar v-if="loading===false" :right="true" :timeout="timeout" :color="mode" v-model="snackbar">
      {{ notice }}
      <v-btn dark flat @click.native="closeSnackbar">Close</v-btn>
    </v-snackbar>
  </v-container>
</template>
<script>
/* global Store */
import ConfirmDialog from '@/components/ConfirmDialog.vue'
import { mapState } from 'vuex'

export default {
	components: {
		ConfirmDialog
	},
	data() {
		return {
			categoryId: null,
			modalTitle: '添加产品',
			modalText: '从列表中选出需要的产品分类和产品',
			addProductModal: false,
			dialog: false,
			dialogTitle: '删除产品',
			dialogText: '确定要删除这个产品吗?',
			orderDateMenu: false,
			shippedDateMenu: false,
			errors: [],
			title: '',
			productId: null,
			snackbarStatus: false,
			timeout: 3000,
			color: '',
			selectedProduct: null
		}
	},
	computed: {
		...mapState('orders', {
			order: 'order',
			customers: 'customers',
			categories: 'categories',
			products: 'products',
			loading: 'loading',
			mode: 'mode',
			snackbar: 'snackbar',
			notice: 'notice'
		})
	},
	methods: {
		save() {
			const order = { ...this.order }
			delete order.customer
			Store.dispatch('orders/saveOrder', order).then(() => {
				Store.dispatch('orders/closeSnackBar', 2000)
			})
		},
		selectCustomer(item) {
			this.order.customerId = item.value
		},
		getOrderById() {
			Store.dispatch('orders/getOrderById', this.$route.params.id)
		},
		getCustomers() {
			Store.dispatch('orders/getCustomers')
		},
		cancel() {
			this.$router.push({ name: 'Orders' })
		},
		remove(item) {
			this.selectedProduct = item
			this.dialog = true
		},
		onConfirm() {
			Store.dispatch(
				'orders/deleteProduct',
				Object.assign({}, this.selectedProduct)
			)
			this.selectedProduct = null
			this.dialog = false
		},
		onCancel() {
			this.selectedProduct = null
			this.dialog = false
		},
		addProduct() {
			this.addProductModal = true
		},
		saveProduct() {
			Store.dispatch('orders/addProductToOrder', this.productId)
			this.productId = null
			this.addProductModal = false
		},
		cancelAddProduct() {
			this.addProductModal = false
		},
		getCategories() {
			Store.dispatch('orders/getCategories')
		},
		getProductsByCategory() {
			this.categoryId &&
				Store.dispatch('orders/getProductsByCategory', this.categoryId)
		},
		closeSnackbar() {
			Store.commit('orders/setSnackbar', { snackbar: false })
			Store.commit('orders/setNotice', { notice: '' })
		}
	},
	created() {
		this.getCustomers()
		this.getCategories()
		this.getOrderById()
	},
	mounted() {
		this.title = this.$route.params.id
			? this.$constants.OrderEditName
			: this.$constants.OrderAddName
	}
}
</script>
