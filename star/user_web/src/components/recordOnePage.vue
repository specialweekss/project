
<template>
  <el-button type="primary" @click="close">退出</el-button>
  <h1>{{theme}}</h1>
  <div v-for="question in questions" :key="question.number">
    <h2>问题{{question.number}}.{{question.title}}  --{{getQuestionType(question.type)}}</h2>
    <div v-if="question.type===0||question.type===1">
      <div v-for="selection in selectionForAll.at(question.number-1)" :key="selection.position">
        选项{{getSelectionPosition(selection.position)}}.{{selection.content}}
      </div>
    </div>
    ------------------------------------------------------------------------------------------
    <h2>填写总次数：{{question.answerNum}}</h2>
    <el-button v-if="!IsShowed(question)&&question.answerNum" type="primary" @click="recordOn(question)">展开</el-button>
    <div v-if="IsShowed(question)">
    <div v-for="(value,index) in answerForAll.at(question.number-1)" :key="index">
      <span v-if="question.type===0||question.type===1">
        <span>填写记录{{index+1}}：</span>
      <span v-for="(answer,index) in value.answer" :key="index">
          {{getSelectionPosition(answer)}},
      </span>
      </span>
        <span v-else>
          填写记录{{index+1}}：{{value.answer}}
      </span>
        <span v-if="!isValid(value)" style="color: red" >(已失效)</span>
    </div>
      <el-button  type="primary" @click="recordOff(question)">收回</el-button>
    </div>
    <br><br>
  </div>
</template>

<script>
import axios from "axios";
import myQuestionnaire from "@/components/MyQuestionnaire.vue";
export default {
  props: ["close", "id","userId"],
  data() {
    return {
      theme: null,
      questions: [],
      selectionForAll: [],
      answerForAll: [],
      questionOn:[]
    }
  },
  mounted() {
    this.fetchQuestionnaire()
  },
  methods: {
    async fetchQuestionnaire() {
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      const response = axios.get(window.Ip+'/getById?id=' + this.id);
      this.theme = (await response).data.data.theme;
      const quesResponse = axios.get(window.Ip+'/ListQuestionInIt?id=' + this.id);
      this.questions = (await quesResponse).data.data;
      const list = this.questions
      list.forEach(question => {
        axios.get(window.Ip+'/ListAnswerByUser?questionId=' + question.questionId+'&userId='+this.userId)
            .then(response => {
              console.log(question.questionId)

              console.log(response);
              this.answerForAll[question.number - 1] = response.data.data;
            })

        axios.get(window.Ip+'/ListSelectionInIt?questionId=' + question.questionId)
            .then(response => {

              this.selectionForAll[question.number - 1] = response.data.data;
            })
      })

    },
    async recordOn(question)
    {
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      this.questionOn.push(question.questionId)
    },
    async recordOff(question)
    {
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      let index=this.questionOn.indexOf(question.questionId);
      this.questionOn.splice(index,1);
      console.log(index);
    },
    IsShowed(question){
      const list=this.questionOn;
      let show=false;
      list.forEach(value=>{
        if(question.questionId===value)
          show=true;
      })
      return show;
    },

    isValid(answer)
    {
      console.log(answer)
      return answer.isValid !== 0;
    },
    getQuestionType(type) {
      const types = {
        0: '单选题',
        1: '多选题',
        2: '打分题(1~10)',
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