<template>
  <div style="max-width: 192px">
    <img v-for=" i in (1,9)" :src="'/static/avatar/' + i + '.png'" class="pic" @click="choose(i)" :class="'pic' + i"
         />
  </div>
</template>

<script>
  export default {
    name: "AvatarPicker",
    props: {
      "value": {
        default: 1
      }
    },
    methods: {
      choose(i) {
        this.$emit('input', i)
      }
    }
  }
</script>

<style scoped>
  .pic {
    width: 64px;
    cursor: pointer;
    border-radius: 8px;
  }
  .pic:hover {
    background-color: #eee;
  }
</style>
