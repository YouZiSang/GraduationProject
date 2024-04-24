<script setup lang="js">
import {ref, onMounted, reactive} from "vue";
import {userPersonalInfoService, upGradeService, statusUpGradeService} from "@/api/userInfo.js";
import {uploadSingleFile} from "@/api/upload.js";

const personalInfo = ref({
  avatarUrl: '',
  uid: 0,
  name: '未填写',
  sex: '未填写',
  age: 0,
  certificate: '未填写',
  biography: '未填写',
  email: '未填写',
  phone: 0,
})
const getPersonalInfo = async () => {
  let result = await userPersonalInfoService()
  personalInfo.value = result.data
}
const certificate = ref('')
const biography = ref('')
const avatar = ref()

const justSelect = (file) => {
  if (file.raw) {
    personalInfo.value.avatarUrl = URL.createObjectURL(file.raw);
    avatar.value = file;
  }
}
const upGrade = async () => {
  if (avatar.value) {
    const data = new FormData();
    data.append('file', avatar.value.raw);
    await uploadSingleFile(data).then(res => {
      personalInfo.value.avatarUrl = res.data
    })
  }

  let params = {
    id: personalInfo.value.id,
    uid: personalInfo.value.uid,
    name: personalInfo.value.name,
    avatarUrl: personalInfo.value.avatarUrl,
    sex: sex.value,
    age: personalInfo.value.age,
    certificate: certificate.value,
    email: personalInfo.value.email,
    phone: personalInfo.value.phone,
    biography: biography.value,
    status: '审核中'
  }

  let res = await upGradeService(params)

  ElMessage.success(res.message)
  statusOfUpGrade.value = '审核中'
}
const reset = () => {
  console.log(personalInfo.value[0].avatarUrl)
  personalInfo.value = ''
}
const statusOfUpGrade = ref('')
const getStatusOfUpGrade=async ()=>{
  let res = await statusUpGradeService()
  statusOfUpGrade.value = res.data.status

}
const sex = ref('')
const options = [
  {
    value: '男',
    label: '男',
  },
  {
    value: '女',
    label: '女',
  }]

onMounted(() => {
  getPersonalInfo()
  getStatusOfUpGrade()
})
</script>

<template>
  <div class="main-content">
    <div class="content-left">
      <p style="font-size: 40px;font-family: STXingkai">
        升级成为
      </p>
      <p style="font-size: 50px;font-family: STXingkai;color: coral">
        心理咨询师
      </p>
    </div>
    <div class="content-center">
      <div style="text-align: center;font-family: 华文楷体;font-size: 30px;margin-bottom: 50px">
        <p>请填写信息哦</p>
      </div>
      <div style="display: flex;justify-content: center;align-items: center;">
        <el-form style="width: 400px;display: block" :model="personalInfo">
          <div style="text-align: center;margin-bottom: 20px">
            <el-upload
                v-model="avatar"
                :show-file-list="false"
                :on-change="justSelect"
                :auto-upload="false">
              <el-avatar :src="personalInfo.avatarUrl" :size="100"/>
            </el-upload>
          </div>
          <el-form-item label="账号：">
            <el-input v-model="personalInfo.uid" disabled></el-input>
          </el-form-item>
          <el-form-item label="姓名：">
            <el-input v-model="personalInfo.name"></el-input>
          </el-form-item>
          <el-form-item label="性别：">
            <el-select
                v-model="sex"
                placeholder="Select"
            >
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="年龄：">
            <AgeSelect v-model="personalInfo.age"></AgeSelect>
          </el-form-item>
          <el-form-item label="证书：">
            <el-input v-model="certificate"></el-input>
          </el-form-item>
          <el-form-item label="邮箱：">
            <el-input v-model="personalInfo.email"></el-input>
          </el-form-item>
          <el-form-item label="电话：">
            <el-input v-model="personalInfo.phone"></el-input>
          </el-form-item>
          <el-form-item label="简介：">
            <el-input :autosize="{ minRows: 2, maxRows: 4 }"
                      type="textarea"
                      v-model="biography">
            </el-input>
          </el-form-item>
        </el-form>

      </div>
      <div style="text-align: center">
        <el-button @click="reset" type="info" plain>全部清空</el-button>
        <el-button @click="upGrade" type="primary">升级</el-button>
      </div>
    </div>
    <div style="width: 400px">
      <div>
        <p v-if="statusOfUpGrade ==='审核中'" style="font-size: 25px;font-family: 华文楷体">升级成为心理咨询师的信息真正审核中，请耐心等待~</p>
        <p v-if="statusOfUpGrade ==='审核未通过'" style="font-size: 25px;font-family: 华文楷体">根据你的现在的信息，还没有能够成为心理咨询师哦~</p>
      </div>
      <div style="margin-top: 200px">
        <p style="font-size: 40px;font-family:STXingkai;color: #0ebffc">加入我们</p>
        <p style="font-size: 50px;font-family: STSong;color: coral">一起探索心灵的奥秘</p>
        <p style="font-size: 45px;font-family:STXingkai;color: aquamarine">开启自我发现之旅</p>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.main-content {
  display: flex;
  padding: 20px;
}

.content-left {
  width: 300px;

}

.content-center {
  padding: 0 50px;
  width: 600px;
}
</style>