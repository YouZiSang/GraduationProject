<script setup>
import {ref} from "vue";
import {updatePassword} from "@/api/user.js";

const update = ref({
  odlPwd: '',
  newPwd: '',
  rePwd: ''
})

const rePassword = async () => {
  let res = await updatePassword(update.value)
  ElMessage.success(res.message)
}
</script>

<template>
  <el-card >
    <el-form label-width="100px">
      <el-form-item label="原密码：">
        <el-col :span="8">
          <el-input v-model="update.odlPwd"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="新密码：">
        <el-col :span="8">
          <el-input v-model="update.newPwd"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="确认密码：">
        <el-col :span="8">
          <el-input v-model="update.rePwd"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item>
        <el-col :span="8">
          <el-button @click="rePassword">确定</el-button>
        </el-col>
      </el-form-item>
    </el-form>
  </el-card>
  <div >
    <img src="@/assets/images/background1.png" slot="reference" alt="统计" style="width: 200px;height: 200px"/>
  </div>
</template>

<style scoped lang="scss">

</style>