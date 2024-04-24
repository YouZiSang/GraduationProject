<script setup>
import {ref, onMounted, computed} from "vue";
import Editor from "wangeditor";
import {
  selectAllArticlesService,
  publishedArticlesService,
  thumbsUpService,
  selectAllThumbsUpService, addCommentsService, selectAllCommentsService, addLandlordCommentsService
} from '@/api/exchange/community.js'
import {ElMessage} from "element-plus";
import {CaretRight} from "@element-plus/icons-vue";
import {getId, getAvatar, getName} from "@/utils/getCurrentUserInfo.js";
//当前登录的头像
const avatar = getAvatar()
//当前登录的昵称
const name = getName()
//当前登录的id
const currentId = getId()

const postsList = ref([{
  landlordId: '',
  messageId: '',
  avatar: '',
  name: '',
  content: '',
  createdTime: '',
  role: '',
  likeCount: '',
  commentCount: ''

}])
//初始化
const selectAllArticles = async () => {
  let res = await selectAllArticlesService()
  postsList.value = res.data
}

selectAllArticles()
let editor

//富文本初始化
function initWangEditor(content) {
  setTimeout(() => {
    if (!editor) {
      editor = new Editor('#editor')
      editor.config.placeholder = '请输入内容'
      editor.config.uploadFileName = 'file'
      editor.config.uploadImgServer = 'http://localhost:8080/api/upload/wang/upload'
      editor.create()
    }
    editor.txt.html(content)
  }, 0)
}

const action = ref('')
//抽屉
const dialogVisible = ref(false)
const message = ref('')
const comment = ref('')
const thumbsUpLandlord = ref('')
const thumbsUp = ref('')
//打开抽屉
const openDrawer = (currentAction, currentMessage, currentComment) => {
  action.value = currentAction
  message.value = currentMessage
  comment.value = currentComment
  dialogVisible.value = true
  initWangEditor("")
  console.log(message.value)
}

//发表文章，评论
const submit = async () => {
  if (action.value === 'post') {
    let params = {
      messageId: currentId,
      avatar: avatar,
      name: name,
      content: editor.txt.html()
    }
    let res = await publishedArticlesService(params)
    ElMessage.success(res.message)
  } else if (action.value === 'commentLandlord') {
    let params = {
      landlordId: message.value.landlordId,
      commentId: currentId,
      avatar: avatar,
      name: name,
      content: editor.txt.html(),
      messageId: message.value.messageId,
    }
    let res = await addLandlordCommentsService(params)
    ElMessage.success(res.message)
    await selectAllArticles()
  } else if (action.value === 'comment') {
    let params = {
      landlordId: message.value.landlordId,
      commentId: currentId,
      avatar: avatar,
      name: name,
      content: editor.txt.html(),
      messageId: comment.value.commentId,
      replyId: comment.value.id
    }
    let res = await addCommentsService(params)
    ElMessage.success(res.message)
  }
  await commentAndLike()

  dialogVisible.value = false

}
//取消发表
const cancel = () => {
  dialogVisible.value = false
}


//点赞
const toggleImage = async (messageType, message, comment) => {
  if (messageType === 'thumbsUpLandlord') {
    if (!message.like) {
      const params = {
        landlordId: message.landlordId,
        messageId: message.messageId,
        name: name,
        thumbsUpId: currentId
      }
      message.like = true
      await thumbsUpService(params)
    } else {
      console.log(222)
    }
  } else if (messageType === 'thumbsUp') {
    if (!comment.like) {
      let params = {
        landlordId: message.landlordId,
        thumbsUpId: currentId,
        messageId: comment.commentId,
        commentReplyId: comment.id,
      }
      comment.like = true
      await thumbsUpService(params)
    } else {
      console.log(222)
    }
  }
  await commentAndLike()
}
//点赞列表
const thumbsUpList = ref([{
  landlordId: '',
  messageId: '',
  thumbsUpId: '',
  commentReplyId: ''
}])

const commentList = ref([{
  id: '',
  landlordId: '',
  //当前评论的ID
  commentId: '',
  avatar: '',
  name: '',
  content: '',
  createdTime: '',
  //被评论者的ID
  messageId: '',
  replyId: '',
  //角色
  role: '',
  likeCount: '',
  commentCount: ''
}])
//所有评论和点赞
const commentAndLike = async () => {
  let res = await selectAllThumbsUpService()
  thumbsUpList.value = res.data
  let result = await selectAllCommentsService(); // 通过API获取评论数据

  commentList.value = result.data; // 将获取到的评论数据存储在commentList中

  thumbsUpList.value.forEach((thumbsUpItem) => {
    postsList.value.forEach((message) => {
      console.log(postsList.value)
      if (
          thumbsUpItem.landlordId === message.landlordId &&
          thumbsUpItem.messageId === message.messageId &&
          thumbsUpItem.thumbsUpId === currentId
      ) {
        message.like = true;
      }
      commentList.value.forEach(comment => {
        if (comment.replyId) { // 检查当前评论是否有被回复的评论
          const repliedComment = commentList.value.find(c => c.id === comment.replyId); // 通过replyId找到被回复的评论
          if (repliedComment) {
            comment.replyName = repliedComment.name; // 将被回复的评论者姓名存储在replyName字段中
          }
        }
      });
      commentList.value.forEach(comment => {
        if (
            thumbsUpItem.landlordId === comment.landlordId &&
            thumbsUpItem.messageId === comment.commentId &&
            thumbsUpItem.thumbsUpId === currentId &&
            thumbsUpItem.commentReplyId === comment.id
        ) {
          comment.like = true;
        }
      });
    });
  });
}
commentAndLike();

const currentTime = ref(new Date());
console.log(currentTime)
onMounted(() => {
  setInterval(() => {
    currentTime.value = new Date();
  }, 1000);
});

//发帖时间
const formatTime = (time) => {
  const createdTime = new Date(time);
  const currentTime = new Date();
  const timeDiff = currentTime - createdTime;
  const diffMinutes = Math.floor(timeDiff / (1000 * 60));
  const diffHours = Math.floor(diffMinutes / 60);
  const diffDays = Math.floor(diffHours / 24);

  if (diffMinutes < 1) {
    return '刚刚';
  } else if (diffMinutes < 60) {
    return `${diffMinutes}分钟前`;
  } else if (diffHours < 24) {
    return `${diffHours}小时前`;
  } else if (diffDays < 30) {
    return `${diffDays}天前`;
  } else {
    const months = Math.floor(diffDays / 30);
    return `${months}个月前`;
  }
};
const visibleComments = ref([]);
const commentLimit = ref(3); // 控制显示的评论数量
const expanded = ref(false); // 控制是否展开评论

onMounted(async () => {
  await fetchComments();
  await commentAndLike();
});

const fetchComments = async () => {
  let result = await selectAllCommentsService(); // 通过API获取评论数据
  commentList.value = result.data; // 将获取到的评论数据存储在commentList中
  updateVisibleComments();
};

const toggleExpand = () => {
  expanded.value = !expanded.value;
  if (!expanded.value) {
    // 如果折叠评论，则清空 visibleComments 数组
    visibleComments.value = [];
  } else {
    // 如果展开评论，则更新可见评论
    updateVisibleComments();
  }
};

const updateVisibleComments = () => {
  if (expanded.value) {
    // 如果展开状态，则显示所有评论
    visibleComments.value = commentList.value;
  } else {
    // 如果折叠状态，则只显示前面的commentLimit条评论
    visibleComments.value = commentList.value.slice(0, commentLimit.value);
  }
};

const moreCommentsAvailable = computed(() => {
  return commentLimit.value < commentList.value.length;
});

</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-card :header-overflow-hidden="true" :footer-overflow-hidden="true">
        <template #header>
          社区交流
        </template>
        <div class="main">
          <!--楼主区-->
          <div v-for="message in postsList" :key="message.landlordId" class="container">
            <el-avatar icon="el-icon-user" :src="message.avatar" size="large"
                       style="margin-right: 10px;display: inline-block"></el-avatar>
            <div style="display: inline-block">
            <span v-if="message.role === '咨询师'"
                  style="vertical-align: super;margin-right: 10px ">[{{ message.role }}]</span>
                <span style="vertical-align: super; color: #c1c1c1">{{ message.name }}</span>
              <div v-html="message.content" class="content" style="max-width: 500px;max-height: 500px"></div>
            </div>
            <div style="margin-left: 65px;font-size: 13px">{{ formatTime(message.createdTime) }}</div>
            <div class="interaction">
              <div @click="toggleImage('thumbsUpLandlord',message,comment)">
                <img
                    class="hover-effect"
                    :src="message.like ? '/images/support.png' : '/images/notSupport.png'"
                    alt="Image"
                    style="height: 22px; width: 22px;display: inline-block"
                />
                <div class="hover-effect" style="display: inline-block">
                  <span style="font-size: 14px;margin-right: 5px">{{ message.like ? '已赞' : '赞' }}</span>
                  <span class="number">{{ message.likeCount }}</span>
                </div>
              </div>
              <div class="hover-effect" style="display: inline-block">
                <span style="font-size: 14px ;margin-right: 5px" @click="openDrawer('commentLandlord',message,comment)">评论</span>
                <span class="number">{{ message.commentCount }}</span>
              </div>
            </div>
            <!--评论区-->
            <div v-for="(comment, index) in visibleComments" :key="index" style="margin-left: 60px"  v-show="expanded || index < commentLimit">
              <div v-if="comment.landlordId === message.landlordId">
                <div style="display: inline-block">
                  <el-avatar :src="comment.avatar" style="margin-right: 10px;"></el-avatar>
                </div>
                <div style="display: inline-block">
                  <span v-if="comment.role === '咨询师'">[{{ comment.role }}]</span>
                  <span style="color: #c1c1c1">{{ comment.name }}</span>
                  <el-icon style="margin-left: 10px" color="#c1c1c1" size="15px">
                    <CaretRight/>
                  </el-icon>

                  <span style="margin-left: 10px;color:#c1c1c1">{{
                      comment.replyName ? comment.replyName : message.name
                    }}</span>
                  <div v-html="comment.content" class="comment-content"></div>
                </div>
                <div style="margin-left: 50px;font-size: 13px;color:#c1c1c1 ">{{ formatTime(comment.createdTime) }}</div>

                <div class="interaction">
                  <div @click="toggleImage('thumbsUp',message,comment)">
                    <img
                        class="hover-effect"
                        :src="comment.like ? '/images/support.png' : '/images/notSupport.png'"
                        alt="Image"
                        style="height: 17px; width: 17px;display: inline-block"
                    />
                    <div class="hover-effect" style="display: inline-block">
                      <span style="font-size: 13px;margin-right: 5px">{{ comment.like ? '已赞' : '赞' }}</span>
                      <span class="number">{{ comment.likeCount }}</span>
                    </div>
                  </div>
                  <div class="hover-effect" style="display: inline-block">
                    <span style="font-size: 13px;margin-right: 5px"
                          @click="openDrawer('comment',message,comment)">评论</span>
                    <span class="number">{{ comment.commentCount }}</span>
                  </div>
                </div>
              </div>
            </div>
            <div @click="toggleExpand(messageIndex)" style="margin-bottom: 10px">
              <span style="margin-right: 45%"></span><span>{{ expanded ? '折叠评论' : '展开更多评论' }}</span>
            </div>
            <el-divider />
          </div>
        </div>

        <el-dialog
            v-model="dialogVisible"
            draggable
            overflow
            width="50%"
            :before-close="handleClose"
        >
          <div id="editor">
          </div>
          <div class="controller">
            <el-button @click="cancel()">取消</el-button>
            <el-button @click="submit()" type="primary">发表</el-button>
          </div>
        </el-dialog>
        <template #footer >
          <div style="height: 100%" >
            <el-button @click="openDrawer('post',message)">发表文章</el-button>
          </div>
        </template>
      </el-card>
    </el-container>
  </div>
</template>

<style scoped lang="scss">
.common-layout {
  width: 60%;
  height: 100%;
  margin: auto;
  .main{
    overflow-x: hidden;
  }
  .el-card {
    width: 100%;
    height: 100%;
    overflow: auto; /* 启用滚动条 */

    .container {
      margin-top: 20px;


      .interaction {
        display: flex;
        justify-content: flex-end;
        margin: 0 10px 10px 0
      }

      .content {
        height: 100%;
      }

      .number {
        margin-right: 14px;
        font-size: 14px;

        :hover {
          color: #409eff;;
          cursor: pointer;
        }

      }

      .hover-effect:hover {
        color: #409eff;;
        cursor: pointer;
      }

    }

    .controller {
      display: flex;
      justify-content: flex-end;
    }

  }


}


</style>