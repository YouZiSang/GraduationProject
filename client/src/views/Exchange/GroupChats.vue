<script lang="js" setup>
import {ref, onMounted, nextTick} from "vue";
import {getId, getAvatar, getName} from "@/utils/getCurrentUserInfo.js";

import emojis from "@/assets/emojis/emojis.js";
import {FolderOpened, Microphone, SwitchButton} from "@element-plus/icons-vue";
import {groupChatHistoryService} from "@/api/chat.js";

const currentId = getId()
const currentAvatar = getAvatar()
const currentName = getName()

const avatar = window.btoa(encodeURI(currentAvatar))
const name = window.btoa(encodeURI(currentName))

const chatMessage = ref([])

const webSocket = new WebSocket(`ws://localhost:8080/chat/${name}/${avatar}`)
webSocket.onopen = () => {
  console.log("websocket open")
}

webSocket.onclose = () => {
  console.log("websocket close")
}
webSocket.onmessage = (msg) => {
  console.log(msg.data)
  let jsonStr = msg.data;
  let json = JSON.parse(jsonStr);
  //判断是否是连接消息
  if (json.messageType === 'onlineUsers') {

    onlineUsers.value = [];

    for (const uid in json.user) {
      if (json.user.hasOwnProperty(uid)) {
        const user = json.user[uid];
        onlineUsers.value.push({
          uid: parseInt(uid, 10),
          avatar: user.avatar,
          name: user.name,
        });
      }
    }
  } else {
    //接收服务端消息
    chatMessage.value.push({
      uid: json.uid,
      avatarUrl: json.avatarUrl,
      name: json.name,
      type: json.type,
      content: json.content
    })
  }
  scrollToBottom()
}
const message = ref('')
const getGroupChatHistory = async () => {
  let res = await groupChatHistoryService()
  chatMessage.value = res.data
}

const onlineUsers = ref([]);
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


onMounted(async () => {
  // 设置可编辑内容区域的引用为包含类名为 .content 的 DOM 元素
  editableContentRef.value = document.querySelector('.content');
  await getGroupChatHistory()
  getMessage()
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
      if (editableContentRef.value) {
        editableContentRef.value.textContent += result;
      }
    }
  };

})

const editableContentRef = ref('');

// 处理点击表情的函数
const clickEmojis = (emoji) => {
  editableContentRef.value.innerText += emoji;
};

const getMessage = (type) => {
  const content = editableContentRef.value.innerText; // 获取编辑的内容

  if (!content && type === 'text') {
    return
  }

  return {
    uid: currentId,
    avatarUrl: currentAvatar,
    name: currentName,
    content: content,
    type: type
  }
  scrollToBottom()
}
const handleFile = (file) => {

  if (webSocket) {
    let message = getMessage('img');

    // 获取文件名部分（不包含参数）
    let fileName = file.data.split('?')[0];

    message.content = fileName;
    let extName = fileName.substring(fileName.lastIndexOf('.') + 1);

    if (['png', 'jpg', 'jpeg', 'gif', 'bmp', 'tiff', 'svg', 'webp'].includes(extName)) {
      message.type = 'img';
    } else if (['avi', 'wmv', 'mpeg', 'mp4', 'flv', 'rmvb', 'mkv'].includes(extName)) {
      message.type = 'video';
    } else {
      message.type = 'file';
    }
    webSocket.send(JSON.stringify(message));
  }
}

//点击发送消息
const sendContent = () => {
  let content = getMessage('text')
  if (webSocket) {
    webSocket.send(JSON.stringify(content.valueOf()))
  }
  //清空输入框
  editableContentRef.value.innerText = ''
  scrollToBottom()
};
const scrollToBottom = () => {
  nextTick(() => {
    let messageBox = document.getElementById("messageBox");
    setTimeout(() => {
      messageBox.scrollTop = messageBox.scrollHeight - messageBox.clientHeight;
    }, 100); // 添加一些延迟
  });
}

//下载文件
const download = (file)=> {
  let windowObjectReference = window.open()
  windowObjectReference.location.href = (file)
}


// 判断是否显示时间线的函数
function shouldDisplayTime(index) {
  // 如果是第一条消息，则直接显示时间线
  if (index === 0) {
    return true;
  }

  // 获取前一条消息和当前消息的时间对象
  const prevTime = new Date(chatMessage.value[index - 1].time);
  const currTime = new Date(chatMessage.value[index].time);

  // 判断时间间隔是否大于等于30分钟
  return isTimeGapGreaterThan30Minutes(prevTime, currTime);
}

// 判断时间间隔是否大于等于30分钟的函数
function isTimeGapGreaterThan30Minutes(time1, time2) {
  // 计算两个时间之间的毫秒数差值
  const diffInMilliseconds = Math.abs(time2 - time1);
  // 将毫秒数差值转换为分钟数
  const diffInMinutes = diffInMilliseconds / (1000 * 60);

  // 判断时间间隔是否大于等于30分钟
  return diffInMinutes >= 30;
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-aside>
        <div class="asideHeader">在线用户{{ onlineUsers.length - 1 }}</div>
        <div class="online-list-item"
             v-for="user in onlineUsers"
             :key="user.uid">
          <template v-if="user.uid !== currentId">
            <div>
              <el-avatar :size="50" :src="user.avatar" style="margin-right: 10px"/>
            </div>
            <span>{{ user.name }}</span>
            <span>({{ user.uid }})</span>
          </template>
        </div>
      </el-aside>
      <el-container>
        <el-header>心理咨询聊天室</el-header>
        <el-container>
          <el-main id="messageBox">
            <div v-for="(message, index) in chatMessage" :key="message.id" class="message-box"
                 :class="{ 'message-right': message.uid === currentId, 'message-left': message.uid !== currentId }">
              <div v-if="shouldDisplayTime(index)">
                <el-timeline class="timeline">{{ message.time }}</el-timeline>
              </div>
              <div v-if="message.uid === currentId" class="message-right-content">
                <div style="display: flex;justify-content: flex-end">
                  <div class="bubble-right" v-if="message.type === 'text'" v-html="message.content"></div>
                  <div v-if="message.type === 'img'">
                    <el-image :src="message.content"
                              :preview-src-list=[message.content]></el-image>
                  </div>
                  <div class="bubble-right" v-if="message.type === 'file'"
                       @click="download(message.content)">
                    <span><el-icon color="#252525"><FolderOpened/></el-icon>
                       {{ decodeURIComponent(message.content.substring(message.content.lastIndexOf('-') + 1)) }}
                    </span>
                  </div>
                  <div v-if="message.type === 'video'">
                    <video ref="videoElement" v-bind:src="message.content" controls>
                      <track kind="subtitles" srclang="en" src="path/to/your/subtitles.vtt" default>
                    </video>
                  </div>
                  <el-avatar :src="message.avatarUrl" :size="50" style="margin-left: 15px"/>
                </div>
              </div>
              <div v-else class="message-left-content">
                <div style="display: flex">
                  <div style="margin-right: 15px">
                    <el-avatar :src="message.avatarUrl" :size="50"/>
                  </div>
                  <div>
                    <div style="font-size: 15px;color: #8d8882;margin-bottom: 5px">{{ message.name }}</div>
                    <div class="bubble-left" v-if="message.type === 'text'" v-html="message.content"></div>
                    <div v-if="message.type === 'img'">
                      <el-image :src="message.content"
                                :preview-src-list=[message.content]></el-image>
                    </div>
                    <div v-if="message.type === 'file'"
                         @click="download(message.content)">
                    <span><el-icon color="#252525"><FolderOpened/></el-icon>
                      {{ decodeURIComponent(message.content.substring(message.content.lastIndexOf('-') + 1)) }}
                    </span>
                    </div>
                    <div v-if="message.type === 'video'">
                      <video ref="videoElement" v-bind:src="message.content" controls>
                        <track kind="subtitles" srclang="en" src="path/to/your/subtitles.vtt" default>
                      </video>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-main>
          <el-footer>
            <div style="display: flex">

              <!-- 表情弹出框 -->
              <el-popover width="350px" trigger="click" placement="top-start">
                <div class="emojis-box" style="font-size: 24px;cursor: default;">
                  <!-- 遍历表情列表 -->
                  <span v-for="(emoji, index) in emojis" :key="index" v-html="emoji" @click="clickEmojis(emoji)"></span>
                </div>
                <!-- 触发表情弹出框的按钮 -->
                <template #reference>
                  <img src="@/assets/images/smile.png" slot="reference" class="emojis" alt="表情"/>
                </template>
              </el-popover>
              <div style="margin-left: 20px">
                <el-upload action="http://localhost:8080/api/upload/singleFile" :show-file-list="false"
                           :on-success="handleFile">
                  <img src="@/assets/images/file.png" style="width: 23px;height: 23px" alt="文件">
                </el-upload>
              </div>
              <el-icon style="margin-left: 10px" size="23" color="#bfbfbf" @click="startRecognition"><Microphone /></el-icon>
              <el-icon style="margin-left: 10px;margin-top: 2px" size="20" color="#bfbfbf"  @click="stopRecognition"><SwitchButton /></el-icon>
            </div>
            <!-- 可编辑的内容区域 -->
            <div class="content" contenteditable ref="editableContent">
            </div>
            <el-button type="primary" @click="sendContent" style="float: right">发送</el-button>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped lang="scss">
.common-layout {
  width: 80%;
  height: 99%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5); /* 全方位阴影效果 */
  margin: auto;
}

.el-aside {
  max-width: 250px;
  padding: 10px;
  background-color: #ece7df;
}

.asideHeader {
  padding: 5px;
  font-size: 20px;
}

.online-list-item {
  display: flex;
  align-items: center;
}


.online-list-item span {
  margin-right: 10px;
  overflow: hidden; // 超出的文本隐藏
  white-space: nowrap; // 溢出不换行
}

.el-header {
  height: 60px;
  background-color: #ece7df;
  font-size: 20px;
  color: #252525;
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-main {
  height: 480px;
  background-color: #faf9f7;
  border-left: solid 2px #d9d4cd;
  border-top: solid 1px #d9d4cd;
  overflow: auto; /* 启用滚动条 */

  .message-box {
    display: flex;
    flex-direction: column;
    margin-bottom: 10px;
  }

  .timeline {
    color: #8a8a8a;
    display: flex;
    justify-content: center;
  }

  .el-image{
    max-width: 300px;
    max-height: 300px;
    border-radius: 8px;
  }

  video{
    max-width: 300px;
    max-height: 300px;
    border-radius: 8px;
  }

}

.message-right-content {
  text-align: right;
}

.message-left-content {
  display: flex;
  align-items: center;
  text-align: left;
}

.bubble-left {
  max-width: 380px;
  word-wrap: break-word; /* 过时的属性，但在一些老浏览器中可能仍然需要 */
  overflow-wrap: break-word; /* 标准属性，用于控制内容的换行 */
  white-space: normal; /* 确保文本可以正常换行，而不是保持在同一行 */
  display: inline-block;
  position: relative;
  padding: 10px;
  background-color: #e0dfdd;
  color: black;
  border-radius: 10px;
}

.bubble-left::before {
  content: "";
  position: absolute;
  top: 10%;
  right: 100%;
  margin-top: 4px; /* 调整箭头位置 */
  border-width: 8px;
  border-style: solid;
  border-color: transparent #e0dfdd transparent transparent;
}

.bubble-right {
  max-width: 380px;
  word-wrap: break-word; /* 过时的属性，但在一些老浏览器中可能仍然需要 */
  overflow-wrap: break-word; /* 标准属性，用于控制内容的换行 */
  white-space: normal; /* 确保文本可以正常换行，而不是保持在同一行 */
  display: inline-block;
  position: relative;
  padding: 10px;
  background-color: #e0dfdd;
  color: black;
  border-radius: 10px;
}

.bubble-right::before {
  content: "";
  position: absolute;
  top: 10%;
  left: 100%; /* 将箭头放在气泡的左边缘 */
  margin-top: 4px; /* 调整箭头位置 */
  border-width: 8px;
  border-style: solid;
  border-color: transparent transparent transparent #e0dfdd; /* 透明部分对应气泡的边缘 */
}

.el-footer {
  border-top: solid 2px #d9d4cd;
  border-left: solid 2px #d9d4cd;
  height: 200px;
  background-color: #faf9f7;
}

.emojis {
  margin-top: 2px;
  width: 20px;
  height: 20px;
}

.emojis:hover {
  cursor: default;
}

.content {
  font-size: 20px;
  margin: 20px;
  height: 70px;
  outline: none;
  overflow: auto; /* 启用滚动条 */
}

.selected {
  background-color: #f0f0f0;
}
</style>