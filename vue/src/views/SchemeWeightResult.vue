<template>
    <div>
        <el-page-header @back="goBack" content="方案权重" style="margin-bottom: 20px;">
        </el-page-header>

        <div>
            <h2>方案得分</h2>
            <div class="echart" id="mychart" :style="myChartStyle"></div>

            <el-card class="box-card" style="width: 80%; margin-top: 5%; margin-left: 8%;">
                <div slot="header" class="clearfix">
                    <h3>图表说明</h3>
                    <!-- <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button> -->
                </div>
                <div>
                    <span
                        style="margin-top: 5px;">上表展示了层次分析法的权重计算结果，根据结果对各个指标的权重进行分析，通过展示了一致性检验结果，用于判断是否存在构建判断矩阵的逻辑问题。指标定义如下：</span>
                </div>
                <div v-for="o in describeStrings" :key="o" class="text item" line-height:1.5>
                    {{ o }}
                </div>
            </el-card>
        </div>

        <div>
            <h2>方案指标权重表</h2>
            <el-table :data="tableData" row-key="index" style="width: 80%; margin-top: 5%; margin-left: 8%;">
                <el-table-column label="节点项">
                    <template slot-scope="scope">
                        {{ nodeTopics[scope.$index].topic }}
                    </template>
                </el-table-column>
                <el-table-column v-for="(item, colIndex) in tableData[0]" :key="colIndex"
                    :label="colIndex < schemeTopics.length ? schemeTopics[colIndex] : '一致性检验结果'">
                    <template slot-scope="scope">
                        <!-- 使用scope.row[index]来访问表格中的数据，并将其显示在表格中 -->
                        {{ scope.row[colIndex].grade }}
                    </template>
                </el-table-column>
            </el-table>
            <el-card class="box-card" style="width: 80%; margin-top: 5%; margin-left: 8%;">
                <div slot="header" class="clearfix">
                    <h3>图表说明</h3>
                    <!-- <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button> -->
                </div>
                <div>
                    <span
                        style="margin-top: 5px;">上表展示了层次分析法的权重计算结果，根据结果对各个指标的权重进行分析，通过展示了一致性检验结果，用于判断是否存在构建判断矩阵的逻辑问题。指标定义如下：</span>
                </div>
                <div v-for="o in describeStrings" :key="o" class="text item" line-height:1.5>
                    {{ o }}
                </div>
            </el-card>
        </div>


    </div>
</template>
  
<script>
import * as echarts from "echarts";

export default {

    data() {
        return {
            tableData: [
            ],
            tableDatas: [

            ],
            nodeTopics:
                [],
            schemeTopics: [],
            additionalData: [[1, 2, 3],
            [4, 5, 6]],
            describeStrings: [
                '●权重值: 即为特征变量的归一化结果,将特征向量映射到0~1的区间上。',
                '●一致性检验结果: 要求CR值(CR=CI/RI)小于0.1,用于判断人们在构建判断矩阵时,是否存在逻辑错误,例如有ABC三个指标,我们判断A比B重要,B比C重要,因此逻辑上A肯定比C重要,但是如果在构建判断A比C时,认为C比A重要,那么就犯了逻辑错误,无法通过一致性检验。'
            ],
            xData: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"], //横坐标
            yData: [23, 24, 18], //数据
            myChartStyle: { float: "left", width: "100%", height: "400px" }, //图表样式
            totalGrades: []
        };
    },
    created() {
        this.initData();
    },
    mounted() {
        this.initEcharts();
    },
    methods: {
        initData() {
            // console.log("rmap");
            // console.log(this.$route.params.rMarix);
            // console.log("nodeTopics");
            // console.log(this.$route.params.schemeTopics);
            this.tableData = JSON.parse(window.sessionStorage.getItem("schemeResultRMarix"));
            this.nodeTopics = JSON.parse(window.sessionStorage.getItem("schemeResultNodeTopics"));
            this.schemeTopics = JSON.parse(window.sessionStorage.getItem("schemeResultSchemeTopics"));


            console.log("tableData");
            console.log(this.tableData);
            console.log("nodeTopics");
            console.log(this.nodeTopics);
            console.log("schemeTopics");
            console.log(this.schemeTopics);

        },
        initEcharts() {
            this.request.get("/ns/totalGrades",
                {
                    params: {
                        modelId: window.sessionStorage.getItem("modelId")
                    }
                }
            ).then(res => {
                console.log("totalGrades---------");
                console.log(res);
                this.totalGrades = res;
                // 基本柱状图
                const option = {
                    xAxis: {
                        data: this.schemeTopics
                    },
                    yAxis: {},
                    series: [
                        {
                            type: "bar", //形状为柱状图
                            data: this.totalGrades,
                            barWidth: "20%",
                            label: {
                                show: true, //开启显示
                                position: 'top', //在上方显示
                                //formatter: '{c}%',//显示百分号

                                textStyle: { //数值样式
                                    color: 'black',//字体颜色
                                    fontSize: 10//字体大小
                                }
                            }

                        }
                    ]
                };
                const myChart = echarts.init(document.getElementById("mychart"));
                myChart.setOption(option);
                //随着屏幕大小调节图表
                window.addEventListener("resize", () => {
                    myChart.resize();
                });

            })

        },
        goBack(){
            this.$router.push('/');
        }

    },
};
</script>

<style>
.text {
    font-size: 14px;
}

.item {
    margin-top: 18px;
    margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}

.clearfix:after {
    clear: both
}

.box-card {
    width: 480px;
}
</style>