<template>
  <div class="control">
    <el-button type="primary" @click="close" class="theButton">退出</el-button>
    <h1>{{questionnaire.theme}}：【{{getState(questionnaire.state)}}】</h1>
    <h2 v-if="questionnaire.state===1">
      发布时间：{{questionnaire.releaseTime}} <br> 截止时间：{{ questionnaire.endTime}}
    </h2>
    -----------------------------------------------------------------------------<br>
    <div v-for="question in questions" :key="question.number" >

      问题{{question.number }}. {{question.title}} -{{getQuestionType(question.type)}} -{{getNecessary(question.necessary)}}

      <el-button type="text"  class="delete-button" @click="deleteQuestion(question.questionId)" >删除</el-button>
      <el-button type="text"   @click="modOn(question)">修改</el-button>
      <div v-if="showMod&&beMod(question.questionId)">
        <form @submit.prevent="handleSubmit">
          <input  v-model="title" placeholder="问题标题" required><br>
          <div>是否必填：
            <label>
              <input type="radio" v-model="necessary" value="1" > 必填
            </label>
            <label>
              <input type="radio" v-model="necessary" value="0"> 非必填
            </label><br>
          </div>
          <!-- 这里可以添加更多关于问题的输入项 -->
        </form>
        <el-button v-if="showMod" type="primary" @click="modQuestion(question.questionId)">确定</el-button>
      </div>
      <div v-if="isChoice(question)">
        <div v-for="selection in selectionsForAll.at(question.number-1)" :key="selection.position">
          选项{{getSelectionPosition(selection.position)}}.{{selection.content}}
          <el-button type="text" class="delete-button" @click="deleteSelection(selection.selectionId)">删除</el-button>
          <el-button type="text" @click="modSelectionOn(selection)">修改</el-button>
          <div v-if="showModSelection&&selection.selectionId===beModSelectionId">
            <input v-model="content" placeholder="选项标题" required><br>
            <el-button type="primary" @click="modSelection(selection.selectionId)">确定</el-button>
          </div>
        </div>
        <el-button type="text" @click="addSelection(question.questionId)">添加选项</el-button>
      </div>

      <br>
      -----------------------------------------------------------------------------
    </div>
    <el-button type="primary" @click="addOn" class="theButton">添加问题</el-button>
    <br><br><br>
    <div v-if="showAdd">
      <form @submit.prevent="handleSubmit">
        <input class="cin" v-model="title" placeholder="问题标题" required><br><br>
        <div>是否必填：
          <label>
            <input type="radio" v-model="necessary" value="1" > 必填
          </label>
          <label>
            <input type="radio" v-model="necessary" value="0"> 非必填
          </label><br><br>
        </div>
        <div>问题类型：
          <select v-model="type" class="cin">
            <option v-for="(value, key) in types" :key="key" :value="key">{{ getQuestionType(value) }}</option>
          </select>
        </div>
        <br><br>
      </form>
      <el-button v-if="showAdd" type="primary" @click="addQuestion" class="theButton">确定</el-button>
    </div>
  </div>
</template>>



<script>
import axios from "axios";
import check from '@/components/MyQuestionnaire.vue'
export default {
  props: ["id", "userId","close"],
  data() {
    return {
      showMod:false,
      showAdd:false,
      beModQuestionId:null,
      questions:[],
      necessary:1,
      types:[0,1,2,3],
      type:0,
      questionnaire: [],
      title: '问题标题',
      selectionsForAll:[],
      content:'选项',
      showModSelection:false,
      beModSelectionId:null
    };
  },

  mounted() {
    this.fetchQuestionnaire();

  },
  methods: {
    async fetchQuestionnaire() {
      if(!await check.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      const update=await axios.get(window.Ip+'/checkState?id='+this.id);
      if(update.data.code===400)
      {
        if(update.data.data===-1)
        {
          alert('问卷已被系统删除！')
          this.close();
          return
        }
      }
      console.log(update)
      this.title = "问题标题";
      this.content="选项"
      this.type=0;
      this.necessary=0;
      console.log(this.creatorId);
        const response = await axios.get(window.Ip+'/getById?id='+this.id);
        this.questionnaire = response.data.data;
      let releaseTime=new Date(this.questionnaire.releaseTime)
      let endTime=new Date(this.questionnaire.endTime)
      this.questionnaire.releaseTime=releaseTime;
      this.questionnaire.endTime=endTime
        console.log(response)
      this.questions=[];
      await axios.get(window.Ip+'/ListQuestionInIt?id='+this.id)
          .then(response=>{
            if(response.data.code===200) {
              const list = response.data.data;
              list.forEach(question => {

                this.questions.push(question);
              })
            }
            else
            {
              this.questions=[];
            }
          })
      this.selectionsForAll=[];
      const list=this.questions
     list.forEach(question=>{
         axios.get(window.Ip+'/ListSelectionInIt?questionId='+question.questionId)
             .then(response=>{
               this.selectionsForAll[question.number-1]=response.data.data;
               });
             })

    },
    getQuestionType(type) {
      const types = {
        0: '单选题',
        1: '多选题',
        2: '打分题(1~10)',
        3:'填空题'
      };
      return types[type] || '未知类型';
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
    },
   async addOn(){
     if(!await check.methods.checkUser(this.userId))
     {
       this.close();
       return;
     }
     const update=await axios.get(window.Ip+'/checkState?id='+this.id);
     if(update.data.code===400)
     {
       if(update.data.data===-1)
       {
         alert('问卷已被系统删除！')
         this.close();
         return
       }
     }
     this.showAdd=true;
     this.showMod=false;
     this.showModSelection=false;
   },
    async addQuestion() {
      if(!await check.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      const update=await axios.get(window.Ip+'/checkState?id='+this.id);
      if(update.data.code===400)
      {
        if(update.data.data===-1)
        {
          alert('问卷已被系统删除！')
          this.close();
          return
        }
      }
        const questionData = {
          "title": this.title,
          "necessary": this.necessary,
          "type": this.type,
        };
        console.log(questionData);

        const response = await axios.post(window.Ip+'/saveQuestion?id='+this.id,questionData);

        if (response.data.code === 200) {
          console.log("问题添加成功");

          this.showAdd=false;
            await this.fetchQuestionnaire();
        } else {
          alert('问题无标题！')

        }


    },
    async deleteQuestion(questionId){

      if(!await check.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      const update=await axios.get(window.Ip+'/checkState?id='+this.id);
      if(update.data.code===400)
      {
        if(update.data.data===-1)
        {
          alert('问卷已被系统删除！')
          this.close();
          return
        }
      }
console.log(questionId);
      const response = await axios.post(window.Ip+'/deleteQuestion?questionId='+questionId);
      console.log(response);
      await this.fetchQuestionnaire();
    },
   async modOn(question)
    {
      if(!await check.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      const update=await axios.get(window.Ip+'/checkState?id='+this.id);
      if(update.data.code===400)
      {
        if(update.data.data===-1)
        {
          alert('问卷已被系统删除！')
          this.close();
          return
        }
      }
      this.showMod=true;
      this.showAdd=false;
      this.showModSelection=false;
      this.beModQuestionId=question.questionId;
      this.necessary=question.necessary;
      this.title=question.title;
      this.type=question.type
      this.showAdd=false;
      this.showModSelection=false;
    },
    beMod(questionId)
    {
      if(questionId === this.beModQuestionId)
        return 1;
      else
        return 0;
    },
    async modQuestion(questionId)
    {
      if(!await check.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      const update=await axios.get(window.Ip+'/checkState?id='+this.id);
      if(update.data.code===400)
      {
        if(update.data.data===-1)
        {
          alert('问卷已被系统删除！')
          this.close();
          return
        }
      }
        const questionData = {
          "title": this.title,
          "necessary": this.necessary,
          "type" :this.type
        };
        console.log(questionData);

        const response = await axios.post(window.Ip+'/modQuestion?questionId='+questionId,questionData);
      if (response.data.code === 200) {
        console.log("问题修改成功");
      } else {
        alert('问题无标题！')
        return
      }

          console.log(response);
          this.title = "";
          this.necessary=0;
          this.showMod=0;
          await this.fetchQuestionnaire();

      console.log(questionId)
    },

    isChoice(question)
    {

     if(question.type===1||question.type===0)
      {
        return 1;
      }
     else
       return false;


    },
  async deleteSelection(selectionId){
    if(!await check.methods.checkUser(this.userId))
    {
      this.close();
      return;
    }
    const update=await axios.get(window.Ip+'/checkState?id='+this.id);
    if(update.data.code===400)
    {
      if(update.data.data===-1)
      {
        alert('问卷已被系统删除！')
        this.close();
        return
      }
    }
      const  response=await axios.post(window.Ip+'/deleteSelection?selectionId='+selectionId);
      console.log(response)
    if(response.data.code===400)
    {
      if(response.data.data===0)
      {
        alert('选择题需存在至少两个选项!')
        return;
      }
    }
      await this.fetchQuestionnaire();
  },
  async  addSelection(questionId) {
    if(!await check.methods.checkUser(this.userId))
    {
      this.close();
      return;
    }
    const update=await axios.get(window.Ip+'/checkState?id='+this.id);
    if(update.data.code===400)
    {
      if(update.data.data===-1)
      {
        alert('问卷已被系统删除！')
        this.close();
        return
      }
    }
    const response = await axios.post(window.Ip+'/saveSelection?questionId=' + questionId, {"content": "选项"});
    console.log(response);
    this.showModSelection=false;
    this.showAdd=false;
    this.showMod=false;
    await this.fetchQuestionnaire();
  },
   async modSelectionOn(selection)
    {
      if(!await check.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      const update=await axios.get(window.Ip+'/checkState?id='+this.id);
      if(update.data.code===400)
      {
        if(update.data.data===-1)
        {
          alert('问卷已被系统删除！')
          this.close();
          return
        }
      }
      this.showModSelection=true;
      this.showAdd=false;
      this.showMod=false;
      this.beModSelectionId=selection.selectionId;
   this.content=selection.content
    },
    async modSelection(selectionId)
    {
      if(!await check.methods.checkUser(this.userId))
      {
        this.close();
        return;
      }
      const update=await axios.get(window.Ip+'/checkState?id='+this.id);
      if(update.data.code===400)
      {
        if(update.data.data===-1)
        {
          alert('问卷已被系统删除！')
          this.close();
          return
        }
      }
      const response=await axios.post(window.Ip+'/modSelection?selectionId='+selectionId+'&content='+this.content);
      console.log(response);
      if (response.data.code === 200) {
        console.log("选项修改成功");
      } else {
        alert('选项无文本！')
        return
      }
      this.showModSelection=false;
      await this.fetchQuestionnaire();
    }
  }
}
</script>
<style>
@import '@/css/border.css';
@import '@/css/bordertext.css';
@import '@/css/noUseButton.css';
@import '@/css/usingButton.css';
@import '@/css/buttonHover.css';
.delete-button{
  color: red;
}
.recordMessage {
  left: 550px;
  top: -700px;
  width: 1400px;
  border-radius: 30px;
  background: linear-gradient(135deg, rgba(174, 235, 198, 1) 0%, rgba(111, 227, 158, 0.01) 100%);
  position: relative;
  display: flex;
}
.theButton{
  left: 383px;
  top: 470px;
  width: 220px;
  height: 120px;
  opacity: 1;
  border-radius: 40px;
  background: rgba(42, 130, 228, 1);

}
.cin{
  width: 200px;
  height: 60px;
  border-radius: 30px;
}
.recordMessage img{
  margin-left: 50px;
}

.recordMessage a {
  padding: 6px 12px;
  font-size: 14px;
  color: #007bff;
  text-decoration: none;
  transition: background-color 0.3s ease;
  border-radius: 30px;
}

.recordMessage a:hover {
  background-color: #e9ecef;
}
.control{
  width: 50%;
  margin: 0 auto;
  text-align: center;
  background: radial-gradient(50.14% 50% at 50.13623978201635% 50%, rgba(185, 247, 171, 1) 0%, rgba(71, 214, 39, 0) 100%);
}

</style>