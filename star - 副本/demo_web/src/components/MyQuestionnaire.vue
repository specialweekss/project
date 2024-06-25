<template>
  <div v-if="showMyQuestionnaire">
  <div v-if="!showEdit&!showData" >
    <div class="border" >
      <!-- 返回按钮 -->
      <el-button  type="text">
        <img src="../assets/img/返回.png" alt="返回" @click="goHome"/>
     </el-button>

      <!-- 创建问卷按钮 -->
        <button class="noUseButton" type="button" @click="CreateOn">
          <img src="../assets/img/创建问卷.png" alt="图片" />
          <p class="bordertext">创建问卷</p>
        </button>

      <!-- 我的问卷按钮 -->
      <div class="usingButton" @click="handleMyQuestionnaireClick">
        <img src="../assets/img/我的问卷.png" alt="图片" />
        <p class="bordertext">我的问卷</p>
      </div>

      <!-- 个人资料按钮 -->
        <button class="noUseButton" type="button" @click="PersonalOn">
          <img src="../assets/img/个人资料.png" alt="图片" />
          <p class="bordertext">个人资料</p>
        </button>

      <!-- 填写记录按钮 -->
        <button class="noUseButton" type="button" @click="RecordOn">
          <img src="../assets/img/填写记录.png" alt="图片" />
          <p class="bordertext">填写记录</p>
        </button>

      <div class="scrollable-container">
        <div v-for="(value,index) in questionnaires" :key="index">
          <div  class="recordMessage">
            <h3 class="Qname">{{value.theme}}</h3>
            <img src="../assets/img/编辑.png" alt="图片" @click="editOn(value)" />
            <el-button type="text" class="edit" @click="editOn(value)">编辑</el-button>
            <img src="../assets/img/删除.png" alt="图片" @click="Delete(value)" />
            <el-button type="text" class="edit" @click="Delete(value)">删除</el-button>
            <img src="../assets/img/发布.png" alt="图片" @click="OnPublishDialog(value)" />
            <el-button type="text" class="edit" @click="OnPublishDialog(value)">发布</el-button>
            <div v-if="publishDialog" class="dialog">
              <div>请输入截止时间</div>
              <el-date-picker v-model="endTime"    type="datetime" /><br>
              <button @click="publish(value)">确定</button>
              <button @click="OffPublishDialog(value)">取消</button>
            </div>
            <img src="../assets/img/截止.png" alt="图片" @click="end(value) "/>
            <el-button type="text" class="edit" @click="end(value)">截止</el-button>
            <div v-if="endDialog" class="dialog">
              <p>问卷截止成功！</p>
              <button @click="closeEndDialog">确定</button>
            </div>
            <img src="../assets/img/查看.png" alt="图片" @click="dataOn(value)" />
            <el-button type="text" class="edit" @click="dataOn(value)">查看</el-button>
            <img src="../assets/img/发布.png" alt="图片" @click="getLink(value)" />
            <el-button type="text" class="edit" @click="getLink(value)">生成链接</el-button>
            <div class="dialog" v-if="showLink">
              <div>链接生成成功！</div>
              {{link}}<br>
              <button @click="linkOff">确定</button>
            </div>
          </div>
          <br><br>
        </div>
      </div>
  </div>
  </div>
    <o-data v-if="showData" :id="questionnaireId" :close="dataOff" :user-id="userId"></o-data>
    <edit v-if="showEdit" :id="questionnaireId" :user-id="userId" :close="editOff"></edit>
    </div>

  <CreateQuestionnaire v-if="showCreate" :user-id="userId" :my="MyOn" :personal-on="PersonalOn" :record-on="RecordOn" :go-home="goHome"></CreateQuestionnaire>
  <PersonalPage v-if="showPersonal" :user-id="userId" :my="MyOn" :create-on="CreateOn" :record-on="RecordOn" :go-home="goHome"></PersonalPage>
  <Record v-if="showRecord" :my="MyOn" :user-id="userId" :create-on="CreateOn" :personal-on="PersonalOn" :go-home="goHome"></Record>
</template>
<script >

import '@vuepic/vue-datepicker/dist/main.css'
import CreateQuestionnaire from "@/components/CreateQuestionnaire.vue";
import PersonalPage from "@/components/PersonalPage.vue";
import Record from "@/components/RecordPage.vue";
import axios from "axios";
import edit from "@/components/editPage.vue";
import oData from "@/components/dataPage.vue";
export default {
  props:['userId','goHome'],
  data(){
    return{
      releaseTime :null,
      endTime : null,
      showCreate:false,
      showPersonal:false,
      showRecord:false,
      showMyQuestionnaire:true,
      questionnaires:[],
      showData :false,
      showEdit:false,
      questionnaireId:null,
      publishDialog: false,
      endDialog:false,
      link:'',
      showLink:false
    }
  },
  components:{
    CreateQuestionnaire,
    PersonalPage,
    Record,
    edit,
   oData
  },
  mounted() {
    this.fetchQuestionnaire()
  },
  methods: {
    async checkUser(userId){
      const response=await axios.get(window.Ip+'/getUserById?userId='+userId);
      console.log(response)
      if(response.data.code===400)
      {
        alert('账号已不存在！')
        return false
      }
      else
        return true
    },
    async getLink(questionnaire){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      if(await this.deletedByManager(questionnaire.id)===-1)
        return
      const response=await axios.get(window.Ip+'/getIp');
      const ip=response.data;
      this.link='http://'+ip+':8080/?questionnaireId='+questionnaire.id;
      console.log(this.link)
      this.showLink=true;
    },
    async linkOff(){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      this.showLink=false;
    },
    async dataOn(questionnaire){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      if(await this.deletedByManager(questionnaire.id)===-1)
        return
      this.questionnaireId=questionnaire.id;
      console.log(questionnaire.id);
      this.showData=true;
    },
    async editOn(questionnaire)
    {
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      if(await this.deletedByManager(questionnaire.id)===-1)
        return
      this.showEdit=true;
      this.questionnaireId=questionnaire.id;
      console.log(questionnaire.id);
    },
    async dataOff(){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      this.showData=false;
    },
    async editOff(){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      this.showEdit=false;
      await this.fetchQuestionnaire();
    },
    async deletedByManager(id)
    {
      const update=await axios.get(window.Ip+'/checkState?id='+id);
      console.log(update)
      if(update.data.code===400)
      {
        if(update.data.data===-1)
        {
          alert('问卷已被系统删除！')
          await this.fetchQuestionnaire()
          return -1;
        }
      }
      else
      {
        return update.data.data;
      }
    },
    async fetchQuestionnaire(){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      this.questionnaires=[];
      const response=await axios.get(window.Ip+'/ListByUser?userId='+this.userId)
      console.log(response)
      const list =response.data.data;
      if(response.data.code===400)
        this.questionnaires=[];
      else
        list.forEach(questionnaire=>{
          this.questionnaires.push(questionnaire)
        })
    },
    async Delete(questionnaire){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      if(await this.deletedByManager(questionnaire.id)===-1)
        return
      const response=await axios.post(window.Ip+'/delete?id='+questionnaire.id)
      console.log(response)
      await this.fetchQuestionnaire();
    },
   async OnPublishDialog(questionnaire){
     if(!await this.checkUser(this.userId))
     {
       this.goHome();
       return
     }
      console.log(questionnaire.questionNum)
      if(!questionnaire.questionNum)
      {
        alert('问卷问题为空！')
        return
      }
      const update=await axios.get(window.Ip+'/checkState?id='+questionnaire.id);
      if(update.data.code===200)
      {
        alert('问卷已发布！')
        return
      }
      else if (update.data.data===-1)
      {
        alert('问卷已被系统删除！')
        await this.fetchQuestionnaire()
        return
      }
      this.publishDialog = true;
    },
    async OffPublishDialog(){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      this.publishDialog = false;
    },
    async publish(questionnaire){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      if(await this.deletedByManager(questionnaire.id)===-1) {
        this.publishDialog = false;
        return
      }
      let commit;
      this.releaseTime=new Date()
      let releaseTime=Date.parse(this.releaseTime),
          endTime=Date.parse(this.endTime)
      if(releaseTime>=endTime)
      {
        alert('发布时间不应小于截止时间！')
        return
      }

      commit={
        "id": questionnaire.id,
        "releaseTime":releaseTime,
        "endTime":endTime
      }
      console.log(commit)
      const response=await axios.post(window.Ip+'/publish?',commit)
      console.log(response)
      this.publishDialog = false;
      await this.fetchQuestionnaire();
    },
    async end(questionnaire){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      if(await this.deletedByManager(questionnaire.id)===-1)
        return
      const update=await axios.get(window.Ip+'/checkState?id='+questionnaire.id);
      console.log(update)
      if(update.data.code===400) {
        if (update.data.data === 0) {
          alert('问卷未发布！')
          return;
        }
        else  if(update.data.data === -1)
        {
          alert('问卷已被系统删除！')
        }
        else {
          alert('问卷已截止！')
          return;
        }
      }
     await axios.post(window.Ip+'/end?id='+questionnaire.id)

      this.endDialog=true;
      await this.fetchQuestionnaire();
    },
    async closeEndDialog(){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      this.endDialog=false;
    },
   async CreateOn(){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      this.showMyQuestionnaire=false,
          this.showRecord=false,
          this.showCreate=true,
          this.showPersonal=false
    },
    async PersonalOn(){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return
      }
      this.showMyQuestionnaire=false,
          this.showRecord=false,
          this.showCreate=false,
          this.showPersonal=true
    },
   async RecordOn(){
     if(!await this.checkUser(this.userId))
     {
       this.goHome();
       return
     }
      this.showMyQuestionnaire=false,
          this.showRecord=true,
          this.showCreate=false,
          this.showPersonal=false
    },
   async MyOn(){
     if(!await this.checkUser(this.userId))
     {
       this.goHome();
       return
     }
      console.log(1);
      this.showMyQuestionnaire=true;
      this.showRecord=false;
          this.showCreate=false;
          this.showPersonal=false;
      await this.fetchQuestionnaire();
    },
  }
};
</script>

<style scoped>
@import '@/css/border.css';
@import '@/css/bordertext.css';
@import '@/css/noUseButton.css';
@import '@/css/usingButton.css';
@import '@/css/buttonHover.css';

.recordMessage {
  left: 0px;
  width: 1400px;
  height: 70px;
  top: 00px;
  background: linear-gradient(135deg, rgba(174, 235, 198, 1) 0%, rgba(111, 227, 158, 0.01) 100%);

}
.scrollable-container  {
  left: 540px;
  height: 1100px;
  top: -1000px;
  width: 1400px;
  position: relative;
  max-height: 120vh;
  overflow-y: auto;
}

.Qname {
  padding-left: 200px;
  padding-top: 10px;
  font-size: 30px;
  margin: 0%;
}
.recordMessage img{
  margin-left: 50px;
}

.recordMessage a {
  padding: 6px 12px;
  font-size: 14px;
  color: #007bff;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.recordMessage a:hover {
  background-color: #e9ecef;
}

body {
  margin-top: 0%;
  margin-left: 0%;
}

.dialog {
  position: fixed;
  z-index: 1000;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.dialog button {
  margin-top: 10px;
  padding: 5px 10px;
}
</style>