<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 80px">
      <div style="font-size: x-large">注册</div>
      <div style="font-size: smaller;color: gray">欢迎注册我们的学习平台</div>
    </div>
    <div style="margin-top: 50px">
      <!--form      -->
      <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
        <el-form-item prop="username">
          <!--      username-->
          <el-input v-model="form.username" :maxlength="20" type="text" placeholder="用户名">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
          <!--      end of username-->
        </el-form-item>
        <el-form-item prop="password">
          <!--      password-->
          <el-input v-model="form.password" :maxlength="20" type="password" placeholder="密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
          <!--      end of password-->
        </el-form-item>
        <el-form-item prop="password_repeat">
          <!--      re-input-password-->

          <el-input v-model="form.password_repeat" :maxlength="20" type="password" placeholder="再次输入密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
          <!--      end of re-input-password-->
        </el-form-item>
        <el-form-item prop="email">
          <!--      email-->
          <el-input v-model="form.email" type="email" placeholder="邮箱">
            <template #prefix>
              <el-icon>
                <Message/>
              </el-icon>
            </template>
          </el-input>
          <!--      end of email-->
        </el-form-item>
        <el-form-item prop="code">
          <!--      code-->
          <el-row style="width: 100%">
            <el-col :span="16" style="text-align: left">
              <el-input v-model="form.code" :maxlength="6" type="text" placeholder="请输入验证码">
                <template #prefix>
                  <el-icon>
                    <EditPen/>
                  </el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="8" style="text-align: right">
              <el-button style="padding: 2px 2px" @click="validateEmail" type="primary" :disabled="!isEmailValid || cold > 0">
                {{ cold > 0 ? '请稍后: ' + cold : "发送验证码" }}
              </el-button>
            </el-col>
          </el-row>
          <!--      end of code-->
        </el-form-item>
      </el-form>
      <!--end of form      -->
      <!--register-button      -->
      <div style="margin-top: 30px">
        <el-button @click="register" style="width: 60%;" type="danger" plain>注册账号</el-button>
      </div>
      <!--end of register-button      -->
      <div style="margin-top: 5px">
        <span style="font-size: smaller;color: gray">已有账号？</span>
        <el-link @click="router.push('/')" style="translate: 0 -2px" type="primary">回到登录</el-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/js";

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^.{1,20}$/.test(value)) {
    callback(new Error('长度应该在 1 到 20 之间'))
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('密码不一致'))
  } else {
    callback()
  }
}

const isEmailValid = ref(true);

const onValidate = (prop, isValid) => {
  // 更新的是email则更新
  if (prop === 'email')
    isEmailValid.value = isValid
}

const formRef = ref();

const register = () => {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('/api/auth/register', {
        username: form.username,
        password: form.password,
        email: form.email,
        code: form.code
      }, (message)=>{
        ElMessage.success(message);
        router.push('/')
      })
    } else {
      ElMessage.warning('请完整填写表单')
    }
  })
}

const cold = ref(0);

const validateEmail = ()=> {
  // 防止连点
  cold.value = 60;
  post('api/auth/validate-register-email', {
    email: form.email
  }, (message)=>{
    ElMessage.success(message);
    setInterval(()=> cold.value--, 1000);
  }, (message) => {
    ElMessage.warning(message)
    cold.value = 0;
  })
}

const rules = {
  username: [
    {validator: validateUsername, trigger: ['blur', 'change']},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 20, message: '密码长度只能在 6 到 20 之间', trigger: ['blur', 'change']}
  ],
  password_repeat: [
    {validator: validatePassword}
  ],
  email: [
    {required: true, message: '请输入邮箱', trigger: ['blur', 'change']},
    {type: 'email', message: '邮箱格式错误', trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: ['blur', 'change']},
  ]
}
</script>

<style scoped>

</style>