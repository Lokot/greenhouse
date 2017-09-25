var options = {

		chart: {
			type: 'spline',
		    zoomType: 'xy',
		    backgroundColor: '#FAFAFA'
		},
		
		title: {
		    text: 'CHART_TITLE'
		},
		
		subtitle: {
		    text: 'CHART_SUBTITLE'
		},
		
		credits: { 
			enabled: false 
		},
		
		plotOptions: {
		    spline: {
		        marker: {
		            enabled: false
		        },
		        dataLabels: {
		            enabled: false
		        }
		    }
		},
		
		xAxis: [{
			type: 'category',
			title: {
		        text: 'Дата'
		    }
		}],
		yAxis: [{ 
		    labels: {
		        format: '{value}°C',
		        style: {
		            color: Highcharts.getOptions().colors[2]
		        }
		    },
		    title: {
		        text: 'Температура',
		        style: {
		            color: Highcharts.getOptions().colors[2]
		        }
		    }
		
		}, { 
		    gridLineWidth: 0,
		    title: {
		        text: 'Влажность',
		        style: {
		            color: Highcharts.getOptions().colors[0]
		        }
		    },
		    labels: {
		        format: '{value} %',
		        style: {
		            color: Highcharts.getOptions().colors[0]
		        }
		    },
		    opposite: true
		}, { 
		    gridLineWidth: 0,
		    title: {
		        text: 'Освещенность',
		        style: {
		            color: Highcharts.getOptions().colors[1]
		        }
		    },
		    labels: {
		        format: '{value}',
		        style: {
		            color: Highcharts.getOptions().colors[1]
		        }
		    }
		}],
		
		tooltip: {
		    shared: true,
		    headerFormat: '<b>{point.key}</b><br>'
		},
		
		series: [
			{
			    name: 'Температура',
			    type: 'spline',
			    yAxis: 0,
			    data: [TEMP],
			    tooltip: {
			        valueSuffix: ' °C'
			    },
				color: Highcharts.getOptions().colors[2]
			},
			{
				name: 'Влажность',
			    type: 'spline',
			    yAxis: 1,
			    data: [HUM],
			    dashStyle: 'Dash',
			    tooltip: {
			        valueSuffix: ' %'
			    }
			
			}, {
				name: 'Освещенность',
			    type: 'spline',
			    yAxis: 2,
			    data: [ILLUM],
			    dashStyle: 'LongDashDotDot',
			    tooltip: {
			        valueSuffix: ''
			    },
				color: Highcharts.getOptions().colors[1]
			}
		]
};