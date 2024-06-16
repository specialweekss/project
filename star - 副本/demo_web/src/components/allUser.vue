<template>
      <div class="border" >
        <!-- 返回按钮 -->
        <el-button  type="text">
          <img src="../assets/img/返回.png" alt="返回" @click="goHome"/>
        </el-button>


        <!-- 我的问卷按钮 -->
        <button class="noUseButton" @click="my">
          <img src="@/assets/img/我的问卷.png" alt="图片" />
          <p class="bordertext">用户问卷</p>
        </button>

        <!-- 个人资料按钮 -->
        <button class="noUseButton" type="button" @click="personalOn">
          <img src="../assets/img/个人资料.png" alt="图片" />
          <p class="bordertext">管理员资料</p>
        </button>

        <!-- 填写记录按钮 -->
        <div class="usingButton" >
          <img src="../assets/img/填写记录.png" alt="图片" />
          <p class="bordertext">用户列表</p>
        </div>

        <main>
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
  props:['recordOn','personalOn','my','goHome','userId'],
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
    getUserType(type){
      const types={
        '0':'普通用户',
        '1':'管理员'
      }
      return types[type];
    },

    async fetchQuestionnaire(){
      this.users=[];
      console.log(this.userId)
      const response=await axios.get('http://localhost:8090/UserList')
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
      const response=await axios.post('http://localhost:8090/DeleteUser?userId='+user.userId)
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
  left: 550px;
  top: -700px;
  width: 1400px;
  border-radius: 30px;
  background: linear-gradient(135deg, rgba(174, 235, 198, 1) 0%, rgba(111, 227, 158, 0.01) 100%);
  position: relative;
  display: flex;
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