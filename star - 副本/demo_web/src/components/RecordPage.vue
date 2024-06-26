<template>

  <div v-if="!showRecord">
    <div class="border">
      <!-- 返回按钮 -->
      <el-button  type="text">
        <img src="../../../../../../微信文件/WeChat%20Files/wxid_t0cfs5oik6xb22/FileStorage/File/2024-06/src/assets/img/返回.png" alt="返回" @click="goHome"/>
      </el-button>

      <!-- 创建问卷按钮 -->
        <button class="noUseButton" @click="createOn">
          <img src="@/assets/img/创建问卷.png" alt="图片" />
          <p class="bordertext">创建问卷</p>
        </button>

      <!-- 我的问卷按钮 -->
        <button class="noUseButton" @click="my">
          <img src="@/assets/img/我的问卷.png" alt="图片" />
          <p class="bordertext">我的问卷</p>
        </button>

      <!-- 个人资料按钮 -->
        <button class="noUseButton" @click="personalOn">
          <img src="@/assets/img/个人资料.png" alt="图片" />
          <p class="bordertext">个人资料</p>
        </button>

      <!-- 填写记录按钮 -->
      <div class="usingButton">
        <img src="@/assets/img/填写记录.png" alt="图片" />
        <p class="bordertext">填写记录</p>
      </div>
    </div>
    <main class="scrollable-container">
      <div v-for="(value,index) in records" :key="index">
        <div v-if="value.isDeleted===0">
      <div class="recordMessage">
        <div class="Qname">问卷id：{{value.questionnaireId}} <br>
        问卷名：{{value.theme}}<br>
          填写日期：{{value.recordTime}}
        </div>
        <img src="../assets/img/删除.png" alt="图片" @click="Delete(value)" />
        <div class="check" @click="Delete(value)">删除</div>
        <img src="@/assets/img/查看.png" alt="图片" @click="recordOn(value.questionnaireId)" />
        <div class="check" @click="recordOn(value.questionnaireId)">查看</div>
      </div>
      </div>
        <br><br>
      </div>
    </main>
  </div>
  <div v-else>
    <record-one :close='recordoff' :id="questionnaireId" :user-id="userId"></record-one>
  </div>
</template>

<script>
import axios from "axios";
import recordOne from "@/components/recordOnePage.vue";
import myQuestionnaire from "@/components/MyQuestionnaire.vue";
export default {
  props:['createOn','personalOn','my','goHome','userId'],
  data(){
    return {
      records: [],
      showRecord:false,
      questionnaireId:null,
    }
  },
  components:{
    recordOne
  },
  mounted() {
this.fetchRecord()
  },
  methods: {
    async Delete(record){
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.goHome();
        return;
      }

      const response=await axios.post('http://localhost:8090/deleteRecord?recordId='+record.recordId);
      console.log(response);
      await this.fetchRecord()

    },
    async fetchRecord(){
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.goHome();
        return;
      }
      this.records=[];
      const response=await axios.get(window.Ip+'/ListRecordByUser?userId='+this.userId);

      const list =response.data.data;
      if(response.data.code===400)
        this.records=[];
      else {
        list.forEach(record => {
          let time=new Date( record.recordTime)
         record.recordTime=time.getFullYear()+'-'+time.getMonth()+'-'+time.getDate()+' '+time.getHours()+':'+time.getUTCMinutes()+':'+time.getSeconds()
          this.records.push(record);
          
        })
      }
      console.log(this.records);
    },
    async deletedByManager(id)
    {

      const update=await axios.get(window.Ip+'/checkState?id='+id);
      console.log(update)
      if(update.data.code===400)
      {
        if(update.data.data===-1)
        {
          alert('问卷已被系统删除！仅记录时间与问卷名保留')
          await this.fetchRecord()
          return -1;
        }
      }
      else
      {
        return update.data.data;
      }
    },
    async recordOn(questionnaireId)
    {
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.goHome();
        return;
      }
      if(await this.deletedByManager(questionnaireId)===-1) {

        return
      }
      const response=await axios.get(window.Ip+'/getById?id='+questionnaireId)
      console.log(response.data.data)
       if(response.data.data.questionNum===0)
       {
         alert('问卷问题已被全部删除！')
         return;
       }
      this.questionnaireId=questionnaireId
      this.showRecord=true;
    },
   async  recordoff()
    {
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.goHome();
        return;
      }
      this.showRecord=false;
    },
  }
}
</script>

<style scoped>
@import '@/css/border.css';
@import '@/css/bordertext.css';
@import '@/css/noUseButton.css';
@import '@/css/usingButton.css';
@import '@/css/buttonHover.css';
.scrollable-container  {
  left: 540px;
  height: 1100px;
  top: -1100px;
  width: 1400px;
  position: relative;
  max-height: 120vh;
  overflow-y: auto;
}
.recordMessage {
  left: 0px;
  top: 00px;
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
}

.check {
  padding-top: 70px;
  width: 70px;
  font-size: 30px;
  color: rgba(121, 72, 234, 1);
}

main img {
  padding-left: 200px;
  padding-top: 70px;
}

body {
  margin-top: 0%;
  margin-left: 0%;
}

main a:hover {
  background-color: bisque;
}
</style>