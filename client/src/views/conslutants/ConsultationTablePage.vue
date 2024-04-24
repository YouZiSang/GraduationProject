<script setup>
import {ref, onMounted} from "vue";

import {findConsultationTableService, updateConsultationStatusService} from "@/api/consultation/consultation.js"
import {useRouter} from "vue-router";
import {updateAppStatusService} from "@/api/consultation/appoimtment.js";

const router = useRouter();
const tableDate = ref([])
const dialog = ref(false)
const descriptionText = ref('')
const currentRow = ref(null);
const findConsultationTable = async () => {
  let res = await findConsultationTableService()
  console.log(res.data)
  tableDate.value = res.data
}

const start = async (row) => {
  // 将用户信息存储到 sessionStorage 中
  sessionStorage.setItem('chatParams', JSON.stringify({
    user_uid: row.uid,
    user_avatarUrl: row.avatarUrl,
    user_name: row.name,
    user_nickname: row.nickname
  }));
  // 重定向到页面而不带查询参数
  await router.push({path: "/consultants/consultation"});
  const params = {
    uid: row.uid,
    status: '正在咨询',
    consultationDate: row.consultationDate
  }
  await updateConsultationStatusService(params)
  const appParams = {
    uid: row.uid,
    status: '进行中',
    appointmentDate: row.consultationDate
  }
  await updateAppStatusService(appParams)

}
const end = async (row) => {
  dialog.value = true
  currentRow.value = row; // 存储当前选中的行数据
}

const sure = async () => {
  if (currentRow.value) {
    const row = currentRow.value;
    const params = {
      uid: row.uid,
      status: '咨询结束',
      consultationDate: row.consultationDate,
      description: descriptionText.value
    }
    await updateConsultationStatusService(params)

    const appParams = {
      uid: row.uid,
      status: '咨询结束',
      appointmentDate: row.consultationDate
    }
    await updateAppStatusService(appParams)
  } else {
    console.error('No row data available for sure function');
  }
  await findConsultationTable()
  dialog.value = false
}

const cancel = async () => {
  descriptionText.value = ''
  dialog.value = false
}
onMounted(() => {
  findConsultationTable()
})
</script>

<template>
  <el-container>
    <el-table :data="tableDate">
      <el-table-column prop="type" label="咨询类型" width="160" align="center"/>
      <el-table-column prop="avatarUrl" label="头像" width="70" align="center">
        <template #default="{row}">
          <el-avatar :src="row.avatarUrl" size="large"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="140" align="center"></el-table-column>
      <el-table-column prop="name" label="姓名" width="140" align="center"></el-table-column>
      <el-table-column prop="consultationDate" label="咨询日期" width="160" align="center"></el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="120" align="center"></el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="120" align="center"></el-table-column>
      <el-table-column prop="description" label="描述" width="200" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" width="120" align="center">
        <template #default="{row}">
          <el-tag size="large">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="" label="操作" width="120" align="center">
        <template #default="{row}">
          <el-button type="success" @click="start(row)" v-if="row.status ==='等待咨询'">开始</el-button>
          <el-button type="success" @click="end(row)" v-if="row.status ==='正在咨询'" plain>结束</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialog" title="主要咨询的问题和描述" width="500px" center>
      <el-input v-model="descriptionText" type="textarea" style="font-size: 17px"
                :autosize="{ minRows: 2, maxRows: 4 }"></el-input>
      <el-button @click="cancel">取消</el-button>
      <el-button @click="sure">确定</el-button>
    </el-dialog>
  </el-container>
</template>

<style scoped lang="scss">

</style>