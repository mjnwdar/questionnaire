import { Doughnut } from 'vue-chartjs'

export default Doughnut.extend({
	mounted() {
		this.renderChart(
			{
				labels: ['优秀客户', '良好客户', '一般客户', '其他'],
				datasets: [
					{
						backgroundColor: ['#41B883', '#E46651', '#00D8FF', '#DD1B16'],
						data: [40, 20, 80, 10]
					}
				]
			},
			{ responsive: true, maintainAspectRatio: false }
		)
	}
})
