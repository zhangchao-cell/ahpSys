<template>
  <!-- 右键菜单 -->
  <div class="jsmind_layout">
    <div class="jsmind_toolbar" v-if="showBar">
      <el-upload
        class="pad"
        :multiple="false"
        ref="upload"
        action="action"
        :before-upload="beforeUpload"
        :http-request="upload">
        <el-button type="primary" size="medium">导入</el-button>
      </el-upload>
      <el-button @click="save_nodearray_file" size="medium">保存</el-button>
      <el-button @click="screen_shot" size="medium">下载导图</el-button>
      <el-button @click="get_nodearray_data" size="medium">获取数据</el-button>
      <span class="pad-left">展开：</span>
      <el-select v-model="level" placeholder="展开节点" @change="expand_to_level" class="pad pad-left">
        <el-option
          v-for="item in nodeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <span>主题：</span>
      <el-select v-model="theme" placeholder="选择主题" @change="set_theme">
        <el-option
          v-for="item in themeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </div>
    <div id="jsmind_container" ref="container">
      <div class="zoom_in_out">
        <span><i class="zoom-icon el-icon-plus" @click="zoomIn" :class="isZoomIn === true ? 'disabled' : ''"></i></span>
        <span><i class="zoom-icon el-icon-minus" @click="zoomOut" :class="isZoomOut === true ? 'disabled' : ''"></i></span>
      </div>
    </div>
  </div>
</template>

<script>
import 'jsmind/style/jsmind.css'
import jsMind from 'jsmind/js/jsmind.js'
window.jsMind = jsMind

const { init, reBuild } = require('@/assets/js/jsmind.menu.js')
require('jsmind/js/jsmind.draggable.js')
require('jsmind/js/jsmind.screenshot.js')
init(jsMind)
export default {
  props: {
    showBar: { // 是否显示工具栏
      type: Boolean,
      default: true
    },
    isEdit: { // 是否启用编辑，启用编辑可以显示右键功能
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      mind: {},
      jm: null,
      isZoomIn: false,
      isZoomOut: false,
      level: 0,
      nodeOptions: [
        { value: 1, label: '展开到一级节点' },
        { value: 2, label: '展开到二级节点' },
        { value: 3, label: '展开到三级节点' },
        { value: 4, label: '展开到四级节点' },
        { value: 0, label: '展开全部节点' },
        { value: -1, label: '隐藏全部节点' }
      ],
      themeOptions: [
        { value: 'default', label: 'default' },
        { value: 'primary', label: 'primary' },
        { value: 'warning', label: 'warning' },
        { value: 'danger', label: 'danger' },
        { value: 'success', label: 'success' },
        { value: 'info', label: 'info' },
        { value: 'greensea', label: 'greensea' },
        { value: 'nephrite', label: 'nephrite' },
        { value: 'belizehole', label: 'belizehole' },
        { value: 'wisteria', label: 'wisteria' },
        { value: 'asphalt', label: 'asphalt' },
        { value: 'orange', label: 'orange' },
        { value: 'pumpkin', label: 'pumpkin' },
        { value: 'pomegranate', label: 'pomegranate' },
        { value: 'clouds', label: 'clouds' },
        { value: 'asbestos', label: 'asbestos' }
      ],
      theme: 'success'
    }
  },
  created() {
  },
  mounted() {
    this.getData()
    this.mouseWheel()
  },
  methods: {
    beforeUpload (file) { // 上传文件之前钩子
      if (file) {
        jsMind.util.file.read(file,(jsmind_data) => {
          const mind = jsMind.util.json.string2json(jsmind_data)
          if(mind){
            this.jm.show(mind)
            reBuild()
          }else{
            this.prompt_info('can not open this file as mindmap')
          }
        })
      } else {
        this.prompt_info('please choose a file first')
        return false
      }
    },
    upload() {},
    getData() {
      this.$API({
        name: 'getMind'
      }).then(res => {
        this.mind = res.data
        this.open_empty()
      }).catch(error => {
        this.$message.error(error)
      })
    },
    open_empty() {
      const options = {
        container: 'jsmind_container', // 必选，容器ID
        editable: this.isEdit, // 可选，是否启用编辑
        theme: this.theme, // 可选，主题
        view: {
          line_width: 2, // 思维导图线条的粗细
          line_color: 'skyblue' // 思维导图线条的颜色
        },
        shortcut: {
          enable: false // 禁用快捷键
        },
        mode: 'side', // 显示模式，子节点只分布在根节点右侧
        menuOpts:{  // 这里加入一个专门配置menu的对象
          showMenu: this.isEdit, //showMenu 为 true 则打开右键功能 ，反之关闭
          injectionList: [
            { target: 'edit', text: '编辑节点' },
            { target: 'delete', text: '删除节点' },
            { target: 'addChild', text: '添加子节点' },
            { target: 'addBrother', text: '添加兄弟节点' }
          ],
          style: {
            menuItem:{
              'line-height': '28px'
            }
          }
        }
      }
      this.jm = jsMind.show(options, this.mind)
      // 改变窗口大小重置画布
      window.onresize = () => {
        this.jm.resize()
      }
    },
    save_nodearray_file(){
      const mind_data = this.jm.get_data('node_array')
      const mind_name = mind_data.meta.name
      const mind_str = jsMind.util.json.json2string(mind_data)
      jsMind.util.file.save(mind_str, 'text/jsmind', mind_name + '.jm')
    },
    screen_shot(){
      this.jm.screenshot.shootDownload()
    },
    expand_all(){
      this.jm.expand_all()
    },
    collapse_all(){
      this.jm.collapse_all()
    },
    expand_to_level(num){
      switch(num) {
        case -1:
          this.collapse_all()
          break
        case 0:
          this.expand_all()
          break
        default:
          this.jm.expand_to_depth(num)
          break
      }
    },
    zoomIn() {
      if (this.jm.view.zoomIn()) {
        this.isZoomOut = false
      } else {
        this.isZoomIn = true
      }
    },
    zoomOut() {
      if (this.jm.view.zoomOut()) {
        this.isZoomIn = false
      } else {
        this.isZoomOut = true
      }
    },
    prompt_info(msg){
      alert(msg)
    },
    get_nodearray_data() {
      const mind_data = this.jm.get_data('node_array')
      const mind_string = jsMind.util.json.json2string(mind_data)
      this.prompt_info(mind_string)
    },
    set_theme(theme_name){
      this.jm.set_theme(theme_name)
    },
    scrollFunc(e) {
      e = e || window.event
      if (e.wheelDelta) {
        if (e.wheelDelta > 0) {
          this.zoomIn()
        } else {
          this.zoomOut()
        }
      } else if (e.detail) {
        if (e.detail > 0) {
          this.zoomIn()
        } else {
          this.zoomOut()
        }
      }
      this.jm.resize()
    },
    // 鼠标滚轮放大缩小
    mouseWheel() {
      if (document.addEventListener) {
        document.addEventListener('domMouseScroll', this.scrollFunc, false)
      }
      this.$refs.container.onmousewheel = this.scrollFunc
    }
  },
  beforeDestroy() {
    document.removeEventListener('domMouseScroll', this.scrollFunc, false)
  }
}
</script>

<style lang="less">
.jsmind_layout {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 100%;
  height: calc(100% - 40px);
  overflow: hidden;
  /deep/ .el-button--medium, /deep/ .el-input--medium {
    margin-top: 10px;
  }
}
jmnode.selected {
  background-color: #b9b9b9 !important;
  box-shadow: 2px 2px 8px #777 !important;
}
jmnode:hover {
  box-shadow: 2px 2px 8px #777 !important;
}
.jsmind_toolbar {
  width: 100%;
  padding: 10px;
  height: auto;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  background-color: #f8f9fa;
  box-shadow: 0 0 4px #b8b8b8;
}
#jsmind_container {
  flex: 1 1 auto;
  position: relative;
}
.jsmind-inner {
  overflow: hidden auto !important;
}
.el-upload-list {
  display: none !important;
}
.zoom_in_out {
  position: absolute;
  bottom: 20px;
  right: 20px;
  height: auto;
  display: flex;
  align-items: center;
  flex-direction: column;
  padding: 5px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 2px 2px 4px #e0e0e0;
  border: 1px solid #f5f5f5;
  z-index: 999;
}
.zoom-icon {
  cursor: pointer;
  font-size: 20px;
  padding: 6px 5px;
}
.disabled {
  color: #bdbcbc;
  cursor: not-allowed;
}
.el-icon-plus {
  border-bottom: 1px solid #9f9f9f;
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
</style>