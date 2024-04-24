<script setup lang="ts">
import {ref, onBeforeUnmount, onMounted, defineEmits, computed} from "vue";
import {
  ordersListService,
  updateStatusService,
  evaluationImgService,
  orderEvaluationService,
  delOrderService
} from "@/api/orders.js";
import {Delete, Download, Plus, ZoomIn, Camera, Top, Bottom} from '@element-plus/icons-vue'

const ordersList = ref([{
  id: '',
  orderNo: '',
  goodsId: '',
  goodsName: '',
  number: '',
  totalPrice: '',
  createdTime: '',
  payNo: '',
  payTime: '',
  payStatus: '',
  status: ''
}])

import {useTokenStore} from '@/stores/token.js'
import {ElMessage, UploadProps} from "element-plus";
import {UploadFile} from "element-plus";


const userInfoStore = useTokenStore();


const currentPage = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(10)//每页条数

const params = {
  currentPage: currentPage.value,
  pageSize: pageSize.value,
}

// const selectOrdersList = async () => {
//   let res = await ordersListService(params)
//   console.log(params)
//   ordersList.value = res.data
// }
// selectOrdersList()

const pay = (row) => {
  window.open('http://localhost:8080/api/alipay/pay?orderNo=' + row.orderNo)
}

const selectCondition = ref('')
const searchOrder = async () => {
  let params = {
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    selectCondition: selectCondition.value ? selectCondition.value : null,
  }
  let res = await ordersListService(params)
  total.value = res.data.total
  ordersList.value = res.data.items;
}
searchOrder()

//重置表单
const resetInputs = () => {
  selectCondition.value = ''
}

const receipt = async (row) => {
  let params = {
    status: '已收货',
    orderNo: row.orderNo
  }
  let res = await updateStatusService(params)
  ElMessage.success(res.message)
  await searchOrder()

}

const evaluationGoodsId = ref('')

//评价
const evaluation = async (row) => {

  for (const order of ordersList.value) {
    // 如果订单号匹配
    if (order.orderNo === row.orderNo) {
      // 设置评价的商品ID和订单号
      evaluationGoodsId.value = order.goodsId;
      evaluationOrderNo.value = row.orderNo;
      dialogVisible.value = true;
      // 找到匹配的订单后，可以选择跳出循环，因为已经找到了所需的数据
      break;
    }
  }
}

const dialogVisible = ref(false)

const resetFormData = () => {
  fileList.value = [];
  content.value = '';
  description.value = 0;
  logistics.value = 0;
  attitude.value = 0;
  // 其他需要重置的数据也可以在这里添加
  // ...
}
const content = ref('');
let recognition = null;

const startRecognition = () => {
  if (!('webkitSpeechRecognition' in window)) {
    alert('您的浏览器不支持语音输入');
    return;
  }


  recognition.onerror = (event) => {
    console.error('语音识别发生错误：', event);
    stopRecognition();
  };

  recognition.start();
};

const stopRecognition = () => {
  if (recognition) {
    recognition.stop();
    recognition = null;
  }
};

const handleClose = async (done) => {
  stopRecognition();
  done();
  resetFormData()
};

const remainingTime = (row: any) => {
  const now = new Date();
  const targetTime = new Date(row.payTime);
  let diff = (targetTime.getTime() - now.getTime()) / 1000; // 差值（秒）

  if (diff < 0) {
    return "已过期"; // 已过期
  }

  const minutes = Math.floor(diff / 60);
  const seconds = Math.floor(diff % 60);

  // 格式化时间，仅显示分钟和秒
  return `${(minutes < 10 ? '0' : '') + minutes}:${(seconds < 10 ? '0' : '') + seconds}`;
};
onMounted(() => {
  // 可以在这里执行一些初始化操作
  recognition = new webkitSpeechRecognition();
  recognition.continuous = true;
  recognition.interimResults = true; // 设置为false以避免中间结果触发onresult
  recognition.lang = 'zh-CN';
  recognition.onresult = (event) => {
    let result = '';
    for (let i = event.resultIndex; i < event.results.length; ++i) {
      if (event.results[i].isFinal) {
        result += event.results[i][0].transcript;
      }
    }
    if (result) {
      content.value += result;
    }
  };


  setInterval(() => {
    ordersList.value = [...ordersList.value]; // 触发Vue的响应性更新
  }, 1000);
})

onBeforeUnmount(() => {
  stopRecognition();
});

const appraiseFile = ref('')
// const imge = ref('')
//
// const justSelect = (uploadBook: UploadFile) => {
//   if (uploadBook.raw) {
//     imge.value = URL.createObjectURL(uploadBook.raw);
//     appraiseFile.value = uploadBook;
//   }
// }

let limit = 3
const fileList = ref([])
const disabled = ref(false)
//
const mediaVisible = ref(false)
const dialogVideoUrl = ref('')
const dialogImageUrl = ref('')
//评价图片
const evaluationMedia = ref([])
//评论订单号
const evaluationOrderNo = ref('')
//是否评价
const isEvaluated = ref(false)
//描产品述
const description = ref(0)

//物流物流
const logistics = ref(0)
//服务态度
const attitude = ref(0)

const shouldShowButton = computed(() => {
      return (
          description.value !== 0 &&
          logistics.value !== 0 &&
          attitude.value !== 0 &&
          content.value !== ''
      )
    }
);
const hideBtn = ref(false)
const handleFileChange = (file, fileList) => {
  file.progress = 0; // 初始化进度为0
  fileList.value = fileList

  // 更新fileList的引用
  hideBtn.value = fileList.value.length >= limit
  if (file.raw.type.startsWith('video/')) {
    // 如果选择的文件是视频文件，则显示视频预览
    file.url = URL.createObjectURL(file.raw);

    file.isVideo = true;
  } else {
    file.isVideo = false;

  }
};

//删除文件
const handleRemove = (file: UploadFile) => {

  const index = fileList.value.findIndex((item) => item.uid === file.uid);
  if (index !== -1) {
    fileList.value.splice(index, 1);
  }

  //上传按钮
  hideBtn.value = fileList.value.length >= limit
}

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.isVideo ? '' : file.url
  dialogVideoUrl.value = file.isVideo ? file.url : ''
  mediaVisible.value = true
};

//发布评论
const publish = async () => {

  if (fileList.value.length > 0) {
    const formData = new FormData();
    fileList.value.forEach((file) => {
      formData.append('file', file.raw); // 将文件数据添加到 FormData 对象中
    });
    let res = await evaluationImgService(formData)
    evaluationMedia.value = res.data
  }

  let mediaValue = JSON.stringify(evaluationMedia.value);

  let params = {
    goodsId: evaluationGoodsId.value,
    media: mediaValue,
    orderNo: evaluationOrderNo.value,
    content: content.value,
    description: description.value,
    logistics: logistics.value,
    attitude: attitude.value
  }

  //评价接口
  let res = await orderEvaluationService(params)
  ElMessage.success(res.message)

  let orderStatus = {
    status: '已评价',
    orderNo: evaluationOrderNo.value
  }
  await updateStatusService(orderStatus)
  await searchOrder()
  resetFormData()
  dialogVisible.value = false
}

const cancel = () => {
  resetFormData()
  dialogVisible.value = false
}

//删除订单
const del = async (row) => {
  let res = await delOrderService(row.orderNo)
  ElMessage.success(res.message)
  await searchOrder()
}

const handleSizeChange = (size) => {
  pageSize.value = size;
  params.pageSize = size;  // 更新请求参数中的每页条数
  searchOrder();
}

//当前页码发生变化，调用此函数
const handleCurrentChange = (number) => {
  currentPage.value = number
  params.currentPage = number // 更新请求参数中的当前页码
  searchOrder();
}

const fromData = () => {

}

</script>

<template>
  <el-card>
    <template #header>
      订单列表
    </template>
    <div class="select-container">
      <el-input v-model="selectCondition" placeholder="查询条件"
                @keyup.enter.native="searchOrder" style="width: 200px;margin-right: 5px"></el-input>

      <el-button @click="searchOrder" type="primary">查询</el-button>
      <el-button @click="resetInputs" type="warning">重置</el-button>
    </div>
    <el-table :data="ordersList" :default-sort="{prop: 'createdTime', order: 'descending'}">
      <el-table-column prop="createdTime" label="创建时间" width="180" align="center" sortable />
      <el-table-column prop="orderNo" label="订单编号" width="140" align="center">
        <template #default="{row}">
          <el-popover placement="top-start" :width="300" trigger="hover">
            <template #reference>
              <el-button text>订单编号</el-button>
            </template>
            <p>{{ row.orderNo }}</p>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="goodsName" label="商品名称" width="140" align="center"/>
      <el-table-column prop="number" label="数量" width="100" align="center"/>
      <el-table-column prop="totalPrice" label="总价" width="100" align="center"/>
      <el-table-column prop="payNo" label="支付编号" width="140" align="center">
        <template #default="{row}">
          <el-popover placement="top-start" :width="300" trigger="hover">
            <template #reference>
              <el-button text>支付编号</el-button>
            </template>
            <p>{{ row.payNo }}</p>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="payTime" label="支付时间" width="180" align="center">
        <template #default="{ row }">
          <!-- 如果没有支付编号，显示倒计时 -->
          <span v-if="!row.payNo"><span style="color: red;font-size: 15px">{{ remainingTime(row) }}</span></span>
          <!-- 如果有支付编号，显示支付时间 -->
          <span v-else>{{ row.payTime }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="payStatus" label="支付状态" width="100" align="center">
        <template #default="{row}">
          <div v-if="!row.payNo">
            <el-tag type="warning" size="large">{{ row.payStatus }}</el-tag>
          </div>
          <el-tag v-else type="success" size="large">已支付</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="订单状态" width="100" align="center">
        <template #default="{row}">
          <div v-if="row.status">
            <el-tag type="warning" size="large">{{ row.status }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="deliveryAdd" label="收货地址" width="160" align="center"/>
      <el-table-column prop="" label="操作" width="200" align="center" fixed="right">
        <template #default="{row}">
          <el-button v-if="!row.payNo && remainingTime(row) !== '已过期'" type="primary" @click="pay(row)">支付
          </el-button>
          <el-button v-if="row.status === '已发货'" type="primary" @click="receipt(row)">收货</el-button>
          <el-button v-if="row.status === '已收货'" type="primary" @click="evaluation(row)">评价</el-button>
          <el-button v-if="row.status === '已评价'" type="success" disabled>评价成功</el-button>
          <el-button v-if="row.status === '已评价'||row.payStatus==='未支付'" type="danger" @click="del(row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
        v-model="dialogVisible"
        title="评价"
        width="600"
        :before-close="handleClose"
    >

      <el-card class="card-appraise">
        <el-upload
            @update:fileList="fileList = $event"
            @remove="handleRemove"
            list-type="picture-card"
            :class="{uploadBtn:hideBtn}"
            :on-change="handleFileChange"
            :auto-upload="false"
            :limit
            :multiple="true"
        >
          <template #file="{file}">
            <div v-if="file.isVideo">
              <video controls width="120">
                <source :src="file.url" type="video/mp4">
              </video>
            </div>
            <div v-else>
              <img class="el-upload-list__item-thumbnail" :src="file.url" alt=""/>
            </div>
            <span class="el-upload-list__item-actions">
            <span
                v-if="!disabled"
                class="el-upload-list__item-preview"
                @click="handlePictureCardPreview(file)"
            >
              <el-icon><zoom-in/></el-icon>
            </span>
            <span
                v-if="!disabled"
                class="el-upload-list__item-delete"
                @click="handleRemove(<UploadFile>file)"
            >
              <el-icon><Delete/></el-icon>
            </span>
          </span>

          </template>
          <div v-if="fileList.length < limit">
            <el-icon size="80px">
              <!-- 使用你的Camera组件，或者替换为默认的icon -->
              <Camera/>
            </el-icon>
          </div>

        </el-upload>
      </el-card>
      <div class="evaluation-container">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" v-model="content"
                  style="margin-bottom: 20px"></el-input>

        <div class="voice-container">
          <el-button @click="startRecognition" type="primary">语音输入</el-button>
          <el-button @click="stopRecognition" type="danger">结束</el-button>
        </div>

        <div class="rate-container">
          <div class="rate-item">
            <span>描述相符</span>
            <el-rate v-model="description" allow-half size="large"/>
          </div>
          <div class="rate-item">
            <span>物流服务</span>
            <el-rate v-model="logistics" allow-half size="large"/>
          </div>
          <div class="rate-item">
            <span>服务态度</span>
            <el-rate v-model="attitude" allow-half size="large"/>
          </div>
        </div>
        <div class="controller-container">
          <el-button v-if="shouldShowButton" @click="publish" type="primary">发布</el-button>
          <el-button @click="cancel">取消</el-button>
        </div>
        <el-dialog v-model="mediaVisible">
          <div v-if="dialogImageUrl">
            <img :src="dialogImageUrl" alt="Preview Image"/>
          </div>
          <div v-if="dialogVideoUrl">
            <video controls>
              <source :src="dialogVideoUrl" type="video/mp4">
            </video>
          </div>
        </el-dialog>
      </div>
    </el-dialog>
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

.evaluation-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.voice-container {
  margin-bottom: 20px;

  .el-button {
    width: 80px;
  }
}

.rate-container {
  display: flex;
  flex-direction: column;
}

.rate-item {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  margin-bottom: 10px;
}

.rate-item:last-child {
  margin-bottom: 20px;
}

/* 可能需要额外的样式来调整文本和星星之间的间距 */
.rate-item span {
  margin-right: 10px; /* 根据需要调整 */
}

.card-appraise {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  height: 200px; /* 或者一个具体的值，取决于你希望的高度 */
}

.controller-container {
  .el-button {
    width: 80px;
  }
}
</style>
<style>
.uploadBtn .el-upload--picture-card {
  display: none;
}
</style>