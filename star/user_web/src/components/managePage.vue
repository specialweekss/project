<template>
  <div>
    <div class="border">

      <el-button  type="text">
        <img src="../assets/img/返回.png" alt="返回" @click="goHome"/>
      </el-button>

      <button class="noUseButton" @click="questionnaire">
        <img src="@/assets/img/我的问卷.png" alt="图片" />
        <p class="bordertext">用户问卷</p>
      </button>

      <div class="usingButton" >
        <img src="@/assets/img/个人资料.png" alt="图片" />
        <p class="bordertext">管理员资料</p>
      </div>
      <button class="noUseButton" @click="userOn">
        <img src="@/assets/img/填写记录.png" alt="图片" />
        <p class="bordertext">用户列表</p>
      </button>
    </div>
    <main>
      <p id="P" class="p2">账号:{{userId}}</p>
      <br/><br/>
      <p id="P" class="p1">用户名</p>
      <input id="I" class="i1" type="text" placeholder="用户名" v-model="name" />
      <p id="P" class="p3">密码</p>
      <input id="I" class="i3" type="text" placeholder="密码" v-model="password" />
      <button id="B" class="b1" @click="saveData">保存</button>
      <button id="B" class="b2" @click="logout">注销</button>
      <button id="B" class="b3" @click="goHome">退出登录</button>
    </main>
  </div>
</template>

<script>
import axios from "axios";
import myQuestionnaire from "@/components/MyQuestionnaire.vue";
export default {
  props:['questionnaire','userOn','goHome','userId'],
  data() {
    return {
      name: '',
      password: '',
      user:[]
    };
  },
  mounted() {
    this.fetchUser();
  },
  methods: {
    async fetchUser(){
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.goHome();
        return;
      }
      const response=await axios.get(window.Ip+'/getUserById?userId='+this.userId)
      this.user=response.data.data;
      this.name=response.data.data.name;
      this.password=response.data.data.password;
    },
    async saveData() {
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.goHome();
        return;
      }
      const response=await  axios.post(window.Ip+'/ModUser?userName='+this.name+'&password='+this.password+'&userId='+this.userId)
      console.log(response);
      if(response.data.code===400)
      {
        if(response.data.data===-1)
        {
          alert('请将昵称和密码填写完毕！')
          return
        }
        else if(response.data.data===1)
        {

          alert('重复昵称!')
          return
        }
        else
        {
          alert('密码不合规！应同时包含大小写字母和数字！')
          return
        }
      }
      await this.fetchUser();
    },
    async logout() {
      if(!await myQuestionnaire.methods.checkUser(this.userId))
      {
        this.goHome();
        return;
      }
      const response=await  axios.post(window.Ip+'/logOut?userId='+this.userId)
      console.log(response);
      this.goHome()
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

#P {
  top:-1000px;
  left: 600px;
  width: 197px;
  height: 0px;
  opacity: 1;
  font-family: "Noto Sans SC";
  font-size: 48px;
  font-weight: 400;
  letter-spacing: 0px;
  line-height: 69.5px;
  color: rgba(0, 0, 0, 1);
  text-align: left;
  vertical-align: top;
  position: relative;
  margin-top: 20px;
}

#I {
  top:-1070px;
  left: 780px;
  width: 746px;
  height: 117px;
  border-radius: 20px;
  font-family: "Noto Sans SC";
  font-size: 36px;
  font-weight: 400;
  background: rgba(229, 229, 229, 1);
  letter-spacing: 0px;
  line-height: 52.13px;
  color: rgba(122, 113, 113, 1);
  text-align: left;
  vertical-align: top;
  position: relative;
  margin-top: 10px;
}

#B{
  top: -800px;
  width: 164px;
  height: 89px;
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 1);
  position: relative;
  font-family: "Noto Sans SC";
  font-size: 36px;
  font-weight: 400;
  letter-spacing: 0px;
  line-height: 52.13px;
  color: rgba(0, 0, 0, 1);
  text-align: center;
  vertical-align: top;
}
.b1{
  left: 0%;
  background: rgba(84, 170, 199, 1);
}
.b2{
  left:6%;
  background: rgba(199, 84, 85, 1);
}
.b3{
  left:12%;
  background: rgba(199, 84, 199, 1);
}
body{
  margin-top: 0%;
  margin-left: 0%;
}
main button:hover{
  background-color: aqua;
}
</style>