<template>
  <div v-if="showAllQuestionnaire">
    <div v-if="!showData">
      <div class="border" >

        <el-button  type="text">
          <img src="../assets/img/返回.png" alt="返回" @click="goHome"/>
        </el-button>



        <div class="usingButton" >
          <img src="../assets/img/我的问卷.png" alt="图片" />
          <p class="bordertext">用户问卷</p>
        </div>


        <button class="noUseButton" type="button" @click="ManageOn">
          <img src="../assets/img/个人资料.png" alt="图片" />
          <p class="bordertext">管理员资料</p>
        </button>

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
  </div>

  
  <manage-page v-if="showManage" :user-id="userId" :questionnaire="QuestionnaireOn"  :user-on="UserOn" :go-home="goHome"></manage-page>
  <User v-if="showUser" :questionnaire="QuestionnaireOn" :user-id="userId"  :manage-on="ManageOn" :go-home="goHome"></User>
</template>

<script>

import '@vuepic/vue-datepicker/dist/main.css'
import ManagePage from "@/components/managePage.vue";
import User from "@/components/allUser.vue";
import axios from "axios";
import oData from "@/components/dataPage.vue";
export default {
  props:['userId','goHome'],
  data(){
    return{
      releaseTime : new Date(),
      endTime : null,
      showCreate:false,
      showManage:false,
      showUser:false,
      showAllQuestionnaire:true,
      questionnaires:[],
      questionnaireId:null,
      showData:false
    }
  },
  components:{
    oData,
    ManagePage,
    User,
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
    dataOff(){
      this.showData=false;
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

    ManageOn(){
      this.showAllQuestionnaire=false,
          this.showUser=false,
          this.showManage=true
    },
   UserOn(){
      this.showAllQuestionnaire=false,
          this.showUser=true,
          this.showManage=false
    },
    QuestionnaireOn(){
      console.log(1);
      this.showAllQuestionnaire=true;
      this.showUser=false
      this.showManage=false;
      this.fetchQuestionnaire();
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



.dialog button {
  margin-top: 10px;
  padding: 5px 10px;
}
</style>