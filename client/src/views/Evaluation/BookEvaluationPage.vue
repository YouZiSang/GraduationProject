<script setup>
import {ref, onMounted} from 'vue'
import {useRoute} from "vue-router";
import {findBookEvaluationService, getRelatedBooksService} from "@/api/book.js";
import Breadcrumb from "@/components/Breadcrumb.vue";

const route = useRoute();
const bookDetails = ref({})
const bookEvaluation = ref([])
const getEvaluation = async (id) => {
  let res = await findBookEvaluationService(id)
  bookEvaluation.value = res.data

  bookEvaluation.value.forEach(item => {
    // 提取每个对象中的 media 属性值
    let mediaString = item.media;
    // 如果 mediaString 是字符串类型，则处理并推入 mediaSteams.value 数组
    if (typeof mediaString === 'string') {
      let mediaArray = mediaString.replace(/\[|\]/g, '').split(',').map(item => item.trim());
      mediaSteams.value.push(mediaArray);
    }
  });

}

const relatedBooks = ref([])
const getRelatedBook = async (type, ISBN) => {
  let res = await getRelatedBooksService(type, ISBN)
  relatedBooks.value = res.data
  console.log(relatedBooks.value.length)
}
const mediaSteams = ref([])
const getBookDetails = async () => {

}
// Helper methods to check media type
const isImage = (url) => ['.png', '.jpg', '.jpeg', '.gif'].some(extension => url.includes(extension));
const isVideo = (url) => ['.mp4', '.webm', '.ogg'].some(extension => url.includes(extension));

const getImagesUrl = (name) => {
  return new URL(name, import.meta.url).href
}
const imageUrl = "../../assets/images/noData.png"
const bookUrl = "../../assets/images/book.jpg"


onMounted(() => {
  const params = JSON.parse(sessionStorage.getItem('bookParams'));
  if (params) {
    const details = {
      id: params.book_id,
      ISBN: params.book_ISBN,
      title: params.book_title,
      image: params.book_image,
      author: params.book_author,
      publisher: params.book_publisher,
      oldPrice: params.book_oldPrice,
      price: params.book_price,
      bookType: params.book_type,
      number: params.book_number,
      sales: params.book_sales,
    }
    bookDetails.value = details
    getEvaluation(details.id)
    getRelatedBook(details.bookType, details.ISBN)

    getBookDetails()
  }
})
</script>

<template>
  <el-tabs type="border-card" class="demo-tabs" style="background-color: #fdebdb">
    <el-tab-pane label="详细信息">
      <div class="book-info">
        <!-- 添加相关书籍推荐 -->
        <div>
          <div class="book-list">
            <div class="related-books">
              <div>相关书籍推荐</div>
            </div>
            <div class="item">
              <div class="book-item" v-for="book in relatedBooks" :key="book.id">
                <el-image :src="book.image"></el-image>
                <div>
                  <p style="font-size: 20px">{{ book.title }}</p>
                  <span style="font-size:20px">￥：<strong style="font-size:17px;color: orangered"> {{
                      book.price
                    }}</strong></span>
                  <span style="text-decoration-line: line-through;margin-left: 20px;color: #8c939d">￥：{{
                      book.oldPrice
                    }}</span>
                </div>
              </div>
            </div>
            <div v-if="!relatedBooks.length" style="padding: 20px">
              <el-image :src="getImagesUrl(imageUrl)" style="width: 100px;height: 100px"></el-image>
              暂无相关书籍
            </div>
          </div>
        </div>
        <div style="display: flex">
          <div class="book-cover">
            <div style="display: flex">
              <div style="margin-right: 30px">
                <el-image :src="bookDetails.image"></el-image>
              </div>
              <div>
                <div style="display: block">
                  <div>
                    <p style="font-size: 30px">{{ bookDetails.title }}</p>
                    <p style="font-size: 20px">销售量：<span style="font-size:24px;color: orangered">{{
                        bookDetails.sales
                      }}</span></p>
                    <p style="font-size: 18px">{{ bookDetails.author }}</p>
                    <b style="font-size: 18px;">￥：<span style="font-size: 20px;color: orangered">{{
                        bookDetails.price
                      }}</span></b>
                    <span style="text-decoration-line: line-through;margin-left: 20px;color: #8c939d">￥：{{
                        bookDetails.oldPrice
                      }}</span>
                    <p style="font-size: 18px">{{ bookDetails.bookType }}</p>
                    <p>库存：<span style="font-size:20px;color: orangered">{{ bookDetails.number }}</span></p>
                    <p>ISBN：{{ bookDetails.ISBN }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="评价">
      <div class="evaluation-page">
        <div v-for="item in bookEvaluation" :key="item.id">
          <div class="evaluation-content">
            <el-avatar :src="item.avatar" size="large"></el-avatar>
            <div style="display: flex;flex-direction: column;">
              <div class="name">{{ item.name }}</div>
              <div class="content">
                <div style="display: flex">
                  <div class="goods-name">
                    已购：{{ bookDetails.title }}
                  </div>
                  <div class="evaluation-date">
                    {{ new Date(item.createdTime).toLocaleDateString('zh-CN', {month: 'numeric', day: 'numeric'}) }}
                  </div>
                </div>
                <div class="text-content">
                  {{ item.content }}
                </div>
                <div class="media-content">
                  <div v-for="media in item.media.split(',')" :key="media.trim()">
                    <!-- 检查媒体类型并渲染相应组件 -->
                    <template v-if="isImage(media.trim())">
                      <el-image :src="media.trim()"
                                :preview-src-list="[media.trim()]"></el-image>
                    </template>
                    <template v-else-if="isVideo(media.trim())">
                      <video controls v-bind:src="media.trim()">
                      </video>
                    </template>
                  </div>
                </div>
                <div class="evaluation-score">
                  描述相符：
                  <el-rate v-model="item.description" disabled
                           show-score text-color="#ff9900"
                           score-template="{value} 分"></el-rate>
                </div>
                <div class="evaluation-score">
                  物流服务：
                  <el-rate v-model="item.logistics" disabled
                           show-score text-color="#ff9900"
                           score-template="{value} 分"></el-rate>
                </div>
                <div class="evaluation-score">
                  服务态度：
                  <el-rate v-model="item.attitude" disabled
                           show-score text-color="#ff9900"
                           score-template="{value} 分"></el-rate>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="bottom-news">
          <div class="text-news">
            <span>暂时没有新评价了</span>
          </div>
          <div>
            <el-image :src="getImagesUrl(imageUrl)" style="width: 100px;height: 100px"></el-image>
          </div>
        </div>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<style scoped lang="scss">
.book-info {
  background-color: #fdebdb;
  display: flex;
}

.book-list {
  margin-top: 20px;
  max-width: 600px;
  border: #98d8cd 2px dashed;
}

.related-books {
  display: block;
  text-align: center;
  align-content: center;
  width: 200px;
  height: 50px;
  margin: auto;
  font-size: 20px;
}

.item {
  display: flex;

  max-width: 600px;
}

.book-item {
  background-color: white;
  width: 200px;
  height: 300px;
  border-radius: 5px;
  box-shadow: #8c939d 1px 1px 5px;
  padding: 20px;
  margin: 20px;

  .el-image {
    min-width: 100px;
    max-width: 180px;
    min-height: 150px;
    max-height: 200px;
  }
}

.book-cover {
  width: 600px;
  margin-left: 50px;
  margin-top: 20px;
  margin-right: 30px;

  .el-image {
    width: 250px;
    height: 350px;
    border-radius: 5px;
    margin-right: 20px;
  }
}

.evaluation-page {
  background-color: white;
  width: 800px;
  box-shadow: #8c939d 1px 1px 5px;
  border-radius: 10px;
  padding: 20px 50px;
  margin: 0 auto;
}

.evaluation-content {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}

.name {
  margin-top: 15px;
  margin-left: 20px;
  font-size: 18px;
}

.content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  min-width: 500px;
  min-height: 200px;
  max-width: 600px;
  max-height: 500px;
  border-radius: 8px;
  box-shadow: #8c939d 1px 1px 5px;
  margin-left: 20px;
  margin-top: 20px;

  .goods-name {
    font-size: 13px;
    color: #8c939d;
    min-width: 300px;
    margin-right: 30px;
    margin-bottom: 10px;
  }

  .evaluation-date {
    font-size: 13px;
    color: #8c939d;
  }

  .text-content {
    min-width: 300px;
    margin-right: 30px;
  }

  .media-content {
    margin-top: 20px;
    display: flex;
    margin-bottom: 20px;
  }

  .el-image {
    min-width: 150px;
    min-height: 150px;
    max-width: 200px;
    max-height: 200px;
    margin-right: 15px;
    border-radius: 5px;
  }

  video {
    min-width: 150px;
    min-height: 150px;
    max-width: 200px;
    max-height: 200px;
    margin-right: 15px;
    border-radius: 5px;
  }
}

.evaluation-score {
  display: flex;
  align-items: center;
}

.bottom-news {
  display: flex;
  align-items: center;
  margin-left: 200px;
  font-size: 20px;

}

.text-news {
  span {
    font-size: 20px;
    color: white;
    filter: drop-shadow(1px 1px 10px #232323);;
  }
}
</style>