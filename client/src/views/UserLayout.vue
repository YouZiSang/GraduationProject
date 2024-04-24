<script setup>
import {ref, computed} from 'vue'
import {
  User,
  Crop,
  EditPen,
  SwitchButton,
  CaretBottom, Fold, Expand, House, ChatLineRound, Reading
} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import useUserInfoStore from '@/stores/userInfo.js'
import {useTokenStore} from '@/stores/token.js'



const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
const currentName = computed(() => userInfoStore.info.nickname); // 使 currentName 成为响应式
const currentAvatar = computed(() => userInfoStore.info.avatarUrl); // 同样使 currentAvatar 成为响应式

const router = useRouter();
const getImagesUrl = (name) => {
  return new URL(name, import.meta.url).href
}
//调用函数,获取用户详细信息

const handleCommand = (command) => {
  //判断指令
  if (command === 'logout') {
    //退出登录
    ElMessageBox.confirm(
        '您确认要退出吗?',
        '温馨提示',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        }
    )
        .then(async () => {
          //退出登录
          //1.清空pinia中存储的token以及个人信息
          tokenStore.removeToken()
          userInfoStore.removeInfo()

          //2.跳转到登录页面
          router.push('/LoginPage')
          ElMessage({
            type: 'success',
            message: '退出登录成功',
          })

        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '用户取消了退出登录',
          })
        })
  } else {
    //路由
    router.push('/user/' + command)
  }
}
const imageUrl = "../assets/images/logo.png"
const isCollapse = ref(false)
const collapse = () => {
  isCollapse.value = !isCollapse.value
}

//快速
</script>

<template>
  <div class="common-layout">
    <el-container class="layout-container">
      <el-container>
        <el-aside
            :style="{ width: isCollapse ? '60px' : '180px' }">
          <el-affix position="top">
            <el-menu
                default-active="2"
                class="el-menu-vertical-demo"
                :collapse="isCollapse"
                router
                active-text-color="#409eff"

            >
              <div class="logo-side">
                <div>
                  <el-image :src="getImagesUrl(imageUrl)" style="width:50px;height: 50px"></el-image>
                </div>
                <div>
                  <span style="color: #232323" v-if="!isCollapse">用户</span>
                </div>
              </div>
              <el-sub-menu @click.native="router.push('home')" class="hide-icon">
                <template #title>
                  <el-icon>
                    <House/>
                  </el-icon>
                  <span>主页</span>
                </template>
              </el-sub-menu>
              <el-sub-menu index="2">
                <template #title>
                  <el-icon>
                    <ChatLineRound/>
                  </el-icon>
                  <span>咨询服务</span>
                </template>
                <el-menu-item index="groupChats">心理咨询聊天室</el-menu-item>
                <el-menu-item index="consultation">在线咨询</el-menu-item>
                <el-menu-item index="consultantsList">预约</el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="4">
                <template #title>
                  <el-icon>
                    <Reading/>
                  </el-icon>
                  <span>心理科普</span>
                </template>
                <el-menu-item index="communityExchange">社区交流</el-menu-item>
                <el-menu-item index="bookHouse">心理书屋</el-menu-item>
                <el-menu-item index="questionnaires">问卷调查</el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="3">
                <template #title>
                  <el-icon>
                    <User/>
                  </el-icon>
                  <span>个人中心</span>
                </template>
                <el-menu-item index="info">基本资料</el-menu-item>
                <el-menu-item index="orders">订单</el-menu-item>
                <el-menu-item index="updatePsw">修改密码</el-menu-item>
                <el-menu-item index="upgrade">升级</el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-affix>
        </el-aside>

        <el-container>
          <el-header>
            <div style="text-align: right" v-if="!isCollapse">
              <el-icon size="30" @click="collapse">
                <Fold/>
              </el-icon>
            </div>
            <div style="text-align: center" v-else >
              <el-icon size="30" @click="collapse">
                <Expand/>
              </el-icon>
            </div>
            <div class="title">
              <span>青</span><span>少</span><span>年</span><span>心</span><span>理</span>
              <span>咨</span><span>询</span><span>平</span><span>台</span>
            </div>
            <!-- 下拉菜单 -->
            <!-- command: 条目被点击后会触发,在事件函数上可以声明一个参数,接收条目对应的指令 -->

            <span class="el-dropdown__box">

              <span class="name">{{ currentName }}</span>
              <el-avatar :src="currentAvatar"
                         style="width: 70px;height: 70px"/>
            </span>
            <el-dropdown placement="bottom-end" @command="handleCommand">
              <el-icon>
                <CaretBottom/>
              </el-icon>

              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
                  <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                  <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
                  <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-header>
          <el-main style="background-color: #ffffff;min-height:690px;width: 100%">
            <router-view></router-view>
          </el-main>
          <el-footer style="height: 50px">
            Footer
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>
.layout-container {

}
.title {
  margin-left: 80px;
  font-size: 40px;
  color: white;
  display: flex;
  align-items: center;
  flex: 7;

  span {
    margin-right: 20px;
    font-weight: bold;
    font-family: "华文楷体";
    animation: colorChange 1s infinite ease-in-out alternate;
  }

  @keyframes colorChange {
    to {
      color: #ff0266;
    }
  }

  @for $i from 1 through 9 {
    span:nth-child(#{$i}) {
      animation-delay: ($i - 1) * 0.2s;
    }
  }

}
.el-header {
  background-color: #19CAAD;
  height: 80px;
  display: flex;
  align-items: center;


  .el-dropdown__box {
    display: flex;
    align-items: center;
    justify-items: flex-end;
    float: right;
    flex: 1.5;

    .el-icon {
      color: #999;
      margin-left: 10px;
    }

    &:active,
    &:focus {
      outline: none;
    }
  }
}

.logo-side {
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f3d19e;
}

.name {
  font-size: 20px;
  margin-right: 20px;
  color: black;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 180px;
  min-height: 400px;
  background-color: #f8e3c5;
}

.el-menu-item {
  background-color: #87CEFA;
}

.el-menu-item:hover {
  background-color: #a0cfff;
}

.el-aside {
  color: white;
  overflow: hidden;
  height: 100%;
  background-color: #f3d19e;

  //span {
  //  color: #2c3e50;
  //}


}
.el-sub-menu.hide-icon :deep(.el-sub-menu__icon-arrow) {
  display: none;
}
</style>