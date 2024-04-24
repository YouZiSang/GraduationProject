<script setup>
import {ref} from "vue";
import {appointmentManage,findAppointment} from "@/api/consultants.js";

const setting = ref(false); // 控制弹窗的显示
const isCurrentMonth = (date) => {
  const today = new Date();
  const currentYear = today.getFullYear();
  const currentMonth = today.getMonth(); // 注意：getMonth() 返回的月份是从0开始的
  const [year, month] = date.split('-').map(Number);
  return year === currentYear && month - 1 === currentMonth; // 月份减1是因为date的月份是从1开始的
}
const shouldShowButton = (dateString) => {
  let today = new Date();
  today.setHours(0, 0, 0, 0); // 重置时间为当天的开始

  let targetDate = new Date(dateString);
  let diffTime = targetDate - today;
  let diffDays = diffTime / (1000 * 3600 * 24);

  // 仅当日期是今天或之后的15天内时返回 true
  return diffDays >= 1 && diffDays <= 15;
};
const set = ref({
  number: '',
  appointmentDate: '',
  cid: '',
  reservations:''
})

const appointments = ref({})

const appointmentInfo = async()=> {
  let res = await findAppointment()

  // 遍历后端返回的数据并保存到appointments对象中
  res.data.forEach((item) => {
    appointments.value[item.appointmentDate] = { number: item.number ,reservations: item.reservations};
  });

}
appointmentInfo();
const handleButtonClick = (data) => {
  set.value.number = appointments.value[data.day]?.number || 0; // 设置预约人数为当前日期的预约人数
  set.value.appointmentDate = data.day; // 保存当前选中的日期
  // 打开弹窗
  setting.value = true;
};

const handleConfirm = async () => {
  // 在这里处理用户的输入，例如发送到服务器
  await appointmentManage(set.value)
  await appointmentInfo();
  // 关闭弹窗
  setting.value = false;
};

</script>

<template>
  <el-card>
    <template #header>
      预约
    </template>
    <el-calendar>
      <template #date-cell="{ data }">
        <div :class="{'cell-content': true, 'current-month': shouldShowButton(data.day)}">
          <span :class="data.isSelected ? 'is-selected' : ''">
            {{ data.day.split('-').slice(1).join('-') }}
          </span>
          <div class="bookingData">
            <!-- 添加按钮 -->
            <p v-if="shouldShowButton(data.day)"
                style="color: #60cd8c">
              可预约：{{ appointments[data.day]?.number || 0 }}
            </p>
            <span v-if="shouldShowButton(data.day)" style="color: orange">已预约：{{ appointments[data.day]?.reservations || 0}}</span>
            <el-button v-if="shouldShowButton(data.day)" @click="handleButtonClick(data)" color="#d6ffff"
                       style="float: right">设置
            </el-button>
          </div>
        </div>
      </template>
    </el-calendar>
    <!-- 弹窗组件 -->
    <div class="s">
      <el-dialog v-model="setting" title="预约设置" :before-close="handleConfirm" width="400px">
        <el-input v-model="set.number" type="number" label="预约人数"></el-input>
        <template #footer>
          <el-button @click="setting = false">取消</el-button>
          <el-button type="primary" @click="handleConfirm">确认</el-button>
        </template>
      </el-dialog>
    </div>
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


:deep( .el-calendar-table .el-calendar-day) {
  height: 100%;
}
</style>