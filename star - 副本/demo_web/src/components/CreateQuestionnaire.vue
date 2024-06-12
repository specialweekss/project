<template>
  <div class="border">
    <!-- 返回按钮 -->
    <el-button  type="text" @click="goHome">
    <img src="../assets/img/返回.png" alt="返回" />
   </el-button>

    <!-- 创建问卷区域 -->
    <div class="create-questionnaire">
      <div class="usingButton" @click="createQuestionnaire">
        <img src="@/assets/img/创建问卷.png" alt="图片" />
        <p class="bordertext">创建问卷</p>
      </div>
    </div>

    <!-- 我的问卷按钮 -->
      <button class="noUseButton" @click="my">
        <img src="@/assets/img/我的问卷.png" alt="图片" />
        <p class="bordertext">我的问卷</p>
      </button>

    <!-- 个人信息按钮 -->
      <button class="noUseButton" @click="personalOn">
        <img src="@/assets/img/个人资料.png" alt="图片" />
        <p class="bordertext">个人资料</p>
      </button>

    <!-- 填写记录按钮 -->
      <button class="noUseButton" @click="recordOn">
        <img src="@/assets/img/填写记录.png" alt="图片" />
        <p class="bordertext">填写记录</p>
      </button>


    <!-- 主体内容区域 -->
    <main>
      <p>创建问卷</p>
      <input type="text" placeholder="问卷名称" v-model="questionnaireTitle" />
      <button @click="saveQuestionnaire">保存</button>
    </main>
  </div>
</template>

<script>
import axios from "axios";
export default {
  props:['recordOn','personalOn','my','goHome','userId'],
  data() {
    return {
      questionnaireTitle: '', // 绑定问卷标题的输入值
    };
  },
  methods: {
    createQuestionnaire() {
      // 逻辑处理，例如弹窗提示创建问卷的功能尚未实现
      alert('创建问卷功能即将上线！');
    },
    async saveQuestionnaire() {
      // 模拟保存问卷标题的逻辑
      if (this.questionnaireTitle.trim() !== '') {
        console.log('保存问卷标题:', this.questionnaireTitle);
        const response = await axios.post('http://localhost:8090/save',{
          "theme":this.questionnaireTitle,
          "creatorId":this.userId
        });
        console.log(response);
        this.my();
      } else {
        alert('请输入问卷标题！');
      }
    },
  },
};
</script>

<style scoped>
@import '@/css/border.css';
@import '@/css/bordertext.css';
@import '@/css/noUseButton.css';
@import '@/css/usingButton.css';
@import '@/css/buttonHover.css';

main p {
  left: 600px;
  top: -700px;
  width: 197px;
  height: 51px;
  font-size: 48px;
  font-weight: 400;
  letter-spacing: 0;
  line-height: 69.5px;
  color: rgba(0, 0, 0, 1);
  text-align: left;
  vertical-align: top;
  position: relative;
}

main input {
  left: 850px;
  top: -820px;
  width: 746px;
  height: 117px;
  border-radius: 20px;
  font-size: 36px;
  font-weight: 400;
  background: rgba(229, 229, 229, 1);
  letter-spacing: 0;
  line-height: 52.13px;
  color: rgba(122, 113, 113, 1);
  text-align: left;
  vertical-align: top;
  position: relative;
}

main button {
  top: -600px;
  width: 164px;
  height: 89px;
  border-radius: 30px;
  background: rgba(84, 170, 199, 1);
  position: relative;
  border: 1px solid rgba(255, 255, 255, 1);
  left: 1100px;
  font-size: 36px;
  font-weight: 400;
  letter-spacing: 0;
  line-height: 52.13px;
  color: rgba(0, 0, 0, 1);
  text-align: center;
  vertical-align: top;
}

main button:hover{
  background-color: aquamarine;
}
body {
  margin-top: 0;
  margin-left: 0;
}
</style>