<template>
  <div>

    <div class="title" @click="$router.push('/article/' + item.id)">{{item.title}}</div>
    <div class="info">{{item.owner}}&nbsp;&nbsp;&nbsp;{{new Date(item.dateTime) | formatDate('DATETIME')}}
      &nbsp;&nbsp;
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icon-zan2"></use>
      </svg>&nbsp;{{item.likes}}
    </div>
    <div class="content" v-if="item.type=='ARTICLE'">{{item.content}}...</div>
    <div v-if="item.type=='PHOTO'">
      <img v-for="img in item.photos" :src="img" class="photo"/>
    </div>
  </div>
</template>

<script>
  export default {
    name: "NewsItem",
    props: ["item"]
  }
</script>

<style scoped>
  .photo {
    max-width: 120px;
    max-height: 120px;
    margin: 10px 10px;
  }

  .title {
    font-size: 20px;
    color: #555;
    margin: 10px 0;
    font-weight: bold;
    cursor: pointer;
  }

  .title:hover {
    color: #de2070;
  }

  .content {
    margin: 20px;
    font-size: 15px;
    color: #555;
  }

  .info {
    margin-bottom: 10px;
    margin-left: 30px;
  }

</style>
