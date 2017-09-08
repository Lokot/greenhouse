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
			type: 'datetime',
		    dateTimeLabelFormats: {
		        month: '%e. %b',
		        year: '%b'
		    },
		    title: {
		        text: 'Время'
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
		    },
			plotBands: [{ // Ночь
				from: 0,
				to: 550,
				color: 'rgba(68, 170, 213, 0.1)',
				label: {
					text: 'Ночь',
					style: {
						color: '#606060'
					}
				}
			}, { // День
				from: 550,
				to: 1000,
				color: 'rgba(0, 0, 0, 0)',
				label: {
					text: 'День',
					style: {
						color: '#606060'
					}
				}
			}]
		}],
		
		tooltip: {
		    shared: true
		},
		
		series: [
			{
			    name: 'Конек',
			    type: 'column',
			    yAxis: 3,
			    data: [TEMP],
			    tooltip: {
			        valueSuffix: ' %'
			    },
				color: Highcharts.getOptions().colors[3]
			
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
			
			}, {
			    name: 'Влажность',
			    type: 'spline',
			    yAxis: 1,
			    data: [HUM],
			    dashStyle: 'Dash',
			    tooltip: {
			        valueSuffix: ''
			    }
			}
		]
};