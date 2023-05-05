<template>
    <el-container>
        <el-header>
            <h1 style="display: inline">基于层次分析法的评价系统</h1>
            <div style="margin-left: 90%;" v-if="isLogon">
                <el-dropdown trigger="click" style="margin-bottom: 20px;" @command="handleCommand">
                    <span class="el-dropdown-link">
                        <el-avatar size="large"
                            src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"></el-avatar>
                        <!-- <img class="avatar" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"> -->
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="personalInfo">个人信息</el-dropdown-item>
                        <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
            <div style="margin-left: 90%;" v-if="!isLogon">
                <el-button @click="logon" type="text">请登录</el-button>
            </div>
            <el-divider></el-divider>
        </el-header>
        <el-container>
            <el-aside width="200px">
                <el-menu default-active="/ruleWeightResult" class="el-menu-vertical-demo" router>
                    <el-menu-item index="/ruleWeightResult">
                        <i class="el-icon-menu"></i>
                        <span slot="title">指标权重</span>
                    </el-menu-item>
                    <el-menu-item index="/schemeWeightResult">
                        <i class="el-icon-menu"></i>
                        <span slot="title">方案得分</span>
                    </el-menu-item>
                    <el-menu-item index="/personInfo">
                        <i class="el-icon-setting"></i>
                        <span slot="title">个人信息</span>
                    </el-menu-item>
                </el-menu>
                <el-divider direction="vertical"></el-divider>
            </el-aside>

            <!-- <el-container> -->
            <el-main>
                <router-view></router-view>

            </el-main>
            <!-- <el-footer>
            
          </el-footer> -->
            <!-- </el-container> -->
        </el-container>
    </el-container>
</template>
  
<script>
import "jsmind/style/jsmind.css";
import jsMind from "jsmind/js/jsmind.js";
window.jsMind = jsMind;

require("jsmind/js/jsmind.draggable.js");
require("jsmind/js/jsmind.screenshot.js");
require("@/assets/js/jsmind.menu.js");

import request from "@/utils/request";
import router from "../router";

export default {
    name: "MyJsmind",
    data: function () {
        return {
            jm: null,
            active: 0,
            isLogon: false,
            maxId: 0,
            nodesData: [
                { "id": "1", "isroot": true, "topic": "中心主题" },
                { "id": "2", "parentid": "1", "topic": "指标1" },
                // { "id": "easy1", "parentid": "easy", "topic": "Easy to show" },
                // { "id": "easy2", "parentid": "easy", "topic": "Easy to edit" },
                // { "id": "easy3", "parentid": "easy", "topic": "Easy to store" },
                // { "id": "easy4", "parentid": "easy", "topic": "Easy to embed" },
                { "id": "3", "parentid": "1", "topic": "指标2" },
                { "id": "4", "parentid": "3", "topic": "指标2.1" },
                { "id": "5", "parentid": "3", "topic": "指标2.2" },
            ],
            nodeTopics: [],
            schemeTopics: [],
            ruleSliders: [],
            schemeSliders: [],
            leafNodes: [],
            marks: {
                "-8": "9",
                "-6": "7",
                "-4": "5",
                "-2": "3",
                0: "1",
                2: "3",
                4: "5",
                6: "7",
                8: "9",
            },
            isAvialable: false,
            isAvialable2: false,
            jsMindDialogVisible: false,
            saveModelDialogVisible: false,
            selectModelDialogVisible: false,
            schemes: [],
            inputVisible: false,
            inputValue: "",
            model_id: window.sessionStorage.getItem("modelId"),
            modelForm: {
                modelName: "",
                modelDetail: ""
            },
            allChildren: [],
            models: [
                { modelId: "1", modelName: "h1", modelDetail: "hhh1" },
                { modelId: "2", modelName: "h2", modelDetail: "hhh2" },
            ],
            selectedRow: null,
            dialogWidth: "800",
            dialogHeight: "500", // 对话框的高度
        };
    },
    mounted: function () {
        //this.init_data();
        //this.originNodes();
        //this.originSchemes();
        //this.initActive();
        this.initUser();
    },
    created() {
    },
    methods: {
        initUser() {
            const user = JSON.parse(window.sessionStorage.getItem('user'));
            if (user === null) {
                this.isLogon = false;
            } else {
                // do something else
                this.isLogon = true;
            }
        },
        handleCommand(command) {
            if (command === 'personalInfo') {
                this.goToPersonalInfo();
                // do something
            } else if (command === 'logout') {
                // do something else
                this.logout();
            }
        },
        goToPersonalInfo() {
            this.$router.push("/personInfo");
        },
        logout() {
            console.log("logout")

            window.sessionStorage.setItem('user', null)
            this.$router.push("/logon");
            // 进行退出登录操作
        },
        logon() {
            this.$router.push("/logon");
        },
        initActive() {
            if (window.sessionStorage.getItem("active") != null) {
                this.active = parseInt(window.sessionStorage.getItem("active"));
                console.log("this.active init~~~~~~~~~~~~~~");
                console.log(this.active);
            }
        },
        next() {
            if (this.active == 0) {
                window.sessionStorage.setItem("active", 1);
            }
            else {
                window.sessionStorage.setItem("active", parseInt(window.sessionStorage.getItem("active")) + 1);
            }
            console.log("next getItem~~~~~~~~~~~~~~");
            console.log(window.sessionStorage.getItem("active"));
            this.initActive();
            switch (this.active) {
                case 1:
                    this.showRuleSlider();
                    break;
                case 2:
                    break;
                case 3:
                    this.showSchemeSlider();
                    break;
                default:
                    break;
            }
            if (this.active > 3)
                this.active = 0;
        },
        
        init_data: function (modelIdPara) {
            this.request.get("/node/selectByModelId", {
                params: {
                    modelId: modelIdPara
                }
            }).then(res => {
                console.log("-----res------");
                console.log(res);
                this.nodesData = res;
                console.log("-----nodes------");
                console.log(this.nodesData);
                var mind = {
                    /* 元数据，定义思维导图的名称、作者、版本等信息 */
                    meta: {
                        name: "jsMind-demo-array",
                        author: "zc@163.com",
                        version: "0.2",
                    },
                    /* 数据格式声明 */
                    format: "node_array",
                    /* 数据内容 */
                    data: this.nodesData,
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
                                    console.log(node);
                                },
                            },
                            {
                                target: "addChild",
                                text: "添加子节点",
                                callback: function (node) {
                                    console.log(node);
                                    // let nowNode = node;
                                    // while(nowNode.children != null){
                                    //   nowNode = node.children[node.children.length-1];
                                    // }
                                    // let maxId = nowNode.id;
                                    // console.log("maxId----");
                                    // console.log(maxId);
                                    // setTimeout(function () {
                                    //   console.log("node.children");
                                    // console.log(node.children[node.children.length-1]);
                                    // node.children[node.children.length-1].id = maxId+1;
                                    // }, 500);
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
                            // {
                            //   target: "customMenu",
                            //   text: "自定义菜单",
                            //   callback: this.handleCustomMenu
                            // },
                        ],
                    },
                };
                if (this.jm == null) {
                    this.jm = new jsMind(options);
                    // this.jm.show(mind);
                }
                this.jm.show(mind);
                // 遍历所有节点，将节点数据存储到数组中
                // var nodes = this.jm.get_data().data;
                // this.traverseNodes(nodes, this.nodesData);
                //自定义根节点
                //this.nodesData.unshift({ "id": res[0].node_id, "isroot": true, "topic": res[0].topic });
            });
        },
        init_scheme(modelIdPara) {
            request.get("/scheme/selectByModelId", {
                params: {
                    modelId: modelIdPara
                }
            }).then(res => {
                console.log("scheme---------");
                console.log(res);
                this.schemes = res;
            });
        },
        originNodes() {
            if (window.sessionStorage.getItem("modelId") != null) {
                this.init_data(window.sessionStorage.getItem("modelId"));
            }
            else {
                var mind = {
                    /* 元数据，定义思维导图的名称、作者、版本等信息 */
                    meta: {
                        name: "jsMind-demo-array",
                        author: "zc@163.com",
                        version: "0.2",
                    },
                    /* 数据格式声明 */
                    format: "node_array",
                    /* 数据内容 */
                    data: this.nodesData,
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
                                    console.log(node);
                                },
                            },
                            {
                                target: "addChild",
                                text: "添加子节点",
                                callback: function (node) {
                                    console.log(node);
                                    // let nowNode = node;
                                    // while(nowNode.children != null){
                                    //   nowNode = node.children[node.children.length-1];
                                    // }
                                    // let maxId = nowNode.id;
                                    // console.log("maxId----");
                                    // console.log(maxId);
                                    // setTimeout(function () {
                                    //   console.log("node.children");
                                    // console.log(node.children[node.children.length-1]);
                                    // node.children[node.children.length-1].id = maxId+1;
                                    // }, 500);
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
                            // {
                            //   target: "customMenu",
                            //   text: "自定义菜单",
                            //   callback: this.handleCustomMenu
                            // },
                        ],
                    },
                };
                if (this.jm == null) {
                    this.jm = new jsMind(options);
                    // this.jm.show(mind);
                }
                this.jm.show(mind);
            }
        },
        originSchemes() {
            if (window.sessionStorage.getItem("modelId") != null) {
                this.init_scheme(window.sessionStorage.getItem("modelId"));
            }
        },
        showJsMindDialog() {
            this.init_data();
            this.jsMindDialogVisible = true;
        },
        saveMindMap() {
            //const data = this.mindMapInstance.get_data("node_array");
            // 将数据保存到后端
            // ...
            this.jsMindDialogVisible = false;
            const mindMapHtml = this.$refs.jsmind_container.innerHTML;
            // this.$nextTick(() => {
            //   const mindMapHtml = this.$refs.jsmind_container.innerHTML;
            //   this.$refs.dashedBoxContent.innerHTML = mindMapHtml;
            //   // 调用jsMind插件的resize方法
            //   this.jm.resize();
            // });
            console.log("mindMapHtml----------");
            console.log(mindMapHtml);
            this.$refs.dashedBoxContent.innerHTML = "<span>hhhhh</span>";
            console.log("this.$refs.dashedBoxContent.innerHTML----------");
            console.log(this.$refs.dashedBoxContent.innerHTML);
        },
        closeDialog() {
            //this.jm = null;
        },
        // 遍历所有节点，将节点数据存储到数组中
        traverseNodes: function (node, result) {
            if (!node) {
                return;
            }
            // 分配 ID
            if (!node.id) {
                node.id = jsMind.util.uuid.newid();
            }
            result.push(node);
            var children = node.children;
            if (children && children.length > 0) {
                for (var i = 0; i < children.length; i++) {
                    this.traverseNodes(children[i], result);
                }
            }
        },
        formatTooltip(val, slider) {
            const marks = {
                // 0: `${slider.text1}绝对重要`,
                // 12.5: `${slider.text1}十分重要`,
                // 25: `${slider.text1}比较重要`,
                // 37.5: `${slider.text1}稍微重要`,
                // 50: '',
                // 62.5: `${slider.text2}稍微重要`,
                // 75: `${slider.text2}比较重要`,
                // 87.5: `${slider.text2}十分重要`,
                // 100: `${slider.text2}绝对重要`,
                "-8": `${slider.text1}绝对重要`,
                "-6": `${slider.text1}十分重要`,
                "-4": `${slider.text1}比较重要`,
                "-2": `${slider.text1}稍微重要`,
                0: "",
                2: `${slider.text2}稍微重要`,
                4: `${slider.text2}比较重要`,
                6: `${slider.text2}十分重要`,
                8: `${slider.text2}绝对重要`,
            };
            return marks[val];
        },
        showModels() {
            request.get("/model").then(res => {
                console.log("models---------");
                console.log(res);
                this.models = res;
                this.selectModelDialogVisible = true;
            });
        },
        handleRowClick(row) {
            // 点击表格行时触发
            console.log(row);
            this.selectedRow = row;
            console.log("handleRowClick---selectedRow");
            console.log(this.selectedRow);
            window.sessionStorage.setItem("modelId", row.modelId);
        },
        selectModel() {
            // 点击对话框的确认按钮时触发
            //const selectedRows = this.$refs.table.getCurrentRow();
            console.log("selectModel--- modelId");
            console.log(window.sessionStorage.getItem("modelId"));
            this.init_data(window.sessionStorage.getItem("modelId"));
            this.init_scheme(window.sessionStorage.getItem("modelId"));
            this.selectModelDialogVisible = false;
        },
        savacheme() {
            this.request.post("/scheme/saveScheme", this.schemes).then(res => {
                if (res) {
                    console.log(res);
                    this.$message.success("保存成功");
                    // this.dialogFormVisible = false
                    // this.load()
                }
                else {
                    this.$message.error("保存失败");
                }
            });
        },
        handleClose(tag) {
            this.schemes.splice(this.schemes.indexOf(tag), 1);
        },
        showInput() {
            this.inputVisible = true;
            this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        },
        handleInputConfirm() {
            let inputValue = this.inputValue;
            console.log("handleInputConfirm-sessionStorage!!!!!!!!!!");
            console.log(window.sessionStorage.getItem("modelId"));
            let inputObject = {
                modelId: window.sessionStorage.getItem("modelId"),
                schemeDetail: "",
                schemeName: inputValue
            };
            if (inputValue) {
                this.schemes.push(inputObject);
            }
            console.log("scheme2-----");
            console.log(this.schemes);
            this.inputVisible = false;
            this.inputValue = "";
        },
        zoomIn: function () {
            this.jm.view.zoomIn();
        },
        zoomOut: function () {
            this.jm.view.zoomOut();
        },
        checkData: function () {
            console.log(this.nodesData);
        },
        // 获取选中标签的 ID
        get_selected_nodeid() {
            let selectedNode = this.jm.get_selected_node();
            if (selectedNode) {
                return selectedNode.id;
            }
            else {
                return null;
            }
        },
        getNodeId() {
            // console.log("----a----")
            let selectedId = this.get_selected_nodeid();
            // console.log(selectedId)
            if (!selectedId) {
                this.$message({ type: "warning", message: "请先选择一个节点!" });
                return;
            }
            let nodeObj = this.jm.get_node(selectedId);
            console.log(nodeObj);
            //this.$message("成功！");
            // for (let index = 0; index < this.nodesData.length; index++) {
            //   let n = this.nodesData[index];
            //   var children = n.children;
            //   if (children && children.length > 0) {
            //     for (let j = 0; j < children.length; j++) {
            //       while (this.jm.fined_node_after(children[j]) != null) {
            //         this.sliderValues.push({ value: 50, text1: children.topic, text2: children.topic });
            //       }
            //     }
            //   }
            // };
        },
        showRuleSlider() {
            // let r1 = this.jm.find_node_before(this.nodesData[4].id);
            // let r2 = this.jm.find_node_after(this.nodesData[1].id);
            // let r3 = this.jm.find_node_after(this.nodesData[4].id);
            this.getRuleSliders2();
            // let r1 = this.hhhh(this.nodesData[1]);
            // this.hhhh(r1.children[0]);
            // let r = this.jm.find_node_before(r1.id);
            // while (r != null) {
            //   this.hhhh(r.children[0]);
            //   r = this.jm.find_node_before(r.id);
            // }
            this.isAvialable = true;
        },
        showSchemeSlider() {
            this.getLeafNodes(window.sessionStorage.getItem("modelId"));
            this.isAvialable2 = true;
        },
        getLeafNodes(modelIdPara) {
            request.get("/node/leaf", {
                params: {
                    modelId: modelIdPara
                }
            }).then(res => {
                console.log("leaf---------");
                console.log(res);
                this.leafNodes = res;
                if (this.schemeSliders.length == 0) {
                    for (let i = 0; i < this.leafNodes.length; i++) {
                        console.log("abc-------");
                        this.getSchemeSliders(this.leafNodes[i]);
                    }
                }
            });
        },
        //把叶子节点对应的slider数组组传入封装到sliderValues2数组中
        getSchemeSliders(inode) {
            console.log(this.schemes);
            let sv = [];
            for (let i = 0; i < this.schemes.length - 1; i++) {
                for (let j = i + 1; j < this.schemes.length; j++) {
                    if (i == j) {
                        continue;
                    }
                    let n1 = this.schemes[i];
                    let n2 = this.schemes[j];
                    sv.push({
                        value: 0,
                        text1: n1.schemeName,
                        text2: n2.schemeName,
                        id1: n1.schemeId,
                        id2: n2.schemeId,
                        node: inode,
                        scheme1: n1,
                        scheme2: n2
                    });
                    // this.sliderValues2.push({
                    //   value: 0, text1: n1.schemeName, text2: n2.schemeName,
                    //   id1: n1.schemeId, id2: n2.schemeId, node: inode,
                    //   scheme1: n1, scheme2: n2
                    // });
                }
            }
            this.schemeSliders.push(sv);
        },
        //得到所有slider的值到数组里，方法一，递归
        hhhh(n) {
            // let n1 = this.nodesData[1];
            let n1 = n;
            let n2 = this.jm.find_node_after(n1.id);
            while (n2 != null) {
                console.log("--------n1--------");
                console.log(n1);
                this.ruleSliders.push({
                    value: 0,
                    text1: n1.topic,
                    text2: n2.topic,
                    id1: n1.id,
                    id2: n2.id,
                    location: n1.location
                });
                n2 = this.jm.find_node_after(n2.id);
            }
            if (this.jm.find_node_after(n1.id) == null) {
                this.ruleSliders.push({ value: "9", text1: "-", text2: "-" });
                return n1;
            }
            return this.hhhh(this.jm.find_node_after(n1.id));
            //n1 = this.jm.find_node_after(n1.id);
        },
        //得到所有slider的值到数组里，方法二，循环。目前只能有两层
        getRuleSliders() {
            let nodeParent = this.jm.get_data("node_tree").data;
            console.log("-----nodeParent--------");
            console.log(nodeParent);
            console.log("-----nodeParentC--------");
            console.log(nodeParent.children);
            for (let i = 0; i < nodeParent.children.length - 1; i++) {
                for (let j = i + 1; j < nodeParent.children.length; j++) {
                    if (i == j) {
                        continue;
                    }
                    let n1 = nodeParent.children[i];
                    let n2 = nodeParent.children[j];
                    this.ruleSliders.push({
                        value: 0,
                        text1: n1.topic,
                        text2: n2.topic,
                        id1: n1.id,
                        id2: n2.id,
                        location: n1.location,
                        node1: n1,
                        node2: n2
                    });
                }
            }
            for (let z = 0; z < nodeParent.children.length; z++) {
                let nodeP = nodeParent.children[z];
                if (nodeP.children != null) {
                    for (let i = 0; i < nodeP.children.length - 1; i++) {
                        for (let j = i + 1; j < nodeP.children.length; j++) {
                            if (i == j) {
                                continue;
                            }
                            let n1 = nodeP.children[i];
                            let n2 = nodeP.children[j];
                            this.ruleSliders.push({
                                value: 0,
                                text1: n1.topic,
                                text2: n2.topic,
                                id1: n1.id,
                                id2: n2.id,
                                location: n1.location,
                                node1: n1,
                                node2: n2
                            });
                        }
                    }
                }
            }
            console.log("-----ruleSliders--------");
            console.log(this.ruleSliders);
        },
        //得到所有slider的值到数组里，方法三，递归+循环。支持多层，直接和数据库关联，数据格式
        //为node_array
        getRuleSliders2() {
            request.get("/node/selectByModelId", {
                params: {
                    modelId: window.sessionStorage.getItem("modelId")
                }
            }).then(res => {
                console.log("selectByModelId---------");
                console.log(res);
                this.allChildren = this.getEveryChildren(res);
                console.log("allChildren---------");
                console.log(this.allChildren);
                if (this.ruleSliders.length == 0) {
                    for (let i = 0; i < this.allChildren.length; i++) {
                        const childrenNow = this.allChildren[i];
                        //const ruleSlidersNow = [];
                        for (let j = 0; j < childrenNow.length - 1; j++) {
                            for (let z = j + 1; z < childrenNow.length; z++) {
                                let n1 = childrenNow[j];
                                let n2 = childrenNow[z];
                                this.ruleSliders.push({
                                    value: 0,
                                    text1: n1.topic,
                                    text2: n2.topic,
                                    id1: n1.id,
                                    id2: n2.id,
                                    location: n1.location,
                                    node1: n1,
                                    node2: n2
                                });
                            }
                        }
                    }
                }
                console.log(" this.ttttt()");
                console.log(this.allChildren);
            });
        },
        getEveryChildren(nodes) {
            // const nodes = [
            //   { "id": "root", "isroot": true, "topic": "jsMind" },
            //   { "id": "easy", "parentid": "root", "topic": "Easy", "direction": "left" },
            //   { "id": "easy1", "parentid": "easy", "topic": "Easy to show" },
            //   { "id": "easy2", "parentid": "easy", "topic": "Easy to edit" },
            //   { "id": "easy3", "parentid": "easy", "topic": "Easy to store" },
            //   { "id": "easy4", "parentid": "easy", "topic": "Easy to embed" },
            //   { "id": "easy41", "parentid": "easy4", "topic": "Easy 41" },
            //   { "id": "open", "parentid": "root", "topic": "Open Source", "expanded": false, "direction": "right" },
            //   { "id": "open1", "parentid": "open", "topic": "on GitHub" },
            //   { "id": "open2", "parentid": "open", "topic": "BSD License" },
            //   { "id": "powerful", "parentid": "root", "topic": "Powerful", "direction": "right" },
            //   { "id": "powerful1", "parentid": "powerful", "topic": "Base on Javascript" },
            //   { "id": "powerful2", "parentid": "powerful", "topic": "Base on HTML5" },
            //   { "id": "powerful3", "parentid": "powerful", "topic": "Depends on you" }
            // ]
            const allChildren = [];
            const getChildren = (nodeId) => {
                const children = [];
                nodes.forEach((node) => {
                    if (node.parentid === nodeId) {
                        children.push(node);
                    }
                });
                return children;
            };
            const traverseNodes = (node) => {
                const children = getChildren(node.id);
                console.log(children);
                if (children.length != 0) {
                    allChildren.push(children);
                }
                children.forEach((child) => { traverseNodes(child); });
                return allChildren;
            };
            const rootNode = nodes.find((node) => node.isroot);
            const result = traverseNodes(rootNode);
            console.log("result");
            console.log(result);
            return result;
        },
        //把准则层slider数组传入到后端
        sendRuleSliders() {
            console.log(JSON.stringify(this.ruleSliders));
            this.request.post("/node/sliderValues", this.ruleSliders).then(res => {
                if (res) {
                    console.log(res);
                    this.$message.success("传入成功! O(∩_∩)O哈哈~");
                    // this.dialogFormVisible = false
                    //this.init_data();
                    this.getNodeTopics();
                    window.sessionStorage.setItem("ruleResultRmap", JSON.stringify(res));
                    window.sessionStorage.setItem("ruleResultNodeTopics", JSON.stringify(this.nodeTopics));
                    console.log("ruleResultRmap");
                    console.log(JSON.parse(window.sessionStorage.getItem("ruleResultRmap")));
                    console.log("ruleResultNodeTopics");
                    console.log(JSON.parse(window.sessionStorage.getItem("ruleResultNodeTopics")));
                    //this.active++;
                    window.sessionStorage.setItem("active", parseInt(window.sessionStorage.getItem("active")) + 1);
                    console.log("send getItem~~~~~~~~~~~~~~");
                    console.log(window.sessionStorage.getItem("active"));
                    this.initActive();
                    this.isAvialable = false;
                    //this.$router.push({ name: 'ruleSingleResult', params: { rmap: res, nodeTopics: this.nodeTopics } });
                }
                else {
                    this.$message.error("更新失败");
                }
            });
        },
        resetRuleSliders() {
            this.ruleSliders = [];
            this.getRuleSliders2();
            //this.isAvialable = false
        },
        //把方案层slider数组传入到后端，汇总结果传到schemeSingleResultDetail
        sendSchemeSlidersToD() {
            console.log("---------schemeSliders------------");
            console.log(this.schemeSliders);
            this.request.post("/scheme/sliderValues2", this.schemeSliders).then(res => {
                if (res) {
                    console.log("sendSchemeSlidersToD");
                    console.log(res);
                    this.$message.success("O(∩_∩)O哈哈~schemeSliders");
                    this.getSchemesTopic();
                    this.$router.push({ name: "schemeSingleResultDetail", params: { rmap: res, schemeTopics: this.schemeTopics } });
                }
                else {
                    this.$message.error("更新失败");
                }
            });
        },
        resetSchemeSliders() {
            this.schemeSliders = [];
            this.getLeafNodes(window.sessionStorage.getItem("modelId"));
        },
        //得到所有scheme名字，放到schemeTopics数组中
        getSchemesTopic() {
            console.log("我在这呢！");
            for (var i = 0; i < this.schemes.length; i++) {
                this.schemeTopics.push(this.schemes[i].schemeName);
            }
        },
        //把方案层slider数组传入到后端,汇总结果传到schemeSingleResult
        sendSchemeSlidersToR() {
            console.log("---------sendSchemeSlidersToR slider------------");
            console.log(this.schemeSliders);
            this.request.post("/ns/sliderValues2", this.schemeSliders).then(res => {
                if (res) {
                    console.log("---------sendSchemeSlidersToR res-------");
                    console.log(res);
                    this.$message.success("O(∩_∩)O哈哈~");
                    this.getSchemesTopic();
                    window.sessionStorage.setItem("schemeResultRMarix", JSON.stringify(res));
                    window.sessionStorage.setItem("schemeResultNodeTopics", JSON.stringify(this.leafNodes));
                    window.sessionStorage.setItem("schemeResultSchemeTopics", JSON.stringify(this.schemeTopics));
                    console.log("schemeResultRMarix");
                    console.log(JSON.parse(window.sessionStorage.getItem("schemeResultRMarix")));
                    console.log("schemeResultNodeTopics");
                    console.log(JSON.parse(window.sessionStorage.getItem("schemeResultNodeTopics")));
                    console.log("schemeResultSchemeTopics");
                    console.log(JSON.parse(window.sessionStorage.getItem("schemeResultSchemeTopics")));
                    //this.active++;
                    window.sessionStorage.setItem("active", parseInt(window.sessionStorage.getItem("active")) + 1);
                    this.initActive();
                    this.isAvialable2 = false;
                    // this.$router.push({ name: 'schemeSingleResult', params: { rMarix: res, nodeTopics: this.leafNodes, schemeTopics: this.schemeTopics } });
                }
                else {
                    this.$message.error("更新失败");
                }
            });
        },
        //得到所有的节点名字，放到数组nodeTopics中
        getNodeTopics() {
            let nodeParent = this.jm.get_data("node_tree").data;
            let nodeParentNow = nodeParent;
            let j = 0;
            while (j <= nodeParent.children.length) {
                if (nodeParentNow.children != null) {
                    for (var i = 0; i < nodeParentNow.children.length; i++) {
                        this.nodeTopics.push(nodeParentNow.children[i].topic);
                    }
                }
                nodeParentNow = nodeParent.children[j++];
            }
            console.log("nodeTopics");
            console.log(this.nodeTopics);
        },
        // 生成唯一随机数
        generateUniqueRandom() {
            var timestamp = new Date().getTime();
            var randomNumber = Math.floor(Math.random() * 100) + 1;
            var uniqueRandom = timestamp.toString().substring(7) + randomNumber.toString();
            return uniqueRandom;
        },
        handleClick() {
            var uniqueRandom = this.generateUniqueRandom();
            console.log(uniqueRandom);
        },
        saveModelData() {
            console.log("this.modelForm");
            console.log(this.modelForm);
            // var nodes = this.jm.get_data("node_array").data;
            // console.log("----node_array------");
            // console.log(nodes);
            //生成惟一的随机数作为model_id
            //this.model_id = this.generateUniqueRandom();
            let randomModelId = this.generateUniqueRandom();
            window.sessionStorage.setItem("modelId", randomModelId);
            console.log("saveModelData!!!!!!");
            console.log(this.model_id);
            console.log("saveModelData-sessionStorage!!!!!!");
            console.log(window.sessionStorage.getItem("modelId"));
            const model = {
                modelId: window.sessionStorage.getItem("modelId"),
                modelName: this.modelForm.modelName,
                modelDetail: this.modelForm.modelDetail
            };
            this.saveModel(model);
        },
        saveModel(model) {
            this.request.post("/model/saveModel", model).then(res => {
                if (res) {
                    console.log(res);
                    this.$message.success("保存成功");
                    this.saveNodesData();
                    this.saveModelDialogVisible = false;
                    // this.load()
                }
                else {
                    this.$message.error("保存失败");
                }
            });
        },
        closeModelDialog() {
            this.saveModelDialogVisible = false;
            this.modelForm = {
                model_name: "",
                model_detail: ""
            };
        },
        saveNodesData() {
            var nodes = this.jm.get_data("node_array").data;
            console.log("----node_array------");
            console.log(nodes);
            // //生成惟一的随机数作为model_id
            // this.model_id = this.generateUniqueRandom();
            console.log("----saveNodesData------");
            console.log(this.model_id);
            // 循环遍历节点数据，并将每个节点的id属性名称改为node_id
            //1.map函数遍历节点数据数组
            //2.使用对象解构将原始对象的id属性和其他属性分开
            //3.创建一个新的对象，其中包含一个新的属性node_id，该属性的值等于原始对象的id属性值
            //4.我们使用展开运算符(...)将其他属性添加到新对象中，从而创建一个新的节点对象
            nodes = nodes.map((node) => {
                let nid = 1;
                const { id, ...rest } = node;
                return { modelId: window.sessionStorage.getItem("modelId"), nodeId: id, ...rest };
            });
            console.log("----node_array2------");
            console.log(nodes);
            nodes = this.transType(nodes);
            console.log("----transType 后------");
            console.log(nodes);
            this.saveNodes(nodes);
            //this.jm.remove_node(1);
            //this.traverseNodes(nodes, this.nodesData);
        },
        //把每个节点的id数据类型由string转化为int，递增即可。
        transType(nodeArray) {
            // const nodeArray = [
            //   { nodeId: '0', topic: '根节点', isroot: true },
            //   { nodeId: 'his', topic: '子节点1', parentid: '0' },
            //   { nodeId: 'gjs', topic: '子节点2', parentid: '0' },
            //   { nodeId: 'thc', topic: '子节点2的子节点1', parentid: 'gjs' },
            //   { nodeId: 'urw', topic: '子节点2的子节点2', parentid: 'gjs' },
            //   { nodeId: 'kfh', topic: '子节点3', parentid: '0' },
            //   { nodeId: 'ads', topic: '子节点3的子节点1', parentid: 'kfh' },
            //   { nodeId: 'jsw', topic: '子节点3的子节点2', parentid: 'kfh' },
            //   { nodeId: 'yhs', topic: '子节点3的子节点3', parentid: 'kfh' }
            // ];
            const nodeIdMap = {};
            let nodeIdCounter = 0;
            const transformedNodeArray = nodeArray.map(node => {
                const newNode = { ...node }; // 创建一个新节点对象
                if (!newNode.isroot) {
                    newNode.nodeId = nodeIdCounter++; // 将nodeId替换为递增的数字
                    newNode.parentid = nodeIdMap[newNode.parentid]; // 根据nodeIdMap替换parentid
                }
                if (newNode.isroot) {
                    newNode.nodeId = nodeIdCounter++; // 将nodeId替换为递增的数字
                }
                nodeIdMap[node.nodeId] = newNode.nodeId; // 存储nodeId和对应的数字值
                return newNode;
            });
            console.log("transformedNodeArray");
            console.log(transformedNodeArray); // 数组中的nodeId和parentid已经被修改
            console.log("nodeIdMap");
            console.log(nodeIdMap); // 存储了nodeId和数字值之间的对应关系
            return transformedNodeArray;
        },
        saveNodes(nodes) {
            // this.request.post("/node/save", nodes).then(res => {
            //   if (res) {
            //     console.log(res)
            //     this.$message.success("保存成功")
            //     // this.dialogFormVisible = false
            //     // this.load()
            //   } else {
            //     this.$message.error("保存失败")
            //   }
            // })
            this.request.post("/node/saveJsNodes", nodes).then(res => {
                if (res) {
                    console.log(res);
                    this.$message.success("保存成功");
                    // this.dialogFormVisible = false
                    // this.load()
                }
                else {
                    this.$message.error("保存失败");
                }
            });
        },
        updateNodesData() {
            var nodes = this.jm.get_data("node_array").data;
            console.log("----nodes------");
            console.log(nodes);
            this.updateNodes(nodes);
            //this.jm.remove_node(1);
            //this.traverseNodes(nodes, this.nodesData);
        },
        updateNodes(nodes) {
            this.request.post("/node/update", nodes).then(res => {
                if (res) {
                    console.log(res);
                    this.$message.success("更新成功");
                    // this.dialogFormVisible = false
                    this.init_data();
                }
                else {
                    this.$message.error("更新失败");
                }
            });
        },
    },

};
</script>
  
<style scoped>
#jsmind_container {
    width: 100%;
    height: 500px;
}

.dashed-box {
    border: 2px dashed #999;
    width: 1000px;
    height: 500px;
    margin: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
}

.text {
    font-size: 14px;
}

.avatar {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    cursor: pointer;
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
    width: 600px;
}

.el-tag+.el-tag {
    margin-left: 10px;
}

.button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
}

/* #jsmind_container {
    width: 800px;
    height: 600px;
  } */

.input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
}
</style>