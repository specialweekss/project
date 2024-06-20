<script>
import fillPage from "@/components/fillPage.vue";
import axios from "axios";
export default {
  props:['userId','goHome','questionnaireId'],
  data() {

    return {
      id: null,
      showFill:false
    }
  },
  mounted() {
    this.getQuestionnaire()
  },
  components:{
    fillPage
  },
  methods:{
    getQuestionnaire(){
      this.id=this.questionnaireId
      if(this.id)
      {
          this.showFill=true;
      }
    },
    async fillOn(){
      const state=(await axios.get('http://localhost:8090/checkState?id=' + this.id)).data.data
      if(state===0)
      {
        alert('问卷不存在或尚未发布！')
        return
      }
      if(state===2)
      {
        alert('问卷已截止！')
        return false
      }
      this.showFill=true;
      return  true
    },
    filloff(){
      this.showFill=false;
    }
}
  }
</script>

<template>
    <!-- 返回按钮 -->

<div v-if="!showFill" >
  <br>
  <el-button  type="text">
    <img src="../assets/img/返回.png" alt="返回" @click="goHome"/>
  </el-button>
  <div class="center">
  <h2>请输入问卷ID</h2>
  <input class="inputstring" type="text" placeholder="问卷ID" v-model="id"/><br>
  <button id="B" @click="fillOn">保存</button>
  </div>
</div>
  <fill-page :user-id="userId" :id="id" :close="filloff" v-else></fill-page>
</template>

<style scoped>
@import '@/css/inputstring.css';
@import '@/css/border.css';
@import '@/css/bordertext.css';
@import '@/css/noUseButton.css';
@import '@/css/usingButton.css';
@import '@/css/buttonHover.css';
#B{
  top: 300px;
  width: 164px;
  height: 89px;
  border-radius: 30px;
  border: 1px solid rgba(255, 255, 255, 1);
  position: relative;
  font-size: 36px;
  font-weight: 400;
  letter-spacing: 0px;
  line-height: 52.13px;
  color: rgba(0, 0, 0, 1);
  text-align: center;
  vertical-align: top;
  background: rgba(199, 84, 85, 1);
}
div button:hover{
  background-color: aqua;
}
.center{
  text-align: center;
}
</style>