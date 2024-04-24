<script setup>
import {ref, onMounted} from 'vue'
import {useRoute, useRouter} from "vue-router";
import {userLoginService} from "@/api/user";
import {consultantPersonalInfoService, consultantsLoginService} from "@/api/consultants.js";
import {userPersonalInfoService} from "@/api/userInfo.js";

import {useTokenStore} from "@/stores/token";
import router from "@/router";
import {ElMessage} from "element-plus";
import useUserInfoStore from "@/stores/userInfo.js";
import {adminLoginService, adminPersonalService} from "@/api/admin.js";
import {getRole, getId, getUserInfo} from "@/utils/getCurrentUserInfo.js";

const userInfoStore = useUserInfoStore();
const tokenStore = useTokenStore()
const formLogin = ref({
  uid: useRoute().query.uid || '',
  password: '',
  verCode: '',
  key: ''
})

const role = ref('')
const captchaURL = ref('')
onMounted(() => {
  formLogin.value.key = Math.random().toString();

  captchaURL.value = 'http://localhost:8080/api/captcha/img?key=' + formLogin.value.key;
});


const login = async () => {
  const params = {
    uid: formLogin.value.uid,
    password: formLogin.value.password,
    role: role.value,
    verCode: formLogin.value.verCode,
    key: formLogin.value.key,
  };

  if (role.value === 'ADMIN') {
    clickImg();
    formLogin.value.password = '';
    let res = await adminLoginService(params);
    ElMessage.success(res.message);
    tokenStore.setToken(res.data);
    userInfoStore.setInfo(params);

    router.push({ path: "/admin/home" });
  } else if (role.value === 'CONSULTANT') {
    // 老师登录
    clickImg();
    formLogin.value.password = '';
    let res = await consultantsLoginService(params);
    tokenStore.setToken(res.data);
    ElMessage.success(res.message);

    const getUserInfo = async () => {
      //调用接口
      let res = await consultantPersonalInfoService();
      let { cid, avatarUrl, name } = res.data;
      let params = { cid, avatarUrl, name, role: role.value };
      userInfoStore.setInfo(params);
    }
    await getUserInfo();
    router.push({ path: "/admin/home" });
  } else if (role.value === 'USER') {
    clickImg();
    formLogin.value.password = '';
    let res = await userLoginService(params);
    tokenStore.setToken(res.data);
    ElMessage.success(res.message);

    const getUserInfo = async () => {
      //调用接口
      let res = await userPersonalInfoService();
      let { uid, avatarUrl, nickname } = res.data;
      let params = { uid, avatarUrl, nickname, role: role.value };
      userInfoStore.setInfo(params);
    }
    await getUserInfo();
    router.push({ path: "/user/home" });
  }
}
onMounted(async () => {
  await clickImg()
})
const clickImg = () => {
  formLogin.value.key = Math.random().toString()
  captchaURL.value = 'http://localhost:8080/api/captcha/img?key=' + formLogin.value.key
}

</script>

<template>
  <div class="page_account" id="account_login">
    <div class="login_screen">
      <!--登录-->
      <div class="form_title">青少年心理咨询服务平台</div>
      <el-form :model="formLogin">
        <div class="form_login">
          <el-form-item label="">
            <el-input v-model="formLogin.uid" prefix-icon="el-icon-user" placeholder="请输入账号" maxlength="12"
                      clearable></el-input>
          </el-form-item>
        </div>
        <div class="form_login">
          <el-form-item label="">
            <el-input v-model="formLogin.password" prefix-icon="el-icon-unlock" placeholder="请输入密码" maxlength="18"
                      show-password></el-input>
          </el-form-item>
        </div>
        <div class="form_login" style="margin-bottom: 20px">
          <el-radio-group v-model="role">
            <el-radio label="ADMIN" style="margin-right: 15px">管理员</el-radio>
            <el-radio label="CONSULTANT" style="margin-right: 15px">咨询师</el-radio>
            <el-radio label="USER" style="margin-right: 15px">用户</el-radio>
          </el-radio-group>
        </div>
        <el-form-item>
          <div class="form_login">
            <el-input v-model="formLogin.verCode" placeholder="请输入验证码"
                      style="margin-right:10px;width: 30%;"></el-input>
            <img :src="captchaURL" @click="clickImg" style="width: 20%" alt=""/>
          </div>
        </el-form-item>
        <div class="form_login">
          <el-button type="primary" style="width: 230px;margin-bottom: 20px" @click="login">登录</el-button>
        </div>
        <div style="font-family:'微软雅黑';font-size: 14px">
          <span style="text-align: center;margin-right: 10px">没有账号?</span>
          <router-link to="/register" style="text-decoration: none">点击注册</router-link>
        </div>
      </el-form>

    </div>
  </div>
</template>

<style scoped>

.page_account {
  background-image: url("../../public/images/background.jpg");
  min-height: 950px;
  background-size: cover;
  background-attachment: fixed;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login_screen {
  padding: 1rem;
  width: 400px;
  height: 400px;
  justify-content: center;
  align-items: center;
  border-radius: 30px;
  background-color: rgba(255, 255, 255, 0.5)
}

.form_title {
  font-size: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px 0 30px 0;
}

.form_login {
  width: 400px;
  display: flex;
  justify-content: center;
  align-items: center
}

el-button {
  height: 200px;
}

</style>