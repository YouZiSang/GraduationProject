<script setup>
import {ref, onMounted} from "vue";
import {
  answerScoreService,
  answerService,
  questionnairesService,
  surveyCompletedService
} from "@/api/questionnaires.js";

const scoreMapping = {
  'A': 5, 'B': 4, 'C': 3, 'D': 2, 'E': 1
};

const questions = ref([{
  id: '',
  questionText: '',
  questionType: '',
  options: '',
  optionText: '',
  answerId: '',
}]);
const options = ref([]);
const selectedAnswers = ref({});

const questionRefs = ref([]); // 存储问题元素的引用
//未回答
const surveyCompleted = ref(false);

const type = ref({});


const initQuestions = async () => {
  const res = await questionnairesService()
  options.value = res.data.filter(item => item.options !== null);

  const questionIds = new Set(options.value.map(item => item.id));

  questions.value = Array.from(questionIds).map(id => options.value.find(item => item.id === id));

  questions.value.forEach(question => {
    selectedAnswers.value[question.id] = null;
  });

  questions.value.forEach(question => {
    type.value[question.id] = question.questionType;
  });

  console.log(type.value)
}

const questionAnswers = async () => {
  let res = await surveyCompletedService()
  surveyCompleted.value = res.data
}
const filteredOptions = (questionId) => {
  return options.value.filter(option => option.id === questionId);
};
const submitSurvey = async () => {
  let totalScore = 0;
  let firstUnansweredQuestionId = null;
  let selectedResponses = []; // 用于记录用户选择的问题ID和答案
  let typeScores = {}; // 用于记录每个类型的分数

  // 初始化每个问题类型的分数为 0
  Object.values(type.value).forEach((questionType) => {
    if (!typeScores[questionType]) {
      typeScores[questionType] = 0;
    }
  });
  // 遍历用户选择的答案，根据 scoreMapping 计算总分
  // 同时记录第一个未回答的问题的 ID
  let hasUnanswered = false;
  Object.keys(selectedAnswers.value).forEach((questionId) => {
    const answerOption = selectedAnswers.value[questionId];
    const typeQuestion = type.value[questionId]; // 获取问题的类型
    if (answerOption) {
      const score = scoreMapping[answerOption];
      if (score !== undefined) { // 确保scoreMapping中有对应的分数
        totalScore += score;
        typeScores[typeQuestion] += score; // 根据问题类型累加分数
        selectedResponses.push({questionId, answerOption, typeQuestion}); // 记录问题ID和回答的答案及其类型
      }
    } else {
      if (!firstUnansweredQuestionId) firstUnansweredQuestionId = questionId;
      hasUnanswered = true;
    }
  });
  if (hasUnanswered) {
    // 如果有未回答的问题，则滚动到第一个未回答的问题处
    const questionElementId = `question${firstUnansweredQuestionId}`;
    const questionElement = document.getElementById(questionElementId);
    if (questionElement) {
      questionElement.scrollIntoView({behavior: 'smooth', block: 'nearest'});
    }
    ElNotification({
      title: '温馨提示',
      message: '你还有题目未填',
      type: 'warning',
    })

  } else {
    // 显示总分和每个问题类型的分数
    const dataToSend = {
      selectedResponses,
      typeScores,
      totalScore
    };

    try {
      await answerService(selectedResponses);
      await answerScoreService(typeScores);
      // 发送数据到后端，并等待响应
      // const response = await answerService(dataToSend);
      // if (response && response.status === 200) {
      //   alert(`问卷已提交！您的总分是: ${totalScore}。`);
      //   surveyCompleted.value = true;
      // } else {
      //   // 处理错误情况
      //   alert("提交问卷时发生错误，请重试。");
      // }

      console.log(dataToSend)
    } catch (error) {
      console.error("提交问卷时发生错误：", error);
      alert("提交问卷时发生网络错误，请重试。");
    }
  }
};
onMounted(async () => {
  await initQuestions();
  await questionAnswers();
});
// 暴露到模板中的方法
const submit = submitSurvey;
</script>
<template>
  <el-container>
    <el-card style="color: #0ebffc;margin: 0 auto; width: 70%">
      <div class="title">
        <p>青少年心理健康调查</p>
      </div>
      <div class="greetings">
        <p>&nbsp;&nbsp;亲爱的用户:</p>
        <span>你好本问卷调查旨在收集您对个人心理健康状态的看法和感受。大约占用你5-8分钟时间，
            所有问题均无对错之分，您的回答将严格保密，并仅用于研究目的。
            请根据您的实际情况，诚实地选择最符合您感受的选项。</span>
      </div>
      <el-divider/>
      <div v-if="!surveyCompleted">
        <div v-for="(question,index) in questions"
             :key="question.id"
             ref="questionRefs"
             class="radio-group"
             :id="`question${index + 1}`"
        >
          <p>题目{{ index + 1 }}： {{ question.questionText }}</p>
          <el-radio-group v-model="selectedAnswers[question.id]">
            <el-radio v-for="option in filteredOptions(question.id)" :key="option.id" :label="option.options">
              {{ option.options }}. {{ option.optionText }}
            </el-radio>
          </el-radio-group>
        </div>
        <div class="submit">
          <el-button type="primary" @click="submit">提交问卷</el-button>
        </div>
      </div>
      <div v-else class="survey-completed">
        <p>您已完成问卷调查，感谢您的参与！</p>
      </div>
    </el-card>
  </el-container>
</template>

<style scoped lang="scss">
.title {
  text-align: center;
  font-size: 22px;
  margin-bottom: 20px;
}

.greetings {
  text-align: left;
  padding: 0 100px;
}

.radio-group {
  padding: 0 100px;

}

.el-radio-group {
  display: flex;
  flex-flow: column;
  align-items: flex-start;

}

.el-radio::v-deep .el-radio__label {
  font-size: 16px !important;

}

.submit {
  margin-top: 20px;
  text-align: center;

  .el-button {
    font-size: 20px;
    height: 40px;
    width: 200px;
  }
}

.survey-completed {
  height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
}
</style>