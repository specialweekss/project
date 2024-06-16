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

      <main>
        <div v-for="(value,index) in questionnaires" :key="index">
          <div class="recordMessage" >
            <h3 class="Qname">{{value.theme}}</h3>
            <img src="../assets/img/编辑.png" alt="图片" @click="editOn(value)" />
            <el-button type="text" class="edit" @click="editOn(value)">编辑</el-button>
            <img src="../assets/img/删除.png" alt="图片" @click="Delete(value)" />
            <el-button type="text" class="edit" @click="Delete(value)">删除</el-button>
            <img src="../assets/img/发布.png" alt="图片" @click="OnPublishDialog" />
            <el-button type="text" class="edit" @click="OnPublishDialog">发布</el-button>
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
      </main>
  </div>
  </div>
    <o-data v-if="showData" :id="questionnaireId" :close="dataOff" ></o-data>
    <edit v-if="showEdit" :id="questionnaireId" :creator-id="userId" :close="editOff"></edit>
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
      releaseTime : new Date(),
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
    async getLink(questionnaire){
      const response=await axios.get('http://localhost:8090/getIp');
      const ip=response.data;
      this.link='http://'+ip+':8081/?questionnaireId='+questionnaire.id;
      console.log(this.link)
      this.showLink=true;
    },
    linkOff(){
      this.showLink=false;
    },
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
      const response=await axios.get('http://localhost:8090/ListByUser?userId='+this.userId)
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
    OnPublishDialog(){
      this.publishDialog = true;
    },
    OffPublishDialog(){
      this.publishDialog = false;
    },
    async publish(questionnaire){
      let commit;
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
      const response=await axios.post('http://localhost:8090/publish?',commit)
      console.log(response)
      this.publishDialog = false;
    },
    async end(questionnaire){
      if(questionnaire.state===0)
      {
        alert('问卷未发布！')
        return;
      }
      const response=await axios.post('http://localhost:8090/end?id='+questionnaire.id)
      console.log(response)
      this.endDialog=true;
    },
    closeEndDialog(){
      this.endDialog=false;
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
          this.showCreate=false,
          this.showPersonal=true
    },
    RecordOn(){
      this.showMyQuestionnaire=false,
          this.showRecord=true,
          this.showCreate=false,
          this.showPersonal=false
    },
    MyOn(){
      console.log(1);
      this.showMyQuestionnaire=true;
      this.showRecord=false;
          this.showCreate=false;
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
  top: -800px;
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