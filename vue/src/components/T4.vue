<template>
    <div>
        <div>
            <input type="button" value="放大" v-on:click="zoomIn" />
            <input type="button" value="缩小" v-on:click="zoomOut" />
            <input type="button" value="查看当前数据" v-on:click="checkData" />
            <el-card class="box-card" v-show="isAvialable">
                <div slot="header" class="clearfix">
                    <span>卡片名称</span>
                    <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
                </div>
                <div class="text item">
                    <div class="block">
                        <span class="demonstration">自定义初始值{{ }}</span>
                        <el-slider :step="12.5" show-stops v-model="value" :marks="marks"></el-slider>
                    </div>
                </div>
            </el-card>

        </div>
        <div id="jsmind_container"></div>
    </div>
</template>
 
<script>
import "jsmind/style/jsmind.css";
import jsMind from "jsmind/js/jsmind.js";
window.jsMind = jsMind;

require("jsmind/js/jsmind.draggable.js");
require("jsmind/js/jsmind.screenshot.js");
require("@/assets/js/jsmind.menu.js");

export default {
    name: "MyJsmind",

    data: function () {
        return {
            jm: null,
            nodesData: [], // 存储思维导图节点数据的数组
            value2: 50,
            value: 50,
            marks: {
                0: '9',
                12.5: '7',
                25: '5',
                37.5: '3',
                50: '1',
                62.5: '3',
                75: '5',
                87.5: '7',
                100: '9',

            },
            isAvialable: true
        };
    },

    mounted: function () {
        this.init_data();
    },

    methods: {
        init_data: function () {
            var mind = {
                /* 元数据，定义思维导图的名称、作者、版本等信息 */
                meta: {
                    name: "jsMind-demo-tree",
                    author: "hizzgdev@163.com",
                    version: "0.2",
                },
                /* 数据格式声明 */
                format: "node_array",
                /* 数据内容 */
                data:
                    [
                        { "id": "root", "isroot": true, "topic": "jsMind" },

                        { "id": "easy", "parentid": "root", "topic": "Easy", "direction": "left" },
                        { "id": "easy1", "parentid": "easy", "topic": "Easy to show" },
                        { "id": "easy2", "parentid": "easy", "topic": "Easy to edit" },
                        { "id": "easy3", "parentid": "easy", "topic": "Easy to store" },
                        { "id": "easy4", "parentid": "easy", "topic": "Easy to embed" },

                        { "id": "open", "parentid": "root", "topic": "Open Source", "expanded": false, "direction": "right" },
                        { "id": "open1", "parentid": "open", "topic": "on GitHub" },
                        { "id": "open2", "parentid": "open", "topic": "BSD License" },

                        { "id": "powerful", "parentid": "root", "topic": "Powerful", "direction": "right" },
                        { "id": "powerful1", "parentid": "powerful", "topic": "Base on Javascript" },
                        { "id": "powerful2", "parentid": "powerful", "topic": "Base on HTML5" },
                        { "id": "powerful3", "parentid": "powerful", "topic": "Depends on you" },
                    ]

                ,
            };

            var options = {
                container: "jsmind_container",
                editable: true,
                theme: "primary",

                menuOpts: {
                    showMenu: true,
                    injectionList: [
                        {
                            target: "edit",
                            text: "编辑",
                            callback: function (node) {
                                // this.isAvialable = true
                                console.log(node.parent);
                            },
                        },
                        
                        {
                            target: "addChild",
                            text: "添加子节点",
                            callback: function (node) {
                                console.log(node);
                            },
                        },
                        {
                            target: "addBrother",
                            text: "添加兄弟节点",
                            callback: function (node) {
                                console.log(node);
                            },
                        },
                        {
                            target: "delete",
                            text: "删除节点",
                            callback: function (node) {
                                console.log(node);
                            },
                        },
                        {
                            target: "screenshot",
                            text: "下载导图",
                            callback: function (node) {
                                console.log(node);
                            },
                        },
                        {
                            target: "showAll",
                            text: "展开全部节点",
                            callback: function (node) {
                                console.log(node);
                            },
                        },
                        {
                            target: "hideAll",
                            text: "收起全部节点",
                            callback: function (node) {
                                console.log(node);
                            },
                        },
                        {
                            target: "customMenu",
                            text: "自定义菜单",
                            callback: function (node) {
                                console.log(node);
                                // this.isAvialable = true
                            }
                        },
                    ],
                },
            };

            this.jm = new jsMind(options);
            this.jm.show(mind);

            // 遍历所有节点，将节点数据存储到数组中
            var nodes = this.jm.get_data().data;
            this.traverseNodes(nodes, this.nodesData);
        },
        // 遍历所有节点，将节点数据存储到数组中
        traverseNodes: function (node, result) {
            if (!node) {
                return;
            }

            result.push(node);

            var children = node.children;
            if (children && children.length > 0) {
                for (var i = 0; i < children.length; i++) {
                    this.traverseNodes(children[i], result);
                }
            }
        },
        formatTooltip(val) {
            return val / 100;
        },
        zoomIn: function () {
            this.jm.view.zoomIn();
        },

        zoomOut: function () {
            this.jm.view.zoomOut();
        },
        checkData: function () {
            console.log(this.nodesData);
            console.log()

        },
    },
};
</script>
 
<style scoped>
#jsmind_container {
    width: 100%;
    height: 800px;
}

.text {
    font-size: 14px;
}

.item {
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