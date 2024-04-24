<script setup lang="ts">
import {ref, onMounted, inject} from 'vue';
import {chartsBookService, chartsScoreService, chartSalesService, chartAppNumberService,chartAppNumberByCidService} from '@/api/charts.js';
import {getRole} from "@/utils/getCurrentUserInfo.js";

// 获取echart挂载的DOM节点
const books = ref(null);
const scores = ref(null);
const sales = ref(null)
const appNumber = ref(null)
const appNumberByCid = ref(null)
const role = getRole()

// 通过 inject 接收Echarts
const Echarts = inject('$echarts');

// 定义图表数据
const booksData = ref([]);

const scoresData = ref([]);

const initEcharts = async () => {
  const booksResult = await chartsBookService();
  const scoresResult = await chartsScoreService();
  const salesResult = await chartSalesService();
  const appNumberByCidResult = await chartAppNumberByCidService();
  const appNumberResult = await chartAppNumberService();
  // 假设 res.data 是一个数组，且每个元素都有 value 和 name 属性
  booksData.value = booksResult.data.map(item => ({
    value: item.value,
    name: item.name
  }));


  // 初始化 ECharts 实例和配置
  if (books.value) {
    const myChartBooks = (Echarts as any).init(books.value);

    const option = {
      title: {
        text: '心理书屋图书数量统计图',
        subtext: '统计维度：图书分类',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      // ... 其他配置项
      series: [
        {
          name: 'Access From',
          type: 'pie',
          radius: '50%',
          // ... 其他系列配置
          data: booksData.value, // 使用响应式的 data 变量
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };


    if (scores.value) {
      const myChartScores = (Echarts as any).init(scores.value);
      const optionScores = {
        title: {
          text: '问卷调查平均分数统计图',
          subtext: '统计维度：问卷题目类型分数',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },

        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: scoresResult.data.map(item => item.name), // X轴数据
          axisIndex: 0
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '分数',
            type: 'bar', // 指定图表类型为柱状图
            data: scoresResult.data.map(item => item.value / item.count), // Y轴数据
            itemStyle: {
              color: '#3398DB'
            }
          }
        ]
      }


      if (sales.value) {
        const myChartSales = (Echarts as any).init(sales.value);
        const optionSales = {
          title: {
            text: '图书销售量',
            left: 'center'
          },
          xAxis: {
            type: 'category',
            data: salesResult.data.map(item => item.month), // X轴数据
            axisIndex: 0
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              type: 'line', // 指定图表类型为折线图
              data: salesResult.data.map(item => item.sales), // Y轴数据
              name: '月份',
              itemStyle: {
                color: '#3398DB' // 线条颜色
              },
              lineStyle: {
                width: 2, // 线条粗细
                type: 'solid' // 线条样式
              },
              // 标记折线图中每个数据点的图形
              symbol: 'circle',
              symbolSize: 8 // 标记大小
            }
          ]
        }
        if (appNumberByCid.value) {
          const myChartAppNumberByCid = (Echarts as any).init(appNumberByCid.value);
          const optionAppNumberByCid = {
            title: {
              text: '预约人数',
              left: 'center'
            },
            xAxis: {
              type: 'category',
              data: appNumberByCidResult.data.map(item => item.date), // X轴数据
              axisIndex: 0
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                type: 'line', // 指定图表类型为折线图
                data: appNumberByCidResult.data.map(item => item.number), // Y轴数据
                itemStyle: {
                  color: '#3398DB' // 线条颜色
                },
                lineStyle: {
                  width: 2, // 线条粗细
                  type: 'solid' // 线条样式
                },
                // 标记折线图中每个数据点的图形
                symbol: 'circle',
                symbolSize: 8 // 标记大小
              }
            ]
          }
          if (appNumber.value) {
            const myChartAppNumber = (Echarts as any).init(appNumber.value);
            const optionAppNumber = {
              title: {
                text: '预约人数',
                left: 'center'
              },
              xAxis: {
                type: 'category',
                data: appNumberResult.data.map(item => item.date), // X轴数据
                axisIndex: 0
              },
              yAxis: {
                type: 'value'
              },
              series: [
                {
                  type: 'line', // 指定图表类型为折线图
                  data: appNumberResult.data.map(item => item.number), // Y轴数据
                  itemStyle: {
                    color: '#3398DB' // 线条颜色
                  },
                  lineStyle: {
                    width: 2, // 线条粗细
                    type: 'solid' // 线条样式
                  },
                  // 标记折线图中每个数据点的图形
                  symbol: 'circle',
                  symbolSize: 8 // 标记大小
                }
              ]
            }
            option && myChartBooks.setOption(option);
            optionScores && myChartScores.setOption(optionScores);
            optionSales && myChartSales.setOption(optionSales);
            optionAppNumberByCid && myChartAppNumberByCid.setOption(optionAppNumberByCid);
            optionAppNumber && myChartAppNumber.setOption(optionAppNumber);

            // 监听窗口大小变化
            window.addEventListener("resize", function () {
              myChartBooks.resize();
              myChartScores.resize(); // 也要对柱状图进行响应式调整
              myChartSales.resize(); // 也要对柱状图进行响应式调整
              myChartAppNumber.resize(); // 也要对柱状图进行响应式调整
              myChartAppNumberByCid.resize(); // 也要对柱状图进行响应式调整
            });
          }
        }
      }
    }
  }
}

onMounted(() => {

  initEcharts(); // 初始化数据及 ECharts 实例

})
</script>

<template>
  <el-card>
    <div class="echarts">
      <div v-show="role ==='ADMIN'" ref="books" style="width: 600px;height: 350px;margin-bottom: 20px"></div>
      <div ref="scores" style="width: 600px;height: 350px;margin-bottom: 20px"></div>
      <div v-show="role ==='ADMIN'" ref="sales" style="width: 600px;height: 350px;margin-bottom: 20px"></div>
      <div v-show="role ==='CONSULTANT'" ref="appNumberByCid" style="width: 600px;height: 350px;margin-bottom: 20px"></div>
      <div v-show="role ==='ADMIN'" ref="appNumber" style="width: 600px;height: 350px;margin-bottom: 20px"></div>
    </div>
  </el-card>
</template>

<style scoped>
.echarts{
  display: flex;
  flex-wrap: wrap;
}
</style>
