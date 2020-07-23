<template>
  <v-container fluid>
    <v-flex xs12>
      <v-card class="grey lighten-4 elevation-0">
        <v-card-title class="title">
          编辑问卷得分
          <v-spacer/>
          <v-btn @click.native="backBtn" class="grey" fab small>
            <v-icon>cancel</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <v-container fluid grid-list-md>
            <v-layout class="px-10" row wrap>
              <v-flex xs12>
                <p>问卷截止日期: {{quData.time}}</p>
                <p>发布状态: {{quData.stateName}}</p>
              </v-flex>
              <v-flex xs12>
                <div class="qu-wrap">
                  <header>
                    <p>{{ quData.title }}</p>
                  </header>
                  <div class="qu-content">
                    <section class="qu-item" v-for="(item, index) in quData.questions">
                      <h3 :key="`Q${index + 1}`">
                        <span>{{`Q${index + 1}`}}</span>
                        <span>{{ item.topic }}</span>
                        <span v-if="item.isMandatory"> *</span>
                      </h3>
                      <div v-if="item.type === 'textarea'">
                        <textarea cols="80" rows="8"/>
                        <input v-if="itemIndex === index && itemEdit" @keyup.enter="doneItemEdit($event, index)" style="display: inline-block;border: 1px solid #ddd" type="text"/>
                        <span @click="itemEdit = true; itemIndex = index" v-else>{{ item.score }}分</span>
                      </div>
                      <ul class="options-list" v-else>
                        <li v-for="(option, optIndex) in item.options">
                          <v-layout row wrap>
                            <v-flex xs6>
                              <span style="padding-right: 50px">{{ option.label }}</span>
                              <input v-if="itemIndex === index && optItemIndex === optIndex && optEdit" @keyup.enter="doneOptionEdit($event, option)" style="display: inline-block;border: 1px solid #ddd" type="text"/>
                              <span @click="optEdit = true; itemIndex = index; optItemIndex = optIndex" v-else>{{ option.score }}分</span>
                            </v-flex>
                            <v-flex xs6>
                              <v-text-field hint="请输入互斥项" label="互斥项" maxlength="10"
                                            name="lastName"
                                            v-model="option.mutex" value="Input text"/>
                            </v-flex>
                          </v-layout>
                        </li>
                      </ul>
                    </section>
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
export default {
	data() {
		return {
			itemIndex: null,
			optItemIndex: null,
			itemEdit: false,
			optEdit: false,
			quData: {}
		}
	},
	methods: {
		getData() {
			let item = {}
			item.id = this.$route.params.id
			item.title = `问卷调查${item.id}`
			item.state = 0
			item.stateName = '未发布'
			item.time = '2018-12-31'
			item.questions = [
				{
					type: 'radio',
					topic: '单选题',
					options: [
						{
							label: '选项1',
							score: 5
						},
						{
							label: '选项2',
							score: 3
						},
						{
							label: '选项3',
							score: 2
						},
						{
							label: '选项4',
							score: 1,
							mutex: '2.2,3.1'
						}
					]
				},
				{
					type: 'checkbox',
					topic: '多选题',
					options: [
						{
							label: '选项1',
							score: 5
						},
						{
							label: '选项2',
							score: 3
						},
						{
							label: '选项3',
							score: 2
						},
						{
							label: '选项4',
							score: 1
						}
					]
				},
				{
					type: 'textarea',
					topic: '文本题',
					isMandatory: false,
					score: 5
				}
			]
			this.quData = item
		},
		backBtn() {
			this.$router.go(-1)
		},
		doneItemEdit(e, index) {
			this.quData.questions[index].score = e.target.value
			this.itemEdit = false
			this.itemIndex = null
		},
		doneOptionEdit(e, option) {
			option.score = e.target.value
			this.optEdit = false
			this.optItemIndex = null
		}
	},
	created() {
		this.getData()
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

.qu-wrap > header p {
	width: 50rem;
	margin: 0 auto;
	font-size: 1.8rem;
	font-weight: 700;
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
