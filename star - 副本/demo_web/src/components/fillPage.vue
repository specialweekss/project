<template>
  <br>
  <el-button  type="text">
    <img src="../assets/img/返回.png" alt="返回" @click="close"/>
  </el-button>

    <h1>{{theme}}</h1>
  <div v-for="question in questions" :key="question.number">
    <h2>问题{{question.number}}.{{question.title}}  --{{getQuestionType(question.type)}} --{{getNecessary(question.necessary)}}</h2>

    <div v-if="question.type===0||question.type===1">
      <div v-for="selection in selectionForAll.at(question.number-1)" :key="selection.position">
        <h3>选项{{getSelectionPosition(selection.position)}}.{{selection.content}}</h3>
      </div  >
      <div v-if="question.type===1">
      <label v-for="selection in selectionForAll.at(question.number-1)" :key="selection.position" >

           <input type="checkbox" v-model="answers[question.number-1]" :value=selection.position> {{getSelectionPosition(selection.position)}}
        </label>
      </div>
      <div v-if="question.type===0">
        <label v-for="selection in selectionForAll.at(question.number-1)" :key="selection.position" >

          <input type="radio" v-model="answers[question.number-1]" :value=selection.position> {{getSelectionPosition(selection.position)}}
        </label>
      </div>
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
      commits:[],
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
        this.answers[question.number-1]=[];
      })
    },
    fillOver()
    {
      const list=this.answers;
      let over=true;
      list.forEach((answer,index)=>{
        if(this.questions[index].necessary===1)
        {
          if(answer.length===0) {
           if(over===true) {
             over = false;
             alert('尚未填写完毕');
           }
           }
          }

        if(this.questions[index].type===2&&this.answers[index].length>0)
        {
          if(answer[0]!==('1'||'2'||'3'||'4'||'5'||'6'||'7'||'8'||'9'||'10')){
            over=false;
            alert('打分题'+index+'应输入1~10')
          }
        }
      })
      return over;
    },
    async commit(){
      if(!this.fillOver())
      {
        return;
      }
      let index,answer;

      for(index=0;index<this.questionNUm;index++)
         {
           if(this.questions[index].type===1)
           answer=this.answers[index].join('')
           else
             answer=this.answers[index]
           this.commits.push({
             questionId:this.questionIds[index],
             answer:answer
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
    },
    getNecessary(necessary)
    {
      const states= {
        0: '选填',
        1: '必填',
      };
      return states[necessary];
    },
  }
}

</script>



<style scoped>

</style>