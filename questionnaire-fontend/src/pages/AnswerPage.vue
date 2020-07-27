<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card class="grey lighten-4 elevation-0">
        <v-card-title class="headline" primary-title>
          <div class="header-title">{{title}}</div>
          <v-btn @click.native="back" small>
            返回
          </v-btn>
        </v-card-title>
        <v-card-text>
          <v-container fluid grid-list-md>
            <v-flex class="bg" offset-xs1 xs10>
              <div class="qu-content">
                <section class="qu-item" v-for="(item, index) in questions">
                  <h3>{{ `Q${index + 1}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${item.topic}` }}
                    <span v-if="item.isMandatory"> *</span>
                  </h3>
                  <textarea :required="item.isMandatory"
                            class=""
                            cols="80"
                            rows="8"
                            v-if="item.type === 'textarea'"
                            v-model="item.answer">
					          </textarea>
                  <ul class="options-list" v-else>
                    <li v-for="(option, optIndex) in item.options">
                      <label>
                        <input :name="index + 1"
                               :type="item.type"
                               @change="item.answer = optIndex"
                               v-if="item.type === 'radio'">
                        <input :name="index + 1"
                               :type="item.type"
                               @change="checkboxAnswer($event, optIndex, item.answer)"
                               v-else>
                        <span>{{ option }}</span>
                      </label>
                    </li>
                  </ul>
                </section>
              </div>
            </v-flex>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <div class="btn-nav">
            <v-btn @click.native="showConfirm" class="red--text darken-1" flat large>提交问卷</v-btn>
          </div>
        </v-card-actions>
      </v-card>
    </v-flex>
    <confirm-dialog :dialog="dialog" @onCancel="onCancel" @onConfirm="onConfirm" dialogText="是否要提交问卷？" dialogTitle="答题提示"/>
  </v-container>
</template>
<script>
/* global Store */
import ConfirmDialog from '@/components/ConfirmDialog.vue'

export default {
	components: {
		ConfirmDialog
	},
	data() {
		return {
			dialog: false,
			title: '企业调查问卷',
			questions: [
				{
					topic: '你的年龄？',
					type: 'radio',
					options: [
						'A.18-30岁',
						'B.31-50岁',
						'C.31-50岁',
						'D.51-65岁',
						'E.高于65岁'
					]
				},
				{
					topic: '你的年收入？',
					type: 'radio',
					options: [
						'A.50万元以下',
						'B.50―100万元',
						'C.100―500万元',
						'D.500―1000万元',
						'E.1000万元以上'
					]
				},
				{
					topic: '你过去接触过哪些投资？',
					type: 'radio',
					options: ['A.20%', 'B.30%', 'C.50%', 'D.70%']
				},
				{
					topic: '你会将总资金的多少用于投资？',
					type: 'radio',
					options: ['A.民营', 'B.私企', 'C.联合', 'D.非盈利']
				},
				{
					topic: '你过去投资理财方面的风险承受能力是多少？',
					type: 'radio',
					options: ['A.1000W', 'B.3000W', 'C.5000W', 'D.7000W以上']
				},
				{
					topic: '你的期望回报值是多少？',
					type: 'radio',
					options: ['A.5%', 'B.9%', 'C.10%', 'D.无上限']
				},
				{
					topic: '你从哪个渠道了解我们公司？',
					type: 'radio',
					options: ['A.互联网', 'B.微信', 'C.别人推荐']
				},
				{
					topic: '对于我们公司的哪些产品你比较感兴趣？',
					type: 'checkbox',
					options: ['A.公司文化', 'B.公司产品']
				},
				{
					topic: '理财顾问主动联系你的频率多少你能够接受？',
					type: 'checkbox',
					options: [
						'A.每天一次',
						'B.三天一次',
						'C.一周一次',
						'D.一月一次'
					]
				},
				{
					topic:
						'如果我公司现在向你推荐一款好的产品，你是否会马上投资？',
					type: 'checkbox',
					options: ['A.会', 'B.不会']
				},
				{
					topic: '你对公司有什么意见？',
					type: 'textarea',
					isMandatory: true
				}
			],
			questionModel: {
				typeName: '公司问答',
				body: '是谁呢'
			}
		}
	},
	methods: {
		back() {
			this.$router.go(-1)
		},
		showConfirm() {
			this.dialog = true
		},
		onConfirm() {
			Store.dispatch(
				'personal/saveInfo',
				Object.assign({}, this.user)
			).then((res) => {
				this.$router.push('/')
			})
			this.dialog = false
		},
		onCancel() {
			this.dialog = false
		}
	}
}
</script>
<style scoped>
.bg {
	background-color: white;
}

body {
	margin: 0px;
	padding: 0px;
}

textarea {
	background-color: rgba(0, 0, 0, 0.05);
}

ul {
	display: block;
	list-style-type: disc;
	margin-block-start: 1em;
	margin-block-end: 1em;
	margin-inline-start: 0px;
	margin-inline-end: 0px;
	padding-inline-start: 40px;
}

ol,
ul {
	list-style: none;
}

.header-title {
	width: 93%;
	line-height: 50px;
	text-align: center;
	float: left;
}

.btn-nav {
	width: 100%;
	line-height: 50px;
	text-align: right;
}

.qu-content {
	padding: 2rem;
	border-top: 2px solid rgb(187, 187, 187);
	border-bottom: 2px solid rgb(187, 187, 187);
}

.qu-content .qu-item {
	position: relative;
	margin: 1rem 0px;
	padding: 1rem 2rem;
}

.qu-content .qu-item:hover {
	background-color: rgba(0, 0, 0, 0.05);
}

.qu-content .qu-item h3 {
	height: 2rem;
	font-size: 1.4rem;
	font-weight: 600;
	line-height: 2rem;
}
</style>
