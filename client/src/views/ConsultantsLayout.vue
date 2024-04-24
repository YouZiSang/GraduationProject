<script setup>
import {ref,computed} from 'vue'
import {
  User,
  Crop,
  EditPen,
  SwitchButton,
  CaretBottom, Fold, Expand, House, ChatLineRound
} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import useUserInfoStore from '@/stores/userInfo.js'
import {useTokenStore} from '@/stores/token.js'
import {getId, getAvatar, getName} from "@/utils/getCurrentUserInfo.js";


const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
const currentName = computed(() => userInfoStore.info.name); // 使 currentName 成为响应式
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
            >
              <div class="logo-side">
                <div>
                  <el-image :src="getImagesUrl(imageUrl)" style="width:50px;height: 50px"></el-image>
                </div>
                <div>
                  <span style="color: #232323" v-if="!isCollapse"><span>咨询师</span></span>
                </div>
              </div>
              <el-sub-menu @click.native="router.push('home')" class="hide-icon">
                <template #title>
                  <el-icon><House /></el-icon>
                  <span>主页</span>
                </template>
              </el-sub-menu>
              <el-sub-menu index="1">
                <template #title>
                  <img src="@/assets/images/booking.png" slot="reference" alt="公告" style="width: 20px;margin-right: 5px"/>
                  <span>预约管理</span>
                </template>
                <el-menu-item index="bookingManage">预约设置</el-menu-item>
                <el-menu-item index="appointmentTable">预约表</el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="4">
                <template #title>
                  <img src="@/assets/images/psychology.png" slot="reference" alt="公告" style="width: 20px;margin-right: 5px"/>
                  <span>心理科普</span>
                </template>
                <el-menu-item index="bookHouse"><span>心理书屋</span></el-menu-item>
                <el-menu-item index="orders">订单</el-menu-item>
                <el-menu-item index="communityExchange">社区</el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="2">
                <template #title>
                  <el-icon><ChatLineRound/></el-icon>
                  <span>在线交流</span>
                </template>
                <el-menu-item index="appointmentTable">咨询表</el-menu-item>
                <el-menu-item index="consultation">咨询</el-menu-item>
                <el-menu-item index="groupChats">心理聊天室</el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="3">
                <template #title>
                  <el-icon><User /></el-icon>
                  <span>个人管理</span>
                </template>
                <el-menu-item index="info">个人中心</el-menu-item>
                <el-menu-item index="consultation">咨询</el-menu-item>
                <el-menu-item index="groupChats">心理聊天室</el-menu-item>
              </el-sub-menu>
              <el-sub-menu  @click.native="router.push('charts')" class="hide-icon">
                <template #title>
                  <img src="@/assets/images/statistics.png" slot="reference" alt="统计" style="width: 20px;margin-right: 5px"/>
                  <span>统计</span>
                </template>
              </el-sub-menu>
            </el-menu>
          </el-affix>
        </el-aside>

        <el-container>
          <el-header >
            <div style="text-align: right" v-if="!isCollapse">
              <el-icon size="30" @click="collapse">
                <Fold/>
              </el-icon>
            </div>
            <div style="text-align: center" v-else>
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
            <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                       <span class="name">{{ currentName }}</span>
                        <el-avatar :src="currentAvatar"
                                   style="width: 70px;height: 70px"/>
                        <el-icon>
                            <CaretBottom/>
                        </el-icon>
                    </span>
              <span>咨询师</span>
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
          <el-main style="background-color: #ffffff;min-height:710px;width: 100%">
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
  .title {
    font-size: 40px;
    color: white;
    display: flex;
    align-items: center;

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
}

.el-header {
  background-color: #19CAAD;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;


  .el-dropdown__box {
    display: flex;
    align-items: center;
    justify-items: flex-end;

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
.logo-side{
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color:#f3d19e;
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

.el-menu-item{
  background-color: #87CEFA;
}
.el-menu-item:hover{
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