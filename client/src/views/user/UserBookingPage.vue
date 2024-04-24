<script setup>
import {ref, onMounted} from "vue";
import {appointmentService, findUserAppointmentInfo, getAppointmentTable} from "@/api/user.js";
import {useRoute} from "vue-router";
import {ElMessageBox} from "element-plus";
import {getAppointmentTypeService, updateAppointmentInfo} from "@/api/consultation/appoimtment.js";

const route = useRoute();
const cid = route.query.cid;
const setting = ref(false); // 控制弹窗的显示

//给今天以后的14天添加预约按钮
const showStyles = (dateString) => {
  let today = new Date();
  today.setHours(0, 0, 0, 0); // 重置时间为当天的开始

  let targetDate = new Date(dateString);
  let diffTime = targetDate - today;
  let diffDays = diffTime / (1000 * 3600 * 24);

  // 仅当日期是今天或之后的15天内时返回 true
  return diffDays >= 1 && diffDays <= 15;
};
const appointments = ref({})
const userAppointmentInfo = ref({})
const appointmentInfo = async () => {

  let res = await getAppointmentTable(cid)
  // 遍历后端返回的数据并保存到appointments对象中
  res.data.forEach((item) => {
    appointments.value[item.appointmentDate] = {number: item.number, reservations: item.reservations};
  });
}
appointmentInfo();
const typeObjs = ref([])
const type = ref()
const getAppointmentType = async () => {
  let res = await getAppointmentTypeService()
  typeObjs.value = res.data
}
const handleButtonClick = (data) => {

  ElMessageBox.confirm('你确定要预约今天的时间吗？', '确认预约', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 用户点击了确定
    let params = {
      cid: cid,
      appointmentDate: data.day,
      status: '等待同意',
      appointmentType:type.value,
    }
    // 调用发送预约请求的函数，将consultantId和日期作为参数传递过去
    let res = await appointmentService(params)

    res.data.forEach((item) => {
      userAppointmentInfo.value[item.appointmentDate] = {uid: item.uid, isBooked: true};
    });

    let result = await getAppointmentTable(cid)
    result.data.forEach((item) => {
      appointments.value[item.appointmentDate] = {number: item.number, reservations: item.reservations};
    });

    let selectedAppointment = result.data.find(item => item.appointmentDate === data.day);
    //传回后端
    let list = {
      uid: selectedAppointment.uid,
      appointmentDate: selectedAppointment.appointmentDate,
      number: selectedAppointment.number,
      reservations: selectedAppointment.reservations,
    }

    await updateAppointmentInfo(list)
    await appointmentInfo();
  })

};

const isBooked = async () => {
  let res = await findUserAppointmentInfo()

  res.data.forEach(appointment => {
    // 假设每个appointment都有appointmentDate和userId属性
    // 现在可以安全地访问这些属性，并根据它们更新userAppointmentInfo.value
    userAppointmentInfo.value[appointment.appointmentDate] = {uid: appointment.uid, isBooked: true};
  });
}
isBooked();

const dialogVisible = ref(false)
const appointmentType = (data) => {
  dialogVisible.value = true
}

onMounted(() => {
  appointmentInfo()
  getAppointmentType()
})
</script>

<template>
  <el-card>
    <template #header>
      预约
    </template>
    <el-calendar>
      <template #date-cell="{ data }">
        <div :class="{'cell-content': true, 'current-month': showStyles(data.day)}">
          <span :class="data.isSelected ? 'is-selected' : ''">
            {{ data.day.split('-').slice(1).join('-') }}
          </span>
          <div class="bookingData">
            <div v-if="appointments[data.day]?.number && showStyles(data.day)">
              <p style="color: #60cd8c">
                可预约：{{ appointments[data.day]?.number || 0 }}人
              </p>
              <span v-if="userAppointmentInfo[data.day]?.isBooked" style="color: red">已预约</span>
              <div v-else-if="!userAppointmentInfo[data.day]?.isBooked">
              <span style="color: #e6a23c">
                已预约：{{ appointments[data.day]?.reservations || 0 }}人
              </span>
                <el-button @click="appointmentType(data)" color="#d6ffff" style="float: right">预约</el-button>
                <el-dialog v-model="dialogVisible" title="咨询类型" center width="400px">
                  <div class="select-box">
                    <el-select v-model="type" placeholder="请选择想要的咨询类型" style="width: 250px">
                      <el-option
                          v-for="item in typeObjs"
                          :key="item.id"
                          :label="item.appointmentType"
                          :value="item.id"
                      />
                    </el-select>
                  </div>
                  <div class="dialog-controller">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button @click="handleButtonClick(data)" color="#d6ffff">预约</el-button>
                  </div>
                </el-dialog>
              </div>
            </div>

            <div v-else-if="showStyles(data.day)
            && ((appointments[data.day]?.reservations >0 )
            && (appointments[data.day]?.number ===0 ))"
                 style="height: 60px;color: red;display: flex; justify-content: center; align-items: center">
              <p>预约已满</p>
            </div>
            <div v-else-if="showStyles(data.day)" style="text-align: center">
              <p>无预约时间</p>
            </div>
          </div>
        </div>
      </template>
    </el-calendar>
  </el-card>
</template>

<style lang="scss" scoped>
.el-card {
  .current-month {
    padding: 5px;
    background-color: #d6ffff; /* 或你希望的任何颜色 */
  }

  .el-button {
    color: #0ebffc;
  }

  .el-dialog {
    width: 200px;
    height: 200px;
  }

  .bookingData {
    height: 70px;
  }
}

.select-box {
  text-align: center;
  margin: 20px 0 50px 0
}

.dialog-controller {
  text-align: center;

  .el-button {
    width: 100px;
  }
}

:deep( .el-calendar-table .el-calendar-day) {
  height: 100%;
}
</style>