<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import { getRole, getId, getUserInfo } from "@/utils/getCurrentUserInfo.js";
console.log(getRole())
interface User {
  id: string;
  cid: string;
  name: string;
  nickname: string;
  sex: string;
  age: number;
  email: string;
  phone: number;
  delivery_add:string;
  row: { avatarUrl: string; }

}

const usersList = ref<User[]>([])

import {getUsersListService,deleteUserService} from '@/api/admin.js'

const currentPage = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(7)//每页条数

//查询
const selectCondition = ref('')
const minAge = ref('')
const maxAge = ref('')
const searchUser = async () => {
  let params = {
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    selectCondition: selectCondition.value ? selectCondition.value : null,
    minAge: minAge.value ? minAge.value : null,
    maxAge: maxAge.value ? maxAge.value : null
  }
  let res = await getUsersListService(params)
  total.value = res.data.total
  usersList.value = res.data.items;

}
const resetInputs = () => {
  selectCondition.value = ''
  maxAge.value = ''
  minAge.value = ''
}

//当每页条数发生了变化，调用此函数
const handleSizeChange = (size) => {

  pageSize.value = size
  searchUser()
}
//当前页码发生变化，调用此函数
const handleCurrentChange = (number) => {
  currentPage.value = number
  searchUser()
}
const router = useRouter();


//点击预约
const deleteAccount = async (uid) => {
  let deleted = 1
  await deleteUserService(uid,deleted)
  await searchUser()

}
onMounted(() => {
  searchUser();
});

</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>普通用户</span>
      </div>
    </template>
    <div class="select-container">
      <el-input v-model="selectCondition" placeholder="查询条件"
                @keyup.enter.native="searchUser" style="width: 160px;margin-right: 5px"></el-input>
      <el-input v-model="minAge" placeholder="最小年龄"
                @keyup.enter.native="searchUser" style="width: 100px"></el-input>
      __
      <el-input v-model="maxAge" placeholder="最大年龄"
                @keyup.enter.native="searchUser" style="width: 100px;margin-right: 5px"></el-input>
      <el-button @click="searchUser" type="primary">查询</el-button>
      <el-button @click="resetInputs" type="warning">重置</el-button>
    </div>
    <el-table :data="usersList"  style="width: 100%">
      <el-table-column prop="id" label="序号" width="100" align="center"/>
      <el-table-column prop="uid" label="账号" width="100" align="center"/>
      <el-table-column prop="name" label="姓名" width="120" align="center"/>
      <el-table-column prop="avatarUrl" label="头像" width="100" align="center">
        <template #default="scope">
          <el-avatar :src="scope.row.avatarUrl" alt="头像" size="large"
                     :preview-src-list="[scope.row.avatarUrl]" preview-teleported="true"/>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="160" align="center"/>
      <el-table-column prop="sex" label="性别" width="100" align="center"/>
      <el-table-column prop="age" label="年龄" width="100" align="center"/>
      <el-table-column prop="email" label="邮箱" width="180" align="center"/>
      <el-table-column prop="phone" label="电话" width="180" align="center"/>
      <el-table-column prop="deliveryAdd" label="收货地址" width="180" align="center"/>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button @click="deleteAccount(row.uid)" type="danger">封禁</el-button>
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
</style>