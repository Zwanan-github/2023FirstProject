<template>
  <div>
    欢迎进入学习平台
  </div>
  <div>
    <el-button @click="logout()" type="danger" plain>退出登录</el-button>
  </div>
</template>

<script setup>
import {get} from "@/js";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useStore} from "@/stores/counter";

const store = useStore();

const logout = () => {
  get("/api/auth/logout", (message)=>{
    ElMessage.success(message);
    store.auth.user = null;
    router.push('/');
  })
}
</script>

<style scoped>

</style>