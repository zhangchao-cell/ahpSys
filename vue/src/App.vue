<template>
  <div id="app">
    <router-view />
    <!-- <div v-if="usually">
      <HelloWorld></HelloWorld>
    </div>
    <div v-if="!usually">
      <Test></Test>
    </div> -->
  </div>
</template>

<script>
// import HelloWorld from "./components/HelloWorld.vue";
// import Test from "./components/Test.vue";

export default {
  name: "App",
  components: {
    // HelloWorld,
    // Test,
  },
  data() {
    return {
      usually: true,
    };
  },
};
</script>


