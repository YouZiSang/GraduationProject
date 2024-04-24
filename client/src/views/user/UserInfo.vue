<script setup>
import {ref, computed, onMounted} from "vue"
import {useRouter} from "vue-router";

const router = useRouter();
const headers = [
  {prop: "uid", label: "账号"},
  {prop: "name", label: "姓名"},
  {prop: "nickname", label: "昵称"},
  {prop: "sex", label: "性别"},
  {prop: "age", label: "年龄"},
  {prop: "email", label: "电子邮件 "},
  {prop: "phone", label: "电话 "},
  {prop: "deliveryAdd", label: "收货地址 "}
];
//数组对象
const userInfo = ref([{
  uid: ' 未填写',
  name: '未填写',
  nickname: '未填写',
  sex: '未填写',
  age: '未填写',
  email: '未填写',
  phone: '未填写',
  deliveryAdd: '未填写',
}])
const editInfo = ref({
  userId: ' ',
  name: '未填写',
  nickname: '未填写',
  avatarUrl: '',
  sex: '未填写',
  age: '未填写',
  email: '未填写',
  phone: '未填写',
  deliveryAdd: '未填写',
})

import {editAvatar, editInfoService, userPersonalInfoService} from "@/api/userInfo";
import AgeSelect from "@/components/AgeSelect.vue";
import {Collection, Female, Iphone, Male, Message, Postcard, Sunset, User} from "@element-plus/icons-vue";
import {right} from "core-js/internals/array-reduce.js";
import useUserInfoStore from "@/stores/userInfo.js";
import {uploadSingleFile} from "@/api/upload.js";


//把用户信息显示再卡片上
const getUserInfo = async () => {
  let result = await userPersonalInfoService()
    userInfo.value = result.data;
  console.log(result.data)

}

//编辑按钮
const editPersonalInfo = ref(false)
//头像文件
const fileInput = ref(null);
//取消按钮
const Cancel = () => {
  editPersonalInfo.value=false
}
const handleClick = () => {
  // 打开文件选择对话框
  fileInput.value.click();
};
let file = ref(null)
const avatar = ref('')
const justSelect = (file) => {
  if (file.raw) {
    editInfo.value.avatarUrl = URL.createObjectURL(file.raw);
    avatar.value = file;
  }
}

const editUserInfo = async () => {
    editInfo.value = userInfo.value

    editPersonalInfo.value = true
  // let result = await userPersonalInfoService()

}
const userInfoStore = useUserInfoStore();

const iconStyle = computed(() => {
  return {
    fontSize: '20px',
    marginRight: '10px'
  }
})
const ConfirmTheChanges = async () => {
  //上传头像到七牛云
  if(avatar.value){
    const formData = new FormData();
    formData.append('file', avatar.value.raw)
    let avatarResult = await uploadSingleFile(formData);
    //把头像赋值给修改表单
    editInfo.value.avatarUrl = avatarResult.data
  }
  let res = await editInfoService(editInfo.value);
  let params = {
    uid: editInfo.value.uid,
    avatarUrl: editInfo.value.avatarUrl,
    nickname: editInfo.value.nickname,
    role: 'USER'
  };
  userInfoStore.setInfo(params);
    await getUserInfo();
    //把修改信息赋值给卡片
    editPersonalInfo.value=false
    ElMessage.success(res.message)

};
const upGrade = ()=>{
  router.push({path: "/user/upGrade"});
}
import Breadcrumb from '@/components/Breadcrumb.vue';
onMounted(() => {
  getUserInfo();
});

</script>

<template>
  <div class="container-div">
  <div class="userInfo">
    <el-descriptions
        class="descriptions-info"
        size="large"
        border
        style="width: 1200px;margin-top: 100px;box-shadow: #8c939d 0px 0px 10px 0px;padding: 10px;background-color: white;margin-left: 100px;margin-top: 200px;margin-bottom: 50px"
    >
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <User/>
            </el-icon>
            账号
          </div>
        </template>
        {{ userInfo.uid }}
      </el-descriptions-item>
      <el-descriptions-item  width="150px">
        <template #label>
          <div class="cell-item" >
            <el-icon :style="iconStyle">
              <User/>
            </el-icon>
            姓名
          </div>
        </template>
        {{ userInfo.name }}
      </el-descriptions-item>
      <el-descriptions-item  width="150px">
        <template #label>
          <div class="cell-item">
            昵称
          </div>
        </template>
        {{ userInfo.nickname }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <Male/>
            </el-icon>
            <el-icon :style="iconStyle">
              <Female/>
            </el-icon>
            性别
          </div>
        </template>
        {{ userInfo.sex }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <Sunset/>
            </el-icon>
            年龄
          </div>
        </template>
        {{ userInfo.age }}
      </el-descriptions-item>
      <el-descriptions-item label="电子邮箱">
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <Message/>
            </el-icon>
            电子邮箱
          </div>
        </template>
        {{ userInfo.email }}
      </el-descriptions-item>
      <el-descriptions-item  width="150px">
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <Iphone/>
            </el-icon>
            电话
          </div>
        </template>
        {{userInfo.phone}}
      </el-descriptions-item>
      <el-descriptions-item :span="3">
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <Postcard/>
            </el-icon>
            收货地址
          </div>
        </template>
        {{ userInfo.deliveryAdd }}
      </el-descriptions-item>
    </el-descriptions>
    <div style="text-align: center">
      <el-button @click="editUserInfo" type="primary">修改资料</el-button>
      <el-button @click="upGrade" type="warning" plain>升级</el-button>
    </div>
  </div>
    <el-dialog v-model="editPersonalInfo" style="width: 600px">
      <el-form :model="editInfo" :label-position='right' label-width="100px" class="form">
        <div class="container">
          <div >
            <el-upload
                v-model="avatar"
                :show-file-list="false"
                :on-change="justSelect"
                :auto-upload="false">
              <el-avatar :src="editInfo.avatarUrl" :size="140" style="margin-bottom: 20px"/>
            </el-upload>
          </div>
        </div>
        <div style="justify-content: center;">
          <el-form-item prop="nickname" label="昵称：">
            <el-input v-model="editInfo.nickname" clearable></el-input>
          </el-form-item>
          <el-form-item prop="name" label="姓名：">
            <el-input v-model="editInfo.name" clearable></el-input>
          </el-form-item>
          <el-form-item prop="sex" label="性别：">
            <SexSelect v-model="editInfo.sex"/>
          </el-form-item>
          <el-form-item prop="age" label="年龄：">
            <AgeSelect v-model="editInfo.age"/>
          </el-form-item>
          <el-form-item prop="email" label="电子邮件：">
            <el-input v-model="editInfo.email" clearable></el-input>
          </el-form-item>
          <el-form-item prop="phone" label="电话：">
            <el-input v-model="editInfo.phone" maxlength="11" clearable></el-input>
          </el-form-item>
          <el-form-item prop="delivery_add" label="收货地址：">
            <el-input v-model="editInfo.deliveryAdd" clearable></el-input>
          </el-form-item>
        </div>
        <div class="container">
          <el-button type="primary" @click="ConfirmTheChanges">确认</el-button>
          <el-button @click="Cancel">取消</el-button>
        </div>
      </el-form>
    </el-dialog>

  </div>
</template>

<style lang="scss" scoped>
.container-div{
  min-height: 670px;
  display: flex;
  width: 100%;
  background-image: url("../../../public/images/background.jpg");
  background-size: cover; /* 图片按比例铺满整个背景 */
}
.avatar{

  width: 150px;
  height: 150px;
}
.image-container {
  margin: 0 auto;
  width: 150px;
  height: 150px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 50%;

}
.userInfo {

  display: block;
  justify-content: center;
  align-items: center;
  min-height: 670px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .el-card {

    text-align: center;
    :deep(.el-table tr,td,th)
    {

      border-color: #232323;
    }

    .el-button {
      height: 40px;
      width: 200px;
      margin-top: 20px;
    }

  }






  :deep(.el-dialog) {
    width: 600px;

    .form {
      margin: auto;
      display: flex;
      flex-direction: column;
      width: 400px;

    }
  }

}
.container {

  display: flex;
  justify-content: center;

  .el-button {
    width: 120px;
    height: 40px;
  }
}

</style>