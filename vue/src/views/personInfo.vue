<template>
    <div class="user-info-container">
        <el-page-header @back="goBack" content="用户信息" style="margin-bottom: 20px;">
        </el-page-header>
        <el-card class="user-info-card">
            <!-- <h3 class="user-info-title">用户信息</h3> -->
            <el-form ref="userInfoForm" :model="userInfo" label-width="80px" class="user-info-form">
                <el-form-item label="头像">
                    <el-avatar size="large" :src="userInfo.avatar"></el-avatar>
                </el-form-item>
                <el-form-item label="用户名">
                    <el-input v-model="userInfo.username" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="userInfo.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input v-model="userInfo.phone" autocomplete="off"></el-input>
                </el-form-item>
                <!-- <el-form-item>
                    <el-button type="primary" @click="submitForm">保存</el-button>
                    <el-button @click="resetForm">重置</el-button>
                </el-form-item> -->
            </el-form>
        </el-card>
    </div>
</template>
  
<script>
import axios from 'axios'

export default {
    data() {
        return {
            userInfo: {
                avatar: '<https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png>',
                username: 'John Doe',
                email: 'johndoe@example.com',
                phone: '1234567890'
            }
        }
    },
    created() {
        //   axios.get('/api/userinfo').then(res => {
        //     this.userInfo = res.data
        //   }).catch(err => {
        //     console.error(err)
        //   })
        this.userInfo = JSON.parse(window.sessionStorage.getItem("user"));
    },
    methods: {
        goBack() {
            this.$router.push('/');
        },
        submitForm() {
            this.$refs.userInfoForm.validate(valid => {
                if (valid) {
                    // 发送保存请求
                    console.log('保存成功')
                } else {
                    console.log('保存失败')
                    return false
                }
            })
        },
        resetForm() {
            this.$refs.userInfoForm.resetFields()
        }
    }
}
</script>
  
<style scoped>
/* .user-info-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f5f5f5;
   
} */

.user-info-card {
    width: 50%;
    margin-left: 20%;
    margin-top: 5%;
}

.user-info-title {
    font-size: 24px;
    text-align: center;
    margin: 20px 0;
}

.user-info-form {
    margin-top: 40px;
}
</style>