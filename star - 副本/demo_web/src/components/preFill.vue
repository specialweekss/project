<script>
import fillPage from "@/components/fillPage.vue";
export default {
  data() {
    return {
      userId: null,
      id: null,
      showFill:false
    }
  },
  components:{
    fillPage
  },
  methods:{
    fillOn(){
      this.showFill=true;
    },
    filloff(){
      this.showFill=false;
    }
}
  }
</script>

<template>
<div v-if="!showFill">
  <h2>请输入账号</h2>
  <input class="inputstring" type="text" placeholder="账号" v-model="userId"/>
  <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
  <h2>请输入ID</h2>
  <input class="inputstring" type="text" placeholder="问卷ID" v-model="id"/>
  <button id="B" @click="fillOn">保存</button>
</div>
  <fill-page :user-id="userId" :id="id" :close="filloff" v-else></fill-page>
</template>

<style scoped>
@import '@/css/inputstring.css';

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
</style>