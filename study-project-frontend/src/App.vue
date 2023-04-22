<script setup>
import {get} from "@/js";
import {useStore} from "@/stores/counter";
import router from "@/router";

const store = useStore();

if (store.auth.user == null) {
  get('/api/user/me', (data)=>{
    store.auth.user = data;
    router.push('/index')
  }, ()=>{
    store.auth.user = null;
  })
}



</script>

<template>
  <router-view/>
</template>

<style scoped>

</style>
