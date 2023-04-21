<script setup>
import {Lock, User} from '@element-plus/icons-vue'
</script>

<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1;width: 65vw;">
      <el-image style="width: 100%; height: 100%"
          src="https://ts1.cn.mm.bing.net/th/id/R-C.b233cea1db287ea1ca3e1888da90e6f4?rik=iYNZ47%2bCO2FY1g&riu=http%3a%2f%2fimg.mm4000.com%2ffile%2f4%2f6a%2f1f9bd1c552.jpg&ehk=CXpqivABe8%2bwJCsTp0cfer%2fiZCSuRYGXfLGmLH6kKlk%3d&risl=&pid=ImgRaw&r=0">
      </el-image>
    </div>
    <div style="width: 35vw; background-color: white">
      <div style="text-align: center; margin: 0 20px">
        <div style="margin-top: 120px">
          <div style="font-size: x-large">登录</div>
          <div style="font-size: smaller;color: gray">进入系统前请输入账号密码</div>
        </div>
        <div style="margin-top: 20px">
<!--          username-->
          <div style="margin-top: 10px">
            <el-input type="text" placeholder="用户名/邮箱">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </div>
<!--          end of username-->
<!--          password-->
          <div style="margin-top: 10px">
            <el-input type="password" placeholder="密码">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </div>
<!--          end of password-->
          <div style="margin-top: 10px;">
            <el-row>
              <el-col :span="12" style="text-align: left">
                <el-checkbox v-model="checked1" label="记住我" ></el-checkbox>
              </el-col>
              <el-col :span="12" style="text-align: right">
                <el-link>忘记密码？</el-link>
              </el-col>
            </el-row>
          </div>
        </div>
        <div style="margin-top: 30px">
          <el-button style="width: 60%" type="success" plain>登录</el-button>
        </div>
        <el-divider>
          <span style="color: gray">没有账号?</span>
        </el-divider>
        <el-button style="width: 60%;" type="warning" plain>注册账号</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
