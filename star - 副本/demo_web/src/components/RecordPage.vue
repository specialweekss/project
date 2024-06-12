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
    <main>
      <div v-for="(value,index) in records" :key="index">
      <div class="recordMessage">

        <p class="Qname">问卷id：{{value.questionnaireId}}</p>
        <img src="@/assets/img/查看.png" alt="图片" @click="handleCheckClick" />
        <a class="check" @click="recordOn(value)">查看</a>
      </div>
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
export default {
  props:['createOn','personalOn','my','goHome','userId'],
  data(){
    return {
      records: [],
      showRecord:false,
      questionnaireId:null
    }
  },
  components:{
    recordOne
  },
  mounted() {
this.fetchRecord()
  },
  methods: {
    async fetchRecord(){
      this.records=[];
      const response=await axios.get('http://localhost:8090/ListRecordByUser?userId='+this.userId);

      const list =response.data.data;
      if(response.data.code===400)
        this.records=[];
      else {
        list.forEach(record => {
          this.records.push(record);
        })
      }
      console.log(this.records);
    },
    recordOn(value)
    {
      this.questionnaireId=value.questionnaireId
      this.showRecord=true;
    },
    recordoff()
    {
      this.showRecord=false;
    }
  }
}
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
}

.Qname {
  padding-left: 200px;
  padding-top: 10px;
  font-size: 30px;
}

.check {
  padding-top: 20px;
  width: 60px;
  font-size: 30px;
  color: rgba(121, 72, 234, 1);
}

main img {
  padding-left: 200px;
  padding-top: 20px;
}

body {
  margin-top: 0%;
  margin-left: 0%;
}

main a:hover {
  background-color: bisque;
}
</style>