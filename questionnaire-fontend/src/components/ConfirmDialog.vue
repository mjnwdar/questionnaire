<template>
  <v-dialog v-model="dialog" persistent max-width="320">
    <v-card>
      <v-card-title>{{dialogTitle}}</v-card-title>
      <v-card-text>{{dialogText}}</v-card-text>
      <v-card-actions>
        <v-btn @click.native="$emit('onConfirm')" class="green--text darken-1" flat="flat">确定</v-btn>
        <v-btn @click.native="$emit('onCancel')" class="orange--text darken-1" flat="flat">取消</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script>
export default {
	props: {
		dialogText: '',
		dialogTitle: '',
		dialog: ''
	},
	data() {
		return {
			mini: false,
			isRootComponent: true,
			// clipped: false,
			drawer: true,
			fixed: false
		}
	},
	methods: {
		onConfirm() {},
		onCancel() {}
	},
	mounted() {}
}
</script>
