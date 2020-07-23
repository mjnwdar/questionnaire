<template>
  <v-container fluid>
    <v-flex offset-xs3 xs6>
      <v-card class="grey lighten-4 elevation-0">
        <v-card-title class="title">
          客户信息录入
          <v-spacer></v-spacer>
          <v-btn @click.native="login" class="purple">
            <v-icon>用户登录</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <v-container class="justify-center" fluid grid-list-md>
            <v-layout row>
              <v-flex xs12>
                <v-text-field class="input-group--focused" hint="客户姓氏不能为空" label="客户姓氏" name="lastName" required
                              v-model="user.lastName"/>
              </v-flex>
            </v-layout>
            <v-layout row>
              <v-flex xs12>
                <v-text-field class="input-group--focused" hint="客户名字不能为空" label="客户名字" name="firstName" required
                              v-model="user.firstName"/>
              </v-flex>
            </v-layout>
            <v-layout row>
              <v-flex xs12>
                <v-text-field class="input-group--focused" hint="客户姓名全拼不能为空" label="客户姓名全拼" name="fullName" required
                              v-model="user.fullName"/>
              </v-flex>
            </v-layout>
            <v-layout row>
              <v-flex xs12>
                <v-text-field class="input-group--focused" hint="客户姓氏不能为空" label="客户手机" name="phoneNumber" required
                              v-model="user.phoneNumber"/>
              </v-flex>
            </v-layout>
            <v-layout row>
              <v-flex xs12>
                <v-text-field class="input-group--focused" label="客户工作电话" name="officeNumber"
                              v-model="user.officeNumber"/>
              </v-flex>
            </v-layout>
            <v-layout row>
              <v-flex xs12>
                <v-text-field class="input-group--focused" label="客户邮箱" name="email"
                              v-model="user.email"/>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-btn @click.native="showConfirm" class="green--text darken-1" flat="flat">提交</v-btn>
        </v-card-actions>
      </v-card>
    </v-flex>
    <confirm-dialog :dialog="dialog" @onCancel="onCancel" @onConfirm="onConfirm" dialogText="是否要提交问卷？" dialogTitle="答题提示"/>
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
			dialog: false,
			user: {
				lastName: '',
				firstName: '',
				fullName: '',
				phoneNumber: '',
				email: '',
				officeNumber: '',
				remark: ''
			}
		}
	},
	methods: {
		login() {
			this.$router.push('/login')
		},
		showConfirm() {
			this.dialog = true
		},
		onConfirm() {
			this.$router.push('/answer')
			// Store.dispatch('personal/saveInfo', Object.assign({}, this.user)).then(() => {
			// });
			this.dialog = false
		},
		onCancel() {
			this.dialog = false
		}
	}
}
</script>
