var options = {

    chart: {
        type: 'gauge',
        plotBackgroundColor: null,
        plotBackgroundImage: null,
        plotBorderWidth: 0,
        plotShadow: false,
        backgroundColor: '#FAFAFA'
    },
    
    credits: { 
    	enabled: false 
    },
	
	exporting: {
			enabled: false
	},

    title: {
        text: 'Температура'
    },

    pane: {
        startAngle: -150,
        endAngle: 150,
        background: [{
            backgroundColor: {
                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                stops: [
                    [0, '#FFF'],
                    [1, '#333']
                ]
            },
            borderWidth: 0,
            outerRadius: '109%'
        }, {
            backgroundColor: {
                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                stops: [
                    [0, '#333'],
                    [1, '#FFF']
                ]
            },
            borderWidth: 1,
            outerRadius: '107%'
        }, {
            // default background
        }, {
            backgroundColor: '#DDD',
            borderWidth: 0,
            outerRadius: '105%',
            innerRadius: '103%'
        }]
    },

    // the value axis
    yAxis: {
        min: 10,
        max: 45,

        minorTickInterval: 'auto',
        minorTickWidth: 1,
        minorTickLength: 10,
        minorTickPosition: 'inside',
        minorTickColor: '#666',

        tickPixelInterval: 30,
        tickWidth: 2,
        tickPosition: 'inside',
        tickLength: 10,
        tickColor: '#666',
        labels: {
            step: 2,
            rotation: 'auto'
        },
        title: {
            text: '°C'
        },
        plotBands: [{
            from: 10,
            to: TEMP_MIN,
            color: '#0000FF' // BLUE
        }, {
            from: TEMP_MIN,
            to: TEMP_1,
            color: '#55BF3B' // GREEN
        }, {
            from: TEMP_1,
            to: TEMP_2,
            color: '#ADFF2F' // GREENYELLOW
        }, {
            from: TEMP_2,
            to: TEMP_MAX,
            color: '#FFFF00' // YELLOW
        }, {
            from: TEMP_MAX,
            to: 45,
            color: '#DF5353' // RED
        }]
    },

    series: [{
        name: 'Температура',
        data: [TEMP_NOW],
        tooltip: {
            valueSuffix: ' °C'
        }
    }]

};