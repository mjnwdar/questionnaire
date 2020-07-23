<template>
  <div>
    <v-data-table class="elevation-1" v-bind:headers="headers" v-bind:items="items" v-bind:search="search"
                  v-bind:pagination="pagination" hide-actions>
      <template slot="headers" slot-scope="props">
        <tr>
          <th v-for="(header, index) in props.headers" :key="header.text"
              :class="['column', 'subheading' , index === 0? 'text-xs-left': 'text-xs-center']">
            {{ header.text }}
          </th>
          <th>
          </th>
        </tr>
      </template>
      <template class="body-2" slot="items" slot-scope="props">
        <td v-for="(header, index) in headers" :key="index"
            :class="[ index === 0? 'text-xs-left': 'text-xs-center', 'body-2']" v-if="header.value!==''">
          <a v-if="header.type === 'link'" :href="`${header.url}?id=${props.item[header.key]}`">{{renderData(props.item, header)}}</a>
          <span v-else>{{renderData(props.item, header)}}</span>
        </td>
        <td class="text-xs-right" v-if="showOp">
          <v-btn class="teal" fab small dark @click.native="$emit('edit', props.item)">
            <v-icon>edit</v-icon>
          </v-btn>
          <v-btn @click.native="$emit('remove', props.item)" class="cyan" fab small v-if="showRemove">
            <v-icon>delete</v-icon>
          </v-btn>
        </td>
        <td class="empty-td" v-else/>
      </template>
      <template slot="no-data">
     <span>
        <p class="pt-2 blue--text subheading">   <v-icon medium class="blue--text">info</v-icon>对不起，没有加载到任何数据。</p>
      </span>
      </template>
    </v-data-table>
    <div class="text-xs-center pt-2" v-if="isNotEmpty">
      <v-pagination v-model="pagination.page" :length="pagination.pages" :total-visible="5" circle/>
    </div>
  </div>
</template>
<script>
export default {
	props: {
		headers: '',
		items: '',
		pagination: '',
		showOp: {
			type: Boolean,
			default: true
		},
		showRemove: {
			type: Boolean,
			default: false
		}
	},
	data() {
		return {
			search: ''
		}
	},
	methods: {
		renderData: (item, header) => {
			let val = ''
			if (header.value.includes('.')) {
				const vals = header.value.split('.')
				val = vals.reduce((acc, val) => acc[val], item)
			} else {
				val = item[header.value]
			}
			if (typeof val === 'boolean') {
				val = val ? 'Yes' : 'No'
			}
			return val
		}
	},
	computed: {
		isNotEmpty() {
			return this.items && this.items.length > 0
		}
	}
}
</script>

<style scoped>
.empty-td {
	width: 0;
}
</style>
