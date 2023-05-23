import React from 'react';
import amis from 'amis';
import echarts from 'echarts';

amis.renderer.register('echarts', props => {
  const chartRef = React.useRef(null);
  const chartOption = props.option || {};

  React.useEffect(() => {
    const chartInstance = echarts.getInstanceByDom(chartRef.current) || echarts.init(chartRef.current);
    chartInstance.setOption(chartOption);

    return () => {
      chartInstance.dispose();
    };
  }, []);

  return <div style={{ width: '100%', height: '400px' }} ref={chartRef} />;
});

const schema = {
  type: 'page',
  title: 'ECharts 示例',
  body: [
    {
      type: 'echarts',
      option: {
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [820, 932, 901, 934, 1290, 1330, 1320],
            type: 'line'
          }
        ]
      }
    }
  ]
};

amis.render(schema, document.getElementById('root'));
