<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card class="grey lighten-4 elevation-0">
        <v-card-title class="title">
          {{formTitle}}
          <v-spacer/>
          <v-btn @click.native="backBtn" class="grey" fab small>
            <v-icon>cancel</v-icon>
          </v-btn>
          <v-btn @click="iterator = saveBtn(); iterator.next()" class="purple" fab small>
            <v-icon>save</v-icon>
          </v-btn>
          <v-btn @click="iterator = releaseBtn(); iterator.next()" class="purple" fab small>
            <v-icon>publish</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <v-container fluid grid-list-md>
            <v-layout row wrap class="px-10">
              <v-flex xs12>
                <v-menu :close-on-content-click="false" :nudge-left="40" full-width lazy
                        max-width="290px" offset-y
                        transition="v-scale-transition" v-model="shippedDateMenu">
                  <v-text-field label="问卷截止日期" prepend-icon="event" readonly slot="activator" v-model="shippedDate"/>
                  <v-date-picker no-title scrollable v-model="shippedDate">
                  </v-date-picker>
                </v-menu>
              </v-flex>
              <v-flex xs12>
                <div class="qu-wrap">
                  <header>
                    <p @click="titleEditing = true" v-if="!titleEditing">{{ tempTitle }}</p>
                    <input maxlength="25"
                           style="display: inline-block"
                           v-model="title"
                           @blur="titleEditing = false"
                           @focus="_title = title"
                           @keyup.enter="titleEditing = false"
                           @keyup.esc="cancelTitleEdit()"
                           type="text"
                           v-focus
                           v-show="titleEditing"/>
                  </header>
                  <div class="qu-content">
                    <section class="qu-item" v-for="(item, index) in questions">
                      <h3 :key="`Q${index + 1}`" @click="curIndex = index; curOptIndex=''; topicEditing = true">
                        <span class="qu-num">{{`Q${index + 1}`}}</span>
                        <input @blur="curIndex=''; topicEditing = false"
                               style="display: inline-block"
                               v-if="curIndex === index && topicEditing"
                               @keyup.enter="doneTopicEdit(index)"
                               @keyup.esc="cancelTopicEdit()"
                               type="text"
                               v-focus
                               v-model="item.topic"/>
                        <span class="qu-topic" v-else>{{ item.topic }}</span>
                        <span v-if="item.isMandatory"> *</span>
                      </h3>
                      <div v-if="item.type === 'textarea'">
                        <textarea cols="80" rows="8"/>
                        <label id="require-check">
                          <input type="checkbox" v-model:checked="item.isMandatory"/>此题是否必填
                        </label>
                      </div>
                      <ul class="options-list" v-else>
                        <li v-for="(option, optIndex) in item.options">
                          <input :value="option"
                                 @blur="curIndex=''; optionEditing=false"
                                 @keyup.enter="doneOptionEdit(index, optIndex, $event)"
                                 style="display: inline-block"
                                 type="text"
                                 v-focus
                                 v-if="curIndex === index && curOptIndex === optIndex && optionEditing"/>
                          <span @click="curIndex=index; curOptIndex=optIndex; topicEditing = false; optionEditing = true" v-else>{{ option }}</span>
                          <ul>
                            <li @click="moveUp(optIndex, item.options)" v-if="optIndex !== 0">上移</li>
                            <li @click="moveDown(optIndex, item.options)" v-if="optIndex !== item.options.length - 1">下移</li>
                            <li @click="addOption(item.options)" v-else>添加</li>
                            <li @click="delOption(optIndex, item.options)">删除</li>
                          </ul>
                        </li>
                      </ul>
                      <ul class="operat-list">
                        <li @click="moveUp(index)" v-if="index !== 0">上移</li>
                        <li @click="moveDown(index)" v-if="index !== questions.length - 1">下移</li>
                        <li @click="reuse(index)">复用</li>
                        <li @click="delQuestion(index)">删除</li>
                      </ul>
                    </section>
                    <div class="add-box">
                      <p :style="{height: isAdding ? '2rem' : 0 }" class="qu-type">
                        <span @click="addType('radio')">单选题</span>
                        <span @click="addType('checkbox')">多选题</span>
                        <span @click="addType('textarea')">文本题</span>
                      </p>
                      <v-btn @click.native="isAdding = !isAdding" class="green--text darken-1" flat="flat">+ 添加问题</v-btn>
                    </div>
                  </div>
                </div>
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
export default {
	data() {
		return {
			shippedDate: null,
			shippedDateMenu: false,
			formTitle: '',
			index: '',
			quData: {},
			questions: [],
			questionTemplate: {},
			quList: [],
			date: '',
			title: '',
			_title: '',
			topic: '',
			_topic: '',
			curIndex: '',
			curOptIndex: '',
			promptText: '',
			iterator: {},
			isAdding: false,
			titleEditing: false,
			topicEditing: false,
			optionEditing: false,
			isShowPrompt: false,
			isShowDatepicker: false
		}
	},
	methods: {
		getData() {
			let item = {}
			item.id = this.quList.length + 1
			item.title = `问卷调查${item.id}`
			item.state = 0
			item.stateName = '未发布'
			item.time = '2018-12-31'
			item.questions = [
				{
					type: 'radio',
					topic: '单选题',
					options: ['选项1', '选项2', '选项3', '选项4']
				},
				{
					type: 'checkbox',
					topic: '多选题',
					options: ['选项1', '选项2', '选项3', '选项4']
				},
				{
					type: 'textarea',
					topic: '文本题',
					isMandatory: false
				}
			]
			this.quData = item

			this.date = this.quData.time
			this.title = this.quData.title
			this.index = this.quData.id - 1
			this.questionTemplate = {
				radio: {
					type: 'radio',
					topic: '单选题',
					options: ['选项1', '选项2', '选项3', '选项4']
				},
				checkbox: {
					type: 'checkbox',
					topic: '多选题',
					options: ['选项1', '选项2', '选项3', '选项4']
				},
				textarea: {
					type: 'textarea',
					topic: '文本题',
					isMandatory: false
				}
			}
			this.questions = [...this.quData.questions]
		},
		cancelTitleEdit() {
			this.titleEditing = false
			this.title = this._title
		},

		cancelTopicEdit() {
			this.topicEditing = false
			this.topic = this._topic
		},

		doneTopicEdit(index) {
			this.topicEditing = false
			this.questions[index].topic = this.topic
		},

		doneOptionEdit(index, optIndex, e) {
			this.curOptIndex = ''
			this.questions[index].options[optIndex] = e.target.value
		},

		switchItem(array, index1, index2) {
			this.optIndex = ''
			this.curIndex = ''

			// let arr = array.splice(index, 2);
			// array.splice(index, 0, ...arr.reverse());
			array[index1] = array.splice(index2, 1, array[index1])[0]
		},

		moveUp(index, array) {
			if (!array) {
				array = this.questions
			}
			this.switchItem(array, index, index - 1)
		},

		moveDown(index, array) {
			if (!array) {
				array = this.questions
			}
			this.switchItem(array, index, index + 1)
		},

		errorPrompt(text) {
			this.iterator = null
			this.showPrompt(text)
		},

		reuse(index) {
			if (this.questions.length === 10) {
				this.errorPrompt(`每份问卷至多10个问题！`)
				return
			}
			let oldQuestion = this.questions[index]
			let newQuestion = JSON.parse(JSON.stringify(oldQuestion))
			this.questions.splice(index, 0, newQuestion)
		},

		delQuestion(index) {
			if (this.questions.length <= 1) {
				this.errorPrompt(`每份问卷至少一个问题！`)
				return
			}
			this.questions.splice(index, 1)
		},

		delOption(index, options) {
			if (options.length <= 2) {
				this.errorPrompt(`每个问题至少两个选项`)
				return
			}
			options.splice(index, 1)
		},

		addOption(options) {
			if (options.length === 4) {
				this.errorPrompt(`每个问题至多四个选项`)
				return
			}
			options.push(`请编辑选项内容`)
		},

		showPrompt(text) {
			this.promptText = text
			this.isShowPrompt = true
		},

		addType(type) {
			if (this.questions.length === 10) {
				this.errorPrompt(`每份问卷至多10个问题！`)
				return
			}
			this.questions.push(this.questionTemplate[type])
		},
		saveData() {
			if (this.questions.length < 1) {
				this.errorPrompt(`每份问卷至少一个问题！`)
				return
			}

			this.quData.title = this.title
			this.quData.time = this.date
			this.quData.questions = [...this.questions]

			if (this.index === this.quList.length) {
				this.quList.push(this.quData)
			} else {
				this.quList.splice(this.index, 1, this.quData)
			}
		},

		*backBtn() {
			yield this.showPrompt(`确认未保存回到主页吗？`)
			yield this.$router.go(-1)
		},

		*saveBtn() {
			yield this.showPrompt(`确认要保存问卷？`)
			yield this.saveData()
			yield this.$router.push('/answer')
		},

		*releaseBtn() {
			yield this.showPrompt(`确认要保存并发布问卷？`)
			yield (() => {
				this.quData.state = 1
				this.quData.stateName = '发布中'
				this.saveData()
			})()
			yield this.$router.push('/answer')
		}
	},
	computed: {
		tempTitle() {
			return this.title || this.quData.title
		}
	},
	created() {
		this.getData()
	},
	mounted() {
		if (this.$route.params.id) {
			this.formTitle = '编辑问卷'
		} else this.formTitle = '新建问卷'
	},
	directives: {
		focus: {
			update: (el) => el.focus()
		}
	},
	watch: {
		quList: {
			handler(list) {
				Store.save(list)
				this.iterator && this.iterator.next()
			}
		}
	}
}
</script>
<style scoped>
.qu-wrap {
	box-sizing: border-box;
	padding: 2rem;
	border-radius: 5px;
	background-color: #fff;
	box-shadow: 0 0 5px #ccc;
}

.qu-wrap > header {
	position: relative;
	height: 5rem;
	margin: 0 2rem 2rem;
	line-height: 5rem;
	text-align: center;
}

.qu-wrap > header span {
	position: absolute;
	top: 0;
	left: 0;
	cursor: pointer;
}

.qu-wrap > header input,
.qu-wrap > header p {
	width: 50rem;
	margin: 0 auto;
	font-size: 1.8rem;
	font-weight: 700;
}

.qu-wrap > header input {
	display: none;
	height: 100%;
	text-align: center;
	border: none;
	outline: none;
	background-color: #ccc;
}

.qu-content {
	padding: 2rem;
	border-top: 2px solid #bbb;
	border-bottom: 2px solid #bbb;
}

.qu-content .qu-item {
	position: relative;
	margin: 1rem 0;
	padding: 1rem 2rem;
}

.qu-content .qu-item h3 {
	height: 2rem;
	font-size: 1.4rem;
	font-weight: 600;
	line-height: 2rem;
}

.qu-content .qu-item .qu-num {
	display: inline-block;
	width: 2.5rem;
	margin-right: 1rem;
}

.qu-content .qu-item h3 {
	height: 2rem;
	font-size: 1.4rem;
	font-weight: 600;
	line-height: 2rem;
}

.qu-content .qu-item h3 > input {
	display: none;
	width: 15rem;
	height: 2rem;
	font-size: 1.4rem;
	border: none;
	outline: none;
	background-color: #ccc;
}

.qu-content .qu-item .nowEditing h3 > input {
	display: inline-block;
}

.qu-content .qu-item > input {
	display: inline-block;
}

.qu-content .options-list > li {
	height: 2rem;
	margin: 1rem 0 0 4rem;
	line-height: 2rem;
}

.qu-content .qu-item:hover .operat-list {
	display: inline-flex;
}

.qu-content .qu-item .operat-list {
	display: none;
	height: 2rem;
	-ms-flex-pack: end;
	justify-content: flex-end;
}

.qu-type {
	overflow: hidden;
	height: 0;
	transition: height 0.5s;
	text-align: center;
}

.qu-type span {
	margin: 0.5rem 1rem;
	cursor: pointer;
	border: 1px solid #ccc;
	background-color: #eee;
}

.options-list > li {
	height: 2rem;
	margin: 1rem 0 0 4rem;
	line-height: 2rem;
}

.options-list > li:hover ul {
	display: inline-flex;
	margin-left: 2rem;
}

.options-list > li:hover li {
	margin-left: 1rem;
	font-size: 1rem;
	cursor: pointer;
}

.options-list > li:hover li:hover {
	color: yellow;
}

.options-list > li > ul {
	display: none;
}

.options-list > li > input {
	display: none;
	width: 15rem;
	height: 2rem;
	font-size: 1.4rem;
	border: none;
	outline: none;
	background-color: #ccc;
}

.operat-list {
	display: none;
	height: 2rem;
	justify-content: flex-end;
}

.operat-list li {
	margin-right: 1rem;
	cursor: pointer;
}

.operat-list li:hover {
	color: yellow;
}

textarea {
	border: 1px solid #bbbbbb;
	margin: 1rem 0 0 4rem;
	resize: none;
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
</style>
