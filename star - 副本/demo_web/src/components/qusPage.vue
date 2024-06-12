<script>

import axios from "axios";

export default {
  props:["close"],
  data(){
    return{
      questionnaires:[],
      questionnaire:{},
      showInput: false,
      showQuestionnaires:false,
      showDelete:false,
      deleteId:null
    }
  },
  methods: {
    addQuestionnaire(){
      this.showInput=true;
      this.showQuestionnaires=false;
      this.showDelete=false;

    },
    async save() {
      const questionnaireData ={
        "theme": this.questionnaire.theme,
        "creatorId": 222,
      };
      console.log(questionnaireData);
      await axios.post('http://127.0.0.1:8090/save',questionnaireData)
    },
    async getList()
    {
      this.showQuestionnaires=true;
      this.showInput=false;
      this.showDelete=false;
      this.questionnaires=[];
      await axios.get('http://127.0.0.1:8090/List')
          .then(response=>{
            const list=response.data.data;
            list.forEach(questionnaire=>{
              this.questionnaires.push(questionnaire);
            })
          })
    },
    async Delete(){
      this.showDelete=true;
    },
    async confirm(id)
    {
      await axios.post('http://127.0.0.1:8090/delete?id='+id);
      this.getList();
    }

  }
}
</script>

<template>
  <el-button type="primary" @click="close">退出</el-button>
  <el-button type="primary" @click="getList">所有问卷</el-button>
  <el-button type="primary" @click="addQuestionnaire">添加问卷</el-button>
  <div v-if="showQuestionnaires">
    <div v-for="questionnaire in questionnaires"  :key="questionnaire.id">
      <div v-for="(value, key) in questionnaire" :key="key">
        {{key}}:{{value}}
      </div>
      <el-button type="primary" @click="confirm(questionnaire.id)">删除问卷</el-button>
      <br><br>
    </div>
  </div>
  <div v-if="showInput">
    <input v-model="questionnaire.theme" placeholder="问卷标题">
    <el-button type="primary" @click="save">确定</el-button>
  </div>
</template>

<style scoped>

</style>