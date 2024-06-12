<template>
  <el-button type="primary" @click="close">退出</el-button>
    <h1>{{theme}}</h1>
  <div v-for="question in questions" :key="question.number">
    <h2>问题{{question.number}}.{{question.title}}  --{{getQuestionType(question.type)}}</h2>

    <div v-if="question.type===0||question.type===1">
      <div v-for="selection in selectionForAll.at(question.number-1)" :key="selection.position">
        <h3>选项{{getSelectionPosition(selection.position)}}.{{selection.content}}</h3>
      </div>
        <label v-for="selection in selectionForAll.at(question.number-1)" :key="selection.position" >

           <input type="radio" v-model="answers[question.number-1]" :value=selection.position> {{getSelectionPosition(selection.position)}}
        </label>
    </div>
    <div v-else>
      <input v-model="answers[question.number-1]" placeholder="问题答案">
    </div>
  </div>
  <el-button type="primary" @click="commit">提交</el-button>
</template>
<script >
import axios from "axios";

export default {
  props: ["close", "id","userId"],
  data() {
    return {
      theme: '',
      questions: [],
      selectionForAll:[],
      answers: [],
      questionIds:[],
      questionNUm:0,
      commits:[]
    }
  },
  mounted() {
    this.fetchQuestionnaire();
  },
  methods: {
    async fetchQuestionnaire() {
      const response = axios.get('http://localhost:8090/getById?id=' + this.id);
      this.theme = (await response).data.data.theme;
      const quesResponse = axios.get('http://localhost:8090/ListQuestionInIt?id=' + this.id);
      this.questions = (await quesResponse).data.data;
      this.questionNUm=(await quesResponse).data.total;
      const list=this.questions
      list.forEach(question => {
        axios.get('http://localhost:8090/ListSelectionInIt?questionId=' + question.questionId)
            .then(response => {

              this.selectionForAll[question.number - 1] = response.data.data;
            })
        this.questionIds[question.number-1]=question.questionId
      })
    },
    async commit(){
      let index;
      for(index=0;index<this.questionNUm;index++)
         {
           this.commits.push({
             questionId:this.questionIds[index],
             answer:this.answers[index]
           })
         }
      const  response=await axios.post('http://localhost:8090/commit?id='+this.id+"&userId="+this.userId,this.commits);
      console.log(response);
      this.close()
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
      const positions = {
        1: 'A',
        2: 'B',
        3: 'C',
        4: 'D',
        5: 'E',
        6: 'F',
        7: 'G',
        8: 'H'

      };
      return positions[position];
    }
  }
}

</script>



<style scoped>

</style>