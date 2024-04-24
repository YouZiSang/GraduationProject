<script setup lang="ts">
import {onMounted, reactive, ref} from "vue";
import {Bottom, Delete, Edit, Plus, Search, Top} from "@element-plus/icons-vue";
import {
  getAllBookService,
  updateBookInfoService,
  addBookInfoService,
  delBookInfoService,
  searchBookInfoService,
  findBooksTypeService,
  searchBookTypeInfoService
} from "@/api/admin.js";
import {editAvatar} from "@/api/userInfo"
import {useTokenStore} from '@/stores/token';

const token = useTokenStore().token;

interface Book {
  ISBN: string;
  title: string;
  author: string;
  publisher: string;
  number: number;
  oldPrice: number,
  bookType: string;
  price: number;
  sales: number;
  collectNumber: number;
  row: {
    image: string
  }
}

interface Type {
  id: string;
  name: string;
}

const bookList = ref<Book[]>([])
const bookListResult = async () => {
  try {
    let res = await getAllBookService();
    console.log(res.data);
    bookList.value = res.data;
  } catch (error) {
    console.error("Error fetching book list:", error);
  }
}
import type {FormProps, FormRules, UploadFile, UploadProps} from 'element-plus'
import {ElMessage} from "element-plus";

const labelPosition = ref<FormProps['labelPosition']>('right')
//查找条件输入框
const searchBook = ref('')
const ids = ref([])
//重置搜索框
const reset = () => {
  searchBook.value = ''
  bookListResult()
  bookInfo.bookType = ''

}

const selectType = async () => {
  let res = await searchBookTypeInfoService(bookInfo.bookType)
  bookList.value = res.data
}

const ascIconColor = ref()
const descIconColor = ref()
const sortStatus = ref('asc'); // 'asc' 为升序，'desc' 为降序
const sortSales = () => {
  // 切换排序状态
  sortStatus.value = sortStatus.value === 'asc' ? 'desc' : 'asc';

  // 对数据进行排序
  bookList.value.sort((a, b) => {
    const dateA = new Date(a.sales);
    const dateB = new Date(b.sales);
    return sortStatus.value === 'asc' ? dateA - dateB : dateB - dateA;
  });

  // 更新图标颜色
  ascIconColor.value = sortStatus.value === 'asc' ? 'red' : 'black';
  descIconColor.value = sortStatus.value === 'asc' ? 'black' : 'red';

}

const typeObjs = ref<Type[]>([]);
const findType = async () => {
  let res = await findBooksTypeService()
  typeObjs.value = res.data
}


//修改图书信息弹窗
const editVisible = ref(false)
const search = async () => {

  let res = await searchBookInfoService(searchBook.value)
  bookList.value = res.data
}
let bookInfo = reactive({
  id: '',
  ISBN: '',
  title: '',
  author: '',
  publisher: '',
  bookType: '',
  number: 0,
  oldPrice: 0,
  price: 0,
  sales: 0,
  collectNumber: 0,
  image: ''
});

// // 监听 editBook 对象的变化
// watch(editBook, (newVal) => {
//   // 寻找在 bookList 中与 editBook 对象对应的书籍信息的索引位置
//   const index = bookList.value.findIndex(item => item.ISBN === newVal.ISBN);
//
//   // 如果找到了对应的索引
//   if (index !== -1) {
//     // 创建 editBook 的深拷贝以避免直接引用
//     const updatedBook = JSON.parse(JSON.stringify(newVal));
//
//     // 替换 bookList 中原来的对应书籍信息为更新后的信息
//     bookList.value.splice(index, 1, updatedBook);
//   }
// });
const oldISBN = ref('')
const controller = ref('')

const justSelect = (uploadBook: UploadFile) => {
  if (uploadBook.raw) {
    bookInfo.image = URL.createObjectURL(uploadBook.raw);
    bookCover.value = uploadBook;
  }
}
const add = async () => {
  for (const key in bookInfo) {
    bookInfo[key] = ''; // 将每个属性值置为空字符串或默认值
  }
  controller.value = 'add'
  editVisible.value = true

}

const edit = async (ISBN) => {
  // 将找到的书籍信息赋值给 editBook
  const bookToEdit = bookList.value.find(item => item.ISBN === ISBN);

  // 深拷贝并将属性值赋给 bookInfo
  Object.assign(bookInfo, JSON.parse(JSON.stringify(bookToEdit)));

  controller.value = 'edit'
  oldISBN.value = ISBN
  editVisible.value = true
}
const del = async (ISBN) => {
  const delBookInfo = bookList.value.find(item => item.ISBN === ISBN);
  let res = await delBookInfoService(delBookInfo)
  ElMessage.success(res.message)
  await bookListResult()
}
const handleSelectionChange = async (rows) => {
  ids.value = rows.map(item => item.id)
}
const bookCover = ref()
//批量导出数据
const exportBooks = () => {
  console.log(ids.value)
  if (!ids.value.length) {
    window.open('http://localhost:8080/api/admin/export?token=' + token + "&searchBook=" + searchBook.value)

  }else {
    let idStr = ids.value.join(',')
    window.open('http://localhost:8080/api/admin/export?token=' + token + "&ids=" + idStr)
  }
}

const importSuccess = (res,file,fileList) => {
  ElMessage.success("导入成功")
}
//确定
const confirm = async () => {
  if (bookCover.value) {
    const data = new FormData();
    data.append("file", bookCover.value.raw);
    let res = await editAvatar(data);
    bookInfo.image = res.data
  }

  if (controller.value === 'add') {
    if (bookCover.value) {
      let res = await addBookInfoService(bookInfo)
      if (ElMessage.success(res.message)) {
        editVisible.value = false
      }
    } else {
      ElMessage.error("没有封面")
    }

  } else if (controller.value === 'edit') {
    // 判断 bookCover 是否存在文件
    let editBookParams = {
      id: bookInfo.id,
      ISBN: bookInfo.ISBN,
      title: bookInfo.title,
      image: bookInfo.image,
      author: bookInfo.author,
      bookType: bookInfo.bookType,
      publisher: bookInfo.publisher,
      number: bookInfo.number,
      oldPrice: bookInfo.oldPrice,
      price: bookInfo.price,
      oldISBN: oldISBN.value
    }

    let res = await updateBookInfoService(editBookParams)
    if (ElMessage.success(res.message)) {
      console.log(res.message)
      editVisible.value = false
    }
  }

  await bookListResult()
}

onMounted(() => {
  bookListResult()
  findType()
})

const bookRules = reactive<FormRules<Book>>({
  title: [
    {required: true, message: '请输入书名', trigger: 'blur'}
  ],
  ISBN: [
    {required: true, message: '请输入ISBN', trigger: 'blur'},
    {pattern: /^[0-9]{13}$/, message: '请输入13位数字的ISBN', trigger: 'blur'}
  ],
  author: [
    {required: true, message: '请输入作者', trigger: 'blur'}
  ],
  publisher: [
    {required: true, message: '请输入出版社', trigger: 'blur'}
  ],
  number: [
    {required: true, message: '请输入书籍数量', trigger: 'blur'},
    {type: 'number', message: '请输入数字', trigger: 'blur'}
  ],
  oldPrice: [
    {required: true, message: '请输入价格', trigger: 'blur'},
    {pattern: /^([1-9]\d*|0)(\.\d{1,2})?$/, message: '请输入最多两位小数的数字', trigger: 'blur'}
  ],
  bookType: [
    {required: true, message: '请选择图书类型', trigger: 'blur'}
  ],
  price: [
    {required: true, message: '请输入价格', trigger: 'blur'},
    {pattern: /^([1-9]\d*|0)(\.\d{1,2})?$/, message: '请输入最多两位小数的数字', trigger: 'blur'}
  ]
})

</script>

<template>
  <el-card>
    <template #header>
      图书列表
    </template>
    <div style="display: flex;margin-bottom: 10px">
      <el-input v-model="searchBook" placeholder="查找" style="width: 200px;margin-right: 10px"></el-input>
      <el-select v-model="bookInfo.bookType" placeholder="查找图书类型" style="width: 200px;margin-right:10px"
                 @change="selectType">
        <el-option
            v-for="item in typeObjs"
            :key="item.id"
            :label="item.name"
            :value="item.id"
        />
      </el-select>
      <el-button @keyup.enter.native="search" @click="search" type="primary" :icon="Search">Search</el-button>
      <el-button @click="reset" type="warning">清空</el-button>
    </div>
    <div style="margin-bottom: 10px">
      <el-button @click="add" color="#626aef" style="margin-right:10px">添加书籍</el-button>
      <el-button @click="sortSales" color="#99ccff">按销售量排序
        <el-icon :color="ascIconColor">
          <Top/>
        </el-icon>
        <el-icon :color="descIconColor">
          <Bottom/>
        </el-icon>
      </el-button>
      <el-button type="success" plain @click="exportBooks">导出图书数据</el-button>
      <el-upload action="http://localhost:8080/api/admin/importBooks" :headers="{Authorization: token}"
                 :show-file-list="false"
                 :on-success="importSuccess"
                 accept=".xls,.xlsx"
                 style="display: inline-block;margin-left: 10px">
        <el-button type="primary" plain>导入图书数据</el-button>
      </el-upload>
    </div>
    <el-table :data="bookList" height="600px" style="width: 100%" border :header-cell-style="{textAlign: 'center'}"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column prop="image" label="书籍封面" width="150" align="center">
        <template #default="scope">
          <el-image :src="scope.row.image" alt="书籍封面" style="width: 100px; height: 130px;"
                    :preview-src-list="[scope.row.image]" preview-teleported/>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="书名" width="160" align="center"/>
      <el-table-column prop="ISBN" label="ISBN" width="160" align="center"/>
      <el-table-column prop="author" label="作者" width="180" align="center"/>
      <el-table-column prop="publisher" label="出版商" width="160" align="center"/>
      <el-table-column prop="number" label="图书数量" width="100" align="center"/>
      <el-table-column prop="bookType" label="图书类型" width="120" align="center"/>
      <el-table-column prop="collectNumber" label="收藏数量" width="100" align="center">
        <template #default="{ row }">
          <span v-if="row.collectNumber > 1">{{ row.collectNumber }}🔥</span>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="160" align="center">
        <template #default="{ row }">
          <span style="margin-right: 10px;font-size: 15px;color: orange">￥ {{ row.price }}</span>
          <span
              v-if="row.oldPrice !== undefined && row.oldPrice !== null && row.oldPrice !== '' && row.price !== row.oldPrice"
              style="text-decoration-line: line-through;">￥ {{ row.oldPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="sales" label="销售额" width="120" align="center"/>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="{ row }">
          <el-button @click="edit(row.ISBN)" type="primary" :icon="Edit" circle></el-button>
          <el-button @click="del(row.ISBN)" type="danger" :icon="Delete" circle></el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
        v-model="editVisible"
        title="图书信息"
        width="600"
        draggable
        overflow
    >
      <el-form :model="bookInfo" :label-position="labelPosition" :rules="bookRules" label-width="80px">
        <el-form-item prop="image" style="text-align: center;">
          <el-upload
              class="avatar-uploader"
              v-model="bookCover"
              :show-file-list="false"
              :on-change="justSelect"
              :auto-upload="false"
          >
            <template v-if="bookInfo.image">
              <!-- 显示上传框和头像 -->
              <el-image :src="bookInfo.image" style="width: 150px; height: 200px" id="imageId" class="avatar"/>
            </template>
            <template v-else>
              <el-icon class="avatar-uploader-icon">
                <Plus/>
              </el-icon>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label="书名" prop="title">
          <el-input v-model="bookInfo.title"/>
        </el-form-item>
        <el-form-item label="ISBN" prop="ISBN">
          <el-input v-model="bookInfo.ISBN"/>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="bookInfo.author"/>
        </el-form-item>
        <el-form-item label="出版社" prop="publisher">
          <el-input v-model="bookInfo.publisher"/>
        </el-form-item>
        <el-form-item label="书籍数量" prop="number">
          <el-input v-model.number="bookInfo.number"/>
        </el-form-item>
        <el-form-item label="图书类型" prop="type">
          <el-select v-model="bookInfo.bookType" placeholder="请选择图书类型" style="width: 300px">
            <el-option
                v-for="item in typeObjs"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="原价" prop="oldPrice">
          <el-input v-model="bookInfo.oldPrice"/>
        </el-form-item>
        <el-form-item label="现价" prop="price">
          <el-input v-model="bookInfo.price"/>
        </el-form-item>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button @click="confirm" type="primary">确定</el-button>
      </el-form>
    </el-dialog>
  </el-card>
</template>

<style scoped lang="scss">
.el-form {
  padding: 30px;

  .el-input {
    width: 300px;
  }

  .el-button {
    margin-left: 15%;
    width: 100px
  }

  label {
    color: #de6262;
  }
}


.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  overflow: hidden;
  border-radius: 6px;
  cursor: pointer;
  transition: var(--el-transition-duration-fast);
  border: 1px dashed var(--el-border-color);
}
</style>