<template>
  <a-modal v-model="show" title="点赞详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="recordData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>用户名称：</b>
          {{ recordData.userName ? recordData.userName : '- -' }}
        </a-col>
        <a-col :span="8"><b>点赞时间：</b>
          {{ recordData.createDate ? recordData.createDate : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <div style="font-size: 13px;font-family: SimHei" v-if="authorInfo !== null">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">创作者信息</span></a-col>
          <a-col :span="8"><b>创作者编号：</b>
            {{ authorInfo.code ? authorInfo.code : '- -' }}
          </a-col>
          <a-col :span="8"><b>创作者姓名：</b>
            {{ authorInfo.name ? authorInfo.name : '- -' }}
          </a-col>
          <a-col :span="8"><b>联系方式：</b>
            {{ authorInfo.phone }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>创建时间：</b>
            {{ authorInfo.createDate }}
          </a-col>
          <a-col :span="8"><b>性别：</b>
            <span v-if="authorInfo.sex == 1">男</span>
            <span v-if="authorInfo.sex == 2">女</span>
          </a-col>
        </a-row>
      </div>
      <br/>
      <div style="font-size: 13px;font-family: SimHei" v-if="bookInfo !== null">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">书籍信息</span></a-col>
          <a-col :span="8"><b>书籍编号：</b>
            {{ bookInfo.code ? bookInfo.code : '- -' }}
          </a-col>
          <a-col :span="8"><b>书籍名称：</b>
            {{ bookInfo.name ? bookInfo.name : '- -' }}
          </a-col>
          <a-col :span="8"><b>最后更新时间：</b>
            {{ bookInfo.updateDate ? bookInfo.updateDate : '- -' }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>标签：</b>
            {{ bookInfo.tag }}
          </a-col>
          <a-col :span="8"><b>书籍类型：</b>
            <span v-if="bookInfo.type == 1">玄幻</span>
            <span v-if="bookInfo.type == 2">奇幻</span>
            <span v-if="bookInfo.type == 3">武侠</span>
            <span v-if="bookInfo.type == 4">都市</span>
            <span v-if="bookInfo.type == 5">现实</span>
          </a-col>
          <a-col :span="8"><b>审核状态：</b>
            <span v-if="bookInfo.status == 0">审核中</span>
            <span v-if="bookInfo.status == 1">通过</span>
            <span v-if="bookInfo.status == 2">驳回</span>
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>最后章节：</b>
            {{ bookInfo.lastChapter ? bookInfo.lastChapter : '- -' }}
          </a-col>
          <a-col :span="8"><b>创建时间：</b>
            {{ bookInfo.createDate }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="24"><b>备注：</b>
            {{ bookInfo.content }}
          </a-col>
        </a-row>
      </div>
      <br/>
    </div>
  </a-modal>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'recordView',
  props: {
    recordShow: {
      type: Boolean,
      default: false
    },
    recordData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.recordShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      repairInfo: null,
      recordInfo: null,
      bookInfo: null,
      authorInfo: null,
      bookDetail: [],
      durgList: [],
      logisticsList: [],
      userInfo: null
    }
  },
  watch: {
    recordShow: function (value) {
      if (value) {
        this.selectDetail(this.recordData.bookId)
      }
    }
  },
  methods: {
    selectDetail (id) {
      this.$get(`/cos/book-info/${id}`).then((r) => {
        this.bookInfo = r.data.book
        this.bookDetail = r.data.detail
        this.authorInfo = r.data.author
      })
    },
    local (recordData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(recordData.longitude, recordData.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
