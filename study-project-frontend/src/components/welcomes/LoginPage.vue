<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 80px">
      <div style="font-size: x-large">登录</div>
      <div style="font-size: smaller;color: gray">进入系统前请输入账号密码</div>
    </div>
    <div style="margin-top: 50px">
      <!--          username-->
      <div style="margin-top: 10px">
        <el-input v-model="form.username" type="text" placeholder="用户名/邮箱">
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </div>
      <!--          end of username-->
      <!--          password-->
      <div style="margin-top: 10px">
        <el-input v-model="form.password" type="password" placeholder="密码">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </div>
      <!--          end of password-->
      <div style="margin-top: 10px;">
        <el-row>
          <el-col :span="12" style="text-align: left">
            <el-checkbox v-model="form.remember" label="记住我" ></el-checkbox>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link @click="router.push('/forget')">忘记密码？</el-link>
          </el-col>
        </el-row>
      </div>
    </div>
    <div style="margin-top: 30px">
      <el-button style="width: 60%" @click="login()" type="success" plain>登录</el-button>
    </div>
    <el-divider>
      <span style="color: gray">没有账号?</span>
    </el-divider>
    <el-button @click="router.push('/register')" style="width: 60%;" type="warning" plain>注册账号</el-button>
  </div>
</template>

<script setup>
import {Lock, User} from '@element-plus/icons-vue'
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {get, post} from "@/js";
import router from "@/router";
import {useStore} from "@/stores/counter";

const form = reactive({
  username: '',
  password: '',
  remember: false
})

const store = useStore();

const login = () => {
  if (!form.username || !form.password) {
    ElMessage.warning("请填写用户名和密码");
  }else {
    post('api/auth/login', {
      username: form.username,
      password: form.password,
      remember: form.remember
    }, (message) => {
      ElMessage.success(message);
      get('/api/user/me', (data)=>{
        store.auth.user = data;
        router.push('/index')
      }, ()=>{
        store.auth.user = null;
      })
    })
  }
}

</script>

<style scoped>

</style>