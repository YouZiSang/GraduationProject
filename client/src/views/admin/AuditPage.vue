<script setup lang="ts">
import {onMounted, ref} from "vue";
import {
  auditService,
  consultantsInfoEditService,
  auditSuccessEditService,
  auditFailEditService,
  upGradeInfoListService,
  userInfoListService,
  auditSuccessUpGradeService,
  auditFailUpGradeService
} from "@/api/exchange/audit.js";
import {ElMessage} from "element-plus";

interface Info {
  uid: number,
  cid: number,
  nickname: string,
  name: string,
  avatarUrl: string,
  sex: string,
  age: number,
  certificate: string,
  biography: string,
  email: string,
  phone: number,
  status: string
}

const unAuditInfo = ref<Info[]>([])
const upGradeInfoList = ref<Info[]>([])
const consultantList = ref<Info[]>([])
const userInfo = ref<Info[]>([])
const activeName = ref('unAudit')

const audit = async () => {
  let res = await auditService()
  unAuditInfo.value = res.data
}

const consultantsInfoEdit = async () => {
  let res = await consultantsInfoEditService()
  consultantList.value = res.data
  console.log(res.data)
}

const getUsersList = async () => {
  let res = await userInfoListService()
  userInfo.value = res.data

}
const upGradeInfo = async () => {
  let res = await upGradeInfoListService()
  upGradeInfoList.value = res.data

}
const editInfoPairs = ref<{ old: Info; new: Info }[]>([]); // 添加类型声明
const upGradeInfoPairs = ref<{ old: Info; new: Info }[]>([]); // 添加类型声明
const initCard = () => {
  // 清空matchedPairs数组
  editInfoPairs.value = [];
  upGradeInfoPairs.value = [];
  // 遍历consultantList和editInfo，找出匹配的cid并创建一对对象
  consultantList.value.forEach(consultant => {
    const matchedEditInfo = unAuditInfo.value.find(edit => edit.cid === consultant.cid);
    if (matchedEditInfo) {
      editInfoPairs.value.push({old: consultant, new: matchedEditInfo});
    }
  });

  userInfo.value.forEach(user => {
    const matchedUpGradeInfo = upGradeInfoList.value.find(upGrade => upGrade.uid === user.uid);
    if (matchedUpGradeInfo) {
      upGradeInfoPairs.value.push({old: user, new: matchedUpGradeInfo});
      console.log(upGradeInfoPairs.value)
    }

  });
};

const upGradeSuccess = async (pair) => {
  pair.new.status = '审核通过'
  let res = await auditSuccessUpGradeService(pair.new)
  ElMessage.success(res.message)
  await initCard()
}
const upGradeFail = async (pair) => {
  pair.new.status = '审核未通过'
  let res = await auditFailUpGradeService(pair.new)
  ElMessage.success(res.message)

  await initCard()
}
const success = async (pair) => {
  pair.new.status = '审核通过'
  let res = await auditSuccessEditService(pair.new)
  ElMessage.success(res.message)
  await initCard()
}

const fail = async (pair) => {
  pair.new.status = '审核未通过'
  let res = auditFailEditService(pair.new)
  ElMessage.success(res.message)
  await initCard()
}
onMounted(async () => {
  await audit()
  await consultantsInfoEdit()
  await getUsersList()
  await upGradeInfo()
  await initCard()

})

</script>

<template>
  <el-tabs v-model="activeName" class="demo-tabs" type="border-card">
    <el-tab-pane label="升级咨询师审核" name="gradeAudit">
      <div class="for-card">
        <div v-for="pair in upGradeInfoPairs" :key="pair.old.uid">
          <el-card v-if="pair.new.status === '审核中'">
            <div style="display:flex;">
              <div class="left">
                <span>旧信息</span>
                <el-avatar :src="pair.old.avatarUrl" size="large"></el-avatar>
                <p>账号：{{ pair.old.uid }}</p>
                <p>姓名：{{ pair.old.name }}</p>
                <p>昵称：{{ pair.old.nickname }}</p>
                <p>性别：{{ pair.old.sex }}</p>
                <p>年龄：{{ pair.old.age }}</p>
                <p>邮箱：{{ pair.old.email }}</p>
                <p>电话：{{ pair.old.phone }}</p>
              </div>
              <div class="right">
                <span>新信息</span>
                <el-avatar :src="pair.new.avatarUrl" size="large"></el-avatar>
                <p>账号：{{ pair.new.uid }}</p>
                <p>姓名：{{ pair.new.name }}</p>
                <p>性别：{{ pair.new.sex }}</p>
                <p>年龄：{{ pair.new.age }}</p>
                <p>证书：{{ pair.new.certificate }}</p>
                <p>邮箱：{{ pair.new.email }}</p>
                <p>电话：{{ pair.new.phone }}</p>
                <p>描述：{{ pair.new.biography }}</p>
              </div>
            </div>
            <div style="text-align: center">
              <el-button @click="upGradeFail(pair)" type="danger">审核不通过</el-button>
              <el-button @click="upGradeSuccess(pair)" type="primary">审核通过</el-button>
            </div>
          </el-card>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="修改信息审核" name="unAudit">
      <div class="for-card">
        <div v-for="pair in editInfoPairs" :key="pair.old.cid">
          <el-card v-if="pair.new.status === '审核中'">
            <div style="display:flex">
              <div class="left">
                <span>旧信息</span>
                <el-avatar :src="pair.old.avatarUrl" size="large"></el-avatar>
                <p>账号：{{ pair.old.cid }}</p>
                <p>姓名：{{ pair.old.name }}</p>
                <p>性别：{{ pair.old.sex }}</p>
                <p>年龄：{{ pair.old.age }}</p>
                <p>证书：{{ pair.old.certificate }}</p>
                <p>邮箱：{{ pair.old.email }}</p>
                <p>电话：{{ pair.old.phone }}</p>
                <p>描述：{{ pair.old.biography }}</p>
              </div>
              <div class="right">
                <span>新信息</span>
                <el-avatar :src="pair.new.avatarUrl" size="large"></el-avatar>
                <p>账号：{{ pair.new.cid }}</p>
                <p>姓名：{{ pair.new.name }}</p>
                <p>性别：{{ pair.new.sex }}</p>
                <p>年龄：{{ pair.new.age }}</p>
                <p>证书：{{ pair.new.certificate }}</p>
                <p>邮箱：{{ pair.new.email }}</p>
                <p>电话：{{ pair.new.phone }}</p>
                <p>描述：{{ pair.new.biography }}</p>
              </div>
            </div>
            <div style="text-align: center">
              <el-button @click="fail(pair)" type="danger">审核不通过</el-button>
              <el-button @click="success(pair)" type="primary">审核通过</el-button>
            </div>
          </el-card>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="已审核（升级咨询师）" name="GradeAuditSuccess">
      <div class="for-card">
        <div v-for="pair in upGradeInfoPairs" :key="pair.old.uid">
          <div style="display: flex">
            <el-card v-if="pair.new.status ==='审核通过'">
              <div style="display:flex">
                <div class="right">
                  <span>新信息 (审核通过)</span>
                  <el-avatar :src="pair.new.avatarUrl" size="large"></el-avatar>
                  <p>账号：{{ pair.new.uid }}</p>
                  <p>姓名：{{ pair.new.name }}</p>
                  <p>性别：{{ pair.new.sex }}</p>
                  <p>年龄：{{ pair.new.age }}</p>
                  <p>证书：{{ pair.new.certificate }}</p>
                  <p>邮箱：{{ pair.new.email }}</p>
                  <p>电话：{{ pair.new.phone }}</p>
                  <p>描述：{{ pair.new.biography }}</p>
                </div>
              </div>
            </el-card>
            <el-card v-if="pair.new.status ==='审核未通过'">
              <div style="display:flex">
                <div class="right">
                  <span>旧信息 (审核未通过)</span>
                  <el-avatar :src="pair.old.avatarUrl" size="large"></el-avatar>
                  <p>账号：{{ pair.old.uid }}</p>
                  <p>姓名：{{ pair.old.name }}</p>
                  <p>昵称：{{ pair.old.nickname }}</p>
                  <p>性别：{{ pair.old.sex }}</p>
                  <p>年龄：{{ pair.old.age }}</p>
                  <p>邮箱：{{ pair.old.email }}</p>
                  <p>电话：{{ pair.old.phone }}</p>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="已审核（修改信息）" name="AuditSuccess">
      <div class="for-card">
        <div v-for="pair in editInfoPairs" :key="pair.old.cid">
          <div style="display: flex">
            <el-card v-if="pair.new.status ==='审核通过'">
              <div style="display:flex">
                <div class="right">
                  <span>新信息 (审核通过)</span>
                  <el-avatar :src="pair.new.avatarUrl" size="large"></el-avatar>
                  <p>账号：{{ pair.new.cid }}</p>
                  <p>姓名：{{ pair.new.name }}</p>
                  <p>性别：{{ pair.new.sex }}</p>
                  <p>年龄：{{ pair.new.age }}</p>
                  <p>证书：{{ pair.new.certificate }}</p>
                  <p>邮箱：{{ pair.new.email }}</p>
                  <p>电话：{{ pair.new.phone }}</p>
                  <p>描述：{{ pair.new.biography }}</p>
                </div>
              </div>
            </el-card>
            <el-card v-if="pair.new.status ==='审核未通过'">
              <div style="display:flex">
                <div class="right">
                  <span>旧信息 (审核未通过)</span>
                  <el-avatar :src="pair.old.avatarUrl" size="large"></el-avatar>
                  <p>账号：{{ pair.old.cid }}</p>
                  <p>姓名：{{ pair.old.name }}</p>
                  <p>性别：{{ pair.old.sex }}</p>
                  <p>年龄：{{ pair.old.age }}</p>
                  <p>证书：{{ pair.old.certificate }}</p>
                  <p>邮箱：{{ pair.old.email }}</p>
                  <p>电话：{{ pair.old.phone }}</p>
                  <p>描述：{{ pair.old.biography }}</p>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<style scoped lang="scss">
.for-card {
  display: flex;
  flex-wrap: wrap;
}

.el-card {
  width: 600px;
  margin-right: 10px;
}

.left {
  display: flex;
  flex-direction: column;
  flex: 1;
  padding: 20px;
  min-height: 560px;
  height: 100%;
  background-color: #f0f4f8;
  margin-bottom: 10px;

  span {
    font-size: 18px;
    margin-bottom: 10px;
  }
}

.right {
  display: flex;
  flex-direction: column;
  flex: 1;
  padding: 20px;
  min-height: 560px;
  background-color: rgba(192, 196, 204, 0.41);
  height: 100%;
  margin-bottom: 10px;

  span {
    font-size: 18px;
    margin-bottom: 10px;
  }
}
</style>