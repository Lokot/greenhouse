var options = {

		chart: {
	        type: 'solidgauge'
	    },

	    title: null,
		
		exporting: {
			enabled: false
		},

	    pane: {
	        center: ['50%', '85%'],
	        size: '140%',
	        startAngle: -90,
	        endAngle: 90,
	        background: {
	            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
	            innerRadius: '60%',
	            outerRadius: '100%',
	            shape: 'arc'
	        }
	    },

	    tooltip: {
	        enabled: false
	    },

	    // the value axis
	    yAxis: {
	    	min: 0,
		    max: 100,
		    title: {
		    	text: 'Конек'
		    },
	        stops: [
	            [0.3, '#55BF3B'], // green
	            [0.6, '#DDDF0D'], // yellow
	            [1.0, '#DF5353'] // red
	        ],
	        lineWidth: 0,
	        minorTickInterval: null,
	        tickAmount: 2,
	        title: {
	            y: -70
	        },
	        labels: {
	            y: 16
	        }
	    },

	    plotOptions: {
	        solidgauge: {
	            dataLabels: {
	                y: 5,
	                borderWidth: 0,
	                useHTML: true
	            }
	        }
	    },

	    credits: {
	        enabled: false
	    },

	    series: [{
	        name: 'Конек',
	        data: [GABLE_NOW],
	        dataLabels: {
	            format: '<div style="text-align:center"><span style="font-size:25px;color:' +
	                ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' +
	                   '<span style="font-size:12px;color:silver">%</span></div>'
	        },
	        tooltip: {
	            valueSuffix: ' %'
	        }
	    }]

};