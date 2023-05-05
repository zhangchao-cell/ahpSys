<template>
  <div class="login-container">
    <el-card class="login-card">
      <h3 class="login-title">欢迎登录</h3>
      <el-form ref="loginForm" :model="loginForm" :rules="rules" label-width="80px" class="login-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
  
<script>
export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          // 发送登录请求
          this.request.post("/user/logon", this.loginForm).then(res => {
            if (res) {
              console.log("res")
              console.log(res)
              this.$message.success("登录成功! O(∩_∩)O哈哈~");
              // this.dialogFormVisible = false
              //this.init_data();
              //this.getNodeTopics();
              window.sessionStorage.setItem("user", JSON.stringify(res.data));
              
              console.log("user");
              console.log(JSON.parse(window.sessionStorage.getItem("user")));

             
              this.$router.push("/");
            } else {
              this.$message.error("登录失败");
            }
          })
          // console.log('登录成功')
          // console.log(this.loginForm)
        } else {
          console.log('验证失败')
          return false
        }
      })
    }
  }
}
</script>
  
<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-card {
  width: 400px;
}

.login-title {
  font-size: 24px;
  text-align: center;
  margin: 20px 0;
}

.login-form {
  margin-top: 40px;
}</style>