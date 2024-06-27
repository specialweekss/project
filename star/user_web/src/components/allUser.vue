<template>
      <div class="border" >

        <el-button  type="text">
          <img src="../assets/img/返回.png" alt="返回" @click="goHome"/>
        </el-button>



        <button class="noUseButton" @click="questionnaire">
          <img src="@/assets/img/我的问卷.png" alt="图片" />
          <p class="bordertext">用户问卷</p>
        </button>

      <button class="noUseButton" type="button" @click="manageOn">
          <img src="../assets/img/个人资料.png" alt="图片" />
          <p class="bordertext">管理员资料</p>
        </button>

        <div class="usingButton" >
          <img src="../assets/img/填写记录.png" alt="图片" />
          <p class="bordertext">用户列表</p>
        </div>

        <main class="scrollable-container">
          <div v-for="(value,index) in users" :key="index">
            <div class="recordMessage" >
              <h3 class="Qname">用户id：{{value.userId}} <br> 用户名：{{value.name}}   <br>用户类型：{{getUserType(value.type)}}</h3>
              <div v-if="value.type===0">
              <img src="../assets/img/删除.png" alt="图片" @click="Delete(value)" />
              <el-button type="text" class="edit" @click="Delete(value)">删除</el-button>
              </div>
            </div>
            <br><br>
          </div>
        </main>
      </div>
</template>

<script>

import '@vuepic/vue-datepicker/dist/main.css'
import axios from "axios";
export default {
  props:['manageOn','questionnaire','goHome','userId'],
  data(){
    return{
      users:[],
      deletedUserId:[],
    }
  },
  components:{
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
        if(userId!==this.userId)
        alert('用户账号已不存在！')
        else 
          alert('管理员账号已不存在！')
        return false
      }
      else
        return true
    },
    getUserType(type){
      const types={
        '0':'普通用户',
        '1':'管理员'
      }
      return types[type];
    },

    async fetchQuestionnaire(){
      if(!await this.checkUser(this.userId))
      {
        this.goHome();
        return;
      }
      this.users=[];
      console.log(this.userId)
      const response=await axios.get(window.Ip+'/UserList')
      console.log(response)
      const list =response.data.data;
      if(response.data.code===400)
        this.users=[];
      else
        list.forEach(user=>{
          console.log(this.userId)

          if(user.userId!==this.userId) {
            console.log(user.userId)
            if (user.type === 0)
              this.users.push(user)
            else
              this.users.unshift(user)
          }
        })
    },
    async Delete(user){
      if(!await this.checkUser(user.userId))
      {
        await this.fetchQuestionnaire();
        return;
      }
      const response=await axios.post(window.Ip+'/DeleteUser?userId='+user.userId)
      console.log(response)
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
  top: 00px;
  width: 1400px;
  border-radius: 30px;
  background: linear-gradient(135deg, rgba(174, 235, 198, 1) 0%, rgba(111, 227, 158, 0.01) 100%);
  position: relative;
  display: flex;
}
.scrollable-container  {
  left: 540px;
  height: 1100px;
  top: -750px;
  width: 1400px;
  position: relative;
  max-height: 120vh;
  overflow-y: auto;
}
.Qname {
  padding-left: 50px;
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