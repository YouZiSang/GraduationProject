<script setup>
import {ref, reactive} from 'vue'
import {useRouter} from "vue-router";

import request from "@/utils/request"
import SexSelect from "@/components/SexSelect.vue";

const registerForm = reactive({
  nickname: '',
  password: '',
  sex: '',
  confirmPassword: '',
  phone: '',
})
const isRegister = ref(false)
const uid = ref('')
const checkConfirmPassword = (rules, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('输入密码不一致'))
  } else {
    callback()
  }
}
const rules = {
  nickname: [
    {required: true, message: "昵称不可以为空", trigger: "blur"}
  ],
  password: [
    {required: true, message: "密码不可以为空", trigger: "blur"},
    {min: 8, max: 16, message: "长度为8-16个字符", trigger: "blur"},
    {pattern: /^(?=.*\d)(?=.*[a-zA-Z])(?!.*\s).{8,16}$/, message: "必须包含字母、数字", trigger: "blur"}
  ],
  confirmPassword: [
    {validator: checkConfirmPassword, trigger: "blur"}
  ],
  phone: [
    {required: true, message: "请填写电话号码", trigger: "blur"},
    {pattern: /^1[3456789]\d{9}$/, message: "号码应以数字，以1开头，且长度为11位", trigger: "blur"}
  ]
}

const router = useRouter();

function login() {
  // 跳转到登录页面，并将user_id传递给登录页
  router.push({
    path: '/login',
    query: {
     uid: uid.value
    }
  });
}

//调用后端函数接口，完成注册
const register = async () => {

  const data = {
    password: registerForm.password,
  };

  const res = await request.post('/user/register', data);

  if (res.code === 0) {
    uid.value = res.data;
    const userInfo = {
      uid: uid.value,
      nickname: registerForm.nickname,
      sex: registerForm.sex,
      phone: registerForm.phone
    };
    const response = await request.post('/userInfo/register', userInfo);
      isRegister.value = true;
      console.log(response.message);

  } else {
    console.error(res.message);
  }
}
</script>
<template>
  <div class="register_container">
    <div class="content">
      <div class="text" v-if="!isRegister">
        <div class="welcome">欢迎注册青少年心理咨询</div>
        <div class="header">每一天，都要有好心情</div>
        <el-form :model="registerForm" :rules="rules">
          <el-form-item prop="nickname">
            <el-col :span="16">
              <el-input v-model="registerForm.nickname" placeholder="昵称" maxlength="18"
                        clearable></el-input>
            </el-col>
          </el-form-item>
          <el-form-item prop="password">
            <el-col :span="16">
              <el-input v-model="registerForm.password" placeholder="密码" maxlength="16" minlength="6"
                        clearable></el-input>
            </el-col>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-col :span="16">
              <el-input v-model="registerForm.confirmPassword" placeholder="确认密码" maxlength="16" minlength="6"
                        clearable></el-input>
            </el-col>
          </el-form-item>
          <el-form-item prop="sex">
            <SexSelect v-model="registerForm.sex" style="width: 280px" value=""/>
          </el-form-item>
          <el-form-item prop="phone">
            <el-col :span="16">
              <el-input v-model="registerForm.phone" placeholder="电话" maxlength="11"
                        clearable></el-input>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="register" style="width: 280px">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-else class="appliedContent">
        <div class="filed"><i class="el-icon-success" style="color:#60cd8c;margin-right: 10px"></i>申请成功，您获得账号 ：
        </div>
        <div class="appliedNumber">{{ uid }}</div>
        <div class="login">
          <el-button type="primary" style="font-size: 20px" @click="login">立即登录</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
<style>
img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.register_container {
  background-image: url("../../public/images/background.jpg");
  min-height: 950px;
  background-size: cover;
  background-attachment: fixed;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.welcome {
  font-size: 38px;
  line-height: 1.5;
  font-weight: 500;
  margin-bottom: 8px;
}

.header {
  font-size: 18px;
  margin-bottom: 20px;
  line-height: 1.3;
}

.filed {
  font-size: 25px;
  margin-bottom: 20px;
}

.appliedNumber {
  color: #de6262;
  font-size: 40px;
  margin-bottom: 20px;
}

</style>
