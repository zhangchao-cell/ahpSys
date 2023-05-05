<template>
    <div>
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
        <el-table v-for="(data, tableIndex) in tableDatas" :key="tableIndex" :data="data" row-key="tableIndex">
            <el-table-column :label="data[0][0].node.topic">
                <template slot-scope="scope">
                    {{ schemeTopics[tableIndex][scope.$index] }}
                </template>
            </el-table-column>
            <el-table-column v-for="(item, colIndex) in data[0]" :key="colIndex"
                :label="colIndex < schemeTopics[tableIndex].length ? schemeTopics[tableIndex][colIndex] : colIndex === schemeTopics[tableIndex].length ? '权重值' : '一致性检验结果'">
                <template slot-scope="scope">
                    <!-- 使用scope.row[index]来访问表格中的数据，并将其显示在表格中 -->
                    {{ scope.row[colIndex].value }}
                </template>
            </el-table-column>
        </el-table>

        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <h3>图表说明</h3>
                <!-- <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button> -->
            </div>
            <div>
                <span style="margin-top: 5px;">上表展示了层次分析法的权重计算结果，根据结果对各个指标的权重进行分析，通过展示了一致性检验结果，用于判断是否存在构建判断矩阵的逻辑问题。指标定义如下：</span>
            </div>
            <div v-for="o in describeStrings" :key="o" class="text item" line-height:1.5 >
                {{  o }}
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
            schemeTopics:
                [],
            additionalData: [[1, 2, 3],
            [4, 5, 6]],
            describeStrings:[
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
            console.log("rmap");
            console.log(this.$route.params.rmap);
            console.log("schemeTopics");
            console.log(this.$route.params.schemeTopics);
            
            for (var key in this.$route.params.rmap) {
                console.log(this.$route.params.rmap[key]);
                this.tableDatas.push(this.$route.params.rmap[key]);

                let temp = [];
                for (var i = 0; i < this.$route.params.schemeTopics.length ; i++) {
                    temp.push(this.$route.params.schemeTopics[i]);
                }
                this.schemeTopics.push(temp);
            }
            console.log("tableDatas");
            console.log(this.tableDatas);
            console.log("schemeTopics");
            console.log(this.schemeTopics);

        },
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