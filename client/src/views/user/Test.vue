<script setup>
import {ref, onMounted, nextTick,watchEffect} from "vue";
import {findConsultantsInfoList} from "@/api/consultants.js";
import {getChatMessageService, isReadedService, updateReadedService} from "@/api/chat.js";
import emojis from "@/assets/emojis/emojis.js";
import {getUsersList} from "@/api/userInfo.js";

import {getId, getAvatar, getName} from "@/utils/getCurrentUserInfo.js";
import {ChatDotRound, FolderOpened, VideoCamera} from "@element-plus/icons-vue";
import {useRoute, useRouter} from "vue-router";


const router = useRouter()
const route = useRoute()

const fromId = getId()
const fromAvatar = getAvatar()
const fromName = getName()

const consultants = ref([{
  cid: '',
  avatarUrl: '',
  name: '',
  readed: 0
}])

const users = ref([{
  uid: '',
  avatarUrl: '',
  nickname: '',
  readed: 0
}])
const messages = ref([])
const toName = ref('') // 新增名字的ref


const toId = ref('')
const toAvatar = ref('')
const inputValue = ref('')
const editableContentRef = ref('');
const consultantsInfo = async () => {
  let res = await findConsultantsInfoList()
  consultants.value = res.data.map(consultant => {
    return {
      cid: consultant.cid,
      avatarUrl: consultant.avatarUrl,
      name: consultant.name
    }
  });
}

const usersInfo = async () => {
  let res = await getUsersList()
  users.value = res.data.map(user => {
    return {
      uid: user.uid,
      avatarUrl: user.avatarUrl,
      nickname: user.nickname
    }
  })
}

const onlineList = ref([])


const webSocket = new WebSocket('ws://localhost:8080/videoChat')
webSocket.onopen = () => {
  console.log("websocket open")
}

webSocket.onclose = () => {
  console.log("websocket close")
}

webSocket.onmessage = (msg,event) => {
  if (msg.data) {
    let json = JSON.parse(msg.data)
    console.log(json)
    if (json.content && (json.fromId === fromId && json.toId === toId.value)
        || json.toId === fromId && json.fromId === toId.value) {
      messages.value.push(json)
      scrollToBottom()
    }
    if (json.messageType === 'online') {
      onlineList.value = [];
      onlineList.value = [...json.id]
      onlineConsultants.value = consultants.value.filter(consultant => {
        // 检查顾问的cid是否在onlineList.value中
        return onlineList.value.some(onlineId => consultant.cid === onlineId);
      });

      onlineUsers.value = users.value.filter(user => {
        // 检查用户的uid是否在onlineList.value中
        return onlineList.value.some(onlineId => user.uid === onlineId);
      });
      // 打印更新后的在线列表和在线顾问列表

    }
  }

  const message = JSON.parse(msg.data);
  if (message.type === 'offer') {
    handleOffer(message.offer);
  } else if (message.type === 'answer') {
    handleAnswer(message.answer)
  }else if (message.type === 'candidate') {
    handleCandidate(message.candidate)

  }

}

const selectToConsultants = (consultant) => {
  toId.value = consultant.consultantsId
  toAvatar.value = consultant.avatarUrl

}
const onlineConsultant = (consultant) => {
  // 假设onlineConsultants.value是包含在线顾问ID的数组
  // 检查传入的顾问ID是否在这个数组中

  const isOnline = onlineConsultants.value.some(onlineConsultantObj => {
    return consultant.cid === onlineConsultantObj.cid;
  });

  // 根据是否在线返回相应的颜色或其他标识
  return isOnline ? '#24e68a' : ''; // 或者其他您需要的逻辑
};

const onlineUser = (user) => {

  // 假设onlineUsers.value是包含在线用户ID的数组
  // 检查传入的用户ID是否在这个数组中
  const isOnline = onlineUsers.value.some(onlineUserObj => {
    return user.uid === onlineUserObj.uid;
  });

  // 根据是否在线返回相应的颜色或其他标识
  return isOnline ? '#24e68a' : ''; // 或者其他您需要的逻辑
};
const onlineConsultants = ref([])
const onlineUsers = ref([])
const isReaded = async ()=>{
  let res = await isReadedService()
  console.log(res.data)
  // 假设 res.data 包含了更新后的未读消息计数
  consultants.value.forEach(consultant => {
    consultant.readed = res.data[consultant.cid]
  });
  users.value.forEach(user => {
    user.readed = res.data[user.uid]
  });
}


const getChatMessage = async (data, source) => {

  // 取消其他项的选中状态
  consultants.value.forEach(consultant => {
    consultant.selected = false;
  });
  users.value.forEach(user => {
    user.selected = false;
  });
  // 标记当前点击项为选中状态
  data.selected = true;
  const selectedUser = users.value.find(user => user.uid === data.uid);
  if (selectedUser) {
    selectedUser.selected = true
  }

  data.readed = 0;

  if (source === 'consultants') {

    toId.value = data.cid;
    toAvatar.value = data.avatarUrl;
    toName.value = "咨询师"+ "-" +data.name  ;
    let res = await getChatMessageService(fromId, toId.value);
    messages.value = res.data;
    scrollToBottom()
    let params = {
      fromId: data.cid,
      readed: data.readed
    }
    await updateReadedService(params)
  } else if (source === 'users') {
    toId.value = data.uid;
    toAvatar.value = data.avatarUrl;
    toName.value =  "用户"+ "-" + data.nickname;
    let res = await getChatMessageService(fromId, toId.value);
    console.log(res.data)
    messages.value = res.data;
    scrollToBottom()
    let params = {
      fromId: data.cid,
      readed: data.readed
    }
    await updateReadedService(params)
  }

}

// 处理点击表情的函数
const clickEmojis = (emoji) => {
  editableContentRef.value.innerText += emoji;
};

const getMessage = (type) => {
  const content = editableContentRef.value.innerText; // 获取编辑的内容

  if (!content && type === 'text') {
    return
  }
  const userRole = toName.value.endsWith("-咨询师") ? "用户" : "咨询师";

  return {
    toId: toId.value,
    toAvatar: toAvatar.value,
    toName: toName.value,
    fromId: fromId,
    fromAvatar: fromAvatar,
    fromName: fromName + "-" + userRole,
    content: content,
    type: type
  }

}

const decodedFilename = ref('');

//发送图片，文件
const handleFile = (file) => {

  if (webSocket) {
    let message = getMessage('img');

    // 获取文件名部分（不包含参数）
    let fileName = file.data.split('?')[0];
    console.log(fileName)
    message.content = fileName;
    let extName = fileName.substring(fileName.lastIndexOf('.') + 1);
    console.log(extName)
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
// 发送内容到后端的函数
const sendContent = () => {
  if (!toId.value) {
    // 如果未选择聊天用户，则弹出错误通知
    ElNotification({
      title: 'Error',
      message: '请选择聊天用户',
      type: 'error',
    })
    return;
  }

  let content = getMessage('text')
  if (webSocket) {
    webSocket.send(JSON.stringify(content.valueOf()))
  }
  //清空输入框
  editableContentRef.value.innerText = ''
};

//显示最新一条消息
const scrollToBottom = () => {
  nextTick(() => {
    let messageBox = document.getElementById("messageBox");
    setTimeout(() => {
      messageBox.scrollTop = messageBox.scrollHeight - messageBox.clientHeight;
    }, 100); // 添加一些延迟
  });
}


//下载文件,打开一个新页面
const download = (file) => {
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
  const prevTime = new Date(messages.value[index - 1].time);
  const currTime = new Date(messages.value[index].time);

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

// 当组件加载后执行的逻辑
onMounted(() => {
  consultantsInfo()
  usersInfo()
  getMessage()
  isReaded()
  // 设置可编辑内容区域的引用为包含类名为 .content 的 DOM 元素
  editableContentRef.value = document.querySelector('.content');

  // 从 sessionStorage 读取参数
  const params = JSON.parse(sessionStorage.getItem('chatParams'));
  if (params) {
    const user = {
      uid: parseInt(params.user_uid),
      avatarUrl: params.user_avatarUrl,
      name: params.user_name,
      nickname: params.user_nickname,
      selected: false
    };
    getChatMessage(user, "users");
  }
  sessionStorage.removeItem('chatParams'); // 立即删除参数

})




const dialog = ref(false)
const remoteStream = ref(null);
let peerConnection = new RTCPeerConnection();
let localMediaStream = null; // 用于存储本地媒体流的变量
// let remoteStream = null; // 用于存储远程媒体流的变量
// const peerConnection = ref(null);
// 开始视频通话
const startVideoCall = async () => {
  // 获取本地媒体流
  localMediaStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
  const localVideo = document.getElementById('localVideo');
  localVideo.srcObject = localMediaStream;

  // 创建RTCPeerConnection
  peerConnection = new RTCPeerConnection({
    iceServers: [{ urls: 'stun:stun.l.google.com:19302' }] // 这里使用了Google的STUN服务器
  });

  // 添加本地媒体流到peerConnection
  localMediaStream.getTracks().forEach(track => {
    peerConnection.addTrack(track, localMediaStream);
  });
  // 创建offer
  peerConnection.createOffer().then(offer => {
    return peerConnection.setLocalDescription(offer);
  }).then(() => {
    // 发送offer到服务器
    const offerMessage = {
      type: 'offer',
      fromId: fromId,
      toId: toId.value,
      offer: peerConnection.localDescription
    };
    webSocket.send(JSON.stringify(offerMessage));
  }).catch(error => {
    console.error('Error creating offer', error);
  });

  // 监听icecandidate事件
  peerConnection.onicecandidate = event => {
    if (event.candidate) {
      sendIceCandidate(event.candidate);
    }
  };


  peerConnection.onsignalingstatechange = () => {
    if (peerConnection.signalingState === 'stable') {
      console.log('Connection is stable. Ready for communication.');
      // 执行稳定状态下的操作，例如显示连接成功的提示
    }
  };

};

// 发送ICE候选到服务器
const sendIceCandidate = candidate => {
  const candidateMessage = {
    type: 'candidate',
    fromId: fromId,
    toId: toId.value,
    candidate: candidate
  };
  webSocket.send(JSON.stringify(candidateMessage));
};

const handleOffer = async (offer) => {

  peerConnection.setRemoteDescription(new RTCSessionDescription(offer)).then(async () => {
    await openCameraAndCreateAnswer();
      return peerConnection.createAnswer().then((answer) => {
        if (answer) {
          // 设置本地描述
          return peerConnection.setLocalDescription(new RTCSessionDescription(answer)).then(() => {
            if (peerConnection.localDescription) {
              // 发送 answer 到服务器
              webSocket.send(JSON.stringify({
                type: 'answer',
                fromId: toId.value,
                toId: fromId,
                answer: peerConnection.localDescription
              }));
            }
          });
        }
      });

  }).catch((error) => {
    console.error('Error handling offer', error);
  });
};

// 处理来自服务器的answer
const handleAnswer = (answer) => {
    peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
  // 远程描述设置成功，不需要进一步操作，因为 ontrack 事件会处理视频流
};

// 处理来自服务器的ICE候选
const handleCandidate = candidate => {
  peerConnection.addIceCandidate(new RTCIceCandidate(candidate)).catch(error => {
    console.error('Error adding ICE candidate', error);
  });
};
peerConnection.ontrack = (event) => {
  if (event.track.kind === 'video') {
    console.log('Remote video track received.', event);
    // 获取或创建远程视频元素
    let remoteVideo = document.getElementById('remoteVideo');
    if (!remoteVideo) {
      remoteVideo = document.createElement('video');
      remoteVideo.id = 'remoteVideo';
      remoteVideo.autoplay = true;
      remoteVideo.muted = true; // 通常远端视频是静音的
      document.getElementById("remoteVideoContainer").appendChild(remoteVideo);
    }
    // 设置视频元素的源
    remoteVideo.srcObject = event.streams[0];
  }
};
// 打开摄像头并创建 answer
const openCameraAndCreateAnswer = async () => {
  try {
    // 获取本地媒体流
    const localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
    const localVideo = document.getElementById('localVideo');
    localVideo.srcObject = localStream;

    // 将本地媒体流添加到 RTCPeerConnection
    localStream.getTracks().forEach(track => {
      peerConnection.addTrack(track, localStream);
    });
  } catch (error) {
    console.error('Error accessing camera', error);
  }
};
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-aside>
        <div class="asideHeader">咨询师</div>
        <div class="consultants-list-item"
             v-for="consultant in consultants"
             :key="consultant.cid"
             @click="getChatMessage(consultant, 'consultants')"
             :class="{ 'selected': consultant.selected }">
          <template v-if="fromId !== consultant.cid">
            <div class="online-consultant">
              <el-avatar :size="50" :src="consultant.avatarUrl"></el-avatar>
              <div class="consultant-circle" :style="{ backgroundColor: onlineConsultant(consultant) }"></div>
            </div>
            <div style="margin-right: 10px;margin-top: 20px">
              <div v-if="consultant.readed > 0" class="item">
                <el-badge :value="consultant.readed" class="item">
                  <el-icon size="25"><ChatDotRound /></el-icon>
                </el-badge>
              </div>
              <el-icon v-else size="25"><ChatDotRound /></el-icon>
            </div>
            <span style="margin-right: 10px">{{ consultant.name }} ({{ consultant.cid }})</span>
          </template>
        </div>
        <el-divider/>
        <div class="asideHeader">用户</div>
        <div class="users-list-item"
             v-for="user in users"
             :key="user.uid"
             @click="getChatMessage(user, 'users')"
             :class="{ 'selected': user.selected }"
        >
          <template v-if="fromId !== user.uid">
            <div class="online-user">
              <el-avatar :size="50" :src="user.avatarUrl"/>
              <div class="user-circle" :style="{ backgroundColor: onlineUser(user) }"></div>
            </div>
            <div style="margin-right: 10px;margin-top: 20px">
              <div v-if="user.readed > 0" class="item">
                <el-badge :value="user.readed" class="item">
                  <el-icon size="25"><ChatDotRound /></el-icon>
                </el-badge>
              </div>
              <el-icon v-else size="25"><ChatDotRound /></el-icon>
            </div>

            <span>{{ user.nickname }}  ({{ user.uid }})</span>
          </template>
        </div>

      </el-aside>
      <el-container>
        <el-header>{{ toName.substring(0, toName.lastIndexOf('-')) }}</el-header>
        <el-container>
          <el-main id="messageBox">
            <div v-for="(message, index) in messages" :key="message.id" class="message-box"
                 :class="{ 'message-right': message.fromId === fromId, 'message-left': message.fromId !== fromId }">
              <div v-if="shouldDisplayTime(index)">
                <el-timeline class="timeline">{{ message.time }}</el-timeline>
              </div>
              <div v-if="message.fromId === fromId" class="message-right-content">
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
                  <el-avatar :src="message.fromAvatar" :size="50" style="margin-left: 15px"/>
                </div>
              </div>
              <div v-if="message.fromId !== fromId" class="message-left-content">
                <div style="display: flex">
                  <div style="margin-right: 15px">
                    <el-avatar :src="message.fromAvatar" :size="50"/>
                  </div>
                  <div>
                    <div style="font-size: 15px;color: #8d8882;margin-bottom: 5px">
<!--                      {{ message.fromName.substring(0, message.fromName.lastIndexOf('-')) }}-->
                    </div>
                    <div class="bubble-left" v-if="message.type === 'text'" v-html="message.content"></div>
                    <div v-if="message.type === 'img'">
                      <el-image :src="message.content"
                                :preview-src-list=[message.content]></el-image>
                    </div>
                    <div class="bubble-left" v-if="message.type === 'file'"
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
              <el-dialog v-model="dialog">

              </el-dialog>
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
              <div style="margin-left: 20px">
                <el-icon size="25" color="#bfbfbf">
                  <VideoCamera @click="startVideoCall"/>
                </el-icon>
              </div>
            </div>
            <!-- 可编辑的内容区域 -->
            <div class="content" contenteditable @input="handleInput" ref="editableContent">
            </div>
            <el-button type="primary" @click="sendContent" style="float: right">发送</el-button>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
    <video id="localVideo" autoplay style="width: 320px; height: 240px;"></video>
      <video id="remoteVideo" autoplay style="width: 320px; height: 240px;"></video>
  </div>
</template>
<style scoped lang="scss">
.common-layout {
  width: 75%;
  height: 99%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5); /* 全方位阴影效果 */
  margin: auto;

  .asideHeader {
    padding: 5px;
    font-size: 20px;
  }

  .el-container {

    .el-header {
      height: 60px;
      background-color: #ece7df;
      font-size: 20px;
      color: #252525;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .el-container {

      .el-aside {
        max-width: 250px;
        padding: 10px;
        background-color: #ece7df;


        .consultants-list-item {
          padding: 5px;
          display: flex;
          align-items: center;

          .online-consultant{
            position: relative;
            display: inline-block;
            margin-right: 10px;
          }
          .consultant-circle {
            position: absolute;
            top: 36px; /* Adjust this value to position the icon on top of the avatar */
            right: 2px; /* Adjust this value to position the icon on the right of the avatar */
            width: 15px; /* Adjust this value to set the size of the circle */
            height: 15px; /* Adjust this value to set the size of the circle */
            border-radius: 50%; /* Make the div a circle */
          }
        }

        .users-list-item {
          padding: 5px;
          display: flex;
          align-items: center;

          .online-user{
            position: relative;
            display: inline-block;
            margin-right: 10px;

          }
          .user-circle {
            position: absolute;
            top: 36px; /* Adjust this value to position the icon on top of the avatar */
            right: 2px; /* Adjust this value to position the icon on the right of the avatar */
            width: 15px; /* Adjust this value to set the size of the circle */
            height: 15px; /* Adjust this value to set the size of the circle */
            border-radius: 50%; /* Make the div a circle */
          }

        }

        .users-list-item span {
          overflow: hidden; // 超出的文本隐藏
          white-space: nowrap; // 溢出不换行
        }

        .consultants-list-item span {
          overflow: hidden; // 超出的文本隐藏
          white-space: nowrap; // 溢出不换行
        }

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

        .el-image {
          max-width: 300px;
          max-height: 300px;
          border-radius: 8px;
        }

        video {
          max-width: 300px;
          max-height: 300px;
          border-radius: 8px;
        }

      }

      .timeline {
        color: #8a8a8a;
        display: flex;
        justify-content: center;
      }

      .emojis {
        margin-top: 2px;
        width: 20px;
        height: 20px;
      }

      .emojis:hover {
        cursor: default;
      }
    }
  }

  .el-footer {
    border-top: solid 2px #d9d4cd;
    border-left: solid 2px #d9d4cd;
    height: 200px;
    background-color: #faf9f7;

    .content {
      font-size: 20px;
      margin: 20px;
      height: 70px;
      outline: none;
      overflow: auto; /* 启用滚动条 */
    }
  }

}

//滚动条
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
  background-color: #f0f0f0;
  border-radius: 10px;
}

::-webkit-scrollbar-thumb:hover {
  background-color: #8d8882;
}

::-webkit-scrollbar-thumb {
  background-color: #b4aeaa;
  border-radius: 10px;
}


.message-right {
  align-self: flex-end;
}

.message-left {
  align-self: flex-start;
}

.message-right-content {
  text-align: right;
}

.message-left-content {
  display: flex;
  align-items: center;
  text-align: left;
}

.chat-container {
  width: 300px;
  margin: 20px auto;
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

.name {
  margin-bottom: 5px;
  font-size: 13px;
}

.selected {
  background-color: #f0f0f0;
}
</style>