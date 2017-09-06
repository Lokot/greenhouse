var options = {
	    
		chart: {
			type: 'spline',
	        zoomType: 'xy',
	        backgroundColor: '#FAFAFA'
	    },
	    
	    title: {
	        text: 'Температура за сутки'
	    },
	    
	    subtitle: {
	        text: 'DATE'
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
	    }, { 
	        gridLineWidth: 0,
	        title: {
	            text: 'Состояние конька',
	            style: {
	                color: Highcharts.getOptions().colors[3]
	            }
	        },
	        labels: {
	            format: '{value} %',
	            style: {
	                color: Highcharts.getOptions().colors[3]
	            }
	        },
	        opposite: true
	    }],
	    
	    tooltip: {
	        shared: true
	    },
	    
//	    legend: {
//	        layout: 'horizontal',
//	        align: 'left',
//	        x: 80,
//	        verticalAlign: 'top',
//	        y: 55,
//	        floating: true,
//	        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
//	    },
	    
	    series: [
	             
	    {
	        name: 'Конек',
	        type: 'column',
	        yAxis: 3,
	        data: [GABLE_SER],
	        tooltip: {
	            valueSuffix: ' %'
	        },
			color: Highcharts.getOptions().colors[3]

	    }, {
	        name: 'Освещенность',
	        type: 'spline',
	        yAxis: 2,
	        data: [ILLUM_SER],
	        dashStyle: 'LongDashDotDot',
	        tooltip: {
	            valueSuffix: ''
	        },
			color: Highcharts.getOptions().colors[1]

	    }, {
	        name: 'Влажность',
	        type: 'spline',
	        yAxis: 1,
	        data: [HUM_SER],
	        dashStyle: 'Dash',
	        tooltip: {
	            valueSuffix: ''
	        }

	    }, {
	        name: 'Влажность Max',
	        type: 'spline',
	        yAxis: 1,
	        visible: false,
	        data: [HUM_MAX],
	        dashStyle: 'shortdot',
	        tooltip: {
	            valueSuffix: ''
	        }

	    }, {
	        name: 'Температура',
	        type: 'spline',
	        yAxis: 0,
	        data: [TEMP_SER],
	        tooltip: {
	            valueSuffix: ' °C'
	        },
			color: Highcharts.getOptions().colors[2]
	    }, {
	        name: 'T Max',
	        type: 'spline',
	        yAxis: 0,
	        visible: false,
	        data: [TEMP_MAX],
	        dashStyle: 'shortdot',
	        tooltip: {
	            valueSuffix: ' °C'
	        }
	    }, {
	        name: 'T2',
	        type: 'spline',
	        yAxis: 0,
	        visible: false,
	        data: [TEMP_2],
	        dashStyle: 'shortdot',
	        tooltip: {
	            valueSuffix: ' °C'
	        }
	    }, {
	        name: 'T1',
	        type: 'spline',
	        yAxis: 0,
	        visible: false,
	        data: [TEMP_1],
	        dashStyle: 'shortdot',
	        tooltip: {
	            valueSuffix: ' °C'
	        }
	    }, {
	        name: 'T Min',
	        type: 'spline',
	        yAxis: 0,
	        visible: false,
	        data: [TEMP_MIN],
	        dashStyle: 'shortdot',
	        tooltip: {
	            valueSuffix: ' °C'
	        }
	    }
	    
	    ]
};