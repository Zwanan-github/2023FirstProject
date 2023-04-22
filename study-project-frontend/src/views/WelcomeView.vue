<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1;width: 65vw;">
      <el-image style="width: 100%; height: 100%"
                src="https://ts1.cn.mm.bing.net/th/id/R-C.b233cea1db287ea1ca3e1888da90e6f4?rik=iYNZ47%2bCO2FY1g&riu=http%3a%2f%2fimg.mm4000.com%2ffile%2f4%2f6a%2f1f9bd1c552.jpg&ehk=CXpqivABe8%2bwJCsTp0cfer%2fiZCSuRYGXfLGmLH6kKlk%3d&risl=&pid=ImgRaw&r=0">
      </el-image>
    </div>
    <div class="welcome-title">
      <div>欢迎来到我们的学习平台</div>
      <div>Java boy</div>
      <div>@白马赞助推出</div>
    </div>
    <div style="width: 35vw; background-color: white; z-index: 1">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component" style="height: 100%"/>
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}

</style>