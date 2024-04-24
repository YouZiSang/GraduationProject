<script setup>
import {ref, onMounted, nextTick} from "vue";
import {getId, getAvatar, getName, getRole} from "@/utils/getCurrentUserInfo.js";


const shopperId = getId()
const role = getRole()
const router = useRouter();

const bookList = ref([
  {
    id: '',
    title: '',
    image: '',
    author: '',
    publisher: '',
    number: '',
    price: '',
    ISBN: '',
    bookType: '',
    sales: '',
    selected: false,
  }
])

const currentPage = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(5)//每页条数

const handleSizeChange = (size) => {
  pageSize.value = size;
  params.pageSize = size;  // 更新请求参数中的每页条数
  bookListResult();

}
//当前页码发生变化，调用此函数
const handleCurrentChange = (number) => {
  currentPage.value = number
  params.currentPage = number // 更新请求参数中的当前页码
  bookListResult();
}

import {
  bookListService,
  cancelCollectionService,
  collectionBookService,
  collectionStatusService,
  updateBooksHouseNumberService
} from "@/api/book.js";
import {
  addToCartService,
  batchDeleteShoppingCartService, buyBooksService,
  deleteShoppingCartService,
  selectShoppingCartService, shoppingNumberAndTotalService, updateNumberService
} from "@/api/shoppingCart.js";
import {Delete, Minus, Plus, ShoppingCart, Star, StarFilled} from "@element-plus/icons-vue";

const params = {
  currentPage: currentPage.value,
  pageSize: pageSize.value,
}

const bookListResult = async () => {
  let res = await bookListService(params)
  total.value = res.data.total
  bookList.value = res.data.items
  const ISBNs = res.data.items.map(book => book.ISBN);
  await getCollectionStatus(ISBNs)

}
bookListResult();

//收藏书籍设置
const toggleCollection = async (ISBN) => {
  // 找到被点击书籍的信息
  let selectedBook = bookList.value.find(book => book.ISBN === ISBN);
  if (selectedBook) {
    if (selectedBook.isCollected) {
      // 如果已经收藏，则执行取消收藏的操作
      // 调用取消收藏的接口或方法
      let res = cancelCollectionService(selectedBook.ISBN)
      // ...
      ElMessage.success(res.message);
      selectedBook.isCollected = false; // 取消收藏后将状态设置为未收藏
    } else {
      // 如果未收藏，则执行收藏的操作
      let res = await collectionBookService(selectedBook);
      ElMessage.success(res.message);
      selectedBook.isCollected = true; // 收藏成功后将状态设置为已收藏
    }
  }
};
// 在页面初始化时检查书籍是否已被收藏，如果是，则将 isCollected 设置为 true
const getCollectionStatus = async (ISBNs) => {

  // 调用 collectionStatusService 方法，获取收藏状态
  let res = await collectionStatusService(ISBNs);
  console.log(res.data)
  res.data.forEach(book => {
    let selectedBook = bookList.value.find(item => item.ISBN === book.ISBN);
    if (selectedBook) {
      selectedBook.isCollected = true;
    }
  });
};

//添加购物车
const total_number = ref(0)
const total_price = ref(0)
const showBall = ref(false)
const ballPosition = ref({
  x: 0, y: 0
})
const waiting = ref(false); // 控制是否正在等待

//查看购物车书籍的数量
const selectShoppingCartNumber = async () => {
  let res = await shoppingNumberAndTotalService()
  console.log(res.data)
  total_number.value = res.data.total_number
  total_price.value = res.data.total_price
}
selectShoppingCartNumber()
//点击添加书籍到购物车
const addToCart = async (ISBN, event) => {
  let selectedBook = bookList.value.find(book => book.ISBN === ISBN);
  console.log(selectedBook)
  await updateBooksHouseNumberService(selectedBook)
  if (!waiting.value) {

    let res = await addToCartService(selectedBook)
    ElMessage.success(res.message)
    // 更新小球的位置和显示状态
    showBall.value = true;
    total_number.value++;

    const icon = event.target;
    const rectIcon = icon.getBoundingClientRect();
    const iconPosition = {
      x: rectIcon.left + rectIcon.width / 2,
      y: rectIcon.top + rectIcon.height / 2,
    };

    // 将小球初始位置设置为添加按钮的位置
    ballPosition.value = {x: iconPosition.x, y: iconPosition.y}; // 设置小球初始位置的样式
    await nextTick();
    // 将小球终点位置设置为购物车图标的位置
    const cartIcon = document.querySelector('.shopping-cart-icon');
    const rectCart = cartIcon.getBoundingClientRect();
    const cartPosition = {
      x: rectCart.left + rectCart.width / 2,
      y: rectCart.top + rectCart.height / 2,
    };

    // 计算小球的运动轨迹
    const deltaX = cartPosition.x - iconPosition.x;
    const deltaY = cartPosition.y - iconPosition.y;


    // 使用 CSS 动画实现小球飞向购物车
    const ball = document.querySelector('.ball');
    ball.style.transition = 'transform 1s cubic-bezier(0.5, -0.55, 1, 1)';
    ball.style.transform = `translate(${deltaX}px, ${deltaY}px)`;

    // 在动画结束后隐藏小球，并等待一段时间再恢复可点击状态
    setTimeout(() => {
      showBall.value = false;
      waiting.value = false;
    }, 1000); // 1秒后隐藏小球，你可以根据需要调整时间


  }

  await bookListResult();
}


const drawer = ref(false)

//购物车列表
const shoppingCartList = ref([
  {
    goodsId: '',
    title: '',
    image: '',
    author: '',
    publisher: '',
    oldPrice: '',
    price: '',
    ISBN: '',
    number: '',
    totalPrice: ''
  }
])

import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";

//全选
const selectAll = ref(false)
//查看购物车
const checkShoppingCart = async () => {
  drawer.value = true
  let res = await selectShoppingCartService(shopperId)
  console.log(res.data)
  shoppingCartList.value = res.data
  await selectShoppingCartNumber()
}

//修改book house书的数量
const updateBooksHouseNumber = () => {

}

//修改收藏书的数量
const subtractNumber = async (ISBN) => {
  let selectedBook = shoppingCartList.value.find(book => book.ISBN === ISBN);
  await updateNumberService(selectedBook)
  await checkShoppingCart()
  await selectShoppingCartNumber()
}

//选择全部书籍
const handleSelectAll = (checked) => {
  shoppingCartList.value.forEach(item => {
    item.selected = checked;
  });
}

//批量删除
const batchDelete = async () => {
  const selectedItems = shoppingCartList.value.filter(item => item.selected);
  let res = await batchDeleteShoppingCartService(shopperId, selectedItems)
  ElMessage.success(res.message)
  //刷新购物车
  await checkShoppingCart()
};

//批量选择
const removeItem = async (ISBN) => {
  let selectedBook = shoppingCartList.value.find(book => book.ISBN === ISBN);
  let res = await deleteShoppingCartService(shopperId, selectedBook.ISBN);
  ElMessage.success(res.message)
  console.log(selectedBook)
  // 刷新购物车
  await checkShoppingCart();

};

//结算
const buyBooks = async () => {
  const selectedItems = shoppingCartList.value.filter(item => item.selected);
  let res = await buyBooksService(shopperId, selectedItems)
  ElMessage.success(res.message)
  //刷新购物车
  await checkShoppingCart()
  await router.push({path: 'orders'})
};

const bookDetails =(row)=>{
  // 将用户信息存储到 sessionStorage 中
  sessionStorage.setItem('bookParams', JSON.stringify({
    book_id: row.id,
    book_ISBN: row.ISBN,
    book_title: row.title,
    book_image: row.image,
    book_author: row.author,
    book_publisher: row.publisher,
    book_oldPrice: row.oldPrice,
    book_price: row.price,
    book_type: row.bookType,
    book_number: row.number,
    book_sales: row.sales
  }));
  router.push({path: 'bookDetails',query: {ISBN: row.ISBN}})
}

const handleMouseEnter = (event) => {
  event.target.classList.add('orange'); // 添加橙色样式类
}
const handleMouseLeave = (event) => {
  event.target.classList.remove('orange'); // 移除橙色样式类
}
// 在页面加载完成后调用 checkCollectionStatus 函数来检查收藏状态
onMounted(async () => {
  await bookListResult();
});
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>心理书屋</span>
        <div style="float: right">
          <router-link v-if="role === 'USER'" to="/user/orders">
            <el-button type="primary" style="margin-right: 20px" round>我的订单</el-button>
          </router-link>
          <!-- 如果role等于'ADMIN'，则显示按钮并链接到管理员订单页面 -->
          <router-link v-if="role === 'CONSULTANT'" to="/consultants/orders">
            <el-button type="primary" style="margin-right: 20px" round>我的订单</el-button>
          </router-link>
          <el-badge v-if="total_number >0" :value="total_number" :max="99" class="shoppingCart">
            <el-icon @click="checkShoppingCart()" size="30" class="shopping-cart-icon">
              <ShoppingCart/>
            </el-icon>
          </el-badge>
          <el-icon @click="checkShoppingCart()" size="30" class="shopping-cart-icon" v-else-if="! total_number >0">
            <ShoppingCart/>
          </el-icon>
        </div>
      </div>
    </template>
    <el-drawer v-model="drawer" title="购物车" size="80%">
      <el-button type="danger" @click="batchDelete" style="margin-bottom: 20px">批量删除</el-button>
      <el-table :data="shoppingCartList" border height="600">
        <el-table-column type="selection" width="40" align="center">
          <template #default="{ row }">
            <el-checkbox v-model="row.selected"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="image" label="" width="150" align="center">
          <template #default="scope">
            <el-image :src="scope.row.image" alt="书籍封面" style="width: 100px; height: 130px;"
                      :preview-src-list="[scope.row.image]" :preview-teleported="true"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="书名" width="150" align="center"/>
        <el-table-column prop="author" label="作者" width="200" align="center"/>
        <el-table-column prop="ISBN" label="ISBN" width="140" align="center"/>
        <el-table-column prop="number" label="数量" width="100" align="center"/>
        <el-table-column prop="price" label="价格" width="100" align="center">
          <template #default="{ row }">
            <p>￥ {{ row.price }}</p>
          </template>
        </el-table-column>
        <el-table-column prop="totalPrice" label="总价" width="100" align="center">
          <template #default="{ row }">
            ￥ {{ row.totalPrice }}
          </template>
        </el-table-column>
        <el-table-column prop="deliveryAdd" label="收货地址" width="160" align="center"/>
        <el-table-column label="" align="center">
          <template #default="{row}">
            <div style="display: flex; align-items: center;">
              <el-button type="info" @click="subtractNumber(row.ISBN)" circle>
                <el-icon size="20">
                  <Minus/>
                </el-icon>
              </el-button>
              <el-button type="danger" @click="removeItem(row.ISBN)" circle>
                <el-icon size="20">
                  <Delete/>
                </el-icon>
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <div style="display: flex; align-items: center; justify-content: space-between;">
          <!-- 第一部分：全选复选框 -->
          <div style="flex: 0.5;">
            <el-checkbox v-model="selectAll" @change="handleSelectAll" size="large">
              <span style="font-size: 20px">全选</span></el-checkbox>
          </div>

          <!-- 第二部分：商品数量信息 -->
          <div style="flex: 1; display: flex; align-items: center;">
            <span style="flex: 1;">共</span>
            <span style="font-size: 24px; color: #de6262;">{{ total_number }}</span>
            <span>件商品</span>
          </div>

          <!-- 第三部分：商品价格信息 -->
          <div style="flex: 1; display: flex; align-items: center;">
            <span style="flex: 1;">共</span>
            <span style="font-size: 24px; color: #de6262;">{{ total_price }}</span>
            <span>元</span>
          </div>

          <!-- 第四部分：全部购买按钮 -->
          <div style="flex: 1;">
            <el-button @click="buyBooks" class="buy" size="large">结算</el-button>
          </div>
        </div>
      </template>
    </el-drawer>
    <el-table :data="bookList" height="100%" style="width: 100%" :header-cell-style="{textAlign: 'center'}">
      <el-table-column prop="image" label="封面" width="150" fixed="left" align="center">
        <template #default="scope">
          <el-image :src="scope.row.image" alt="书籍封面" style="width: 100px; height: 130px;"
                    :preview-src-list="[scope.row.image]" preview-teleported="true"/>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="书名" width="150" align="center">
        <template #default="{ row }">
           <span class="book-title"
                 @mouseenter="handleMouseEnter"
                 @mouseleave="handleMouseLeave"
                 @click="bookDetails(row)">
      {{ row.title }}
           </span>
        </template>
      </el-table-column>
      <el-table-column prop="author" label="作者" width="180" align="center"/>
      <el-table-column prop="publisher" label="出版商" width="160" align="center"/>
      <el-table-column prop="bookType" label="图书类型" width="160" align="center"/>
      <el-table-column prop="price" label="价格" width="180" align="center">
        <template #default="{ row }">
          <span style="margin-right: 10px;font-size: 15px;color: orange">￥ {{ row.price }}</span>
          <span
              v-if="row.oldPrice !== undefined && row.oldPrice !== null && row.oldPrice !== '' && row.price !== row.oldPrice"
              style="text-decoration-line: line-through;">￥ {{ row.oldPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="number" label="" width="80" align="center">
        <template #default="{ row }">
          <span v-if="row.number === 0" style="font-size: 18px;color: #de6262">售罄</span>
          <span v-else></span>
        </template>
      </el-table-column>
      <el-table-column prop="sales" label="销量" width="120" align="center">
        <template #default="{ row }">
          已售 <span style="color: #de6262">{{ row.sales }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140" align="center" fixed="right">
        <template #default="{ row }">
          <div style="display: flex; align-items: center;">
            <div @click="toggleCollection(row.ISBN)" style="display: inline;">
              <el-button text v-if="row.isCollected">
                <el-icon size="36" color="#ecd93a">
                  <StarFilled/>
                </el-icon>
              </el-button>
              <el-button text v-else-if="!row.isCollected">
                <el-icon size="30">
                  <Star/>
                </el-icon>
              </el-button>
            </div>
            <el-button style="margin-left: 20px"
                       type="success"
                       @click="addToCart(row.ISBN,$event)"
                       circle>
              <el-icon>
                <Plus/>
              </el-icon>
            </el-button>
            <transition name="ball-fly">
              <div
                  v-if="showBall"
                  class="ball"
                  :style="{ top: ballPosition.y + 'px', left: ballPosition.x + 'px' }"
              ></div>
            </transition>
          </div>
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
.StarFilled {
  color: #de6262;
}

.el-table-column {
  text-align: center;
}

.el-table {


}

.ball {
  position: fixed;
  width: 20px;
  height: 20px;
  background-color: red;
  border-radius: 50%;
  z-index: 9999;
  transition: transform 1s cubic-bezier(0.68, -0.55, 1, 1.55); /* 添加过渡效果 */
}


.ball-fly-enter-active, .ball-fly-leave-active {
  transition: opacity 0s;
}

.ball-fly-enter, .ball-fly-leave-to /* .ball-fly-leave-active in <2.1.8 */
{
  opacity: 0;
}

.buy {
  background: linear-gradient(to right, #f9d423, #ff4e50);
  color: white;
  font-size: 20px;
  border-radius: 30px;
}

.el-p {
  margin-top: 5px;
}

.book-title {
  cursor: pointer; /* 设置鼠标悬停时变成手指状态 */
  transition: color 0.3s; /* 设置颜色过渡效果 */
}

.book-title.orange {
  color: orange; /* 设置标题文字为橙色 */
}
</style>