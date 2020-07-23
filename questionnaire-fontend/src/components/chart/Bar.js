import { Bar } from 'vue-chartjs'

export default Bar.extend({
	mounted() {
		this.renderChart(
			{
				labels: [
					'一月',
					'二月',
					'三月',
					'四月',
					'五月',
					'六月',
					'七月',
					'八月',
					'九月',
					'十月',
					'十一月',
					'十二月'
				],
				datasets: [
					{
						label: '统计',
						backgroundColor: '#f87979',
						data: [40, 20, 12, 39, 10, 40, 39, 80, 40, 20, 12, 11]
					},
					{
						label: '新问答用户',
						borderColor: '#FC2525',
						borderWidth: 2,
						pointBackgroundColor: 'white',
						pointBorderColor: 'white',
						backgroundColor: this.gradient,
						data: [40, 39, 10, 40, 39, 80, 40, 10, 40, 39, 80, 40]
					}
				]
			},
			{ responsive: true, maintainAspectRatio: false }
		)
	}
})
