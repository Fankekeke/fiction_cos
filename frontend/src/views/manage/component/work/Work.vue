<template>
  <a-card :bordered="false" class="card-area">
    <div style="width: 100%">
      <a-col :span="22" v-if="newsList.length > 0">
        <a-alert
          banner
          :message="newsContent"
          type="info"
        />
      </a-col>
      <a-col :span="2">
        <a-button type="primary" style="margin-top: 2px;margin-left: 10px" @click="newsNext">下一页</a-button>
      </a-col>
    </div>
    <br/>
    <br/>
    <a-row :gutter="30" v-if="userInfo != null">
      <a-col :span="6">
        <a-card :bordered="false">
          <span slot="title">
            <a-icon type="user" style="margin-right: 10px" />
            <span>用户信息</span>
          </span>
          <div>
            <a-avatar :src="'http://127.0.0.1:9527/imagesWeb/' + userInfo.images" shape="square" style="width: 100px;height: 100px;float: left;margin: 10px 0 10px 10px" />
            <div style="float: left;margin-left: 20px;margin-top: 8px">
              <span style="font-size: 20px;font-family: SimHei">{{ userInfo.name }}</span>
              <span style="font-size: 14px;font-family: SimHei">{{ userInfo.code }}</span>
            </div>
            <div style="float: left;margin-left: 20px;margin-top: 8px">
              <span style="font-size: 14px;font-family: SimHei">联系方式：{{ userInfo.phone == null ? '- -' : userInfo.phone }}</span>
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>
    <library></library>
  </a-card>
</template>

<script>
import Library from '../../../user/library/Library.vue'
import {mapState} from 'vuex'

const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'Work',
  components: {Library},
  data () {
    return {
      form: this.$form.createForm(this),
      formItemLayout,
      newsContent: '',
      visible: false,
      statusList: [],
      vehicleList: [],
      loading: false,
      userInfo: null,
      memberInfo: null,
      spaceInfo: null,
      newsPage: 0,
      newsList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted () {
    this.selectMemberByUserId()
  },
  methods: {
    newsNext () {
      if (this.newsPage + 1 === this.newsList.length) {
        this.newsPage = 0
      } else {
        this.newsPage += 1
      }
      this.newsContent = `《${this.newsList[this.newsPage].title}》 ${this.newsList[this.newsPage].content}`
    },
    showModal (row) {
      this.spaceInfo = row
      this.visible = true
    },
    handleCancel (e) {
      console.log('Clicked cancel button')
      this.visible = false
    },
    reserveSpace () {
      this.form.validateFields((err, values) => {
        if (!err) {
          values.spaceId = this.spaceInfo.id
          this.$post('/cos/reserve-info', {
            ...values
          }).then((r) => {
            this.$message.success('预约成功！预约时间为30分钟')
            this.visible = false
            this.getWorkStatusList()
          })
        }
      })
    },
    selectMemberByUserId () {
      this.$get(`/cos/member-info/member/${this.currentUser.userId}`).then((r) => {
        this.userInfo = r.data.user
        this.memberInfo = r.data.member
        this.newsList = r.data.bulletin
        if (this.newsList.length !== 0) {
          this.newsContent = `《${this.newsList[0].title}》 ${this.newsList[0].content}`
        }
      })
    },
    getWorkStatusList () {
      this.$get(`/cos/space-status-info/status/list`).then((r) => {
        this.statusList = r.data.data
      })
    }
  }
}
</script>

<style scoped>
>>> .ant-card-meta-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-meta-description {
  font-size: 12px;
  font-family: SimHei;
}
>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 250px;
  line-height: 250px;
  overflow: hidden;
}

</style>
