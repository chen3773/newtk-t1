<template>
  <div id="app">
    <router-view />
    <theme-picker />
    <audio ref="audioPlayer" src="./assets/images/news.mp3"></audio>
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker";
import { getLanguage } from "@/api/index";

export default {
  name: "App",
  components: { ThemePicker },
  data() {
    return {
      timer: null,
    };
  },
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
      }
    }
  },
  created() {
    if (!localStorage.getItem('Language')) {
      localStorage.setItem('Language', 'English')
    }
    // this.getLang()
  },
  watch: {
    // 监听路由对象中的变化
    $route: {
      handler: function (to, from) {
        // 当路由发生变化时会调用这个函数
        // to 和 from 分别代表到达的路由和离开的路由
        if (!from) return;
        if (to.path == "/login") {
          clearInterval(this.timer);
          this.timer = null;
        } else {
          this.getDsp();
        }
        // 在这里可以根据路由变化执行相应的逻辑
      },
      // 如果需要在路由变化时立即响应，可以设置immediate为true
      immediate: true,
    },
  },
  methods: {
    getDsp() {
      if (this.timer) return;
      this.$store
          .dispatch("getTaskNum")
          .then((res) => {
            if (res.Tasks > 0 || res.Withdrawal > 0) {
              this.$refs.audioPlayer.play();
            }
          })
      this.timer = setInterval(() => {
        this.$store
          .dispatch("getTaskNum")
          .then((res) => {
            if (res.Tasks > 0 || res.Withdrawal > 0) {
              this.$refs.audioPlayer.play();
            }
          })
          .catch(() => {
            clearInterval(this.timer);
            this.timer = null;
          });
      }, 10000);
    },
    getLang() {
      getLanguage().then(response => {
        
      });
    }
  }
};
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
<style>

.tag-color1 {
  color: #00d1ff;
}

.tag-color2 {
  color: #0066ff;
}

.tag-color3 {
  color: #9e00ff;
}

.tag-color4 {
  color: #ff004d;
}

.tag-color5 {
  color: #ff6b00;
}

.tag-color6 {
  color: #f4de10;
}
</style>
