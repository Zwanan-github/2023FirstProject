<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin: 30px 10px 0;">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="验证邮件" />
        <el-step title="设置密码" />
        <el-step title="完成验证" />
      </el-steps>
    </div>
    <transition-group name="el-fade-in-linear" appear>
      <div v-if="active === 0" style="height: 100%">
        <div style="margin-top: 50px">
          <div style="font-size: x-large">验证邮件</div>
          <div style="font-size: smaller;color: gray">重置密码需要输入你的电子邮件地址</div>
        </div>
        <div style="margin-top: 50px">
          <!--form      -->
          <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
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
        </div>
        <div style="margin-top: 30px">
          <el-button @click="startReset" style="width: 60%;" type="warning" plain>下一步</el-button>
        </div>
      </div>
      <div v-if="active === 1" style="height: 100%">
        <div style="margin-top: 50px">
          <div style="font-size: x-large">设置密码</div>
          <div style="font-size: smaller;color: gray">重置密码请记好，防止下次丢失</div>
        </div>
        <div style="margin-top: 50px">
          <!--form      -->
          <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
            <el-form-item prop="password">
              <!--      password-->
              <el-input v-model="form.password" :maxlength="20" type="password" placeholder="新密码">
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
          </el-form>
        </div>
        <div style="margin-top: 30px">
          <el-button @click="doReset" style="width: 60%;" type="warning" plain>下一步</el-button>
        </div>
      </div>
      <div v-if="active === 2" style="height: 100%">
        <div style="margin-top: 50px">
          <div style="font-size: x-large">修改完成</div>
          <div style="font-size: smaller;color: gray">祝您玩的愉快</div>
        </div>
        <div style="margin-top: 30px;">
          <!--form      -->
          <el-image src="../src/assets/img/forget-finish.png">
          </el-image>
        </div>
        <div style="margin-top: 30px">
          <el-button @click="()=>router.push('/')" style="width: 60%;" type="warning" plain>去登录</el-button>
        </div>
      </div>
    </transition-group>
  </div>
</template>

<script setup>

import {EditPen, Message, Lock} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive, ref} from "vue";
import {post} from "@/js";
import {ElMessage} from "element-plus";

const active = ref(0);


const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: ''
})

const isEmailValid = ref(true);

const onValidate = (prop, isValid) => {
  // 更新的是email则更新
  if (prop === 'email')
    isEmailValid.value = isValid
}

const formRef = ref();

const cold = ref(0);

const validateEmail = ()=> {
  cold.value = 60;
  post('api/auth/validate-reset-email', {
    email: form.email
  }, (message)=>{
    ElMessage.success(message);
    setInterval(()=> cold.value--, 1000);
  }, (message)=>{
    ElMessage.warning(message);
  })
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

const startReset = ()=> {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('/api/auth/start-reset', {
        email: form.email,
        code: form.code
      }, () => {
        active.value++;
      })
    }else {
      ElMessage.warning('请填写你的邮件和验证码')
    }
  })
}

const doReset = ()=>{
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('/api/auth/do-reset', {
        password: form.password
      }, (message) => {
        active.value++;
        ElMessage.success(message)
      })
    }else {
      ElMessage.warning('请填写你的新密码并确定')
    }
  })
}


const rules = {
  email: [
    {required: true, message: '请输入邮箱', trigger: ['blur', 'change']},
    {type: 'email', message: '邮箱格式错误', trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: ['blur', 'change']},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 20, message: '密码长度只能在 6 到 20 之间', trigger: ['blur', 'change']}
  ],
  password_repeat: [
    {validator: validatePassword}
  ],
}

</script>

<style scoped>

</style>