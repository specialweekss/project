<template>
  <div class="page" v-if="!showMy&&!showManage">
    <h1 id="welcome" >欢迎登录</h1>
    <img src="@/assets/img/登录图片.png" alt="图片" class="logo-image" />
    <div class="denglukuang">
      <input class="inputstring" type="text" placeholder="账号" v-model="userId" />
      <br/><br/>
      <input class="inputstring" type="password" placeholder="密码" v-model="password" />
      <button class="button" type="button" @click="login">登录</button>
    </div>
  </div>

  <my-questionnaire v-if="showMy" :user-id="userId" :go-home="goHome"></my-questionnaire>
  <manage v-if="showManage" :user-id="userId" :go-home="goHome"></manage>
</template>

<script>
import myQuestionnaire from "@/components/MyQuestionnaire.vue";
import axios from "axios";
import manage from "@/components/allQuestionnaire.vue";
export default {
  data() {
    return {
      password: '',
      showMy:false,
      showManage:false,
      userId:null,
      type:null
    };
  },
  components:{
    myQuestionnaire,
    manage
  },
  methods: {
    goHome(){
      this.showMy=false
      this.showManage=false
    },
   async login() {
      const response=await axios.get('http://localhost:8090/LogIn?password='+this.password+'&userId='+this.userId)
      this.type=response.data.data.type
     console.log(response);
      if(response.data.code===400){ alert('账号或密码错误！');}
      else if(this.type===0)
      this.showMy=true;
      else
        this.showManage=true;
      // 这里可以使用axios或fetch等发起登录请求到后端接口
    },
  },
};
</script>

<style scoped>
@import '@/css/inputstring.css';
#welcome {
  left: 100px;
  top: 82px;
  width: 309px;
  height: 117px;
  border: 1px solid rgba(209, 232, 220, 1);
  text-shadow: 0px 2px 4px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(4px);
  font-size: 72px;
  font-weight: 400;
  letter-spacing: 0;
  line-height: 99.79px;
  color: rgba(255, 235, 59, 1);
  text-align: left;
  vertical-align: top;
}

input{
  margin: 0;
  padding: 0;
}
img {
  position: absolute;
  left: 948px;
  top: 188px;
  width: 972px;
  height: 774px;
}

.denglukuang {
  position: absolute;
  left: 36px;
  top: 233px;
  width: 867px;
  height: 729px;
  border-radius: 90px;
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(0, 0, 0, 1);
}

.button{
  left: 47px;
  top: 269px;
  width: 746px;
  height: 117px;
  border-radius: 20px;
  background: linear-gradient(180deg, rgba(161, 140, 209, 1) 0%, rgba(251, 194, 235, 1) 100%);
  box-shadow: 0px 2px 4px  rgba(0, 0, 0, 0.25);
  position: relative;
  font-size: 60px;
  font-weight: 400;
  letter-spacing: 0px;
  line-height: 86.88px;
  color: rgba(242, 220, 220, 1);
  text-align: center;
  vertical-align: top;
}
button:hover {
  background: linear-gradient(180deg, rgba(39, 159, 245, 1) 0%, rgba(55, 13, 191, 1) 10%);
}
input {
  margin: 0;
  padding: 0;
}
.page{
  position: fixed;
  height: 100%;
  width: 100%;
  background-color: rgba(152, 137, 185, 1);
}
</style>
