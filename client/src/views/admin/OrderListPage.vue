<script setup lang="ts">
import {onMounted, ref} from "vue";
import {findAllOrdersService, updateOrderStatusService} from "@/api/admin"
import {ElMessage} from "element-plus";

interface Order {
  id: number,
  orderNo: number,
  goodsName: string,
  number: number,
  totalPrice: number,
  createdTime: string,
  payNo: number,
  payTime: string,
  payStatus: string,
  status: string,
  buttonText: string; // 新增属性用于控制按钮文本
  buttonDisabled: boolean; // 新增属性用于控制按钮禁用状态
}

const currentPage = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(10)//每页条数
const pageParams = {
  currentPage: currentPage.value,
  pageSize: pageSize.value,
}
const handleSizeChange = (size) => {
  pageSize.value = size;
  pageParams.pageSize = size;  // 更新请求参数中的每页条数
  searchOrder()
}

//当前页码发生变化，调用此函数
const handleCurrentChange = (number) => {
  currentPage.value = number
  pageParams.currentPage = number // 更新请求参数中的当前页码
  searchOrder()
}
const selectName = ref('')
const selectGoodsName = ref('')
const resetInputs = () => {
  selectName.value = '',
  selectGoodsName.value = ''
}
const searchOrder = async () => {
  let params = {
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    selectName: selectName.value ? selectName.value : null,
    selectGoodsName: selectGoodsName.value ? selectGoodsName.value : null,
  }
  let res = await findAllOrdersService(params);
  total.value = res.data.total;
  // 确保使用.value来更新响应式引用
  ordersList.value = res.data.items.map((order) => ({
    ...order,
    buttonText: order.status === '已发货' || order.status === '已收货' || order.status === '已评价' ? '发货成功' : '发货',
    buttonDisabled: order.status === '已发货' || order.status === '已收货' || order.status === '已评价',
  }));
};

const ordersList = ref<Order[]>([])

const delivery = async (index: number) => {
  // 使用索引找到对应的订单对象
  const order = ordersList.value[index];
  if (order) {
    // 更新订单状态
    order.status = "已发货";
    // 更新按钮文本和禁用状态
    order.buttonText = "成功发货";
    order.buttonDisabled = true;
  }
  let params = {
    status: order.status,
    orderNo: order.orderNo
  }
  let res = await updateOrderStatusService(params)
  ElMessage.success(res.message)
  await findAllOrdersService(pageParams)

};
onMounted(async () => {
  await searchOrder()

})
</script>

<template>
  <el-card>
    <template #header>
      订单列表
    </template>
    <div class="select-container">
      <el-input v-model="selectName" placeholder="用户名称"
                @keyup.enter.native="searchOrder" style="width: 200px;margin-right: 5px"/>
      <el-input v-model="selectGoodsName" placeholder="商品名称" style="width: 200px;margin-right: 5px"/>

      <el-button @click="searchOrder" type="primary">查询</el-button>
      <el-button @click="resetInputs" type="warning">重置</el-button>
    </div>
    <el-table :data="ordersList" default-sort="{prop: 'createdTime', order: 'descending'}">
      <el-table-column prop="id" label="" width="50" align="center"/>
      <el-table-column prop="buyerId" label="买家账号" width="120" align="center">
        <template #default="{row}">
          <el-popover placement="top-start" :width="150" trigger="hover">
            <template #reference>
              <p>{{ row.buyerId }}</p>
            </template>
            <p>{{ row.buyerName }}</p>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="orderNo" label="订单编号" width="190" align="center"/>
      <el-table-column prop="goodsName" label="商品名称" width="140" align="center"/>
      <el-table-column prop="number" label="数量" width="100" align="center"/>
      <el-table-column prop="totalPrice" label="总价" width="100" align="center"/>
      <el-table-column prop="payTime" label="支付时间" width="180" align="center"/>
      <el-table-column prop="createdTime" label="创建时间" width="180" align="center" sortable/>
      <el-table-column prop="deliveryAdd" label="收货地址" width="120" align="center"/>
      <el-table-column prop="status" label="订单状态" width="120" align="center">
        <template #default="{row}">
          <div v-if="row.status">
            <el-tag type="warning" size="large">{{ row.status }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="" label="操作" width="120" align="center" fixed="right">
        <template #default="{ row, $index }">
          <el-button
              :type="row.buttonDisabled ? 'success' : ''"
              :disabled="row.buttonDisabled"
              @click="delivery($index)"
          >
            {{ row.buttonText }}
          </el-button>
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

<style scoped lang="scss">
.select-container {

  margin-bottom: 10px;
  display: flex;
  justify-content: flex-start;
}
.el-p {
  margin-top: 5px;
}
</style>