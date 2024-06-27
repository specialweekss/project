<template>
  <div id="app">
    <header id="reg_header">
      <div class="reg_h_center">
        <div class="reg_h_left">
          <h3>欢迎注册</h3>
        </div>
      </div>
    </header>

    <div class="reg_back">
      <div class="reg_left">
        <p>新用户注册</p>
        <p>USER REGISTER</p>
      </div>
      <div class="reg_center">
        <div class="reg_form">
          <form @submit.prevent="register">
            <table>
              <tr>
                <td class="td_left"><label for="username">昵称</label></td>
                <td class="td_right">
                  <input type="text" v-model="username" placeholder="请输入昵称" id="username">
                </td>
              </tr>
              <tr>
                <td class="td_left"><label for="password">密码</label></td>
                <td class="td_right">
                  <input type="password" v-model="password" placeholder="请输入密码" id="password">
                </td>
              </tr>

              <tr>
                <td colspan="2" >
                  <input type="submit" value="注册" id="btn_sub">
                </td>
              </tr>
            </table>
            <div  class="node">(密码格式需包含大写字母，小写字母，数字)</div>
          </form>
        </div>
      </div>
      <div class="reg_right">
        <p>已有账号?<a @click="goHome">立即登录</a></p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  props:['goHome'],
  data() {
    return {
      username: '',
      password: '',
      userId:''
    };
  },
  methods: {
   async register() {
     console.log(window.Ip)
      const response=await axios.post(window.Ip+'/Register?username='+this.username+'&password='+this.password)
     console.log(response.data)

    if(response.data.code===200) {
      this.userId=response.data.data
      alert('注册成功,账号ID为'+this.userId+'  账号密码为'+this.password
      )
      this.goHome()}
     else if(response.data.data===0)
       alert('密码格式错误！')
     else if(response.data.data===-1)
       alert('请将昵称和密码全部输入！')
     else
       alert('昵称已存在！')
   }
  }
};
</script>

<style scoped>
*{
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
#reg_header{
  height: 110px;
  width: 100%;
  box-shadow: 0px 0px 10px orange;
}

#reg_header .reg_h_center{
  width: 1200px;
  height: 110px;
  margin: 0 auto;
  display: flex;
}
#reg_header .reg_h_left{
  height: 110px;
  flex: 1;
}

.reg_back {
  width: 850px;
  height: 400px;
  border: 8px solid #eeeeee;
  background: white;
  margin: auto;
  margin-top: 20px;
}

.reg_left {
  float: left;
  margin: 10px;
}

.reg_left > p:first-child {
  color: gray;
  font-size: 20px;
}

.reg_left > p:last-child {
  color: #A6A6A6;
  font-size: 20px;
}

.reg_center {
  float: left;
  width: 450px;
}

.reg_right {
  float: right;
  margin: 30px;
}



.reg_right p a {
  color: crimson;
}

.td_left {
  width: 100px;
  text-align: right;
  height: 40px;
}

.td_right {
  padding-left: 40px;
}

#username ,#password{
  width: 200px;
  height: 30px;
  border: 1px solid #A4A4A4;
  border-radius: 8px;
  padding-left: 10px;
}

#btn_sub{
  margin-left: 150px;
  margin-top: 60px;
  background: orangered;
  color: whitesmoke;
  width: 150px;
  height: 40px;
}
.node{
  margin-left: 70px;
  margin-top: -100px;
}
</style>