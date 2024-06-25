

<template>
  <el-button type="primary" @click="close">退出</el-button>
  <h1>{{theme}}：【{{getState(state)}}】</h1>
  <div v-if="questions!==null">
  <div v-for="question in questions" :key="question.number">
    <h2>问题{{question.number}}.{{question.title}}  --{{getQuestionType(question.type)}} -{{getNecessary(question.necessary)}}</h2>
    <div v-if="question.type===0||question.type===1">
      <div v-for="selection in selectionForAll.at(question.number-1)" :key="selection.position">
        选项{{getSelectionPosition(selection.position)}}.{{selection.content}}
      </div>
    </div>
    ---------------------------------------------------------------
    <h3>填写总次数：{{question.answerNum}}</h3>
    <el-button v-if="!IsShowed(question)&&question.answerNum" type="primary" @click="recordOn(question)">展开</el-button>
    <div v-if="IsShowed(question)">
    <br><div>填写记录：<br></div>
    <div v-for="(value,index) in answerForAll.at(question.number-1)" :key="index">

      <span v-if="question.type===0||question.type===1">
        <span>用户{{value.userId}}：</span>
      <span v-for="(answer,index) in value.answer" :key="index">
          {{getSelectionPosition(answer)}},
      </span>
      </span>
      <span v-else>
          用户{{value.userId}}：{{value.answer}}
      </span>
      <span v-if="!isValid(value)" style="color: red" >(已失效)</span>
      </div>
      <el-button  type="primary" @click="recordOff(question)">收回</el-button>
    </div>
    </div>
    <br><br>
  </div>
  <div v-else>
    请先添加问题！
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
      state:null,
      questionOn:[]
    }
  },
  mounted() {
    this.fetchQuestionnaire()
  },
  methods: {
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
    async fetchQuestionnaire() {
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      const response = axios.get(window.Ip+'/getById?id=' + this.id);
      this.theme = (await response).data.data.theme;
      this.state=(await response).data.data.state;
      const quesResponse = axios.get(window.Ip+'/ListQuestionInIt?id=' + this.id);
      this.questions = (await quesResponse).data.data;
      const list = this.questions
      if ((await quesResponse).data.code===400)
      {
        this.answerForAll=[];
        this.selectionForAll=[];
      }
      else {
        list.forEach(question => {
          axios.get(window.Ip+'/ListAnswerInIt?questionId=' + question.questionId)
              .then(response => {
                this.answerForAll[question.number - 1] = response.data.data;
                console.log(this.answerForAll)
              })

          axios.get(window.Ip+'/ListSelectionInIt?questionId=' + question.questionId)
              .then(response => {

                this.selectionForAll[question.number - 1] = response.data.data;
              })
        })
      }
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
    },
    getState(state)
    {
      const states= {
        0: '未发布',
        1: '已发布',
        2: '已截止',
      };
      return states[state];
    },
    getNecessary(necessary)
    {
      const states= {
        0: '选填',
        1: '必填',
      };
      return states[necessary];
    }
  }
}

</script>
<style scoped>

</style>