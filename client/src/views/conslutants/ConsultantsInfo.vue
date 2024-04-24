<script setup lang="ts">
import {ref, onMounted, reactive} from "vue";
import {consultantPersonalInfoService, editPersonalInfoService, statusEditService} from "@/api/consultants.js";
import {uploadSingleFile} from "@/api/upload.js";
import {Edit} from "@element-plus/icons-vue";
import {ElMessage, UploadFile} from "element-plus";
import useUserInfoStore from "@/stores/userInfo.js";

interface info {
  cid: number,
  name: string,
  avatarUrl: string,
  sex: string,
  age: number,
  certificate: string,
  biography: string,
  email: string,
  phone: number,
}

const personalInfo = ref<info[{}]>([{
  cid: 0,
  name: '未填写',
  avatarUrl: '',
  sex: '未填写',
  age: 0,
  certificate: '未填写',
  biography: '未填写',
  email: '未填写',
  phone: 0,
}])

const getPersonalInfo = async () => {
  let res = await consultantPersonalInfoService()
  personalInfo.value = res.data
}
const userInfoStore = useUserInfoStore();
const statusEdit = ref('')
const getStatus = async () => {
  let res = await statusEditService()
  if (res.data) {
    statusEdit.value = res.data.status
  }
  if (res.data.status==='审核通过')
  {
    let params = {
      cid: res.data.cid,
      avatarUrl: res.data.avatarUrl,
      name: res.data.name,
      role: 'CONSULTANT'
    };
    userInfoStore.setInfo(params);
  }
}

const getImagesUrl = (name) => {
  return new URL(name, import.meta.url).href
}

const imageList = reactive([
  {key: 'cid', src: getImagesUrl('../../assets/images/account.png'), alt: '账号'},
  {key: 'name', src: getImagesUrl('../../assets/images/name.png'), alt: '姓名'},
  {key: 'sex', src: getImagesUrl('../../assets/images/sex.png'), alt: '性别'},
  {key: 'age', src: getImagesUrl('../../assets/images/age.png'), alt: '年龄'},
  {key: 'certificate', src: getImagesUrl('../../assets/images/certificate.png'), alt: '资格证书'},
  {key: 'email', src: getImagesUrl('../../assets/images/email.png'), alt: '邮箱'},
  {key: 'phone', src: getImagesUrl('../../assets/images/phone.png'), alt: '电话'},
  {key: 'biography', src: getImagesUrl('../../assets/images/biography.png'), alt: '简介'}

]);

const dialog = ref(false)
const updateInfo = async () => {
  Object.assign(dialogInfo, JSON.parse(JSON.stringify(personalInfo.value)))
  dialog.value = true
}

const options = [
  {
    value: '男',
    label: '男',
  },
  {
    value: '女',
    label: '女',
  }
]

let dialogInfo = reactive({
  cid: 0,
  name: '',
  avatarUrl: '',
  sex: '',
  age: 0,
  certificate: '',
  biography: '',
  email: '',
  phone: 0,
  status: ''

})
const avatar = ref()
const avatarUrl = personalInfo.value[0].avatarUrl;


const justSelect = (uploadAvatar: UploadFile) => {
  if (uploadAvatar.raw) {
    dialogInfo.avatarUrl = URL.createObjectURL(uploadAvatar.raw);
    avatar.value = uploadAvatar;
  }

}
const confirm = async () => {
  dialogInfo.status = '审核中';
  if (avatar.value) {
    const data = new FormData();
    data.append('file', avatar.value.raw);
    await uploadSingleFile(data).then(res => {
      dialogInfo.avatarUrl = res.data
    })
  }
  let res = await editPersonalInfoService(dialogInfo)
  ElMessage.success(res.message)
  statusEdit.value = "审核中"
  // let params = {
  //   uid: editInfo.value.uid,
  //   avatarUrl: editInfo.value.avatarUrl,
  //   nickname: editInfo.value.nickname,
  //   role: 'USER'
  // };
  // userInfoStore.setInfo(params);
  dialog.value = false


}
onMounted(async () => {
  await getPersonalInfo()
  await getStatus()
})
</script>

<template>
  <el-container>
    <el-card style="margin: auto">
      <div style="display: flex;width: 800px">
        <div class="left">
          <div class="avatar">
            <el-avatar :src="personalInfo.avatarUrl" :size="100">
            </el-avatar>
            <p>心理咨询师<img src="../../assets/images/authentication.png" alt="认证"/></p>
          </div>
        </div>
        <div class="right">
          <div class="information-box">
            <div class="information" v-for="(item, index) in imageList" :key="index">
              <img :src="item.src" :alt="item.alt"/>
              <span>{{ personalInfo[item.key] }}</span>
            </div>
          </div>
          <el-divider/>
          <div class="editPersonalInfo">
            <p>
              <el-icon color="#009bdb">
                <Edit/>
              </el-icon>
              <span @click="updateInfo">修改资料</span>
            </p>
          </div>
          <div style="text-align: center">
            <p v-if="statusEdit ==='审核中'">你的信息正在审核中 </p>
            <p v-if="statusEdit ==='审核通过'">恭喜你，你的信息{{ statusEdit }}了</p>
            <p v-if="statusEdit ==='审核未通过'">很抱歉，你的信息{{ statusEdit }}</p>
          </div>
        </div>
      </div>
    </el-card>
    <el-dialog
        v-model="dialog"
        title="Tips"
        width="500"
        draggable
        overflow
    >
      <el-form :model="dialogInfo">
        <div style="text-align: center;margin-bottom: 20px">
          <el-upload
              v-model="avatar"
              :show-file-list="false"
              :on-change="justSelect"
              :auto-upload="false">
            <el-avatar :src="dialogInfo.avatarUrl" :size="100"/>
          </el-upload>
        </div>
        <el-form-item label="账号">
          <span style="font-size: 20px">{{ dialogInfo.cid }}</span>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="dialogInfo.name"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="dialogInfo.sex" placeholder="Select">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <!--          <el-input v-model="dialogInfo.age" />-->
          <AgeSelect v-model="dialogInfo.age">
          </AgeSelect>
        </el-form-item>
        <el-form-item label="证书">
          <el-input v-model="dialogInfo.certificate"/>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="dialogInfo.email"/>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="dialogInfo.biography" type="textarea" :autosize="{ minRows: 1, maxRows: 4 }"/>
        </el-form-item>
      </el-form>
      <div class="controller">
        <el-button @click="confirm" type="primary">确认</el-button>
        <el-button @click="dialog = false">取消</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<style scoped lang="scss">
.el-card {
  padding: 0;
}

.left {
  height: 600px;
  flex: 1;
  background-image: url('@/assets/images/www.jpg');
  //background-color: black;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: center;

  .avatar {
    height: calc(2 / 3 * 100%);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .el-avatar {
      border: 4px solid white;
    }

    p {
      font-size: 20px;
      color: white;
    }

    img {
      width: 20px;
      height: 20px
    }
  }
}

.right {
  flex: 1;
  display: flex;
  flex-direction: column;

  .information-box {
    padding: 20px;
    max-height: 480px;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .information {
    padding: 5px;
    word-wrap: break-word;
    white-space: normal;

    img {
      width: 30px;
      height: 30px;
      margin-right: 30px;
    }

    span {
      font-size: 20px;

    }
  }

  .editPersonalInfo {
    display: flex;
    justify-content: flex-end;
    align-items: flex-end;

    p {
      cursor: pointer;
      color: #009bdb;
    }

    p:hover {
      color: #0ebffc;
    }
  }

}

.controller {
  display: flex;
  justify-content: flex-end;
}
</style>