<template>
    <!-- 普通菜单 -->
    <div class="jsmind_layout">
      <div class="jsmind_toolbar" v-if="showBar">
        <el-upload
          class="pad"
          :multiple="false"
          ref="upload"
          action="action"
          :before-upload="beforeUpload"
          :http-request="upload"
        >
          <el-button type="primary" size="medium">导入</el-button>
        </el-upload>
        <el-button @click="save_nodearray_file" size="medium">保存</el-button>
        <el-button @click="screen_shot" size="medium">下载导图</el-button>
        <el-button @click="get_nodearray_data" size="medium">获取数据</el-button>
        <el-button @click="addNode" size="medium">新增节点</el-button>
        <el-button @click="addBrotherNode" size="medium">新增兄弟节点</el-button>
        <el-button @click="editNode" size="medium">编辑节点</el-button>
        <el-button @click="removeNode" size="medium">删除节点</el-button>
        <el-button @click="zoomIn" size="medium" :disabled="isZoomIn"
          >放大</el-button
        >
        <el-button
          @click="zoomOut"
          size="medium"
          :disabled="isZoomOut"
          class="pad"
          >缩小</el-button
        >
        <span>展开：</span>
        <el-select
          v-model="level"
          placeholder="展开节点"
          @change="expand_to_level"
          class="pad pad-left"
          size="medium"
        >
          <el-option
            v-for="item in nodeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
        <span>主题：</span>
        <el-select
          v-model="localTheme"
          placeholder="选择主题"
          @change="set_theme"
          size="medium"
        >
          <el-option
            v-for="item in themeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
        <el-button>切换菜单</el-button>
      </div>
      <div id="jsmind_container"></div>
      <!-- <div id="jsmind_container" ref="container"></div> -->
      <el-drawer title="编辑节点" :visible.sync="dialogVisible" size="500px">
        <el-form label-width="80px" class="form-con">
          <el-form-item label="字体大小">
            <el-input-number
              controls-position="right"
              v-model.number="nodeOption.fontSize"
              class="ele-width"
              :min="1"
              :max="30"
              maxLength="2"
            ></el-input-number>
          </el-form-item>
          <el-form-item label="字体粗细">
            <el-select v-model="nodeOption.fontWeight" class="ele-width">
              <el-option value="normal" label="常规"></el-option>
              <el-option value="bold" label="粗体"></el-option>
              <el-option value="bolder" label="更粗"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="字体样式">
            <el-select v-model="nodeOption.fontStyle" class="ele-width">
              <el-option value="normal" label="标准"></el-option>
              <el-option value="italic" label="斜体"></el-option>
              <el-option value="oblique" label="倾斜"></el-option>
            </el-select>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="背景颜色">
                <el-color-picker
                  v-model="nodeOption.bgColor"
                  show-alpha
                  size="mini"
                ></el-color-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="字体颜色">
                <el-color-picker
                  v-model="nodeOption.fontColor"
                  show-alpha
                  size="mini"
                ></el-color-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="节点内容">
            <el-input
              type="textarea"
              :rows="2"
              v-model="nodeOption.content"
              class="ele-width"
              maxLength="64"
            ></el-input>
          </el-form-item>
        </el-form>
        <template v-slot:footer>
          <div class="right mr-10">
            <el-button
              type="primary"
              class="common-btn"
              @click="sureEditNode"
              size="medium"
              >确 定</el-button
            >
          </div>
        </template>
      </el-drawer>
  
    </div>
  </template>
  
  <script>
  import "jsmind/style/jsmind.css";
  import jsMind from "jsmind/js/jsmind.js";
  window.jsMind = jsMind;
  
  require("jsmind/js/jsmind.draggable.js");
  require("jsmind/js/jsmind.screenshot.js");
  export default {
    props: {
      showBar: {
        // 是否显示工具栏，显示启用编辑
        type: Boolean,
        default: true,
      },
      theme: {
        // 主题
        type: String,
        default: "info",
      },
      lineColor: {
        // 线条颜色
        type: String,
        default: "skyblue",
      },
    },
    data() {
      return {
        i: 0,
        mind: {
          data: {
            id: 'root',
            topic: 'jsMind',
            children: [
              {
                id: 'easy', // [必选] ID, 所有节点的ID不应有重复，否则ID重复的结节将被忽略
                topic: 'Easy', // [必选] 节点上显示的内容
                direction: 'right', // [可选] 节点的方向，此数据仅在第一层节点上有效，目前仅支持 left 和 right 两种，默认为 right
                expanded: true, // [可选] 该节点是否是展开状态，默认为 true
                children: [
                  { id: 'easy1', topic: 'Easy to show' },
                  { id: 'easy2', topic: 'Easy to edit' },
                  { id: 'easy3', topic: 'Easy to store' },
                  { id: 'easy4', topic: 'Easy to embed' }
                ]
              },
              {
                id: 'open',
                topic: 'Open Source',
                direction: 'right',
                expanded: true,
                children: [
                  { id: 'open1', topic: 'on GitHub' },
                  { id: 'open2', topic: 'BSD License' }
                ]
              },
              {
                id: 'powerful',
                topic: 'Powerful',
                direction: 'right',
                children: [
                  { id: 'powerful1', topic: 'Base on Javascript' },
                  { id: 'powerful2', topic: 'Base on HTML5' },
                  { id: 'powerful3', topic: 'Depends on you' }
                ]
              },
              {
                id: 'other',
                topic: 'test node',
                direction: 'right',
                children: [
                  { id: 'other1', topic: "I'm from local variable" },
                  { id: 'other2', topic: 'I can do everything' }
                ]
              }
            ]
          }
  
        },
        jm: null,
        isZoomIn: false,
        isZoomOut: false,
        level: 0,
        nodeOptions: [
          { value: 1, label: "展开到一级节点" },
          { value: 2, label: "展开到二级节点" },
          { value: 3, label: "展开到三级节点" },
          { value: 0, label: "展开全部节点" },
          { value: -1, label: "隐藏全部节点" },
        ],
        themeOptions: [
          { value: "default", label: "default" },
          { value: "primary", label: "primary" },
          { value: "warning", label: "warning" },
          { value: "danger", label: "danger" },
          { value: "success", label: "success" },
          { value: "info", label: "info" },
          { value: "greensea", label: "greensea" },
          { value: "nephrite", label: "nephrite" },
          { value: "belizehole", label: "belizehole" },
          { value: "wisteria", label: "wisteria" },
          { value: "asphalt", label: "asphalt" },
          { value: "orange", label: "orange" },
          { value: "pumpkin", label: "pumpkin" },
          { value: "pomegranate", label: "pomegranate" },
          { value: "clouds", label: "clouds" },
          { value: "asbestos", label: "asbestos" },
        ],
        localTheme: this.theme,
        dialogVisible: false,
        nodeOption: {
          content: "",
          bgColor: "",
          fontColor: "",
          fontSize: "",
          fontWeight: "",
          fontStyle: "",
        },
      };
    },
    created() {},
    mounted() {
      this.getData();
      this.mouseWheel();
    },
    methods: {
      beforeUpload(file) {
        // 上传文件之前钩子
        if (file) {
          // const file = event.target.files[0];
  
          // console.log("zg" + file);
          // const reader = new FileReader();
          // reader.onload = (event) => {
          //   const fileResult = event.target.result;
          //   // const blob = new Blob([event.target.result], { type: file.type });
          //   // const url = URL.createObjectURL(blob);
          //   // 此处将 URL 传递给需要使用图片的组件进行显示
  
          //   const mind = jsMind.util.json.string2json(fileResult);
          //   if (mind) {
          //     this.jm.show(mind);
          //     this.$message({ type: "success", message: "打开成功" });
          //   } else {
          //     this.prompt_info("不能打开mindmap文件");
          //   }
          // };
          // reader.readAsArrayBuffer(file);
  
          jsMind.util.file.read(file, (data) => {
            console.log("1:" + data);
            const mind = jsMind.util.json.string2json(data);
            if (mind) {
              this.jm.show(mind);
              this.$message({ type: "success", message: "打开成功" });
            } else {
              this.prompt_info("不能打开mindmap文件");
            }
          });
        } else {
          this.prompt_info("请先选择文件");
          return false;
        }
      },
      upload() {},
      getData() {
        this.$API({
          name: "getMind",
        })
          .then((res) => {
            this.mind = res.data;
            this.open_empty();
          })
          .catch((error) => {
            this.$message.error(error);
          });
      },
      open_empty() {
        const options = {
          container: "jsmind_container", // 必选，容器ID
          editable: this.showBar, // 可选，是否启用编辑
          theme: this.localTheme, // 可选，主题
          view: {
            line_width: 2, // 思维导图线条的粗细
            line_color: this.lineColor, // 思维导图线条的颜色
          },
          shortcut: {
            enable: true, // 禁用快捷键
          },
          layout: {
            hspace: 50, // 节点之间的水平间距
            vspace: 20, // 节点之间的垂直间距
            pspace: 13, // 节点与连接线之间的水平间距（用于容纳节点收缩/展开控制器）
          },
          mode: "side", // 显示模式，子节点只分布在根节点右侧
        };
        this.jm = jsMind.show(options, this.mind);
        // 改变窗口大小重置画布
        window.onresize = () => {
          this.jm.resize();
        };
        this.getDepth(this.jm.mind.root, 1);
      },
      // 获取层级数 i
      getDepth(obj, k) {
        this.i = Math.max(this.i, k);
        if (obj.children) {
          obj.children.forEach((v) => {
            this.getDepth(v, k + 1);
          });
        }
      },
      save_nodearray_file() {
        const mindData = this.jm.get_data("node_array");
        const mindName = mindData.meta.name;
        const mindStr = jsMind.util.json.json2string(mindData);
        jsMind.util.file.save(mindStr, "text/jsmind", mindName + ".jm");
      },
      screen_shot() {
        this.jm.screenshot.shootDownload();
      },
      expand_all() {
        this.jm.expand_all();
      },
      collapse_all() {
        this.jm.collapse_all();
      },
      expand_to_level(num) {
        switch (num) {
          case -1:
            this.collapse_all();
            break;
          case 0:
            this.expand_all();
            break;
          default:
            this.jm.expand_to_depth(num);
            break;
        }
      },
      zoomIn() {
        if (this.jm.view.zoomIn()) {
          this.isZoomOut = false;
        } else {
          this.isZoomIn = true;
        }
      },
      zoomOut() {
        if (this.jm.view.zoomOut()) {
          this.isZoomIn = false;
        } else {
          this.isZoomOut = true;
        }
      },
      prompt_info(msg) {
        this.$message({ type: "warning", message: msg });
      },
      get_nodearray_data() {
        const mindData = this.jm.get_data("node_array");
        const mindString = jsMind.util.json.json2string(mindData);
        this.$message({ type: "info", message: mindString });
      },
      set_theme(themeName) {
        this.jm.set_theme(themeName);
      },
      scrollFunc(e) {
        e = e || window.event;
        if (e.wheelDelta) {
          if (e.wheelDelta > 0) {
            this.zoomIn();
          } else {
            this.zoomOut();
          }
        } else if (e.detail) {
          if (e.detail > 0) {
            this.zoomIn();
          } else {
            this.zoomOut();
          }
        }
        this.jm.resize();
      },
      // 鼠标滚轮放大缩小
      mouseWheel() {
        if (document.addEventListener) {
          document.addEventListener("domMouseScroll", this.scrollFunc, false);
        }
        this.$refs.container.onmousewheel = this.scrollFunc;
      },
      // 新增节点
      addNode() {
        let selectedNode = this.jm.get_selected_node();
        if (!selectedNode) {
          this.$message({ type: "warning", message: "请先选择一个节点!" });
          return;
        }
        let nodeid = jsMind.util.uuid.newid();
        let topic = "new Node";
        let newNode = this.jm.add_node(selectedNode, nodeid, topic);
        if (newNode) {
          this.jm.select_node(nodeid);
          this.jm.begin_edit(nodeid);
          this.getDepth(this.jm.mind.root, 1);
        }
      },
      // 新增兄弟节点
      addBrotherNode() {
        let selectedNode = this.jm.get_selected_node();
        if (!selectedNode) {
          this.$message({ type: "warning", message: "请先选择一个节点!" });
          return;
        } else if (selectedNode.isroot) {
          this.$message({
            type: "warning",
            message: "不能在根节点添加，请重新选择节点!",
          });
          return;
        }
        let nodeid = jsMind.util.uuid.newid();
        let topic = "new Node";
        let newNode = this.jm.insert_node_after(selectedNode, nodeid, topic);
        if (newNode) {
          this.jm.select_node(nodeid);
          this.jm.begin_edit(nodeid);
        }
      },
      // 获取选中标签的 ID
      get_selected_nodeid() {
        let selectedNode = this.jm.get_selected_node();
        if (selectedNode) {
          return selectedNode.id;
        } else {
          return null;
        }
      },
      // 删除节点
      removeNode() {
        let selectedId = this.get_selected_nodeid();
        if (!selectedId) {
          this.$message({
            type: "warning",
            message: "请先选择一个节点!",
          });
          return;
        }
        this.jm.remove_node(selectedId);
        this.i = 0;
        this.getDepth(this.jm.mind.root, 1);
      },
      // 编辑节点
      editNode() {
        let selectedId = this.get_selected_nodeid();
        if (!selectedId) {
          this.$message({ type: "warning", message: "请先选择一个节点!" });
          return;
        }
        let nodeObj = this.jm.get_node(selectedId);
        this.nodeOption.content = nodeObj.topic;
        this.nodeOption.bgColor = nodeObj.data["background-color"];
        this.nodeOption.fontColor = nodeObj.data["foreground-color"];
        this.nodeOption.fontSize = nodeObj.data["font-size"];
        this.nodeOption.fontWeight = nodeObj.data["font-weight"];
        this.nodeOption.fontStyle = nodeObj.data["font-style"];
        this.dialogVisible = true;
      },
      sureEditNode() {
        let selectedId = this.get_selected_nodeid();
        this.jm.update_node(selectedId, this.nodeOption.content);
        this.jm.set_node_font_style(
          selectedId,
          this.nodeOption.fontSize,
          this.nodeOption.fontWeight,
          this.nodeOption.fontStyle
        );
        this.jm.set_node_color(
          selectedId,
          this.nodeOption.bgColor,
          this.nodeOption.fontColor
        );
        this.nodeOption = {
          content: "",
          bgColor: "",
          fontColor: "",
          fontSize: "",
          fontWeight: "",
          fontStyle: "",
        };
        this.dialogVisible = false;
      },
    },
    beforeDestroy() {
      document.removeEventListener("domMouseScroll", this.scrollFunc, false);
    },
  };
  </script>
  
  <style lang="less" scoped>
  .jsmind_layout {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: calc(100% - 40px);
    overflow: hidden;
    .jsmind_toolbar {
      width: 100%;
      padding: 0 10px 10px 10px;
      height: auto;
      flex-shrink: 0;
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      background-color: #f8f9fa;
      box-shadow: 0 0 4px #b8b8b8;
    }
    /deep/ .el-button--medium,
    /deep/ .el-input--medium {
      margin-top: 10px;
    }
    #jsmind_container {
      flex: 1 1 auto;
    }
    /deep/.jsmind-inner {
      overflow: hidden auto !important;
    }
    /deep/.el-upload-list {
      display: none !important;
    }
    /* 隐藏滚动条 */
    .jsmind-inner::-webkit-scrollbar {
      display: none;
    }
    .pad {
      margin-right: 10px;
    }
    .pad-left {
      margin-left: 10px;
    }
    /deep/ jmnode.selected {
      background-color: #b9b9b9;
      color: #fff;
      box-shadow: 2px 2px 8px #777;
    }
    /deep/ jmnode:hover {
      box-shadow: 2px 2px 8px #777;
    }
    .form-con {
      padding-top: 20px;
    }
    .ele-width {
      width: 96%;
    }
  }
  </style>
  