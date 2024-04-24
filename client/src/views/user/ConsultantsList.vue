<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import {getRole, getId, getUserInfo} from "@/utils/getCurrentUserInfo.js";


const role = getRole()

interface Consultant {
  id: string;
  cid: string;
  name: string;
  sex: string;
  age: number;
  email: string;
  phone: number;
  certificate: string,
  biography: string,
  row: { avatarUrl: string }
}

const consultantsList = ref<Consultant[]>([])

import {getConsultantsInfo} from '@/api/userInfo'
import {deleteConsultantService} from '@/api/admin'
import Breadcrumb from "@/components/Breadcrumb.vue";

const currentPage = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(7)//每页条数

//查询
const selectName = ref('')
const minAge = ref('')
const maxAge = ref('')
const searchConsultants = async () => {
  let params = {
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    selectName: selectName.value ? selectName.value : null,
    minAge: minAge.value ? minAge.value : null,
    maxAge: maxAge.value ? maxAge.value : null
  }
  let res = await getConsultantsInfo(params)

  total.value = res.data.total
  consultantsList.value = res.data.items;

}
const resetInputs = () => {
  selectName.value = ''
  maxAge.value = ''
  minAge.value = ''
}

//当每页条数发生了变化，调用此函数
const handleSizeChange = (size) => {

  pageSize.value = size
  searchConsultants()
}
//当前页码发生变化，调用此函数
const handleCurrentChange = (number) => {
  currentPage.value = number
  searchConsultants()
}

//管理员删除账号
const deleteAccount = async (cid) => {
  let deleted = 1
  await deleteConsultantService(cid, deleted)
  await searchConsultants()
}
const router = useRouter();
const handleMouseEnter = (event) => {
  event.target.classList.add('orange'); // 添加橙色样式类
}
const handleMouseLeave = (event) => {
  event.target.classList.remove('orange'); // 移除橙色样式类
}

const consultantsDetails = (row) => {
  sessionStorage.setItem('consultantsParams', JSON.stringify(
      {
        consultants_id: row.id,
        consultants_cid: row.cid,
        consultants_name: row.name,
        consultants_sex: row.sex,
        consultants_age: row.age,
        consultants_email: row.email,
        consultants_phone: row.phone,
        consultants_certificate: row.certificate,
        consultants_biography: row.biography,
        consultants_avatarUrl: row.avatarUrl
      }
  ));
  router.push({path: 'consultantsDetails', query: {cid: row.cid}})
}
//点击预约
const booking = (cid) => {
  // 使用查询参数 (Query Params) 进行页面跳转
  router.push({path: '/user/booking', query: {cid: cid}});
}
onMounted(() => {
  searchConsultants();
});

</script>

<template>
    <el-card class="page-container">
      <template #header>
        <div class="header">
          <span v-if="role === 'USER'">预约</span>
          <span v-if="role === 'ADMIN'">心理咨询师</span>
        </div>
      </template>
      <div class="select-container" style="margin-bottom: 10px">
        <el-input v-model="selectName" placeholder="请输入姓名"
                  @keyup.enter.native="searchConsultants" style="width: 160px;margin-right: 5px"></el-input>
        <el-input v-model="minAge" placeholder="最小年龄"
                  @keyup.enter.native="searchConsultants" style="width: 100px"></el-input>
        __
        <el-input v-model="maxAge" placeholder="最大年龄"
                  @keyup.enter.native="searchConsultants" style="width: 100px;margin-right: 5px"></el-input>
        <el-button @click="searchConsultants" type="primary">查询</el-button>
        <el-button @click="resetInputs" type="warning">重置</el-button>
      </div>
      <el-table :data="consultantsList" height=560px style="width: 100%">
        <el-table-column prop="id" label="序号" width="80" align="center"/>
        <el-table-column prop="cid" label="账号" width="120" align="center"/>
        <el-table-column prop="avatarUrl" label=" " width="100" align="center">
          <template #default="scope">
            <el-avatar :src="scope.row.avatarUrl" alt="头像" size="large"
                       :preview-src-list="[scope.row.avatarUrl]" preview-teleported="true" align="center"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120" align="center">
          <template #default="{ row }">
           <span class="consultants-name"
                 @mouseenter="handleMouseEnter"
                 @mouseleave="handleMouseLeave"
                 @click="consultantsDetails(row)">
                {{ row.name }}
           </span>
          </template>
        </el-table-column>
        <el-table-column prop="sex" label="性别" width="100" align="center"/>
        <el-table-column prop="age" label="年龄" width="100" align="center"/>
        <el-table-column prop="certificate" label="证书" width="200" align="center"/>
        <el-table-column prop="email" label="邮箱" width="180" align="center"/>
        <el-table-column prop="phone" label="电话" width="120" align="center"/>
        <el-table-column prop="biography" label="描述" width="120" align="center">
          <template #default="{row}">
            <el-popover placement="top-start" :width="400" trigger="hover">
              <template #reference>
                <el-button text>简介</el-button>
              </template>
              <p>{{ row.biography }}</p>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button v-if="role === 'USER'" @click="booking(row.cid)" type="primary">预约</el-button>
            <el-button v-if="role === 'ADMIN'" @click="deleteAccount(row.cid)" type="danger">封禁</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          class="el-p"
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]"
          layout="jumper,total, sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .select-container {
    display: flex;
    justify-content: flex-start;
  }

  .el-pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;

  }
}

.consultants-name {
  cursor: pointer; /* 设置鼠标悬停时变成手指状态 */
  transition: color 0.3s; /* 设置颜色过渡效果 */
}

.consultants-name.orange {
  color: orangered; /* 设置标题文字为橙色 */
}
</style>