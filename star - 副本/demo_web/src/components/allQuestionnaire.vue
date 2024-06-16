<template>
  <div v-if="showMyQuestionnaire">
    <div v-if="!showEdit&!showData" >
      <div class="border" >
        <!-- 返回按钮 -->
        <el-button  type="text">
          <img src="../assets/img/返回.png" alt="返回" @click="goHome"/>
        </el-button>


        <!-- 我的问卷按钮 -->
        <div class="usingButton" >
          <img src="../assets/img/我的问卷.png" alt="图片" />
          <p class="bordertext">用户问卷</p>
        </div>

        <!-- 个人资料按钮 -->
        <button class="noUseButton" type="button" @click="PersonalOn">
          <img src="../assets/img/个人资料.png" alt="图片" />
          <p class="bordertext">管理员资料</p>
        </button>

        <!-- 填写记录按钮 -->
        <button class="noUseButton" type="button" @click="UserOn">
          <img src="../assets/img/填写记录.png" alt="图片" />
          <p class="bordertext">用户列表</p>
        </button>

        <main>
          <div v-for="(value,index) in questionnaires" :key="index">
            <div class="recordMessage" >
              <h3 class="Qname">{{value.theme}}</h3>
              <img src="../assets/img/删除.png" alt="图片" @click="Delete(value)" />
              <el-button type="text" class="edit" @click="Delete(value)">删除</el-button>
              <img src="../assets/img/查看.png" alt="图片" @click="dataOn(value)" />
              <el-button type="text" class="edit" @click="dataOn(value)">查看</el-button>
            </div>
            <br><br>
          </div>
        </main>
      </div>
    </div>
    <o-data v-if="showData" :id="questionnaireId" :close="dataOff" ></o-data>
    <edit v-if="showEdit" :id="questionnaireId" :creator-id="userId" :close="editOff"></edit>
  </div>

  <CreateQuestionnaire v-if="showCreate" :user-id="userId" :my="MyOn" :personal-on="PersonalOn" :record-on="UserOn" :go-home="goHome"></CreateQuestionnaire>
  <manage-page v-if="showPersonal" :user-id="userId" :my="MyOn" :create-on="CreateOn" :record-on="UserOn" :go-home="goHome"></manage-page>
  <User v-if="showUser" :my="MyOn" :user-id="userId" :create-on="CreateOn" :personal-on="PersonalOn" :go-home="goHome"></User>
</template>

<script>

import '@vuepic/vue-datepicker/dist/main.css'
import CreateQuestionnaire from "@/components/CreateQuestionnaire.vue";
import ManagePage from "@/components/managePage.vue";
import User from "@/components/allUser.vue";
import axios from "axios";
import edit from "@/components/editPage.vue";
import oData from "@/components/dataPage.vue";
export default {
  props:['userId','goHome'],
  data(){
    return{
      releaseTime : new Date(),
      endTime : null,
      showCreate:false,
      showPersonal:false,
      showUser:false,
      showMyQuestionnaire:true,
      questionnaires:[],
      showData :false,
      showEdit:false,
      questionnaireId:null,
      publishDialog: false,
      endDialog:false
    }
  },
  components:{
    CreateQuestionnaire,
    ManagePage,
    User,
    edit,
    oData
  },
  mounted() {
    this.fetchQuestionnaire()
  },
  methods: {
    dataOn(questionnaire){
      this.questionnaireId=questionnaire.id;
      console.log(questionnaire.id);
      this.showData=true;
    },
    editOn(questionnaire)
    {
      this.showEdit=true;
      this.questionnaireId=questionnaire.id;
      console.log(questionnaire.id);
    },
    dataOff(){
      this.showData=false;
    },
    editOff(){
      this.showEdit=false;
    },
    async fetchQuestionnaire(){
      this.questionnaires=[];
      console.log(this.userId)
      const response=await axios.get('http://localhost:8090/List')
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
      const response=await axios.post('http://localhost:8090/delete?id='+questionnaire.id)
      console.log(response)
      await this.fetchQuestionnaire();
    },
    async publish(questionnaire){
      let commit;
      let releaseTime=new Date(this.releaseTime),
          endTime=new Date(this.endTime)
      if(releaseTime>=endTime)
      {
        alert('发布时间不应小于截止时间！')
        return
      }
      else if(releaseTime.getDate()<new Date().getDate())
      {
        alert('发布时间不可选择过去的时间!')
        return
      }
      commit={
        "id": questionnaire.id,
        "releaseTime":releaseTime,
        "endTime":endTime
      }
      console.log(this.releaseTime)
      console.log(commit)
      const response=await axios.post('http://localhost:8090/publish?',commit)
      console.log(response)
      this.publishDialog = false;
    },

    CreateOn(){
      this.showMyQuestionnaire=false,
          this.showRecord=false,
          this.showCreate=true,
          this.showPersonal=false
    },
    PersonalOn(){
      this.showMyQuestionnaire=false,
          this.showRecord=false,
          this.showUser=false,
          this.showCreate=false,
          this.showPersonal=true
    },
   UserOn(){
      this.showMyQuestionnaire=false,
          this.showUser=true,
          this.showCreate=false,
          this.showPersonal=false
    },
    MyOn(){
      console.log(1);
      this.showMyQuestionnaire=true;
      this.showRecord=false;
      this.showCreate=false;
      this.showUser=false
      this.showPersonal=false;
      this.fetchQuestionnaire();
    },
    handleMyQuestionnaireClick() {
      console.log("我的问卷被点击");
    }
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
  left: 550px;
  top: -700px;
  width: 1400px;
  border-radius: 30px;
  background: linear-gradient(135deg, rgba(174, 235, 198, 1) 0%, rgba(111, 227, 158, 0.01) 100%);
  position: relative;
  display: flex;
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