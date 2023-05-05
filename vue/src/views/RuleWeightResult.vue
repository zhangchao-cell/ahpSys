<template>
    <div style="">
        <!-- <el-table :data="tableData" row-key="index">
            <el-table-column v-for="(item, index) in tableData[0]" :key="index" :label="'Column ' + (index + 1)">
                <template slot-scope="scope">
                    {{ scope.row[index] }}
                </template>
            </el-table-column>
            <el-table-column label="Additional Column">
                <template slot-scope="scope">
                    {{ additionalData[scope.$index] }}
                </template>
            </el-table-column>
        </el-table> -->
        <el-page-header @back="goBack" content="指标权重" style="margin-bottom: 20px;">
        </el-page-header>

        <el-table v-for="(data, tableIndex) in tableDatas" :key="tableIndex" :data="data" row-key="tableIndex"
            style="width: 80%; margin-top: 5%; margin-left: 8%;">
            <el-table-column label="Additional Column">
                <template slot-scope="scope">
                    {{ nodeTopics[tableIndex][scope.$index] }}
                </template>
            </el-table-column>
            <el-table-column v-for="(item, colIndex) in data[0]" :key="colIndex"
                :label="colIndex < nodeTopics[tableIndex].length ? nodeTopics[tableIndex][colIndex] : colIndex === nodeTopics[tableIndex].length ? '权重值' : '一致性检验结果'">
                <template slot-scope="scope">
                    <!-- 使用scope.row[index]来访问表格中的数据，并将其显示在表格中 -->
                    {{ scope.row[colIndex].value }}
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
</template>
  
<script>


export default {

    data() {
        return {
            tableData: [
                [1, 2, 3, 4, 5],
                [4, 5, 6, 6, 7],
                [7, 8, 9, 8, 9]],
            tableDatas: [
                // [
                //     [1, 2, 3],
                //     [4, 5, 6],
                //     [7, 8, 9]
                // ],
                // [
                //     [10, 11, 12],
                //     [13, 14, 15],
                //     [16, 17, 18]
                // ]
            ],
            nodeTopics:
                [],
            additionalData: [[1, 2, 3],
            [4, 5, 6]],
            describeStrings: [
                '●权重值: 即为特征变量的归一化结果,将特征向量映射到0~1的区间上。',
                '●一致性检验结果: 要求CR值(CR=CI/RI)小于0.1,用于判断人们在构建判断矩阵时,是否存在逻辑错误,例如有ABC三个指标,我们判断A比B重要,B比C重要,因此逻辑上A肯定比C重要,但是如果在构建判断A比C时,认为C比A重要,那么就犯了逻辑错误,无法通过一致性检验。'
            ]
        };
    },
    created() {
        this.initData();
    },
    methods: {
        initData() {
            // console.log("rmap");
            // console.log(this.$route.params.rmap);
            // console.log("nodeTopics");
            // console.log(this.$route.params.nodeTopics);

            const rmap = JSON.parse(window.sessionStorage.getItem("ruleResultRmap"));
            const nodeTopics = JSON.parse(window.sessionStorage.getItem("ruleResultNodeTopics"));
            let index = 0;
            for (var key in rmap) {
                console.log(rmap[key]);
                this.tableDatas.push(rmap[key]);
                let temp = [];
                for (var i = index; i < rmap[key].length + index; i++) {
                    temp.push(nodeTopics[i]);
                }
                index = i;
                // console.log("temp");
                // console.log(temp);
                this.nodeTopics.push(temp);

            }
            console.log("tableDatas");
            console.log(this.tableDatas);
            console.log("nodeTopics");
            console.log(this.nodeTopics);

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