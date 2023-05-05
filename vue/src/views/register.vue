<template>
    <div class="register-container">
        <el-card class="register-card">
            <h3 class="register-title">欢迎注册</h3>
            <el-form ref="registerForm" :model="registerForm" :rules="rules" label-width="80px" class="register-form">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="registerForm.username" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="registerForm.password" type="password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input v-model="registerForm.confirmPassword" type="password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="registerForm.email"  autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="registerForm.phone"  autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm">注册</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            registerForm: {
                username: '',
                password: '',
                confirmPassword: '',
                email: '',
                phone: ''
            },
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
                ],
                confirmPassword: [
                    { required: true, message: '请再次输入密码', trigger: 'blur' },
                    { validator: this.validateConfirmPassword, trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        validateConfirmPassword(rule, value, callback) {
            if (value === '') {
                callback(new Error('请再次输入密码'))
            } else if (value !== this.registerForm.password) {
                callback(new Error('两次输入密码不一致'))
            } else {
                callback()
            }
        },
        submitForm() {
            this.$refs.registerForm.validate(valid => {
                if (valid) {
                    // 发送注册请求
                    this.request.post("/user/register", this.registerForm).then(res => {
                        if (res) {
                            console.log("res")
                            console.log(res)
                            this.$message.success("注册成功! O(∩_∩)O哈哈~");
                            this.$router.push("/logon");
                        } else {
                            this.$message.error(res.msg);
                        }
                    })
                  
                } else {
                    
                    return false
                }
            })
        }
    }
}
</script>
  
<style scoped>
.register-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f5f5f5;
}

.register-card {
    width: 400px;
}

.register-title {
    font-size: 24px;
    text-align: center;
    margin: 20px 0;
}

.register-form {
    margin-top: 40px;
}
</style>
  