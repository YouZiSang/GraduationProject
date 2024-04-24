<script setup lang="js">
import {onMounted, ref} from "vue";
import {
  getAppointmentTableService,
  getAppointmentNumberService,
  updateAppStatusService
} from "@/api/consultation/appoimtment.js";
import {useRouter} from "vue-router";
import {Bottom, Search, Top} from "@element-plus/icons-vue";
import {addConsultationService} from "@/api/consultation/consultation.js";
import {getId} from "@/utils/getCurrentUserInfo.js";

const router = useRouter();
const loading = ref(true)

const today = new Date();
const year = today.getFullYear(); // 获取当前年份
const month = today.getMonth() + 1; // 获取当前月份，月份从0开始，所以加1
const date = today.getDate(); // 获取当前日
// 将月份和日期格式化为两位数，例如 '01', '21' 等
const formattedMonth = month.toString().padStart(2, '0');
const formattedDate = date.toString().padStart(2, '0');
const formattedToday = `${year}-${formattedMonth}-${formattedDate}`;


const appointmentInfo = ref([])
const appointmentNumber = ref([])
const cloneAppointmentInfo = ref([])
const getAppointmentTable = async () => {
  let res = await getAppointmentTableService()

  const table = res.data.map((table) => ({
    ...table,
  }));
  cloneAppointmentInfo.value = table;
  appointmentInfo.value = table
  loading.value = false
}
const getAppointmentNumber = async () => {
  let res = await getAppointmentNumberService()
  appointmentNumber.value = res.data
}

const ascIconColor = ref()
const descIconColor = ref()
const sortStatus = ref('asc'); // 'asc' 为升序，'desc' 为降序
const sortDate = () => {
  // 切换排序状态
  sortStatus.value = sortStatus.value === 'asc' ? 'desc' : 'asc';
  // 对数据进行排序
  appointmentInfo.value.sort((a, b) => {
    const dateA = new Date(a.appointmentDate);
    const dateB = new Date(b.appointmentDate);
    return sortStatus.value === 'asc' ? dateA - dateB : dateB - dateA;
  });

  // 更新图标颜色
  ascIconColor.value = sortStatus.value === 'asc' ? 'red' : 'black';
  descIconColor.value = sortStatus.value === 'asc' ? 'black' : 'red';

}
const searchText = ref('')


const search = () => {
  // 每次搜索前，都重置appointmentInfo为完整的数据集
  appointmentInfo.value = [...cloneAppointmentInfo.value];

  const regex = searchText.value ? new RegExp(searchText.value, "i") : null;

  const searchResult = appointmentInfo.value.filter((item) => {
    if (regex === null) {
      return true; // 输入框为空时返回全部内容
    }

    return (
        (item.name && regex.test(item.name)) ||
        (item.nickname && regex.test(item.nickname)) ||
        (item.sex && regex.test(item.sex)) ||
        (item.age && regex.test(item.age)) ||
        (item.appointmentDate && regex.test(item.appointmentDate)) ||
        (item.status && regex.test(item.status))
    );
  });

  appointmentInfo.value = searchResult;
};

const clean = () => {
  searchText.value = '';
  appointmentInfo.value = [...cloneAppointmentInfo.value];
  ascIconColor.value = '';
  descIconColor.value = ''

};
const agree = (row) => {
  // let status = formattedToday === row.appointmentDate ? '进行中' : '预约成功'
  const params = {
    uid: row.uid,
    status: '预约成功',
    appointmentDate: row.appointmentDate,
    cid: getId()
  }
  let res = updateAppStatusService(params)
  const consultationData = {
    uid: row.uid,
    consultationDate: row.appointmentDate,
    type: row.appointmentType,
    status: '等待咨询'
  }
  addConsultationService(consultationData)
  ElMessage.success(res.message)
  getAppointmentTable()
}
const select = async () => {
  await router.push({path: "/consultants/consultationTable"});
}

onMounted(() => {
  getAppointmentTable()
  getAppointmentNumber()
})
</script>

<template>
  <el-container>
    <div class="container">
      <div class="appointmentNumber">
        <div v-for="(item, index) in appointmentNumber" :key="index" class="aside">
          <div style="display: flex">
            <div class="date">日期：{{ item.date }}</div>
            <div class="number">预约人数：{{ item.number }}</div>
          </div>
        </div>
      </div>
      <div class="appTable">
        <div>
          <div class="title">
            预约表
          </div>
          <div class="select">
            <el-input v-model="searchText" style="width: 250px;margin-right: 10px">
              <template #append>
                <el-button :icon="Search" @click="search"/>
              </template>
            </el-input>
            <el-button @click="clean">清空</el-button>
            <el-button @click="sortDate">按日期排序
              <el-icon :color="ascIconColor">
                <Top/>
              </el-icon>
              <el-icon :color="descIconColor">
                <Bottom/>
              </el-icon>
            </el-button>
          </div>
          <el-table :data="appointmentInfo" stripe v-loading="loading">
            <el-table-column prop="appointmentType" label="咨询类型" width="140" align="center"/>
            <el-table-column prop="avatarUrl" label="头像" width="80" align="center">
              <template #default="{row}">
                <el-avatar :src="row.avatarUrl" size="large"></el-avatar>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="160" align="center"></el-table-column>
            <el-table-column prop="nickname" label="昵称" width="160" align="center"></el-table-column>
            <el-table-column prop="sex" label="性别" width="100" align="center"></el-table-column>
            <el-table-column prop="age" label="年龄" width="80" align="center"></el-table-column>
            <el-table-column prop="appointmentDate" label="预约日期" width="120"
                             align="center"></el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{row}">
                <el-tag v-if="row.status !== '进行中'" type="warning" size="large">{{ row.status }}</el-tag>
                <el-tag v-else type="success" size="large">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="" label="操作" width="120" align="center" fixed="right">
              <template #default="{row}">
                <el-button v-if="row.status ==='等待同意'" @click="agree(row)" type="primary">同意预约</el-button>
                <el-button type="success" @click="select">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </el-container>
</template>

<style scoped lang="scss">
.container {
  display: flex;
  height: 100%;
  width: 100%;
}

.appointmentNumber {
  width: 300px;
  padding: 10px;
  display: flex;
  flex-direction: column;
}

.aside {
  height: 40px;
  margin-bottom: 10px;
  background-color: #0ebffc;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  border-radius: 0 40px 0 40px;
  filter: drop-shadow(0 0 3px #000);
}

.title {
  display: block;
  margin: 20px auto;
  text-align: center;
  font-size: 20px;
}

.appTable {
  width: 1100px;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.3);
}

.select {
  display: flex;
  margin-bottom: 10px;
}

.date {
  margin-right: 20px;
}


</style>