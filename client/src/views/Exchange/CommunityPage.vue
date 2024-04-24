<script setup>
import {ref, onMounted} from 'vue'
import {getId, getAvatar, getName, getRole} from "@/utils/getCurrentUserInfo.js";

const id = getId()
const avatar = getAvatar()
const name = getName()

const postList = ref([{
  landlordId: '',
  messageId: '',
  avatar: '',
  name: '',
  content: '',
  commentCount: 0,
  like: false,
  likeCount: 0
}])
const supportList = ref([{
  landlordId: '',
  messageId: '',
  thumbsUpId: '',
  commentReplyId: ''
}])
const commentList = ref([{
  landlordId: '',
  messageId: '',
  id: '',
  avatar: '',
  name: '',
  content: '',
  like: false,
  likeCount: 0

}])
import Editor from "wangeditor";
import {
  addCommentsService,
  addLandlordCommentsService, cancelThumbUpService, delArticleService, delCommentService,
  publishedArticlesService,
  selectAllArticlesService,
  selectAllCommentsService, selectAllThumbsUpService,
  thumbsUpService
} from "@/api/exchange/community.js";
import {ElMessage} from "element-plus";
import {CaretRight} from "@element-plus/icons-vue";

let editor = null
const initWangEditor = (content) => {

  setTimeout(() => {
    if (!editor) {
      editor = new Editor('#editor')
      editor.config.placeholder = '请输入内容'
      editor.config.uploadFileName = 'file'
      editor.config.uploadVideoName = 'video'
      editor.config.uploadImgServer = 'http://localhost:8080/api/upload/wang/upload'
      editor.config.uploadVideoServer = 'http://localhost:8080/api/upload/wang/video'
      editor.config.height = 500;
      editor.create()

    }
    editor.txt.html(content)
  }, 0)
}

const selectAllArticles = async () => {
  let res = await selectAllArticlesService()
  postList.value = res.data
}

const dialogVisible = ref(false)
const action = ref('')
const message = ref('')
const comment = ref('')
const openVisible = (currentAction, currentMessage, currentComment) => {
  action.value = currentAction
  message.value = currentMessage
  comment.value = currentComment
  dialogVisible.value = true
  initWangEditor('')

}

const support = async (messageType, item, comment) => {
  if (messageType === 'thumbsUpLandlord') {
    const params = {
      landlordId: item.landlordId,
      messageId: item.messageId,
      name: name,
      thumbsUpId: id
    }
    if (!item.like) {
      item.like = true
      item.likeCount = item.likeCount + 1;
      console.log(params)
      await thumbsUpService(params)
    } else {
      await cancelThumbUpService(params)
      item.likeCount = item.likeCount - 1;
      item.like = false
    }
  } else if (messageType === 'thumbsUpComment') {
    let params = {
      landlordId: item.landlordId,
      thumbsUpId: id,
      messageId: comment.messageId,
      commentReplyId: comment.id,
    }
    if (!comment.like) {
      comment.like = true
      comment.likeCount = comment.likeCount + 1;
      console.log(params)
      await thumbsUpService(params)
    } else {
      await cancelThumbUpService(params)
      comment.likeCount = comment.likeCount - 1;
      comment.like = false
    }
  }

}
const submit = async () => {
  if (action.value === 'post') {
    let params = {
      messageId: id,
      avatar: avatar,
      name: name,
      content: editor.txt.html()
    }
    let res = await publishedArticlesService(params)
    ElMessage.success(res.message)
    await selectAllArticles()
  } else if (action.value === 'commentLandlord') {
    let params = {
      landlordId: message.value.landlordId,
      commentId: id,
      avatar: avatar,
      name: name,
      content: editor.txt.html(),
      messageId: message.value.messageId,
    }
    message.value.commentCount = message.value.commentCount + 1
    let res = await addLandlordCommentsService(params)
    console.log(params)
    ElMessage.success(res.message)
    await selectAllComments()
  } else if (action.value === 'comment') {
    let params = {
      landlordId: message.value.landlordId,
      commentId: id,
      avatar: avatar,
      name: name,
      content: editor.txt.html(),
      messageId: comment.value.commentId,
      replyId: comment.value.id
    }

    comment.value.commentCount = comment.value.commentCount + 1
    let res = await addCommentsService(params)
    ElMessage.success(res.message)
    await selectAllComments()
  }

  dialogVisible.value = false
}


const selectAllComments = async () => {
  let res = await selectAllCommentsService()
  commentList.value = res.data

  let result = await selectAllThumbsUpService()
  supportList.value = result.data
  supportList.value.forEach((support) => {
    postList.value.forEach((item) => {
      if (support.landlordId === item.landlordId &&
          support.messageId === item.messageId &&
          support.thumbsUpId === id &&
          support.commentReplyId === null) {
        item.like = true
      }
      commentList.value.forEach((comment) => {
        if (support.landlordId === comment.landlordId &&
            support.messageId === comment.commentId &&
            support.thumbsUpId === id &&
            support.commentReplyId === comment.id) {
          comment.like = true
        }
      })

      commentList.value.forEach(comment => {
        if (comment.replyId) { // 检查当前评论是否有被回复的评论
          const repliedComment = commentList.value.find(c => c.id === comment.replyId); // 通过replyId找到被回复的评论
          if (repliedComment) {
            comment.replyName = repliedComment.name; // 将被回复的评论者姓名存储在replyName字段中
            comment.replyRole = repliedComment.role; // 将被回复的评论者角色存储在replyRole字段中
          }
        }
      });
    })
  })
}

const cancel = () => {
  initWangEditor('')
  dialogVisible.value = false
}

const handleClose = (done) => {
  done()
}

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
const menuVisible = ref(false)
const menuCommentVisible = ref(false)
const menuTop = ref(0)
const menuLeft = ref(0)
const menuTopComment = ref(0)
const menuLeftComment = ref(0)
const selectedItem = ref(null)
const deleteButtons = document.querySelectorAll('[id^=delete-button]');
deleteButtons.forEach(button => {
  button.addEventListener('contextmenu', showContextMenu);
});

const deleteComment = document.querySelectorAll('[id^=delete-comment]');
deleteComment.forEach(button => {
  button.addEventListener('contextmenu', showCommentMenu);
});
const showContextMenu = (event, item) => {
  event.preventDefault(); // 阻止默认的右键菜单
  menuTop.value = event.pageY;
  menuLeft.value = event.pageX;
  menuVisible.value = true;
  selectedItem.value = item;
  // 监听整个文档的点击事件，用于隐藏菜单
  document.addEventListener('click', hideContextMenu);
}

const showCommentMenu = (event, comment) => {
  event.preventDefault(); // 阻止默认的右键菜单
  menuTopComment.value = event.pageY;
  menuLeftComment.value = event.pageX;
  menuCommentVisible.value = true;
  selectedItem.value = comment;
  // 监听整个文档的点击事件，用于隐藏菜单
  document.addEventListener('click', hideContextMenu);
}
const hideContextMenu = (event) => {
  // 获取点击事件发生的元素
  const targetElement = event.target;

  // 检查点击的元素是否在上下文菜单内部

  const contextMenu = document.querySelector('.context-menu');
  const commentMenu = document.querySelector('.comment-menu');

  if (contextMenu === null) {
    const isClickInsideCommentMenu = commentMenu.contains(targetElement);
    if (!isClickInsideCommentMenu) {
      menuCommentVisible.value = false;
      document.removeEventListener('click', hideContextMenu); // 移除点击事件监听器
    }
  } else {
    const isClickInsideMenu = contextMenu.contains(targetElement);
    // 如果点击的不是上下文菜单内部，则隐藏菜单
    if (!isClickInsideMenu) {
      menuVisible.value = false;
      document.removeEventListener('click', hideContextMenu); // 移除点击事件监听器

    }
  }
}
const delArticle = async (item) => {
  let res = await delArticleService(item)
  ElMessage.success(res.message)
  await selectAllArticles()
  await selectAllComments()
}

const delComment = async (comment) => {
  let res = await delCommentService(comment)
  ElMessage.success(res.message)
  await selectAllArticles()
  await selectAllComments()
}
onMounted(() => {
  selectAllArticles()
  selectAllComments()

})
const getImagesUrl = (name) => {
  return new URL(name, import.meta.url).href
}
const imageUrl = "../../assets/images/noData.png"
</script>

<template>
  <el-container>
    <el-affix :offset="150" class="affix-Btn">
      <el-button v-if="getRole()!=='ADMIN'" type="primary" @click="openVisible('post',message)">发表文章</el-button>
    </el-affix>
    <div class="main">
      <div class="title">
        <p> 社区交流</p>
      </div>
      <el-backtop :bottom="200"/>
      <!--楼主区-->
      <div v-for="(item,index) in postList" class="landlord" :key="index">
        <div class="personal-data">
          <div v-if="getRole()==='ADMIN'" @contextmenu.prevent="showContextMenu($event, item)"
               :id="'delete-button-' + index">
            <el-avatar :src="item.avatar" size="large"></el-avatar>
          </div>
          <div v-else>
            <el-avatar :src="item.avatar" size="large"></el-avatar>
          </div>
          <div v-if="menuVisible" :style="{ top: menuTop + 'px', left: menuLeft + 'px' }" class="context-menu">
            <el-button type="danger" plain @click="delArticle(selectedItem)">删除文章</el-button>
          </div>
          <div style="display: flex;flex-direction: column;">
            <div class="name"><span v-if="item.role === '咨询师'"
                                    style="margin-right: 10px">[ {{
                item.role
              }} ]</span>{{ item.name }}
            </div>
            <div v-html="item.content" class="w-e-text"></div>
            <div><span class="time">{{ formatTime(item.createdTime) }}</span></div>
          </div>
        </div>
        <div class="interaction">
          <el-button type="text" @click="support('thumbsUpLandlord',item,comment)"><img
              :src="item.like ? '/images/support.png' : '/images/notSupport.png'" style="width: 50%"
              alt=""><span>{{ item.like ? '已赞' : '赞' }}</span>
          </el-button>
          <span>{{ item.likeCount }}</span>
          <el-button type="text" @click="openVisible('commentLandlord',item)">评论</el-button>
          <span>{{ item.commentCount }}</span>
        </div>
        <div v-for="(comment,index) in commentList">
          <div v-if="comment.landlordId === item.landlordId">
            <div class="personal-data-comment">
              <div v-if="getRole()==='ADMIN'" @contextmenu.prevent="showCommentMenu($event, comment)"
                   :id="'delete-comment-' + index">
                <el-avatar :src="comment.avatar" size="default"></el-avatar>
              </div>
              <div v-else>
                <el-avatar :src="comment.avatar" size="default"></el-avatar>
              </div>
              <div v-if="menuCommentVisible" :style="{ top: menuTopComment + 'px', left: menuLeftComment + 'px' }"
                   class="comment-menu">
                <el-button type="danger" plain @click="delComment(selectedItem)">删除评论</el-button>
              </div>
              <div style="display: flex;flex-direction: column;">
                <div class="name">
<!--                  <span v-if="comment.role === '咨询师'"-->
<!--                                        style="margin-right: 10px">[ {{ comment.role }} ]</span>-->
                  {{ comment.name }}
                  <el-icon style="margin-left: 8px;margin-right: 10px" color="#c1c1c1" size="16px">
                    <CaretRight/>
                  </el-icon>
<!--                  <span v-if="comment.replyRole === '咨询师'" style="margin-right: 10px">[ {{ comment.replyRole }} ]</span>-->
                  {{
                    comment.replyName ? comment.replyName : item.name
                  }}
                </div>
                <div v-html="comment.content" class="w-e-text"></div>
                <div><span class="time">{{ formatTime(comment.createdTime) }}</span></div>
              </div>
            </div>
            <div class="interaction-comment">
              <el-button type="text" @click="support('thumbsUpComment',item,comment)"><img
                  :src="comment.like ? '/images/support.png' : '/images/notSupport.png'" style="width: 50%"
                  alt=""><span>{{ comment.like ? '已赞' : '赞' }}</span>
              </el-button>
              <span style="margin-left: 15px">{{ comment.likeCount }}</span>
              <el-button type="text" @click="openVisible('comment',item,comment)" style="margin-left: 10px">
                <span>评论</span></el-button>
              <span style="margin-left: 10px">{{ comment.commentCount }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-news">
        <div class="text-news">
          <span>暂时没有新文章了</span>
        </div>
        <div>
          <el-image :src="getImagesUrl(imageUrl)" style="width: 100px;height: 100px"></el-image>
        </div>
      </div>
    </div>
    <el-dialog
        v-model="dialogVisible"
        draggable
        overflow
        width="60%"
        :before-close="handleClose"
    >
      <div id="editor">
      </div>
      <div class="controller">
        <el-button @click="cancel()">取消</el-button>
        <el-button @click="submit()" type="primary">发表</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<style scoped lang="scss">
.affix-Btn {
  .el-button {
    width: 150px;
    height: 50px;
    font-size: 20px;
  }
}

.title {
  width: 150px;
  height: 60px;
  margin: auto;
  margin-bottom: 20px;
  font-size: 20px;
}

.main {
  margin-left: 40px;
  width: 800px;
  box-shadow: #232323 1px 1px 10px;
  padding: 0 120px;
}

.personal-data {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}

.name {
  margin-left: 20px;
  color: #777790;
}

.w-e-text {
  margin-left: 10px; /* 在内容和头像之间添加一些间隔 */
  border-radius: 8px;
  max-width: 550px;
  max-height: 300px;
  color: #232323;
}

.interaction {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin: 0 10px 10px 0;

  .el-button {
    width: 60px;
  }
}

.personal-data-comment {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
  margin-left: 60px;

}


.interaction-comment {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin: 0 10px 10px 0;

  .el-button {
    width: 40px;

  }

  span {
    font-size: 13px;
    margin-left: 5px;
  }
}

.time {
  color: #777790;
  margin-left: 20px;
  font-size: 15px;
}

.controller {
  margin-top: 20px;
  text-align: center;

  .el-button {
    width: 100px;
    font-size: 15px;
  }
}

.del {
  margin-top: 15px;
  margin-right: 10px;
  cursor: pointer;
}

.context-menu {
  position: absolute;
  margin-left: -130px;
  margin-top: -20px;
}

.comment-menu {
  position: absolute;
  margin-left: -120px;
  margin-top: -17px;
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