<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="8" :sm="8" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div>
            <div class="card-panel-text">
              总用户数量
            </div>
            <count-to :start-val="0" :end-val="info.total_user_count || 0" :duration="2600" class="card-panel-num" />
          </div>
          <div>
            <div class="card-panel-text">
              VIP用户数量
            </div>
            <count-to :start-val="0" :end-val="info.svip_user_count || 0" :duration="2600" class="card-panel-num" />
          </div>
          <div>
            <div class="card-panel-text">
              今日注册用户数量
            </div>
            <count-to :start-val="0" :end-val="info.today_registered_count || 0" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </div>
    </el-col>
    <el-col :xs="8" :sm="8" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('newVisitis')" style="display: flex;">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description" style="display: flex; flex-wrap: wrap;">
          <div v-for="item,index in info.vipNum" :key="index" style="min-width: 110px;">
            <div class="card-panel-text">
              VIP{{item.svip_level}}数量
            </div>
            <count-to :start-val="0" :end-val="item.count || 0" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </div>
    </el-col>
    <el-col :xs="8" :sm="8" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('message')">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="message" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div>
            <div class="card-panel-text">
              今日任务数量
            </div>
            <count-to :start-val="0" :end-val="info.today_task_acceptance_count || 0" :duration="3600" class="card-panel-num" />
          </div>
          <div>
            <div class="card-panel-text">
              今日做任务用户数量
            </div>
            <count-to :start-val="0" :end-val="info.today_completed_task_users || 0" :duration="3200" class="card-panel-num" />
          </div>
          <div>
            <div class="card-panel-text">
              今日登录用户数量
            </div>
            <count-to :start-val="0" :end-val="info.today_login_user_count || 0" :duration="3200" class="card-panel-num" />
          </div>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="12" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="my" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div>
              <div class="card-panel-text">用户总充值</div>
              <div>
                <count-to
                  :start-val="0"
                  :end-val="info.total_recharge_amount || 0"
                  :duration="2600"
                  :decimals="2"
                  class="card-panel-num"
                />
              </div>
            </div>
            <div>
              <div class="card-panel-text">用户当日总充值</div>
              <div>
                <count-to
                  :start-val="0"
                  :end-val="info.today_recharge_amount || 0"
                  :duration="2600"
                  :decimals="2"
                  class="card-panel-num"
                />
              </div>
            </div>
            <div>
              <div class="card-panel-text">用户当月总充值</div>
              <div>
                <count-to
                  :start-val="0"
                  :end-val="info.current_month_recharge_amount || 0"
                  :duration="2600"
                  :decimals="2"
                  class="card-panel-num"
                />
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="12" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="my" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div>
              <div class="card-panel-text">用户总提现</div>
              <div>
                <count-to
                  :start-val="0"
                  :end-val="info.total_withdraw_amount || 0"
                  :duration="2600"
                  :decimals="2"
                  class="card-panel-num"
                />
              </div>
            </div>
            <div>
              <div class="card-panel-text">用户当日总提现</div>
              <div>
                <count-to
                  :start-val="0"
                  :end-val="info.today_withdraw_amount || 0"
                  :duration="2600"
                  :decimals="2"
                  class="card-panel-num"
                />
              </div>
            </div>
            <div>
              <div class="card-panel-text">用户当月总提现</div>
              <div>
                <count-to
                  :start-val="0"
                  :end-val="info.monthly_withdraw_amount || 0"
                  :duration="2600"
                  :decimals="2"
                  class="card-panel-num"
                />
              </div>
            </div>
          </div>
        </div>
      </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  components: {
    CountTo
  },
  props: {
    info: {
      type: Object,
      default: () => {},
    },
  },
  watch: {
    info: {
      handler(val) {},
      deep: true,
      immediate: true,
    },
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    // height: 108px;
    // cursor: pointer;
    font-size: 12px;
    position: relative;
    // overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &::after {
      clear: both;
      display: block;
      content: "";
    }

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;
      text-align: right;

      > div {
        margin-bottom: 12px;
      }

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
