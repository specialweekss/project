
<template>
  <el-button type="primary" @click="close">退出</el-button>
  <div>{{theme}}</div>
  <div v-for="question in questions" :key="question.number">
    问题{{question.number}}.{{question.title}}  --{{getQuestionType(question.type)}}
    <div v-if="question.type===0||question.type===1">
      <div v-for="selection in selectionForAll.at(question.number-1)" :key="selection.position">
        选项{{getSelectionPosition(selection.position)}}.{{selection.content}}
      </div>
    </div>
    <div v-for="(value,index) in answerForAll.at(question.number-1)" :key="index">
      <div v-if="question.type===0||question.type===1">
        <span>填写记录{{index+1}}：</span>
        <span v-for="(answer,index) in value.answer" :key="index">
          {{getSelectionPosition(answer)}},
      </span>
      </div>
      <div v-else>
        填写记录{{index+1}}：{{value.answer}}
      </div>
    </div>
    <br><br>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: ["close", "id","userId"],
  data() {
    return {
      theme: null,
      questions: [],
      selectionForAll: [],
      answerForAll: []
    }
  },
  mounted() {
    this.fetchQuestionnaire()
  },
  methods: {
    async fetchQuestionnaire() {
      const response = axios.get('http://localhost:8090/getById?id=' + this.id);
      this.theme = (await response).data.data.theme;
      const quesResponse = axios.get('http://localhost:8090/ListQuestionInIt?id=' + this.id);
      this.questions = (await quesResponse).data.data;
      const list = this.questions
      list.forEach(question => {
        axios.get('http://localhost:8090/ListAnswerByUser?questionId=' + question.questionId+'&userId='+this.userId)
            .then(response => {
              console.log(question.questionId)

              console.log(response);
              this.answerForAll[question.number - 1] = response.data.data;
            })

        axios.get('http://localhost:8090/ListSelectionInIt?questionId=' + question.questionId)
            .then(response => {

              this.selectionForAll[question.number - 1] = response.data.data;
            })
      })

    },
    getQuestionType(type) {
      const types = {
        0: '单选题',
        1: '多选题',
        2: '打分题',
        3: '填空题'
      };
      return types[type];
    },
    getSelectionPosition(position) {
      const positions= {
        1: 'A',
        2: 'B',
        3: 'C',
        4:'D',
        5:'E',
        6:'F',
        7:'G',
        8:'H'

      };
      return positions[position];
    }
  }
}

</script>
<style scoped>

</style>